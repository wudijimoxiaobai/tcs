package com.csscaps.tcs;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.csscaps.common.utils.AppSP;
import com.csscaps.tcs.activity.NewInvoiceActivity;
import com.csscaps.tcs.database.table.InvoiceTaxType;
import com.csscaps.tcs.database.table.InvoiceTaxType_Table;
import com.csscaps.tcs.database.table.Product;
import com.csscaps.tcs.database.table.ProductModel;
import com.csscaps.tcs.database.table.TaxItem;
import com.csscaps.tcs.database.table.TaxMethod;
import com.csscaps.tcs.database.table.TaxMethod_Table;
import com.csscaps.tcs.database.table.TaxType;
import com.csscaps.tcs.model.MyTaxpayer;
import com.csscaps.tcs.model.RelatedTaxItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/5/28.
 */

public class CalculateUtils {
    private static final String ITEM = "ITEM";
    private static final String AMT = "AMT";
    private static final String FIXED = "FIXED";
    private static final String QTY = "QTY";
    private static final String EXEMPTED = "Exempted";
    private static final String COMMISSION = "COMMISSION";
    private static final String VAT = "VAT";
    private static final String BPTF = "BPTF";
    private static final String BPTP = "BPTP";
    private static final String FEES = "FEES";
    private static final String SDF = "SD-F";
    private static final String SDL = "SD-L";

    private static Map<String, String> map = new HashMap<String, String>();

    static {
        List<TaxType> list = select().from(TaxType.class).queryList();
        for (TaxType taxType : list) {
            map.put(taxType.getTaxtype_uid(), taxType.getTaxtype_code());
        }
    }

