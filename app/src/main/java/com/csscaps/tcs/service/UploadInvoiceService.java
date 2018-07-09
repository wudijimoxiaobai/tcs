package com.csscaps.tcs.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.csscaps.common.utils.ObserverActionUtils;
import com.csscaps.tcs.ServerConstants;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.Invoice_Table;
import com.csscaps.tcs.database.table.ProductModel;
import com.csscaps.tcs.database.table.ProductModel_Table;
import com.csscaps.tcs.fragment.InvoiceQueryFragment;
import com.csscaps.tcs.model.ReceiveUploadInvoiceResult;
import com.csscaps.tcs.model.RequestResultModel;
import com.csscaps.tcs.model.RequestUploadInvoice;
import com.tax.fcr.library.network.Api;
import com.tax.fcr.library.network.IPresenter;
import com.tax.fcr.library.network.RequestModel;

import java.util.List;

import rx.Subscription;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/6/26.
 */

public class UploadInvoiceService extends Service implements IPresenter {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        List<Invoice> list = null;
        if (intent != null) list = (List<Invoice>) intent.getSerializableExtra("list");
        if (list == null)
            list = select().from(Invoice.class).where(Invoice_Table.uploadStatus.eq("0")).queryList();
        uploadInvoice(list);
        return super.onStartCommand(intent, flags, startId);
    }


    private void uploadInvoice(List<Invoice> list) {
        RequestUploadInvoice uploadInvoice = new RequestUploadInvoice();
        for (Invoice invoice : list) {
            String invoiceNo = invoice.getInvoice_no();
            List<ProductModel> productModels = select().from(ProductModel.class).where(ProductModel_Table.invoice_no.eq(invoiceNo)).queryList();
            invoice.setGoods(productModels);
            uploadInvoice.getInvoice_data().add(invoice);
        }
        uploadInvoice.setFuncid(ServerConstants.ATCS012);
        RequestModel requestModel = new RequestModel();
        requestModel.setFuncid(uploadInvoice.getFuncid());
        uploadInvoice.setDevicesn(requestModel.getDevicesn());
        requestModel.setData(JSON.toJSONString(uploadInvoice));
        Api.post(this, requestModel);
    }


    @Override
    public void onSuccess(String requestPath, String objectString) {
        ReceiveUploadInvoiceResult receiveUploadInvoiceResult = JSON.parseObject(objectString, ReceiveUploadInvoiceResult.class);
        List<RequestResultModel> invoiceResult = receiveUploadInvoiceResult.getInvoice_result();
        for (RequestResultModel resultModel : invoiceResult) {
            if ("1".equals(resultModel.getCode())) continue;
            String invoiceNo = resultModel.getInvoice_no();
            Invoice invoice = select().from(Invoice.class).where(Invoice_Table.invoice_no.eq(invoiceNo)).querySingle();
            invoice.setUploadStatus("1");
            invoice.update();
        }
        Subscription subscription = ObserverActionUtils.subscribe(new Invoice(), InvoiceQueryFragment.class);
        if (subscription != null) subscription.unsubscribe();
    }

    @Override
    public void onFailure(String requestPath, String errorMes) {

    }
}
