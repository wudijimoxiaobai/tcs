package com.csscaps.tcs;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.csscaps.common.utils.AppSP;
import com.csscaps.common.utils.AppTools;
import com.csscaps.common.utils.ConvertUtils;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.common.utils.NumberBytesUtil;
import com.csscaps.tcs.database.DailyDatabase;
import com.csscaps.tcs.database.table.ControlData;
import com.csscaps.tcs.database.table.Daily;
import com.csscaps.tcs.database.table.Daily_Table;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.ReportData;
import com.csscaps.tcs.database.table.ReportData_Table;
import com.csscaps.tcs.model.Receive3FKey;
import com.csscaps.tcs.model.Request3FKey;
import com.csscaps.tcs.psam.PSAMUtil;
import com.raizlabs.android.dbflow.sql.language.Where;
import com.tax.fcr.library.network.Api;
import com.tax.fcr.library.network.IPresenter;
import com.tax.fcr.library.network.RequestModel;
import com.tax.fcr.library.utils.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static android.text.TextUtils.substring;
import static com.csscaps.common.utils.ConvertUtils.hexString2Bytes;
import static com.csscaps.tcs.SdcardDBUtil.isWriteSD;
import static com.csscaps.tcs.SdcardDBUtil.lock;
import static com.csscaps.tcs.SdcardDBUtil.querySingleFromSD;
import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/11/9.
 */

