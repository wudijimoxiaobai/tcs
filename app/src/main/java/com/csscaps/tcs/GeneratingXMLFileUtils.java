package com.csscaps.tcs;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.csscaps.common.utils.AppTools;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.common.utils.QRCodeUtil;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.ProductModel;
import com.csscaps.tcs.database.table.ProductModel_Table;
import com.csscaps.tcs.model.Buyer;
import com.csscaps.tcs.model.Head;
import com.csscaps.tcs.model.InvoiceData;
import com.csscaps.tcs.model.QD;
import com.csscaps.tcs.model.Seller;
import com.suwell.ofd.render.util.BitmapUtil;
import com.tax.fcr.library.utils.Logger;
import com.tax.fcr.library.utils.SecurityUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/7/10.
 * 生成模板发票数据源 xml
 */

public class GeneratingXMLFileUtils {

    private static final  String HEADER="<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>";

    public static void generatingXmlFile(Invoice showInvoice) {
        InvoiceData invoiceData=new InvoiceData();

        Head head=new Head();
        head.setINVOICECODE(showInvoice.getInvoice_type_code());
        head.setDEVICENO("80249001000048");
        head.setINVOICENUMBER(showInvoice.getInvoice_no());
        head.setISSUANCEDATE(DateUtils.dateToStr(DateUtils.getStringToDate(showInvoice.getClient_invoice_datetime(), DateUtils.format_yyyyMMddHHmmss_24_EN), DateUtils.format_yyyy_MM_dd_HH_mm_ss_24_EN));
        invoiceData.setHead(head);

        Seller seller=new Seller();
        seller.setTIN(showInvoice.getSeller_tin());
        seller.setNAME(showInvoice.getSeller_name());
        seller.setADDRESS(showInvoice.getSeller_address());
        seller.setBRANCH(showInvoice.getSeller_branch_addr());
        seller.setPHONENO(showInvoice.getSeller_phone());
        invoiceData.setSeller(seller);

        Buyer buyer=new Buyer();
        buyer.setTIN(showInvoice.getPurchaser_tin());
        buyer.setPHONENO(showInvoice.getPurchaser_phone());
        buyer.setNAME(showInvoice.getPurchaser_name());
        buyer.setADDRESS(showInvoice.getPurchaser_address());
        buyer.setNATIONALID(showInvoice.getPurchaser_id_number());
        invoiceData.setBuyer(buyer);

        invoiceData.setTotalofVAT(showInvoice.getTotal_vat());
        invoiceData.setTotalofBPTFinal(showInvoice.getTotal_bpt());
        invoiceData.setTotalofBPTPrepayment(showInvoice.getTotal_bpt_preypayment());
        invoiceData.setTotalofStampDuty_Local(showInvoice.getTotal_stamp());
        invoiceData.setTotalofStamDuty_Federal(showInvoice.getTotal_final());
        invoiceData.setTotalofFees(showInvoice.getTotal_fee());
        invoiceData.setTotalAmountExcl(showInvoice.getTotal_taxable_amount());
        invoiceData.setTotalAmountExclIncl(showInvoice.getTotal_all());
        invoiceData.setRemark(showInvoice.getRemark());
        invoiceData.setIssuedby(showInvoice.getDrawer_name());
        invoiceData.setRWM(getQRCodeToString(showInvoice));

        String invoiceNo = showInvoice.getInvoice_no();
        List<ProductModel> productModels = select().from(ProductModel.class).where(ProductModel_Table.invoice_no.eq(invoiceNo)).queryList();
        List<QD> QDs=new ArrayList<>();
        invoiceData.setQDs(QDs);
        for (int i = 0; i < productModels.size(); i++) {
            ProductModel productModel = productModels.get(i);
            QD qd=new QD();
            qd.setITEMNAME(productModel.getItem_name());
            qd.setDESCRIPTION(productModel.getSpecification());
            qd.setUNIT(productModel.getUnit());
            qd.setUNITPRICE(productModel.getUnit_price());
            qd.setQTY(productModel.getQty());
            qd.setAMOUNT(productModel.getTaxable_amount());
            qd.setVAT(String.format("%.2f", Math.round((productModel.getVat()) * 100) * 0.01d));
            QDs.add(qd);
        }
        String dataXmlPath = /*getContext().getFilesDir().getAbsolutePath()+*/"/sdcard/data1.xml";
        XStream xStream = new XStream(new DomDriver());
        xStream.processAnnotations(InvoiceData.class);
        File dataXmlFile = new File(dataXmlPath);
        try {
            if (!dataXmlFile.exists()) {
                dataXmlFile.createNewFile();
            }
            String xmlData = HEADER
                    + System.getProperty("line.separator")
                    + xStream.toXML(invoiceData);
            FileWriter fileWriter = new FileWriter(dataXmlFile);
            fileWriter.write(xmlData);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

       /* XmlSerializer serializer = Xml.newSerializer();
        File file = new File(dataXmlPath);
        OutputStream os = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            // 指定流目录
            os = new FileOutputStream(file);
            // 设置指定目录
            serializer.setOutput(os, "UTF-8");
            // 开始写入Xml格式数据
            // 开始文档
            // 参数一：指定编码格式   参数二：是不是独立的xml(这个xml与其他xml是否有关联)
            serializer.startDocument("UTF-8", true);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            // 开始根标签
            // 参数一：命名空间   参数二：标签名称
            serializer.startTag(null, "Root");

            serializer.startTag(null, "Head");
            serializer.startTag(null, "INVOICECODE");
            serializer.text(showInvoice.getInvoice_type_code());
            serializer.endTag(null, "INVOICECODE");
            serializer.startTag(null, "DEVICENO");
            serializer.text("80249001000048");
            serializer.endTag(null, "DEVICENO");
            serializer.startTag(null, "OBJECT");
            serializer.text("");
            serializer.endTag(null, "OBJECT");
            serializer.startTag(null, "INVOICENUMBER");
            serializer.text(showInvoice.getInvoice_no());
            serializer.endTag(null, "INVOICENUMBER");
            serializer.startTag(null, "ISSUANCEDATE");
            serializer.text(DateUtils.dateToStr(DateUtils.getStringToDate(showInvoice.getClient_invoice_datetime(), DateUtils.format_yyyyMMddHHmmss_24_EN), DateUtils.format_yyyy_MM_dd_HH_mm_ss_24_EN));
            serializer.endTag(null, "ISSUANCEDATE");
            serializer.endTag(null, "Head");

            serializer.startTag(null, "SELLER");
            serializer.startTag(null, "TIN");
            serializer.text(showInvoice.getSeller_tin());
            serializer.endTag(null, "TIN");
            serializer.startTag(null, "NAME");
            serializer.text(showInvoice.getSeller_name());
            serializer.endTag(null, "NAME");
            serializer.startTag(null, "ADDRESS");
            serializer.text(showInvoice.getSeller_address() == null ? "" : showInvoice.getSeller_address());
            serializer.endTag(null, "ADDRESS");
            serializer.startTag(null, "PHONENO");
            serializer.text(showInvoice.getSeller_phone() == null ? "" : showInvoice.getSeller_phone());
            serializer.endTag(null, "PHONENO");
            serializer.startTag(null, "BRANCH");
            serializer.text(showInvoice.getSeller_branch_addr() == null ? "" : showInvoice.getSeller_branch_addr());
            serializer.endTag(null, "BRANCH");
            serializer.endTag(null, "SELLER");

            serializer.startTag(null, "BUYER");
            serializer.startTag(null, "TIN");
            serializer.text(showInvoice.getPurchaser_tin());
            serializer.endTag(null, "TIN");
            serializer.startTag(null, "NAME");
            serializer.text(showInvoice.getPurchaser_name());
            serializer.endTag(null, "NAME");
            serializer.startTag(null, "ADDRESS");
            serializer.text(showInvoice.getPurchaser_address() == null ? "" : showInvoice.getPurchaser_address());
            serializer.endTag(null, "ADDRESS");
            serializer.startTag(null, "PHONENO");
            serializer.text(showInvoice.getPurchaser_phone() == null ? "" : showInvoice.getPurchaser_phone());
            serializer.endTag(null, "PHONENO");
            serializer.startTag(null, "NATIONALID");
            serializer.text(showInvoice.getPurchaser_id_number() == null ? "" : showInvoice.getPurchaser_id_number());
            serializer.endTag(null, "NATIONALID");
            serializer.endTag(null, "BUYER");

            serializer.startTag(null, "Unit");
            serializer.text("SDG");
            serializer.endTag(null, "Unit");

            serializer.startTag(null, "TotalofVAT");
            serializer.text(showInvoice.getTotal_vat());
            serializer.endTag(null, "TotalofVAT");

            serializer.startTag(null, "TotalofBPTFinal");
            serializer.text(showInvoice.getTotal_bpt());
            serializer.endTag(null, "TotalofBPTFinal");

            serializer.startTag(null, "TotalofBPTPrepayment");
            serializer.text(showInvoice.getTotal_bpt_preypayment());
            serializer.endTag(null, "TotalofBPTPrepayment");

            serializer.startTag(null, "TotalofStampDuty-Local");
            serializer.text(showInvoice.getTotal_stamp());
            serializer.endTag(null, "TotalofStampDuty-Local");

            serializer.startTag(null, "TotalofStamDuty-Federal");
            serializer.text(showInvoice.getTotal_final());
            serializer.endTag(null, "TotalofStamDuty-Federal");

            serializer.startTag(null, "TotalofFees");
            serializer.text(showInvoice.getTotal_fee());
            serializer.endTag(null, "TotalofFees");

            serializer.startTag(null, "TotalAmountExcl");
            serializer.text(showInvoice.getTotal_taxable_amount());
            serializer.endTag(null, "TotalAmountExcl");

            serializer.startTag(null, "TotalAmountExclIncl");
            serializer.text(showInvoice.getTotal_all());
            serializer.endTag(null, "TotalAmountExclIncl");

            serializer.startTag(null, "Remark");
            serializer.text(showInvoice.getRemark() == null ? "" : showInvoice.getRemark());
            serializer.endTag(null, "Remark");

            serializer.startTag(null, "Issuedby");
            serializer.text(showInvoice.getDrawer_name());
            serializer.endTag(null, "Issuedby");

            String invoiceNo = showInvoice.getInvoice_no();
            List<ProductModel> productModels = select().from(ProductModel.class).where(ProductModel_Table.invoice_no.eq(invoiceNo)).queryList();

            for (int i = 0; i < productModels.size(); i++) {
                ProductModel productModel = productModels.get(i);
                serializer.startTag(null, "QD");
                serializer.startTag(null, "ITEMNAME");
                serializer.text(productModel.getItem_name());
                serializer.endTag(null, "ITEMNAME");

                serializer.startTag(null, "DESCRIPTION");
                serializer.text(productModel.getSpecification() == null ? "" : productModel.getSpecification());
                serializer.endTag(null, "DESCRIPTION");

                serializer.startTag(null, "UNIT");
                serializer.text(productModel.getUnit());
                serializer.endTag(null, "UNIT");

                serializer.startTag(null, "QTY");
                serializer.text(productModel.getQty());
                serializer.endTag(null, "QTY");

                serializer.startTag(null, "UNITPRICE");
                serializer.text(productModel.getUnit_price());
                serializer.endTag(null, "UNITPRICE");

                serializer.startTag(null, "AMOUNT");
                serializer.text(productModel.getTaxable_amount());
                serializer.endTag(null, "AMOUNT");

                serializer.startTag(null, "VAT");
                serializer.text(String.format("%.2f", Math.round((productModel.getVat()) * 100) * 0.01d));
                serializer.endTag(null, "VAT");

                serializer.endTag(null, "QD");
            }

            serializer.startTag(null, "RWM");
            serializer.text(getQRCodeToString(showInvoice));
            serializer.endTag(null, "RWM");

            serializer.startTag(null, "title");
            serializer.text(showInvoice.getDrawer_name());
            serializer.endTag(null, "title");

            serializer.endTag(null, "Root");
            serializer.endDocument();
            serializer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
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
        str = AppTools.fillZero(str, 26);
        sb.append(str);

        //HHmmss
        str = TextUtils.substring(showInvoice.getClient_invoice_datetime(), 8, 14);
        str = AppTools.fillZero(str, 6);
        sb.append(str);

        //设备sn号
        str = "80249001000048";
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
        decode(content);
        Bitmap bitmap = QRCodeUtil.createQRCodeBitmap(content, 200, 200);
        bytes = BitmapUtil.Bitmap2Bytes(bitmap);
//        File f=new File("/sdcard/qr.jpg");
//        try {
//           if(!f.exists()) f.createNewFile();
//            FileOutputStream fos=new FileOutputStream(f);
//            fos.write(bytes);
//            fos.flush();
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return SecurityUtil.base64Encode2String(bytes);
    }


    private static void decode(String str) {
        Logger.i(str.length() + " str " + str);
        byte[] bytes = SecurityUtil.base64Decode(str);
        if (bytes.length < 68) return;
//        byte[] sing = AppTools.subByte(bytes, 68, 256);
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
