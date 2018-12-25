package com.csscaps.tcs.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.csscaps.common.utils.AppSP;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.common.utils.ObserverActionUtils;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.MainActivity;
import com.csscaps.tcs.MyTimerTask;
import com.csscaps.tcs.R;
import com.csscaps.tcs.RTCUtil;
import com.csscaps.tcs.ServerConstants;
import com.csscaps.tcs.Util;
import com.csscaps.tcs.database.TaxpayerDatabase;
import com.csscaps.tcs.database.TcsDatabase;
import com.csscaps.tcs.database.table.ControlData;
import com.csscaps.tcs.database.table.InvoiceTaxType;
import com.csscaps.tcs.database.table.InvoiceType;
import com.csscaps.tcs.database.table.TaxItem;
import com.csscaps.tcs.database.table.TaxMethod;
import com.csscaps.tcs.database.table.TaxType;
import com.csscaps.tcs.database.table.Taxpayer;
import com.csscaps.tcs.model.MyTaxpayer;
import com.csscaps.tcs.model.ReceiveAllTaxpayer;
import com.csscaps.tcs.model.ReceiveInvoiceTaxType;
import com.csscaps.tcs.model.ReceiveInvoiceType;
import com.csscaps.tcs.model.ReceiveTaxItem;
import com.csscaps.tcs.model.ReceiveTaxMethod;
import com.csscaps.tcs.model.ReceiveTaxType;
import com.csscaps.tcs.model.ReportDataModel;
import com.csscaps.tcs.model.RequestAllTaxpayer;
import com.csscaps.tcs.model.RequestData;
import com.csscaps.tcs.model.RequestReportData;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.tax.fcr.library.network.Api;
import com.tax.fcr.library.network.IPresenter;
import com.tax.fcr.library.network.RequestModel;
import com.tax.fcr.library.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

import static com.raizlabs.android.dbflow.sql.language.SQLite.delete;

/**
 * Created by tl on 2018/5/9.
 */

public class SynchronizeService extends Service implements IPresenter {

