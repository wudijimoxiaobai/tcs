package com.csscaps.tcs.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.csscaps.common.base.BasePresenter;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.tcs.ServerConstants;
import com.csscaps.tcs.action.IInvoiceIssuingAction;
import com.csscaps.tcs.activity.InvoiceIssuingActivity;
import com.csscaps.tcs.database.TcsDatabase;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.InvoiceNo;
import com.csscaps.tcs.database.table.InvoiceNo_Table;
import com.csscaps.tcs.database.table.Product;
import com.csscaps.tcs.database.table.ProductModel;
import com.csscaps.tcs.model.RequestUploadInvoice;
import com.csscaps.tcs.service.InvoiceNoService;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.tax.fcr.library.network.Api;
import com.tax.fcr.library.network.RequestModel;

import java.util.ArrayList;
import java.util.List;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/5/30.
 */

public class InvoiceIssuingPresenter extends BasePresenter<IInvoiceIssuingAction> {

    public InvoiceIssuingPresenter(IInvoiceIssuingAction view, Context mContext) {
        super(view, mContext);
    }

    @Override
    public void onSuccess(String requestPath, String objectString) {
        if (!isDetached()) view.complete(true);
    }

    @Override
    public void onFailure(String requestPath, String errorMes) {
        if (!isDetached()) view.complete(false);
    }


    public void issuingInvoice(List<Product> products) {
        Invoice invoice = InvoiceIssuingActivity.mInvoice;
        invoice.setClient_invoice_datetime(DateUtils.dateToStr(DateUtils.getDateNow(), DateUtils.format_yyyyMMddHHmmss_24_EN));
        List<ProductModel> productModels = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            ProductModel productModel = p.getProductModel();
            productModel.setLine_no(String.valueOf(i + 1));
            productModels.add(productModel);
        }
        invoice.setGoods(productModels);

        RequestUploadInvoice uploadInvoice = new RequestUploadInvoice();
        uploadInvoice.setFuncid(ServerConstants.ATCS012);
        uploadInvoice.getInvoice_data().add(invoice);
        RequestModel requestModel = new RequestModel();
        requestModel.setFuncid(uploadInvoice.getFuncid());
        uploadInvoice.setDevicesn(requestModel.getDevicesn());
        requestModel.setData(JSON.toJSONString(uploadInvoice));
        Api.post(this, requestModel);

        FlowManager.getDatabase(TcsDatabase.class)
                .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                        new ProcessModelTransaction.ProcessModel<ProductModel>() {
                            @Override
                            public void processModel(ProductModel model, DatabaseWrapper wrapper) {
                                model.save();
                            }
                        }).addAll(productModels).build())
                .error(new Transaction.Error() {
                    @Override
                    public void onError(@NonNull Transaction transaction, @NonNull Throwable error) {

                    }
                })
                .build()
                .execute();

        InvoiceNo invoiceNo = select().from(InvoiceNo.class).where(InvoiceNo_Table.invoice_num.eq(invoice.getInvoice_no())).querySingle();
        invoiceNo.delete();
        mContext.startService(new Intent(mContext, InvoiceNoService.class));
    }


}
