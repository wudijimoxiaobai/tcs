package com.csscaps.tcs.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.csscaps.common.utils.AppSP;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.R;
import com.csscaps.tcs.ServerConstants;
import com.csscaps.tcs.database.TcsDatabase;
import com.csscaps.tcs.database.table.InvoiceTaxType;
import com.csscaps.tcs.database.table.InvoiceType;
import com.csscaps.tcs.database.table.TaxItem;
import com.csscaps.tcs.database.table.TaxMethod;
import com.csscaps.tcs.database.table.TaxType;
import com.csscaps.tcs.model.ReceiveInvoiceTaxType;
import com.csscaps.tcs.model.ReceiveInvoiceType;
import com.csscaps.tcs.model.ReceiveTaxItem;
import com.csscaps.tcs.model.ReceiveTaxMethod;
import com.csscaps.tcs.model.ReceiveTaxType;
import com.csscaps.tcs.model.RequestData;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.tax.fcr.library.network.Api;
import com.tax.fcr.library.network.IPresenter;
import com.tax.fcr.library.network.RequestModel;
import com.tax.fcr.library.utils.Logger;

import java.util.List;

/**
 * Created by tl on 2018/5/9.
 */

public class SynchronizeService extends Service implements IPresenter {

    boolean autoSyn;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        autoSyn = intent.getBooleanExtra("autoSyn", false);
        return null;
    }

    @Override
    public void onCreate() {
        String str = AppSP.getString("MyTaxpayer");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        synTaxpayer();
        synInvoiceType();
        synTaxType();
        synTaxItem();
        synInvoiceTaxType();
        synTaxMethod();
        return super.onStartCommand(intent, flags, startId);
    }



    /**
     * 同步纳税人
     */
    private void synTaxpayer() {
        synData(ServerConstants.ATCS002);
    }

    /**
     * 税目同步
     */
    private void synTaxItem() {
        synData(ServerConstants.ATCS004);
    }

    /**
     * 税种同步
     */
    private void synTaxType() {
        synData(ServerConstants.ATCS005);
    }

    /**
     * 票种信息同步
     */
    private void synInvoiceType() {
        synData(ServerConstants.ATCS006);
    }

    /**
     * 票种税种关联关系
     */
    private void synInvoiceTaxType() {
        synData(ServerConstants.ATCS007);
    }

    /**
     * 同步计税方法
     **/
    private void synTaxMethod() {
        synData(ServerConstants.ATCS008);
    }



    private void synData(String funcId) {
        RequestData requestData = new RequestData();
        requestData.setFuncid(funcId);
        RequestModel requestModel = new RequestModel();
        requestModel.setFuncid(requestData.getFuncid());
        requestData.setDevicesn(requestModel.getDevicesn());
        requestData.setSystime(DateUtils.dateToStr(DateUtils.getDateNow(), DateUtils.format_yyyyMMddHHmmss_24_EN));
        requestModel.setData(JSON.toJSONString(requestData));
        Api.post(this, requestModel);
    }

    @Override
    public void onSuccess(String requestPath, String objectString) {
        switch (requestPath) {
            case ServerConstants.ATCS002:
                AppSP.putString("MyTaxpayer", objectString);
                break;
            case ServerConstants.ATCS004:
                long s = System.currentTimeMillis();
                ReceiveTaxItem receiveTaxItem = JSON.parseObject(objectString, ReceiveTaxItem.class);
                List<TaxItem> taxitems = receiveTaxItem.getTaxitem_info();
                FlowManager.getDatabase(TcsDatabase.class)
                        .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                                new ProcessModelTransaction.ProcessModel<TaxItem>() {
                                    @Override
                                    public void processModel(TaxItem model, DatabaseWrapper wrapper) {
                                        model.save();
                                    }
                                }).addAll(taxitems).build())
                        .build()
                        .execute();
                long e = System.currentTimeMillis();
                Logger.i("批量：" + (e - s));
                break;
            case ServerConstants.ATCS005:
                ReceiveTaxType receiveTaxType = JSON.parseObject(objectString, ReceiveTaxType.class);
                List<TaxType> taxTypes = receiveTaxType.getTaxtype_info();
                FlowManager.getDatabase(TcsDatabase.class)
                        .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                                new ProcessModelTransaction.ProcessModel<TaxType>() {
                                    @Override
                                    public void processModel(TaxType model, DatabaseWrapper wrapper) {
                                        model.save();
                                    }
                                }).addAll(taxTypes).build())
                        .build()
                        .execute();
                break;
            case ServerConstants.ATCS006:
                ReceiveInvoiceType receiveInvoiceType = JSON.parseObject(objectString, ReceiveInvoiceType.class);
                List<InvoiceType> invoiceTypes = receiveInvoiceType.getInvoice_type_info();
                FlowManager.getDatabase(TcsDatabase.class)
                        .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                                new ProcessModelTransaction.ProcessModel<InvoiceType>() {
                                    @Override
                                    public void processModel(InvoiceType model, DatabaseWrapper wrapper) {
                                        model.save();
                                    }
                                }).addAll(invoiceTypes).build())
                        .success(new Transaction.Success() {
                            @Override
                            public void onSuccess(@NonNull Transaction transaction) {
                                startService(new Intent(SynchronizeService.this, InvoiceNoService.class));
                            }
                        })
                        .build()
                        .execute();
                break;
            case ServerConstants.ATCS007:
                ReceiveInvoiceTaxType receiveInvoiceTaxType=JSON.parseObject(objectString, ReceiveInvoiceTaxType.class);
                List<InvoiceTaxType> invoice_tax_info=receiveInvoiceTaxType.getInvoice_tax_info();
                FlowManager.getDatabase(TcsDatabase.class)
                        .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                                new ProcessModelTransaction.ProcessModel<InvoiceTaxType>() {
                                    @Override
                                    public void processModel(InvoiceTaxType model, DatabaseWrapper wrapper) {
                                        model.save();
                                    }
                                }).addAll(invoice_tax_info).build())
                        .build()
                        .execute();
                break;
            case ServerConstants.ATCS008:
                ReceiveTaxMethod receiveTaxMethod=JSON.parseObject(objectString, ReceiveTaxMethod.class);
                List<TaxMethod> calc_rule_info= receiveTaxMethod.getCalc_rule_info();
                FlowManager.getDatabase(TcsDatabase.class)
                        .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                                new ProcessModelTransaction.ProcessModel<TaxMethod>() {
                                    @Override
                                    public void processModel(TaxMethod model, DatabaseWrapper wrapper) {
                                        model.save();
                                    }
                                }).addAll(calc_rule_info).build())
                        .build()
                        .execute();

                break;

        }
    }

    @Override
    public void onFailure(String requestPath, String errorMes) {
        if (autoSyn) {
            switch (errorMes) {
                case Api.ERR_NETWORK:
                    ToastUtil.showShort(getString(R.string.hit3));
                    break;
                case Api.FAIL_CONNECT:
                    ToastUtil.showShort(getString(R.string.hit4));
                    break;
            }
        }
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public boolean isDetached() {
        return false;
    }
}
