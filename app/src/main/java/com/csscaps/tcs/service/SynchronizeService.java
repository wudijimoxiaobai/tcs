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
import com.csscaps.tcs.ServerConstants;
import com.csscaps.tcs.database.TcsDatabase;
import com.csscaps.tcs.database.table.ControlData;
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
import com.csscaps.tcs.model.ReportDataModel;
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
        synInvoiceType();
        synTaxType();
        synTaxItem();
        synInvoiceTaxType();
        synTaxMethod();
        if (!AppSP.getBoolean("ControlData")) synControlData();
        c = 0;
        new MyTimerTask().run();
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
            case ServerConstants.ATCS023:
                RequestReportData requestReportData = JSON.parseObject(objectString, RequestReportData.class);
                List<ControlData> list = new ArrayList<>();
                List<ReportDataModel> invoice_data = requestReportData.getInvoice_data();
                for (ReportDataModel reportDataModel : invoice_data) {
                    String str = reportDataModel.getManagerData();
                    Logger.i(reportDataModel.getInvoice_type_code() + "  " +str.length()+" " +str);
                    list.add(ReportDataService.parseControlDataStr(reportDataModel.getInvoice_type_code(),str));
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

  /*  *//**
     * 解析控制数据
     *
     * @return
     *//*
    private ControlData parseControlDataStr(String invoice_type_code,String controlDataStr) {
        ControlData controlData = new ControlData();
        controlData.setInvoice_type_code(invoice_type_code);
        controlData.setNew_date(TextUtils.substring(controlDataStr,6,14));
        controlData.setIssuing_last_date(TextUtils.substring(controlDataStr,14,22));
        controlData.setTotal_amount_perinvoice(NumberBytesUtil.bytesToLong(ConvertUtils.hexString2Bytes(AppTools.fillZero(TextUtils.substring(controlDataStr,22,34),16))));
        controlData.setTotal_all(NumberBytesUtil.bytesToLong(ConvertUtils.hexString2Bytes(AppTools.fillZero(TextUtils.substring(controlDataStr,34,46),16))));
        controlData.setN_total_all(NumberBytesUtil.bytesToLong(ConvertUtils.hexString2Bytes(AppTools.fillZero(TextUtils.substring(controlDataStr,46,58),16))));
        controlData.setReport_flag(TextUtils.substring(controlDataStr,58,59));
        controlData.setTcs_pwd_flag(TextUtils.substring(controlDataStr,59,60));
        controlData.setS_report_date(TextUtils.substring(controlDataStr,60,68));
        controlData.setE_report_date(TextUtils.substring(controlDataStr,68,76));
        controlData.setInvoice_type(TextUtils.substring(controlDataStr,76,78));
        controlData.setCheck_tax_control_panel(TextUtils.substring(controlDataStr,78,79));
        controlData.setCheck_tax_control_panel_n_invoice(TextUtils.substring(controlDataStr,79,80));
        controlData.setRfu(TextUtils.substring(controlDataStr,80,82));
        controlData.setTax_type_item_index(NumberBytesUtil.bytesToLong(ConvertUtils.hexString2Bytes(AppTools.fillZero(TextUtils.substring(controlDataStr,82,94),16))));
        controlData.setIssuing_n_invoice_days(NumberBytesUtil.bytesToInt(ConvertUtils.hexString2Bytes(AppTools.fillZero(TextUtils.substring(controlDataStr,94,98),8))));
        controlData.setIssuing_invoice_type(String.valueOf(NumberBytesUtil.bytesToInt(ConvertUtils.hexString2Bytes(AppTools.fillZero(TextUtils.substring(controlDataStr,98,100),8)))));
        controlData.setInvoice_number_permonth(NumberBytesUtil.bytesToInt(ConvertUtils.hexString2Bytes(TextUtils.substring(controlDataStr,100,108))));
        return controlData;
    }*/

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
        complete();
    }

    private void complete() {
        if (c == 6 && !autoSyn) {
            Subscription subscription = ObserverActionUtils.subscribe(0, MainActivity.class);
            if (subscription != null) subscription.unsubscribe();
            c = 0;
        }
    }


}