    /**
     * 计算发票内的单个商品税
     *
     * @param item
     */
    public static ProductModel calculateProductTax(Product item) {
        ProductModel productModel = new ProductModel();
        productModel.setSpecification(item.getSpecification());
        productModel.setInvoice_no(NewInvoiceActivity.mInvoice.getInvoice_no());
        String relateTaxItemString = item.getRelatedTaxItemString();
        RelatedTaxItem relatedTaxItem = JSON.parseObject(relateTaxItemString, RelatedTaxItem.class);
        List<TaxItem> mTaxItemList = relatedTaxItem.getTaxItemList();
        productModel.setTax_rate(mTaxItemList.get(0).getTax_rate());
        StringBuffer sb = new StringBuffer();
//        StringBuffer sb1 = new StringBuffer();
        String priceStr = item.getPrice();
        String quantityStr = item.getQuantity();
        double price = TextUtils.isEmpty(priceStr) ? 0 : Double.valueOf(priceStr);
        double quantity = TextUtils.isEmpty(quantityStr) ? 0 : Double.valueOf(quantityStr);
        double amount = price * quantity;
        double totalTax = 0;
        for (TaxItem taxItem : mTaxItemList) {
            String taxTypeUid = taxItem.getTaxtype_uid();
//            String taxableItemUid = taxItem.getTaxable_item_uid();
            String invoiceTypeUid = NewInvoiceActivity.mInvoice.getInvoice_type_uid();
            InvoiceTaxType invoiceTaxType = select().from(InvoiceTaxType.class).where(InvoiceTaxType_Table.invoice_type_uid.eq(invoiceTypeUid)).and(InvoiceTaxType_Table.taxtype_uid.eq(taxTypeUid)).querySingle();
            if (invoiceTaxType == null) continue;
            String invoice_type_taxtype_uid = invoiceTaxType.getInvoice_type_taxtype_uid();
            TaxMethod taxMethod = select().from(TaxMethod.class).where(TaxMethod_Table.invoice_type_taxtype_uid.eq(invoice_type_taxtype_uid)).and(TaxMethod_Table.taxable_item_uid.eq(taxItem.getTaxable_item_uid())).querySingle();
            String taxTypeCode = map.get(taxTypeUid);
            double tax = getProductTax(taxMethod, item, taxTypeCode);
            tax = Math.round(tax * 100) * 0.01d;
            tax = Double.valueOf(String.format("%.2f", tax));
            totalTax += tax;
            sb.append("," + taxTypeUid);
//            sb1.append("," + taxableItemUid);

            switch (taxTypeCode) {
                case VAT:
                    productModel.setVat(tax);
                    productModel.setVat_amount(String.format("%.2f", tax));
                    break;
                case BPTF:
                    productModel.setBpt_final(tax);
                    productModel.setBptf_amount(String.format("%.2f", tax));
                    break;
                case BPTP:
                    productModel.setBpt_prepayment(tax);
                    productModel.setBptp_amount(String.format("%.2f", tax));
                    break;
                case FEES:
                    productModel.setFees(tax);
                    productModel.setFees_amount(String.format("%.2f", tax));
                    break;
                case SDF:
                    productModel.setStamp_duty_federal(tax);
                    productModel.setSdf_amount(String.format("%.2f", tax));
                    break;
                case SDL:
                    productModel.setStamp_duty_local(tax);
                    productModel.setSdl_amount(String.format("%.2f", tax));
                    break;
            }
        }

        String myTaxpayerString = AppSP.getString("MyTaxpayer");
        MyTaxpayer myTaxpayer = JSON.parseObject(myTaxpayerString, MyTaxpayer.class);
        //如果纳税人  withholding 等于Y
        // BPTPrepayment= BPTPrepayment+ (item.StampDutyLocal + item.stampDutyFederal + item.Fee + item.BPTFinal + item.VatAmount) * 0.01；
//        if ("Y".equals(myTaxpayer.getWithholding())) {
//            totalTax -= productModel.getBpt_prepayment();
//            double bptp = productModel.getBpt_prepayment() + (productModel.getBpt_final() + productModel.getFees() + productModel.getStamp_duty_local() + productModel.getStamp_duty_federal() + productModel.getVat()) * 0.01d;
//            bptp = Math.round(bptp * 100) * 0.01d;
//            bptp = Double.valueOf(String.format("%.2f", bptp));
//            totalTax = totalTax + bptp;
//            productModel.setBpt_prepayment(bptp);
//            productModel.setBptp_amount(String.format("%.2f", bptp));
//        } else {
//            totalTax -= productModel.getBpt_prepayment();
//            productModel.setBpt_prepayment(0);
//            productModel.setBptp_amount(String.format("%.2f", 0));
//        }

        totalTax = Double.valueOf(String.format("%.2f", totalTax));

        productModel.setE_tax(Double.valueOf(String.format("%.2f", Math.round((amount) * 100) * 0.01d)));
        productModel.setI_tax(Double.valueOf(String.format("%.2f", Math.round((amount + totalTax) * 100) * 0.01d)));
        sb.delete(0, 1);
//        sb1.delete(0, 1);
        productModel.setQty(item.getQuantity());
        productModel.setUnit_price(String.valueOf(price));
        productModel.setTaxtype(sb.toString());
//        productModel.setTaxable_item_uid(sb1.toString());
        productModel.setItem_name(item.getProductName());
        productModel.setUnit(item.getUnit());
        productModel.setTax_due(String.format("%.2f", Math.round((totalTax) * 100) * 0.01d));
        productModel.setTaxable_amount(String.format("%.2f", Math.round((amount) * 100) * 0.01d));
        productModel.setAmount_inc(String.format("%.2f", Math.round((amount + totalTax) * 100) * 0.01d));
        // item.setTotalTax(String.format("%.2f", Math.round((totalTax - productModel.getFees()) * 100) * 0.01d));
        item.setTotalTax(String.format("%.2f", Math.round((totalTax) * 100) * 0.01d));
        item.seteTax(productModel.getTaxable_amount());
        //item.setiTax(String.format("%.2f", Math.round((amount + totalTax - productModel.getFees()) * 100) * 0.01d));
        item.setiTax(String.format("%.2f", Math.round((amount + totalTax) * 100) * 0.01d));
        return productModel;
    }