public class Util {


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
        controlData.setTotal_amount_perinvoice(NumberBytesUtil.bytesToLong(hexString2Bytes(AppTools.fillZero(substring(controlDataStr, 22, 34), 16))) / 100);
        controlData.setTotal_all(NumberBytesUtil.bytesToLong(hexString2Bytes(AppTools.fillZero(substring(controlDataStr, 34, 46), 16))) / 100);
        controlData.setN_total_all(NumberBytesUtil.bytesToLong(hexString2Bytes(AppTools.fillZero(substring(controlDataStr, 46, 58), 16))) / 100);
        controlData.setReport_flag(substring(controlDataStr, 58, 59));
        controlData.setTcs_pwd_flag(substring(controlDataStr, 59, 60));
        controlData.setS_report_date(substring(controlDataStr, 60, 68));
        controlData.setE_report_date(substring(controlDataStr, 68, 76));
        controlData.setInvoice_type(substring(controlDataStr, 76, 78));
        controlData.setCheck_tax_control_panel(substring(controlDataStr, 78, 79));
        controlData.setCheck_tax_control_panel_n_invoice(substring(controlDataStr, 79, 80));
        controlData.setRfu(substring(controlDataStr, 80, 82));
        controlData.setTax_type_item_index(NumberBytesUtil.bytesToLong(hexString2Bytes(AppTools.fillZero(substring(controlDataStr, 82, 94), 16))));
        controlData.setIssuing_n_invoice_days(NumberBytesUtil.bytesToInt(hexString2Bytes(AppTools.fillZero(substring(controlDataStr, 94, 98), 8))));
        controlData.setIssuing_invoice_type(String.valueOf(NumberBytesUtil.bytesToInt(hexString2Bytes(AppTools.fillZero(substring(controlDataStr, 98, 100), 8)))));
        controlData.setInvoice_number_permonth(NumberBytesUtil.bytesToInt(hexString2Bytes(substring(controlDataStr, 100, 108))));
        return controlData;
    }

    /**
     * 签名发票
     *
     * @param invoice
     */
    public static void signInvoice(Invoice invoice) {
        String yyyyMMddHHmmss = invoice.getClient_invoice_datetime();
        String date = yyyyMMddHHmmss.substring(0, 8);
        String time = yyyyMMddHHmmss.substring(8, 14);
        StringBuffer sb = new StringBuffer();
        sb.append(ConvertUtils.bytes2HexString(AppTools.str2Bcd(date)));
        sb.append(doubleToHex(Double.valueOf(invoice.getTotal_all())));
        sb.append(doubleToHex(Double.valueOf(invoice.getTotal_vat())));
        sb.append(doubleToHex(Double.valueOf(invoice.getTotal_bpt_preypayment())));
        sb.append(doubleToHex(Double.valueOf(invoice.getTotal_bpt())));
        sb.append(doubleToHex(Double.valueOf(invoice.getTotal_stamp())));
        sb.append(doubleToHex(Double.valueOf(invoice.getTotal_fee())));
        sb.append(doubleToHex(Double.valueOf(invoice.getTotal_final())));
        sb.append(doubleToHex(Double.valueOf(invoice.getTotal_taxable_amount())));
        sb.append(AppTools.fillZero(ConvertUtils.bytes2HexString(NumberBytesUtil.longToBytes(Long.valueOf(invoice.getSeller_tin()))), 26));
        if (TextUtils.isEmpty(invoice.getPurchaser_tin())) {
            sb.append(AppTools.fillZero("0", 26));
        } else
            sb.append(AppTools.fillZero(ConvertUtils.bytes2HexString(NumberBytesUtil.longToBytes(Long.valueOf(invoice.getPurchaser_tin()))), 26));
        sb.append(AppTools.fillZero("0", 26));
        sb.append(ConvertUtils.bytes2HexString(AppTools.str2Bcd(time)));
        sb.append(ConvertUtils.bytes2HexString(NumberBytesUtil.longToBytes(Long.valueOf(invoice.getInvoice_type_code()))).substring(6));
        sb.append(AppTools.fillZero(ConvertUtils.bytes2HexString(NumberBytesUtil.longToBytes(Long.valueOf(invoice.getInvoice_no()))), 20));
        byte[] sign = PSAMUtil.RSASign(hexString2Bytes(sb.toString()));
        if (sign != null) {
            invoice.setFiscal_long_code(ConvertUtils.base64Encode2String(sign));
        }

    }


    /**
     * 更新抄报数据 和日志
     *
     * @param invoice
     */
    public static void updateReportDataDaily(final Invoice invoice) {

        SdcardDBUtil.cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    String invoice_type_code = invoice.getInvoice_type_code();
                    String client_invoice_datetime = invoice.getClient_invoice_datetime();
                    String date = substring(client_invoice_datetime, 0, 6);
                    ReportData reportData = select().from(ReportData.class).where(ReportData_Table.invoice_type_code.eq(invoice_type_code)).and(ReportData_Table.date.eq(date)).querySingle();

                    String daily_date = DateUtils.dateToStr(RTCUtil.getRTC(), DateUtils.format_yyyyMMdd);
                    Where where = select().from(Daily.class).where(Daily_Table.invoice_type_code.eq(invoice_type_code)).and(Daily_Table.date.eq(daily_date)).orderBy(Daily_Table.id, false).limit(1);
                    Daily daily = (Daily) querySingleFromSD(where);


                    if (daily == null) {
                        daily = new Daily();
                        daily.setInvoice_type_code(invoice_type_code);
                        daily.setDate(daily_date);
                        daily.setS_invoice_no(invoice.getInvoice_no());
                        where = select().from(Daily.class).orderBy(Daily_Table.addr, false).limit(1);
                        Daily daily1 = (Daily) querySingleFromSD(where);
                        if (daily1 == null) {
                            daily.setAddr(0);
                        } else {
                            int d = daily1.getAddr() + 170;
                            if (d + 170 > FMUtil.getFMCapability()) d = 0;
                            daily.setAddr(d);
                        }
                    }

//                    where = select().from(Daily.class).where(Daily_Table.date.notEq(daily_date));
//                    deleteFromSD(where);

                    if (reportData == null) {
                        reportData = new ReportData();
                        reportData.setInvoice_type_code(invoice_type_code);
                        reportData.setDate(date);
                    }

                    switch (invoice.getStatus()) {
                        case "AVL"://正常
                            reportData.setTotal_all(Double.valueOf(invoice.getTotal_all()) + reportData.getTotal_all());
                            reportData.setTotal_vat(Double.valueOf(invoice.getTotal_vat()) + reportData.getTotal_vat());
                            reportData.setTotal_bpt(Double.valueOf(invoice.getTotal_bpt()) + reportData.getTotal_bpt());
                            reportData.setTotal_fee(Double.valueOf(invoice.getTotal_fee()) + reportData.getTotal_fee());
                            reportData.setTotal_stamp(Double.valueOf(invoice.getTotal_stamp()) + reportData.getTotal_stamp());
                            reportData.setTotal_final(Double.valueOf(invoice.getTotal_final()) + reportData.getTotal_final());
                            reportData.setTotal_bpt_preypayment(Double.valueOf(invoice.getTotal_bpt_preypayment()) + reportData.getTotal_bpt_preypayment());
                            reportData.setTotal_taxable_amount(Double.valueOf(invoice.getTotal_taxable_amount()) + reportData.getTotal_taxable_amount());
                            reportData.setInvoice_number(reportData.getInvoice_number() + 1);

                            daily.setTotal_all(Double.valueOf(invoice.getTotal_all()) + daily.getTotal_all());
                            daily.setTotal_vat(Double.valueOf(invoice.getTotal_vat()) + daily.getTotal_vat());
                            daily.setTotal_bpt(Double.valueOf(invoice.getTotal_bpt()) + daily.getTotal_bpt());
                            daily.setTotal_fee(Double.valueOf(invoice.getTotal_fee()) + daily.getTotal_fee());
                            daily.setTotal_stamp(Double.valueOf(invoice.getTotal_stamp()) + daily.getTotal_stamp());
                            daily.setTotal_final(Double.valueOf(invoice.getTotal_final()) + daily.getTotal_final());
                            daily.setTotal_bpt_preypayment(Double.valueOf(invoice.getTotal_bpt_preypayment()) + daily.getTotal_bpt_preypayment());
                            daily.setTotal_taxable_amount(Double.valueOf(invoice.getTotal_taxable_amount()) + daily.getTotal_taxable_amount());
                            daily.setInvoice_count(daily.getInvoice_count() + 1);
                            break;
                        case "NEG"://负数
                            reportData.setN_total_all(Double.valueOf(invoice.getTotal_all()) + reportData.getN_total_all());
                            reportData.setN_total_vat(Double.valueOf(invoice.getTotal_vat()) + reportData.getN_total_vat());
                            reportData.setN_total_bpt(Double.valueOf(invoice.getTotal_bpt()) + reportData.getN_total_bpt());
                            reportData.setN_total_fee(Double.valueOf(invoice.getTotal_fee()) + reportData.getN_total_fee());
                            reportData.setN_total_stamp(Double.valueOf(invoice.getTotal_stamp()) + reportData.getN_total_stamp());
                            reportData.setN_total_final(Double.valueOf(invoice.getTotal_final()) + reportData.getN_total_final());
                            reportData.setN_total_bpt_preypayment(Double.valueOf(invoice.getTotal_bpt_preypayment()) + reportData.getN_total_bpt_preypayment());
                            reportData.setN_total_taxable_amount(Double.valueOf(invoice.getTotal_taxable_amount()) + reportData.getN_total_taxable_amount());
                            reportData.setN_invoice_number(reportData.getN_invoice_number() + 1);

                            daily.setN_total_all(Double.valueOf(invoice.getTotal_all()) + daily.getN_total_all());
                            daily.setN_total_vat(Double.valueOf(invoice.getTotal_vat()) + daily.getN_total_vat());
                            daily.setN_total_bpt(Double.valueOf(invoice.getTotal_bpt()) + daily.getN_total_bpt());
                            daily.setN_total_fee(Double.valueOf(invoice.getTotal_fee()) + daily.getN_total_fee());
                            daily.setN_total_stamp(Double.valueOf(invoice.getTotal_stamp()) + daily.getN_total_stamp());
                            daily.setN_total_final(Double.valueOf(invoice.getTotal_final()) + daily.getN_total_final());
                            daily.setN_total_bpt_preypayment(Double.valueOf(invoice.getTotal_bpt_preypayment()) + daily.getN_total_bpt_preypayment());
                            daily.setN_total_taxable_amount(Double.valueOf(invoice.getTotal_taxable_amount()) + daily.getN_total_taxable_amount());
                            daily.setN_invoice_count(daily.getN_invoice_count() + 1);
                            break;
                        case "DISA"://作废
                            if (invoice.getRequestDate().equals("0")) {
                                daily.setC_total_all(Double.valueOf(invoice.getTotal_all()) + daily.getC_total_all());
                                daily.setC_total_vat(Double.valueOf(invoice.getTotal_vat()) + daily.getC_total_vat());
                                daily.setC_total_bpt(Double.valueOf(invoice.getTotal_bpt()) + daily.getC_total_bpt());
                                daily.setC_total_fee(Double.valueOf(invoice.getTotal_fee()) + daily.getC_total_fee());
                                daily.setC_total_stamp(Double.valueOf(invoice.getTotal_stamp()) + daily.getC_total_stamp());
                                daily.setC_total_final(Double.valueOf(invoice.getTotal_final()) + daily.getC_total_final());
                                daily.setC_total_bpt_preypayment(Double.valueOf(invoice.getTotal_bpt_preypayment()) + daily.getC_total_bpt_preypayment());
                                daily.setC_total_tax_due(Double.valueOf(invoice.getTotal_tax_due()) + daily.getC_total_tax_due());
                                daily.setC_invoice_count(daily.getC_invoice_count() + 1);
                            } else if (invoice.getRequestDate().equals("1")) {
                                reportData.setC_invoice_number(reportData.getC_invoice_number() + 1);
                            }
                            break;
                    }

                    reportData.save();
                    saveDailyToFM(daily);
//                    daily.setId(0);
                    SdcardDBUtil.saveSDDB(daily, DailyDatabase.class);
                }
            }
        });
    }


    /**
     * hex 6字节
     *
     * @param amount
     * @return
     */
    public static String doubleToHex(double amount) {
        long a = (long) (amount * 100);
        String hex = ConvertUtils.bytes2HexString(NumberBytesUtil.longToBytes(a));
        return substring(hex, 4, 16);
    }

    /**
     * 应用主控秘钥
     */
    public static void get3f_pwd_key() {
        RequestModel requestModel = new RequestModel();
        requestModel.setFuncid(ServerConstants.AP003);
        Request3FKey request3FKey = new Request3FKey();
        request3FKey.setDevicesn(requestModel.getDevicesn());
        request3FKey.setFuncid(ServerConstants.AP003);
        requestModel.setData(JSON.toJSONString(request3FKey));
        Api.post(new IPresenter() {
            @Override
            public void onSuccess(String requestPath, String objectString) {
                Receive3FKey receive3FKey = JSON.parseObject(objectString, Receive3FKey.class);
                AppSP.putString("3f_key", receive3FKey.getNew_3fpwd_key());
            }

            @Override
            public void onFailure(String requestPath, String errorMes) {

            }
        }, requestModel);
    }


    /**
     * 保存日报到FM
     *
     * @param daily
     */
    private static void saveDailyToFM(final Daily daily) {
        StringBuffer sb = new StringBuffer();
        sb.append(ConvertUtils.bytes2HexString(AppTools.str2Bcd(daily.getDate())));
        sb.append(ConvertUtils.bytes2HexString(NumberBytesUtil.intToBytes(daily.getInvoice_count())).substring(4));
        sb.append(doubleToHex(daily.getTotal_all()));
        sb.append(doubleToHex(daily.getTotal_taxable_amount()));
        sb.append(doubleToHex(daily.getTotal_vat()));
        sb.append(doubleToHex(daily.getTotal_bpt_preypayment()));
        sb.append(doubleToHex(daily.getTotal_bpt()));
        sb.append(doubleToHex(daily.getTotal_stamp()));
        sb.append(doubleToHex(daily.getTotal_fee()));
        sb.append(doubleToHex(daily.getTotal_final()));

        sb.append(ConvertUtils.bytes2HexString(NumberBytesUtil.intToBytes(daily.getN_invoice_count())).substring(4));
        sb.append(doubleToHex(daily.getN_total_all()));
        sb.append(doubleToHex(daily.getN_total_taxable_amount()));
        sb.append(doubleToHex(daily.getN_total_vat()));
        sb.append(doubleToHex(daily.getN_total_bpt_preypayment()));
        sb.append(doubleToHex(daily.getN_total_bpt()));
        sb.append(doubleToHex(daily.getN_total_stamp()));
        sb.append(doubleToHex(daily.getN_total_fee()));
        sb.append(doubleToHex(daily.getN_total_final()));

        sb.append(ConvertUtils.bytes2HexString(NumberBytesUtil.intToBytes(daily.getC_invoice_count())).substring(4));
        sb.append(doubleToHex(daily.getC_total_all()));
        sb.append(doubleToHex(daily.getC_total_tax_due()));
        sb.append(doubleToHex(daily.getC_total_vat()));
        sb.append(doubleToHex(daily.getC_total_bpt_preypayment()));
        sb.append(doubleToHex(daily.getC_total_bpt()));
        sb.append(doubleToHex(daily.getC_total_stamp()));
        sb.append(doubleToHex(daily.getC_total_fee()));
        sb.append(doubleToHex(daily.getC_total_final()));

        sb.append(ConvertUtils.bytes2HexString(NumberBytesUtil.longToBytes(Long.valueOf(daily.getInvoice_type_code()))));
        sb.append(ConvertUtils.bytes2HexString(NumberBytesUtil.longToBytes(Long.valueOf(daily.getS_invoice_no()))));
        Logger.i(daily.getAddr() + "-- -- " + sb.toString());
        byte b[] = ConvertUtils.hexString2Bytes(sb.toString());
        FMUtil.writeFM(daily.getAddr(), b);
    }


    public static void writeSDFile(String str) {
        SdcardUtil.unlockSdcard();
        //数据库文件是否可以打开
        int status = SdcardUtil.checkLockSdcardStatus();
        if (status == 0) {//已解锁
            boolean isOk = true;
            while (isOk) {
                //SD卡是否可读写
                if (!isWriteSD()) continue;
                isOk = false;
            }
            Map<String, String> map = System.getenv();
            String SDPath = map.get("SECONDARY_STORAGE");
            File sdFile = new File(SDPath + "/FCR/daily.txt");
            FileWriter fileWriter = null;

            try {
                if (!sdFile.exists()) {
                    sdFile.createNewFile();
                }
                fileWriter = new FileWriter(sdFile, false);
                fileWriter.write(str);
                fileWriter.flush();
                fileWriter.close();
            } catch (Exception e) {
                try {
                    if (fileWriter != null) fileWriter.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            SdcardUtil.lockSdcard();
        } else if (status == -1) {//解锁失败
            writeSDFile(str);
        }
    }


    public static String readSDFile() {
        SdcardUtil.unlockSdcard();
        //数据库文件是否可以打开
        int status = SdcardUtil.checkLockSdcardStatus();
        if (status == 0) {//已解锁
            boolean isOk = true;
            while (isOk) {
                //SD卡是否可读写
                if (!isWriteSD()) continue;
                isOk = false;
            }
            Map<String, String> map = System.getenv();
            String SDPath = map.get("SECONDARY_STORAGE");
            File sdFile = new File(SDPath + "/FCR/daily.txt");
            FileReader fileReader = null;

            try {
                if (!sdFile.exists()) {
                    sdFile.createNewFile();
                }
                fileReader = new FileReader(sdFile);
                char[] buf = new char[1024];
                int num = 0;
                StringBuffer sb = new StringBuffer();
                while ((num = fileReader.read(buf)) != -1) {
                    sb.append(buf, 0, num);
                }

                return sb.toString();
            } catch (Exception e) {
                try {
                    if (fileReader != null) fileReader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            SdcardUtil.lockSdcard();
        } else if (status == -1) {//解锁失败
            readSDFile();
        }
        return null;
    }

}
