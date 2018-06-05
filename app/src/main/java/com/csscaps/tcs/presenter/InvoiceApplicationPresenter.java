package com.csscaps.tcs.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.csscaps.common.base.BasePresenter;
import com.csscaps.common.utils.AppSP;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.R;
import com.csscaps.tcs.ServerConstants;
import com.csscaps.tcs.action.IInvoiceApplicationAction;
import com.csscaps.tcs.database.TcsDatabase;
import com.csscaps.tcs.database.table.InvoiceNo;
import com.csscaps.tcs.database.table.InvoiceNo_Table;
import com.csscaps.tcs.database.table.InvoiceType;
import com.csscaps.tcs.model.InvoiceNoModel;
import com.csscaps.tcs.model.ReceiveInvoiceNo;
import com.csscaps.tcs.model.RequestInvoiceNo;
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
 * Created by tl on 2018/5/23.
 */

public class InvoiceApplicationPresenter extends BasePresenter<IInvoiceApplicationAction> {
    boolean refresh;

    public InvoiceApplicationPresenter(IInvoiceApplicationAction view, Context mContext) {
        super(view, mContext);
    }

    public void refresh(boolean refresh) {
        this.refresh = refresh;
        List<InvoiceType> mTaxTypes = select().from(InvoiceType.class).queryList();
        RequestModel requestModel = new RequestModel();
        RequestInvoiceNo requestInvoiceNo = new RequestInvoiceNo();
        requestInvoiceNo.setFuncid(ServerConstants.ATCS010);
        requestInvoiceNo.setDevicesn(requestModel.getDevicesn());
        List<InvoiceNoModel> invoice_type_info = new ArrayList<>();
        for (InvoiceType invoiceType : mTaxTypes) {
            String invoiceCode = invoiceType.getInvoice_type_code();
            int wv = AppSP.getInt(invoiceCode, 0);
            long count = select().from(InvoiceNo.class).where(InvoiceNo_Table.invoice_type_code.eq(invoiceCode)).query().getCount();
            InvoiceNoModel invoiceNoModel = new InvoiceNoModel();
            invoiceNoModel.setInvoice_type_code(invoiceCode);
            if (count == 0) {
                invoiceNoModel.setCurrent_or_next_period("0");
                invoice_type_info.add(invoiceNoModel);
            } else if (count < wv) {
                invoiceNoModel.setCurrent_or_next_period("1");
                invoice_type_info.add(invoiceNoModel);
            }
        }
        if (invoice_type_info.size() > 0) {
            requestInvoiceNo.setInvoice_type_info(invoice_type_info);
            requestModel.setFuncid(requestInvoiceNo.getFuncid());
            requestModel.setData(JSON.toJSONString(requestInvoiceNo));
            Api.post(this, requestModel);
        } else {
            if (refresh) ToastUtil.showShort(mContext.getString(R.string.hit11));
        }
    }

    @Override
    public void onSuccess(String requestPath, String objectString) {
        ReceiveInvoiceNo receiveInvoiceNo = JSON.parseObject(objectString, ReceiveInvoiceNo.class);
        List<InvoiceNoModel> invoiceNos = receiveInvoiceNo.getInvoice_data();
        List<InvoiceNo> list = new ArrayList<>();
        for (InvoiceNoModel item : invoiceNos) {
            String wv = item.getWarning_value();
            String code = item.getInvoice_type_code();
            if (!TextUtils.isEmpty(wv)) AppSP.putInt(code, Integer.valueOf(wv));
            String numEnd = item.getInvoice_num_end();
            String numStart = item.getInvoice_num_start();
            long end = Long.valueOf(numEnd);
            long start = Long.valueOf(numStart);
            for (long i = start; i < end + 1; i++) {
                InvoiceNo invoiceNo = new InvoiceNo();
                invoiceNo.setInvoice_type_code(code);
                invoiceNo.setInvoice_num(item.getInvoice_header() + i);
                invoiceNo.setSegment_cipher(item.getSegment_cipher());
                list.add(invoiceNo);
            }
        }
        FlowManager.getDatabase(TcsDatabase.class)
                .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                        new ProcessModelTransaction.ProcessModel<InvoiceNo>() {
                            @Override
                            public void processModel(InvoiceNo model, DatabaseWrapper wrapper) {
                                model.save();
                            }
                        }).addAll(list).build())
                .success(new Transaction.Success() {
                    @Override
                    public void onSuccess(@NonNull Transaction transaction) {
                        view.callBack();
                    }
                })
                .build()
                .execute();
    }

    @Override
    public void onFailure(String requestPath, String errorMes) {
        if (refresh) {
            switch (errorMes) {
                case Api.ERR_NETWORK:
                    ToastUtil.showShort(mContext.getString(R.string.hit3));
                    break;
                case Api.FAIL_CONNECT:
                    ToastUtil.showShort(mContext.getString(R.string.hit4));
                    break;
            }
        }
    }
}
