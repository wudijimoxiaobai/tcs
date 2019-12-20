package com.csscaps.tcs;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.csscaps.common.utils.AppSP;
import com.csscaps.common.utils.AppTools;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.common.utils.QRCodeUtil;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.Product;
import com.csscaps.tcs.database.table.ProductModel;
import com.csscaps.tcs.database.table.ProductModel_Table;
import com.csscaps.tcs.model.Buyer;
import com.csscaps.tcs.model.Head;
import com.csscaps.tcs.model.InvoiceData;
import com.csscaps.tcs.model.ItemName;
import com.csscaps.tcs.model.MyTaxpayer;
import com.csscaps.tcs.model.QD;
import com.csscaps.tcs.model.Seller;
import com.suwell.ofd.render.util.BitmapUtil;
import com.tax.fcr.library.network.RequestModel;
import com.tax.fcr.library.utils.Logger;
import com.tax.fcr.library.utils.SecurityUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/7/10.
 * 生成ofd发票数据源 xml
 */

public class GeneratingXMLFileUtils {
    private static final String HEADER = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>";

    public static void generatingXmlFile(Invoice showInvoice, String dataXmlPath) {
        InvoiceData invoiceData = new InvoiceData();

        Head head = new Head();
        head.setINVOICECODE(showInvoice.getInvoice_type_code());
        head.setDEVICENO(RequestModel.devicesn);
        head.setINVOICENUMBER(showInvoice.getInvoice_no());
        head.setPRINTINGN("hp14320001");
        head.setADMIN(showInvoice.getDrawer_name());
        head.setISSUANCEDATE(DateUtils.dateToStr(DateUtils.getStringToDate(showInvoice.getClient_invoice_datetime(), DateUtils.format_yyyyMMddHHmmss_24_EN), DateUtils.format_yyyy_MM_dd_HH_mm_ss_24_EN));
        invoiceData.setHead(head);

        Seller seller = new Seller();
        seller.setTIN(showInvoice.getSeller_tin());
        seller.setNAME(showInvoice.getSeller_name());
        if (showInvoice.getSeller_address().length() > 15) {
            seller.setADDRESS(showInvoice.getSeller_address().substring(0, 15));
            if (showInvoice.getSeller_address().substring(15).length() > 15) {
                seller.setADDRESSTWO(showInvoice.getSeller_address().substring(15, 30));
            } else {
                seller.setADDRESSTWO(showInvoice.getSeller_address().substring(15));
            }
        } else {
            seller.setADDRESS(showInvoice.getSeller_address());
        }
        // seller.setBRANCH(showInvoice.getSeller_branch_addr());
        seller.setPHONENO(showInvoice.getSeller_phone());
        //  seller.setCashier(showInvoice.getDrawer_name());
        invoiceData.setSeller(seller);

        Buyer buyer = new Buyer();
        buyer.setTIN(showInvoice.getPurchaser_tin());
        buyer.setPHONENO(showInvoice.getPurchaser_phone());
        buyer.setNAME(showInvoice.getPurchaser_name());
        buyer.setADDRESS(showInvoice.getPurchaser_address());
        buyer.setMoney(String.format("%.2f", (Double.valueOf(showInvoice.getTotal_all()) - Double.valueOf(showInvoice.getTotal_fee()))));
        //    buyer.setNATIONALID(showInvoice.getPurchaser_id_number());
        invoiceData.setBuyer(buyer);

        //总税
        // invoiceData.setTotalofVAT(String.format("%.2f", (Double.valueOf(showInvoice.getTotal_tax_due()) - Double.valueOf(showInvoice.getTotal_fee()))));
        invoiceData.setTotalofVAT(String.format("%.2f", Double.valueOf(showInvoice.getTotal_vat())));
        //invoiceData.setTotasetTotalofVATlofBPTFinal(showInvoice.getTotal_bpt());
        invoiceData.setTotalofBPTPrepayment(showInvoice.getTotal_bpt_preypayment());
        invoiceData.setTotalofStampDuty_Local(showInvoice.getTotal_stamp());
        invoiceData.setTotalofStamDuty_Federal(showInvoice.getTotal_final());
        invoiceData.setTotalofFees(showInvoice.getTotal_fee());
        invoiceData.setTotalAmountExcl(showInvoice.getTotal_taxable_amount());
        //invoiceData.setTotalAmountOtherTax(showInvoice.);todo
        invoiceData.setTotalAmountExclIncl(String.format("%.2f", (Double.valueOf(showInvoice.getTotal_all()) - Double.valueOf(showInvoice.getTotal_fee()))));
        invoiceData.setRemark(showInvoice.getRemark());
        invoiceData.setIssuedby(showInvoice.getDrawer_name());
        invoiceData.setRWM(getQRCodeToString(showInvoice));
        invoiceData.setTitle(showInvoice.getSeller_name());
        invoiceData.setTotalAmountOtherTax(String.format("%.2f", Double.valueOf(showInvoice.getTotal_final()) + Double.valueOf(showInvoice.getTotal_stamp()) + Double.valueOf(showInvoice.getTotal_bpt()) + Double.valueOf(showInvoice.getTotal_bpt_preypayment()) + Double.valueOf(showInvoice.getTotal_fee())));

        String myTaxpayerString = AppSP.getString("MyTaxpayer");
        if (!TextUtils.isEmpty(myTaxpayerString)) {
            MyTaxpayer myTaxpayer = JSON.parseObject(myTaxpayerString, MyTaxpayer.class);
            invoiceData.setTips(myTaxpayer.getInvoice_tips());
        }

        String invoiceNo = showInvoice.getInvoice_no();
        List<ProductModel> productModels = select().from(ProductModel.class).where(ProductModel_Table.invoice_no.eq(invoiceNo)).queryList();
        if (productModels.size() == 0) {
            List<Product> products = showInvoice.getProducts();
            for (int i = 0; i < products.size(); i++) {
                Product p = products.get(i);
                ProductModel productModel = p.getProductModel();
                productModel.setLine_no(String.valueOf(i + 1));
                productModels.add(productModel);
            }
        }
        invoiceData.setItemNumber(String.valueOf(productModels.size()));


        String rStr = productModels.get(0).getTax_rate();
        float r = TextUtils.isEmpty(rStr) ? 0 : Float.valueOf(rStr);
        DecimalFormat df = new DecimalFormat("0.00");
        DecimalFormat df2 = new DecimalFormat("0");
        List<QD> QDs = new ArrayList<>();
        invoiceData.setQDs(QDs);

        for (int i = 0; i < productModels.size(); i++) {
            ProductModel productModel = productModels.get(i);
            String name = productModel.getItem_name();
//            if (name.length() > 17) {
//                QD qd1 = new QD();
//                ItemName itemName = new ItemName();
//                itemName.setName(name);
//                itemName.setExtendSize("true");
//                qd1.setItemName(itemName);
//                qd1.setUNITPRICE("");
//                qd1.setQTY("");
//                qd1.setAMOUNT("");
//                QDs.add(qd1);
//            }

            QD qd = new QD();
            ItemName itemName = new ItemName();
            itemName.setName(name);
            itemName.setExtendSize("false");
            if (name.length() < 7) {
                itemName.setName(name);
            } else {
                itemName.setName(name.substring(0, 7) + "...");
            }
            qd.setItemName(itemName);
            qd.setUNITPRICE(productModel.getUnit_price());
            qd.setQTY(productModel.getQty());
            qd.setAMOUNT(productModel.getTaxable_amount());
            // qd.setVAT(String.format("%s%%", productModel.getVat()));
            // qd.setVAT(String.format("%s%%", df2.format(productModel.getVat())));
            //todo  VAT税率
            QDs.add(qd);
        }

        XStream xStream = new XStream(new DomDriver());
        xStream.processAnnotations(InvoiceData.class);
        xStream.alias("ITEMNAME", String.class);
        File dataXmlFile = new File(dataXmlPath);
        try {
            if (!dataXmlFile.exists()) {
                dataXmlFile.createNewFile();
            }
            String xmlData = HEADER
                    + System.getProperty("line.separator")
                    + xStream.toXML(invoiceData);
            FileWriter fileWriter = new FileWriter(dataXmlFile);
            xmlData = xmlData.replaceAll("\\<name\\>", "");
            xmlData = xmlData.replaceAll("\\</name\\>", "");
            fileWriter.write(xmlData);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 版本号（1位HEX，目前是0x01）开票日期（yyyyMMdd，4字节BCD码）+含税总金额（6位HEX，没有小数位，不足左侧补0x00）
    // +不含税总金额（6位HEX，没有小数位，不足左侧补0x00）+销方tin（13位HEX，不足左侧补0x00）+购方tin（13位HEX，不足左侧补0x00）
    // +开票时间（HHmmss，3字节BCD码）+设备Sn号（7位BCD码，明文是14位数字）+发票代码（5位HEX，不足左侧补0x00）
    // +发票号码（10位HEX，不足左侧补0x00），总共68个字节
    private static String getQRCodeToString(Invoice showInvoice) {
        byte[] bytes = new byte[68];
        StringBuffer sb = new StringBuffer("01");

        //日期yyyyMMdd
        String str = TextUtils.substring(showInvoice.getClient_invoice_datetime(), 0, 8);
        sb.append(str);

        //含税
        str = showInvoice.getTotal_all().split("\\.")[0];
        str = AppTools.fillZero(str, 12);
        sb.append(str);

        //不含税
        str = showInvoice.getTotal_taxable_amount().split("\\.")[0];
        str = AppTools.fillZero(str, 12);
        sb.append(str);

        //销方tin
        str = showInvoice.getSeller_tin();
        str = AppTools.fillZero(str, 26);
        sb.append(str);

        //购方tin
        str = showInvoice.getPurchaser_tin();
        if (str == null) str = "0";
        str = AppTools.fillZero(str, 26);
        sb.append(str);

        //HHmmss
        str = TextUtils.substring(showInvoice.getClient_invoice_datetime(), 8, 14);
        str = AppTools.fillZero(str, 6);
        sb.append(str);

        //设备sn号
        str = RequestModel.devicesn;
        str = AppTools.fillZero(str, 14);
        sb.append(str);


        //发票代码
        str = showInvoice.getInvoice_type_code();
        str = AppTools.fillZero(str, 10);
        sb.append(str);

        //发票号码
        str = showInvoice.getInvoice_no();
        str = AppTools.fillZero(str, 20);
        sb.append(str);
        str = sb.toString();

        for (int i = 0; i < 68; i++) {
            bytes[i] = Byte.valueOf(TextUtils.substring(str, i * 2, i * 2 + 2));
        }

        String content = SecurityUtil.base64Encode2String(bytes);
//        decode(content);
        Bitmap bitmap = QRCodeUtil.createQRCodeBitmap(content, 200, 200);
        bytes = BitmapUtil.Bitmap2Bytes(bitmap);
        return SecurityUtil.base64Encode2String(bytes);
    }


    private static void decode(String str) {
        Logger.i(str.length() + " str " + str);
        byte[] bytes = SecurityUtil.base64Decode(str);
        if (bytes.length < 68) return;
        StringBuffer sb = new StringBuffer();
        boolean fieldStart = false;
        for (int i = 1; i < 68; i++) {
            int d = bytes[i] & 0xff;
            String s = String.format("%02d", d);
            if (fieldStart) {
                if ("00".equals(s)) continue;
                s = String.valueOf(d);
                fieldStart = false;
            }
            sb.append(s);
            //日期yyyyMMdd
            if (i == 4) {
                Logger.i(sb.toString());
                sb.delete(0, sb.length());
                fieldStart = true;
            }
            //含税
            if (i == 10) {
                Logger.i(sb.toString());
                sb.delete(0, sb.length());
                fieldStart = true;
            }
            //不含税
            if (i == 16) {
                Logger.i(sb.toString());
                sb.delete(0, sb.length());
                fieldStart = true;
            }
            //销方tin
            if (i == 29) {
                Logger.i(sb.toString());
                sb.delete(0, sb.length());
                fieldStart = true;
            }
            //购方tin
            if (i == 42) {
                Logger.i(sb.toString());
                sb.delete(0, sb.length());
                fieldStart = false;
            }
            //HHmmss
            if (i == 45) {
                Logger.i(sb.toString());
                sb.delete(0, sb.length());
            }
            //设备sn号
            if (i == 52) {
                Logger.i(sb.toString());
                sb.delete(0, sb.length());
                fieldStart = true;
            }
            //发票代码
            if (i == 57) {
                Logger.i(sb.toString());
                sb.delete(0, sb.length());
                fieldStart = true;
            }
            //发票号码
            if (i == 67) {
                Logger.i(sb.toString());
                sb.delete(0, sb.length());
            }
        }

    }

}
