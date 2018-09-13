package com.csscaps.tcs.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.csscaps.common.utils.AppTools;
import com.csscaps.common.utils.ConvertUtils;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.common.utils.NumberBytesUtil;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.R;
import com.csscaps.tcs.ServerConstants;
import com.csscaps.tcs.database.TcsDatabase;
import com.csscaps.tcs.database.table.ControlData;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.Invoice_Table;
import com.csscaps.tcs.database.table.ReportData;
import com.csscaps.tcs.database.table.ReportData_Table;
import com.csscaps.tcs.model.ReportDataModel;
import com.csscaps.tcs.model.RequestReportData;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.tax.fcr.library.network.Api;
import com.tax.fcr.library.network.IPresenter;
import com.tax.fcr.library.network.RequestModel;

import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.substring;
import static com.csscaps.tcs.database.table.ReportData_Table.date;
import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/6/26.
 */

public class ReportDataService extends Service implements IPresenter {

    private boolean fromOnlineDeclarationActivity;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null)
            fromOnlineDeclarationActivity = intent.getBooleanExtra("fromOnlineDeclarationActivity", false);
        report();
        return super.onStartCommand(intent, flags, startId);
    }

    private void report() {
        List<ControlData> listControlData = select().from(ControlData.class).queryList();
        if (listControlData.size() > 0) {
            ControlData controlData=listControlData.get(0);
            if (checkInvoiceUpload(controlData)) {
                if (fromOnlineDeclarationActivity) {
                    ToastUtil.showShort(getString(R.string.hit50));
                }
                return;
            }

            String dateNow = DateUtils.dateToStr(DateUtils.getDateNow(), DateUtils.format_yyyyMMdd);
            //未到数据报送终止日期，不能抄报
            if (DateUtils.compareDate(dateNow, controlData.getE_report_date(), DateUtils.format_yyyyMMdd) != 1) {
                if (fromOnlineDeclarationActivity) {
                    ToastUtil.showShort(getString(R.string.hit55));
                }
                return;
            }

            String date = TextUtils.substring(controlData.getS_report_date(), 0, 6);
            RequestReportData requestReportData = new RequestReportData();
            requestReportData.setFuncid(ServerConstants.ATCS023);
            requestReportData.setType("2");
            requestReportData.setDate(date);
            requestReportData.setInvoice_data(toReportData(requestReportData.getInvoice_data(), listControlData));
            RequestModel requestModel = new RequestModel();
            requestModel.setFuncid(requestReportData.getFuncid());
            requestReportData.setDevicesn(requestModel.getDevicesn());
            requestModel.setData(JSON.toJSONString(requestReportData));
            Api.post(this, requestModel);
        }
    }

    /**
     * 检查本期发票是否有未上传的
     *
     * @param controlData
     * @return true：是 false ：否
     */
    private boolean checkInvoiceUpload(ControlData controlData) {
        String likeStr = date + "%%";
        //数据报送起始日期
        String sDate=DateUtils.dateToStr(DateUtils.getStringToDate(controlData.getS_report_date(),DateUtils.format_yyyyMMdd), DateUtils.format_yyyyMMddHHmmss_24_EN);
        //数据报送终止日期
        String eDate=DateUtils.dateToStr(DateUtils.getStringToDate(controlData.getE_report_date(),DateUtils.format_yyyyMMdd), DateUtils.format_yyyyMMddHHmmss_24_EN);
        List<Invoice> list = select().from(Invoice.class).where(Invoice_Table.uploadStatus.eq(Invoice.FAILURE))
                .and(Invoice_Table.client_invoice_datetime.between(sDate).and(eDate))
                .queryList();
        if (list.size() > 0) return true;
        return false;
    }

    @Override
    public void onSuccess(String requestPath, String objectString) {
        RequestReportData requestReportData = JSON.parseObject(objectString, RequestReportData.class);
        if ("0".equals(requestReportData.getCode())) {
            List<ReportDataModel> invoice_data = requestReportData.getInvoice_data();
            List<ControlData> listControlData = new ArrayList<>();
            List<ReportData> listReportData = new ArrayList<>();
            for (ReportDataModel reportDataModel : invoice_data) {
                String invoice_type_code = reportDataModel.getInvoice_type_code();
                ReportData reportData = select().from(ReportData.class).where(ReportData_Table.invoice_type_code.eq(invoice_type_code)).querySingle();
                reportData.setReport_status("1");
               /* reportData.setTotal_all(0);
                reportData.setTotal_vat(0);
                reportData.setTotal_bpt(0);
                reportData.setTotal_fee(0);
                reportData.setTotal_stamp(0);
                reportData.setTotal_final(0);
                reportData.setTotal_bpt_preypayment(0);
                reportData.setTotal_taxable_amount(0);
                reportData.setN_total_all(0);
                reportData.setN_total_vat(0);
                reportData.setN_total_bpt(0);
                reportData.setN_total_fee(0);
                reportData.setN_total_stamp(0);
                reportData.setN_total_final(0);
                reportData.setN_total_taxable_amount(0);
                reportData.setInvoice_number(0);
                reportData.setC_invoice_number(0);
                reportData.setN_invoice_number(0);*/
                String str = reportDataModel.getManagerData();
                ControlData controlData = parseControlDataStr(invoice_type_code, str);
                reportData.setReport_date(controlData.getNew_date());
                listControlData.add(controlData);
                listReportData.add(reportData);
            }
            update(listReportData);
            update(listControlData);
        }
    }

    private <T extends BaseModel> void update(final List<T> list) {
        FlowManager.getDatabase(TcsDatabase.class)
                .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                        new ProcessModelTransaction.ProcessModel<T>() {
                            @Override
                            public void processModel(T model, DatabaseWrapper wrapper) {
                                model.save();
                            }
                        }).addAll(list).build())
                .error(new Transaction.Error() {
                    @Override
                    public void onError(@NonNull Transaction transaction, @NonNull Throwable error) {
                        update(list);
                    }
                })
                .build()
                .execute();
    }

    @Override
    public void onFailure(String requestPath, String errorMes) {
        if (fromOnlineDeclarationActivity) {
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

    /**
     * 拼接上报数据
     *
     * @return
     */
    private List<ReportDataModel> toReportData(List<ReportDataModel> invoice_data, List<ControlData> listControlData) {
        for (ControlData controlData : listControlData) {
            ReportData reportData = select().from(ReportData.class)
                    .where(ReportData_Table.invoice_type_code.eq(controlData.getInvoice_type_code()))
                    .and(ReportData_Table.date.eq(substring(controlData.getS_report_date(), 0, 6))).querySingle();

            StringBuffer sb = new StringBuffer();
            if (reportData != null) {
                sb.append(controlData.getS_report_date())
                        .append(controlData.getE_report_date())
                        .append(doubleToHex(reportData.getTotal_all()))
                        .append(doubleToHex(reportData.getTotal_vat()))
                        .append(doubleToHex(reportData.getTotal_stamp()))
                        .append(doubleToHex(reportData.getTotal_bpt()))
                        .append(doubleToHex(reportData.getTotal_bpt_preypayment()))
                        .append(doubleToHex(reportData.getTotal_final()))
                        .append(doubleToHex(reportData.getTotal_fee()))
                        .append(doubleToHex(reportData.getTotal_taxable_amount()))
                        .append(doubleToHex(reportData.getN_total_all()))
                        .append(doubleToHex(reportData.getN_total_taxable_amount()))
                        .append(doubleToHex(reportData.getN_total_vat()))
                        .append(doubleToHex(reportData.getN_total_stamp()))
                        .append(doubleToHex(reportData.getN_total_bpt()))
                        .append(doubleToHex(reportData.getN_total_bpt_preypayment()))
                        .append(doubleToHex(reportData.getN_total_fee()))
                        .append(doubleToHex(reportData.getN_total_final()))
                        .append(ConvertUtils.bytes2HexString(NumberBytesUtil.intToBytes(reportData.getInvoice_number())))
                        .append(ConvertUtils.bytes2HexString(NumberBytesUtil.intToBytes(reportData.getN_invoice_number())))
                        .append(ConvertUtils.bytes2HexString(NumberBytesUtil.intToBytes(reportData.getC_invoice_number())));

                ReportDataModel reportDataModel = new ReportDataModel();
                reportDataModel.setInvoice_type_code(controlData.getInvoice_type_code());
                reportDataModel.setSummary_data(sb.toString());
                invoice_data.add(reportDataModel);
            }
        }
        return invoice_data;
    }

    /**
     * hex 6字节
     *
     * @param amount
     * @return
     */
    private String doubleToHex(double amount) {
        long a = (long) (amount * 100);
        String hex = ConvertUtils.bytes2HexString(NumberBytesUtil.longToBytes(a));
        return substring(hex, 4, 16);
    }

    /**
     * 解析控制数据
     *
     * @return
     */
    public static ControlData parseControlDataStr(String invoice_type_code, String controlDataStr) {
        ControlData controlData = new ControlData();
        controlData.setInvoice_type_code(invoice_type_code);
        controlData.setNew_date(substring(controlDataStr, 6, 14));
        controlData.setIssuing_last_date(substring(controlDataStr, 14, 22));
        controlData.setTotal_amount_perinvoice(NumberBytesUtil.bytesToLong(ConvertUtils.hexString2Bytes(AppTools.fillZero(substring(controlDataStr, 22, 34), 16))) / 100);
        controlData.setTotal_all(NumberBytesUtil.bytesToLong(ConvertUtils.hexString2Bytes(AppTools.fillZero(substring(controlDataStr, 34, 46), 16))) / 100);
        controlData.setN_total_all(NumberBytesUtil.bytesToLong(ConvertUtils.hexString2Bytes(AppTools.fillZero(substring(controlDataStr, 46, 58), 16))) / 100);
        controlData.setReport_flag(substring(controlDataStr, 58, 59));
        controlData.setTcs_pwd_flag(substring(controlDataStr, 59, 60));
        controlData.setS_report_date(substring(controlDataStr, 60, 68));
        controlData.setE_report_date(substring(controlDataStr, 68, 76));
        controlData.setInvoice_type(substring(controlDataStr, 76, 78));
        controlData.setCheck_tax_control_panel(substring(controlDataStr, 78, 79));
        controlData.setCheck_tax_control_panel_n_invoice(substring(controlDataStr, 79, 80));
        controlData.setRfu(substring(controlDataStr, 80, 82));
        controlData.setTax_type_item_index(NumberBytesUtil.bytesToLong(ConvertUtils.hexString2Bytes(AppTools.fillZero(substring(controlDataStr, 82, 94), 16))));
        controlData.setIssuing_n_invoice_days(NumberBytesUtil.bytesToInt(ConvertUtils.hexString2Bytes(AppTools.fillZero(substring(controlDataStr, 94, 98), 8))));
        controlData.setIssuing_invoice_type(String.valueOf(NumberBytesUtil.bytesToInt(ConvertUtils.hexString2Bytes(AppTools.fillZero(substring(controlDataStr, 98, 100), 8)))));
        controlData.setInvoice_number_permonth(NumberBytesUtil.bytesToInt(ConvertUtils.hexString2Bytes(substring(controlDataStr, 100, 108))));
        return controlData;
    }
}