    private boolean autoSyn;
    public int c;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        String str = AppSP.getString("MyTaxpayer");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) autoSyn = intent.getBooleanExtra("autoSyn", false);
        synTaxpayer();
        Util.get3f_pwd_key();
        c = 0;
        return super.onStartCommand(intent, flags, startId);
    }


    private void synData() {
        synInvoiceType();
        synTaxType();
        synTaxItem();
        synInvoiceTaxType();
        synTaxMethod();
        synAllTaxpayer();
        if (!AppSP.getBoolean("ControlData")) synControlData();
        new MyTimerTask().run();
    }


    /**
     * 同步纳税人
     */
    private void synTaxpayer() {
        RequestData.sellerid = null;
        synData(ServerConstants.ATCS002);
        RequestData.sellerid = AppSP.getString("sellerid");
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


    /*同步监控数据*/
    private void synControlData() {
        RequestReportData requestReportData = new RequestReportData();
        requestReportData.setFuncid(ServerConstants.ATCS023);
        requestReportData.setType("1");
        RequestModel requestModel = new RequestModel();
        requestModel.setFuncid(requestReportData.getFuncid());
        requestReportData.setDevicesn(requestModel.getDevicesn());
        requestModel.setData(JSON.toJSONString(requestReportData));
        Api.post(this, requestModel);
    }

    /*全国纳税人信息同步*/
    private void synAllTaxpayer() {
        RequestAllTaxpayer requestAllTaxpayer = new RequestAllTaxpayer();
        requestAllTaxpayer.setData(AppSP.getString("synAllTaxpayerDate"));
        requestAllTaxpayer.setFuncid(ServerConstants.ATCS021);
        RequestModel requestModel = new RequestModel();
        requestModel.setFuncid(requestAllTaxpayer.getFuncid());
        requestAllTaxpayer.setDevicesn(requestModel.getDevicesn());
        requestModel.setData(JSON.toJSONString(requestAllTaxpayer));
        Api.post(this, requestModel);
    }


    private void synData(String funcId) {
        RequestData requestData = new RequestData();
        requestData.setFuncid(funcId);
        RequestModel requestModel = new RequestModel();
        requestModel.setFuncid(requestData.getFuncid());
        requestData.setDevicesn(requestModel.getDevicesn());
        requestData.setSystime(DateUtils.dateToStr(RTCUtil.getRTC(), DateUtils.format_yyyyMMddHHmmss_24_EN));
        requestModel.setData(JSON.toJSONString(requestData));
        Api.post(this, requestModel);
    }

    @Override
    public void onSuccess(String requestPath, String objectString) {
        switch (requestPath) {
            case ServerConstants.ATCS002:
                AppSP.putString("MyTaxpayer", objectString);
                MyTaxpayer myTaxpayer = JSON.parseObject(objectString, MyTaxpayer.class);
                RequestData.sellerid = myTaxpayer.getSellerid();
                AppSP.putString("sellerid",  RequestData.sellerid);
                synData();
                break;
            case ServerConstants.ATCS004:
                delete().from(TaxItem.class).execute();
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
                break;
            case ServerConstants.ATCS005:
                delete().from(TaxType.class).execute();
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
                delete().from(InvoiceType.class).execute();
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
                delete().from(InvoiceTaxType.class).execute();
                ReceiveInvoiceTaxType receiveInvoiceTaxType = JSON.parseObject(objectString, ReceiveInvoiceTaxType.class);
                List<InvoiceTaxType> invoice_tax_info = receiveInvoiceTaxType.getInvoice_tax_info();
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
                delete().from(TaxMethod.class).execute();
                ReceiveTaxMethod receiveTaxMethod = JSON.parseObject(objectString, ReceiveTaxMethod.class);
                List<TaxMethod> calc_rule_info = receiveTaxMethod.getCalc_rule_info();
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
            case ServerConstants.ATCS021:
                final ReceiveAllTaxpayer receiveAllTaxpayer = JSON.parseObject(objectString, ReceiveAllTaxpayer.class);
                List<Taxpayer> data = receiveAllTaxpayer.getData();
                if (data != null && data.size() > 0) {
                    FlowManager.getDatabase(TaxpayerDatabase.class)
                            .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                                    new ProcessModelTransaction.ProcessModel<Taxpayer>() {
                                        @Override
                                        public void processModel(Taxpayer model, DatabaseWrapper wrapper) {
                                            model.save();
                                        }
                                    }).addAll(data).build())
                            .success(new Transaction.Success() {
                                @Override
                                public void onSuccess(@NonNull Transaction transaction) {
                                    AppSP.putString("synAllTaxpayerDate", receiveAllTaxpayer.getLast_update_time());
                                }
                            })
                            .build()
                            .execute();
                }
                break;
            case ServerConstants.ATCS023:
                RequestReportData requestReportData = JSON.parseObject(objectString, RequestReportData.class);
                List<ControlData> list = new ArrayList<>();
                List<ReportDataModel> invoice_data = requestReportData.getInvoice_data();
                for (ReportDataModel reportDataModel : invoice_data) {
                    String str = reportDataModel.getManagerData();
                    Logger.i(reportDataModel.getInvoice_type_code() + "  " + str.length() + " " + str);
                    list.add(Util.parseControlDataStr(reportDataModel.getInvoice_type_code(), str));
                }
                FlowManager.getDatabase(TcsDatabase.class)
                        .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                                new ProcessModelTransaction.ProcessModel<ControlData>() {
                                    @Override
                                    public void processModel(ControlData model, DatabaseWrapper wrapper) {
                                        model.save();
                                    }
                                }).addAll(list).build())
                        .success(new Transaction.Success() {
                            @Override
                            public void onSuccess(@NonNull Transaction transaction) {
                                AppSP.putBoolean("ControlData", true);
                            }
                        })
                        .build()
                        .execute();
                break;

        }
        c++;
        complete();
    }


    @Override
    public void onFailure(String requestPath, String errorMes) {
        if (!autoSyn) {
            switch (errorMes) {
                case Api.ERR_NETWORK:
                    ToastUtil.showShort(getString(R.string.hit3));
                    break;
                case Api.FAIL_CONNECT:
                    ToastUtil.showShort(getString(R.string.hit4));
                    break;
            }
        }
        c++;
        if (requestPath.equals(ServerConstants.ATCS002)) c = 8;
        complete();
    }

    private void complete() {
        if (c == 8 && !autoSyn) {
            Subscription subscription = ObserverActionUtils.subscribe(0, MainActivity.class);
            if (subscription != null) subscription.unsubscribe();
            c = 0;
        }
    }


}
