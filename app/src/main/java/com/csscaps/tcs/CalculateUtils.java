package com.csscaps.tcs;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.csscaps.tcs.activity.InvoiceIssuingActivity;
import com.csscaps.tcs.database.table.InvoiceTaxType;
import com.csscaps.tcs.database.table.InvoiceTaxType_Table;
import com.csscaps.tcs.database.table.Product;
import com.csscaps.tcs.database.table.ProductModel;
import com.csscaps.tcs.database.table.TaxItem;
import com.csscaps.tcs.database.table.TaxMethod;
import com.csscaps.tcs.database.table.TaxMethod_Table;
import com.csscaps.tcs.database.table.TaxType;
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
//        Map mapTax = productModel.getMapTax();
        String relateTaxItemString = item.getRelatedTaxItemString();
        RelatedTaxItem relatedTaxItem = JSON.parseObject(relateTaxItemString, RelatedTaxItem.class);
        List<TaxItem> mTaxItemList = relatedTaxItem.getTaxItemList();
        StringBuffer sb = new StringBuffer();
        String priceStr = item.getPrice();
        String quantityStr = item.getQuantity();
        float price = TextUtils.isEmpty(priceStr) ? 0 : Float.valueOf(priceStr);
        float quantity = TextUtils.isEmpty(quantityStr) ? 0 : Float.valueOf(quantityStr);
        float amount = price * quantity;
        double totalTax = 0;
        for (TaxItem taxItem : mTaxItemList) {
            String taxTypeUid = taxItem.getTaxtype_uid();
            String invoiceTypeUid = InvoiceIssuingActivity.mInvoice.getInvoice_type_uid();
            InvoiceTaxType invoiceTaxType = select().from(InvoiceTaxType.class).where(InvoiceTaxType_Table.invoice_type_uid.eq(invoiceTypeUid)).and(InvoiceTaxType_Table.taxtype_uid.eq(taxTypeUid)).querySingle();
            if (invoiceTaxType == null) continue;
            String invoice_type_taxtype_uid = invoiceTaxType.getInvoice_type_taxtype_uid();
            TaxMethod taxMethod = select().from(TaxMethod.class).where(TaxMethod_Table.invoice_type_taxtype_uid.eq(invoice_type_taxtype_uid)).and(TaxMethod_Table.taxable_item_uid.eq(taxItem.getTaxable_item_uid())).querySingle();
            double tax = getProductTax(taxMethod, item);
            tax = Math.round(tax * 100) * 0.01d;
            tax=Double.valueOf(String.format("%.2f", tax));
            totalTax += tax;
            sb.append("," + taxTypeUid);
            String code = map.get(taxTypeUid);
            switch (code) {
                case VAT:
                    productModel.setVat(tax);
                    break;
                case BPTF:
                    productModel.setBpt_final(tax);
                    break;
                case BPTP:
                    productModel.setBpt_prepayment(tax);
                    break;
                case FEES:
                    productModel.setFees(tax);
                    break;
                case SDF:
                    productModel.setStamp_duty_federal(tax);
                    break;
                case SDL:
                    productModel.setStamp_duty_local(tax);
                    break;
            }
        }
        productModel.setE_tax(amount);
        productModel.setI_tax(amount + totalTax);
        sb.delete(0, 1);
        productModel.setQty(item.getQuantity());
        productModel.setUnit_price(item.getPrice());
        productModel.setTaxtype(sb.toString());
        productModel.setItem_name(item.getProductName());
        productModel.setUnit(item.getUnit());
        productModel.setTax_due(String.valueOf(totalTax));
        productModel.setTaxable_amount(String.valueOf( amount));
        productModel.setAmount_inc(String.valueOf( amount + totalTax));
        item.setTotalTax(productModel.getTax_due());
        item.seteTax(productModel.getTaxable_amount());
        item.setiTax(productModel.getAmount_inc());
        return productModel;
    }


    private static double getProductTax(TaxMethod taxMethod, Product item) {
        String taxedMethod = taxMethod.getTaxed_method();
        String amtQtyMode = taxMethod.getAmt_qty_mode();

        String fixedAmountStr = item.getFixedAmount();
        String purchaseStr = item.getPurchase();
        String pStr = taxMethod.getAdjusted_value();
        String cStr = taxMethod.getTax_amount_ratio();
        String rStr = taxMethod.getTax_rate();
        String priceStr = item.getPrice();
        String quantityStr = item.getQuantity();

        float price = TextUtils.isEmpty(priceStr) ? 0 : Float.valueOf(priceStr);
        float quantity = TextUtils.isEmpty(quantityStr) ? 0 : Float.valueOf(quantityStr);
        float fa = TextUtils.isEmpty(fixedAmountStr) ? 0 : Float.valueOf(fixedAmountStr);
        float purchase = TextUtils.isEmpty(purchaseStr) ? 0 : Float.valueOf(purchaseStr);
        float p = TextUtils.isEmpty(pStr) ? 0 : Float.valueOf(pStr);
        float c = TextUtils.isEmpty(cStr) ? 0 : Float.valueOf(cStr);
        float r = TextUtils.isEmpty(rStr) ? 0 : Float.valueOf(rStr);
        boolean isTaxIn = false;
        if ("Y".equals(taxMethod.getIs_tax_included())) {
            isTaxIn = true;
        }
        double tax = 0;
        switch (taxedMethod) {
            case ITEM:
                tax = getTaxByITEM(amtQtyMode, isTaxIn, price, quantity, purchase, p, c, r);
                break;
            case AMT:
                if (isTaxIn) {
                    tax = (price * quantity + p) * c * r / (1 + r);
                } else {
                    tax = (price * quantity) * c * r;
                }
                break;
            case FIXED:
                tax = fa * c;
                break;
        }
        return tax;
    }


    /**
     * @param amtQtyMode
     * @param isTaxIn
     * @param price      单价
     * @param quantity   数量
     * @param p          参数
     * @param c          系数
     * @param r          税率
     * @return
     */
    private static double getTaxByITEM(String amtQtyMode, boolean isTaxIn, float price, float quantity, float purchase, float p, float c, float r) {
        float tax = 0;
        switch (amtQtyMode) {
            case AMT:
                if (isTaxIn) {
                    tax = (price * quantity + p) * c * r / (1 + r);

                } else {
                    tax = (price * quantity + p) * c * r;
                }
                break;
            case QTY:
                tax = (quantity + p) * c * r;
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