    private static double getProductTax(TaxMethod taxMethod, Product item, String taxTypeCode) {
        String taxedMethod = taxMethod.getTaxed_method();
        String amtQtyMode = taxMethod.getAmt_qty_mode();

        String fixedAmountStr = item.getFixedAmount();
        String purchaseStr = item.getPurchase();
        String pStr = taxMethod.getAdjusted_value();
        String cStr = taxMethod.getTax_amount_ratio();
        String rStr = taxMethod.getTax_rate();
        String priceStr = item.getPrice();
        String quantityStr = item.getQuantity();
        String percentageStr = item.getPercentage();
        String commissionStr = item.getCommission();

        double price = TextUtils.isEmpty(priceStr) ? 0 : Double.valueOf(priceStr);
        double quantity = TextUtils.isEmpty(quantityStr) ? 0 : Double.valueOf(quantityStr);
        double fixedAmount = TextUtils.isEmpty(fixedAmountStr) ? 0 : Double.valueOf(fixedAmountStr);
        double purchase = TextUtils.isEmpty(purchaseStr) ? 0 : Double.valueOf(purchaseStr);
        double p = TextUtils.isEmpty(pStr) ? 0 : Double.valueOf(pStr);
        double c = TextUtils.isEmpty(cStr) ? 0 : Double.valueOf(cStr);
        double r = TextUtils.isEmpty(rStr) ? 0 : Double.valueOf(rStr);
        double percentage = TextUtils.isEmpty(percentageStr) ? 0 : Double.valueOf(percentageStr);
        double commission = TextUtils.isEmpty(commissionStr) ? 0 : Double.valueOf(commissionStr);
        boolean isTaxIn = false;
        if ("Y".equals(taxMethod.getIs_tax_included())) {
            isTaxIn = true;
        }
        double tax = 0;
        switch (taxedMethod) {
            case ITEM:
                tax = getTaxByITEM(taxTypeCode, amtQtyMode, isTaxIn, price, quantity, purchase, p, c, r, percentage, commission, fixedAmount);
                break;
            case AMT:
                if (isTaxIn) {
                    tax = (price * quantity + p) * c * r / (1 + r);
                } else {
                    tax = (price * quantity) * c * r;
                }
                break;
            case FIXED:
                tax = fixedAmount * c;
                break;
        }
        return tax;
    }


    /**
     * @param amtQtyMode
     * @param isTaxIn
     * @param price       单价
     * @param quantity    数量
     * @param p           参数
     * @param c           系数
     * @param r           税率
     * @param percentage  百分比
     * @param commission  佣金
     * @param fixedAmount 定额
     * @return
     */
    private static double getTaxByITEM(String taxTypeCode, String amtQtyMode, boolean isTaxIn, double price, double quantity, double purchase, double p, double c, double r, double percentage, double commission, double fixedAmount) {
        double tax = 0;
        switch (amtQtyMode) {
            case AMT:
                switch (taxTypeCode) {
                    case SDF:
                    case SDL:
                        tax = price * quantity * percentage / 100;
                        break;
                    default:
                        if (isTaxIn) {
                            tax = (price * quantity + p) * c * r / (1 + r);

                        } else {
                            tax = (price * quantity + p) * c * r;
                        }
                        break;
                }

                break;
            case QTY:
                switch (taxTypeCode) {
                    case SDF:
                    case SDL:
                        tax = quantity * fixedAmount;
                        break;
                    default:
                        tax = (quantity + p) * c * r;
                        break;
                }
                break;
            case COMMISSION:
                tax = (price - purchase) * c * r;
                break;
            case EXEMPTED:
                tax = 0;
                break;
        }
        return tax;
    }

}
