package com.csscaps.tcs;

import com.csscaps.tcs.database.SDInvoiceDatabase;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.ProductModel;
import com.csscaps.tcs.database.table.SdInvoice;
import com.csscaps.tcs.database.table.SdProductModel;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tl on 2018/10/17.
 * SD卡数据库操作
 */

public class SdcardDBUtil {

    // sd卡数据库是否打是否打开
    public static boolean isOpen = true;

    public static String lock = "lock";

    public static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();


    /**
     * 关闭sd卡数据库
     */
    public static void closeDB() {
        isOpen = false;
        FlowManager.getDatabase(SDInvoiceDatabase.class).getHelper().closeDB();
    }

    /**
     * 打开sd卡数据库
     *
     * @param SDPath 外挂sd卡路径
     */
    public static void openDB(String SDPath) {
        File file = new File(SDPath + "/FCR");
        FileDatabaseContext mSdDatabaseContext = new FileDatabaseContext(TCSApplication.getAppContext(), file, false);
        FlowManager.getDatabase(SDInvoiceDatabase.class).reset(mSdDatabaseContext);
        FlowManager.getDatabase(SDInvoiceDatabase.class).getWritableDatabase();
    }


    /**
     * 插入或更新数据
     *
     * @param object
     */
    public static void saveSDDB(final Object object) {
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    SdcardUtil.unlockSdcard();
                    //数据库文件是否可以打开
                    int status = SdcardUtil.checkLockSdcardStatus();
                    if (status == 0) {//已解锁
                        boolean isOk = true;
                        Map<String, String> map = System.getenv();
                        String SDPath = map.get("SECONDARY_STORAGE");
                        File sdFile = new File(SDPath);
                        while (isOk) {
                            //SD卡是否可读写
                            if (!sdFile.canWrite()) continue;
                            if (!isOpen) {
                                openDB(SDPath);
                            }
                            isOk = false;
                        }
                        isOpen = true;
                        if (object instanceof Invoice) {
                            saveSdInvoice((Invoice) object);
                        } else if (object instanceof List) {
                            saveListSDProductModel(object);
                        }
                        SdcardDBUtil.closeDB();
                        SdcardUtil.lockSdcard();
                        SdcardUtil.checkLockSdcardStatus();
                    } else if (status == -1) {//解锁失败
                        saveSDDB(object);
                    }
                }
            }
        });
    }

    /**
     * 插入商品
     *
     * @param object
     */
    private static void saveListSDProductModel(Object object) {
        List<ProductModel> list = (List<ProductModel>) object;
        List<SdProductModel> goods = new ArrayList<>();
        for (ProductModel productModel : list) {

            SdProductModel sm = new SdProductModel();
            sm.setInvoice_no(productModel.getInvoice_no());
            sm.setI_tax(productModel.getI_tax());
            sm.setE_tax(productModel.getE_tax());
            sm.setAmount_inc(productModel.getAmount_inc());

            sm.setCategory(productModel.getCategory());
            sm.setCurrency_code(productModel.getCurrency_code());
            sm.setExchange_rate(productModel.getExchange_rate());
            sm.setBpt_final(productModel.getBpt_final());
            sm.setBpt_prepayment(productModel.getBpt_prepayment());
            sm.setVat(productModel.getVat());
            sm.setVat_amount(productModel.getVat_amount());
            sm.setStamp_duty_federal(productModel.getStamp_duty_federal());
            sm.setStamp_duty_local(productModel.getStamp_duty_local());
            sm.setFees(productModel.getFees());

            sm.setUnit(productModel.getUnit());
            sm.setUnit_price(productModel.getUnit_price());
            sm.setUnit_price_after_tax(productModel.getUnit_price_after_tax());
            sm.setUnit_price_tax_rate(productModel.getUnit_price_tax_rate());
            sm.setQty(productModel.getQty());
            sm.setProduct_code(productModel.getProduct_code());
            sm.setTax_due(productModel.getTax_due());
            sm.setTaxable_amount(productModel.getTaxable_amount());
            sm.setTaxable_amount_org(productModel.getTaxable_amount_org());
            sm.setTaxable_amount(productModel.getTaxable_amount());
            sm.setTaxable_item_uid(productModel.getTaxable_item_uid());
            sm.setItem_name(productModel.getItem_name());
            sm.setItem_desc(productModel.getItem_desc());
            sm.setSpecification(productModel.getSpecification());
            sm.setTaxtype(productModel.getTaxtype());
            sm.setTaxtype(productModel.getTaxtype());
            goods.add(sm);
        }

        FlowManager.getDatabase(SDInvoiceDatabase.class)
                .executeTransaction(new ProcessModelTransaction.Builder<>(
                        new ProcessModelTransaction.ProcessModel<SdProductModel>() {
                            @Override
                            public void processModel(SdProductModel model, DatabaseWrapper wrapper) {
                                model.save();
                            }
                        }).addAll(goods).build());
    }

    /**
     * 插入发票
     *
     * @param invoice
     */
    private static void saveSdInvoice(Invoice invoice) {
        SdInvoice sdInvoice = new SdInvoice();
        sdInvoice.setInvoice_no(invoice.getInvoice_no());
        sdInvoice.setDrawer_name(invoice.getDrawer_name());
        sdInvoice.setClient_invoice_datetime(invoice.getClient_invoice_datetime());
        sdInvoice.setInvoice_type_code(invoice.getInvoice_short_code());
        sdInvoice.setInvoice_made_by(invoice.getInvoice_made_by());
        sdInvoice.setSeller_tin(invoice.getSeller_tin());
        sdInvoice.setSeller_address(invoice.getSeller_address());
        sdInvoice.setSeller_name(invoice.getSeller_name());
        sdInvoice.setSeller_phone(invoice.getSeller_phone());
        sdInvoice.setInvoice_type_name(invoice.getInvoice_type_name());
        sdInvoice.setInvoice_type_code(invoice.getInvoice_type_code());
        sdInvoice.setInvoice_type_uid(invoice.getInvoice_type_uid());
        sdInvoice.setPurchaser_tin(invoice.getPurchaser_tin());
        sdInvoice.setPurchaser_phone(invoice.getPurchaser_phone());
        sdInvoice.setPurchaser_address(invoice.getPurchaser_address());
        sdInvoice.setPurchaser_name(invoice.getPurchaser_id_type());
        sdInvoice.setPurchaser_id_type(invoice.getPurchaser_id_type());
        sdInvoice.setPurchaser_id_number(invoice.getPurchaser_id_number());
        sdInvoice.setTotal_vat(invoice.getTotal_vat());
        sdInvoice.setTotal_bpt(invoice.getTotal_bpt());
        sdInvoice.setTotal_final(invoice.getTotal_final());
        sdInvoice.setTotal_stamp(invoice.getTotal_stamp());
        sdInvoice.setTotal_bpt_preypayment(invoice.getTotal_bpt_preypayment());
        sdInvoice.setTotal_fee(invoice.getTotal_fee());
        sdInvoice.setTotal_taxable_amount(invoice.getTotal_taxable_amount());
        sdInvoice.setTotal_all(invoice.getTotal_all());
        sdInvoice.setTotal_tax_due(invoice.getTotal_tax_due());
        sdInvoice.setStatus(invoice.getStatus());
        sdInvoice.setUploadStatus(invoice.getUploadStatus());
        sdInvoice.setApproveFlag(invoice.getApproveFlag());
        sdInvoice.setRequestStatus(invoice.getRequestStatus());
        sdInvoice.setRequestBy(invoice.getRequestBy());
        sdInvoice.setRequestType(invoice.getRequestType());
        sdInvoice.setRequestDate(invoice.getRequestDate());
        sdInvoice.setReason(invoice.getReason());
        sdInvoice.setRemark(invoice.getRemark());
        sdInvoice.setNegative_approval_remark(invoice.getNegative_approval_remark());
        sdInvoice.setInvalid_remark(invoice.getInvalid_remark());
        sdInvoice.save();
    }


}