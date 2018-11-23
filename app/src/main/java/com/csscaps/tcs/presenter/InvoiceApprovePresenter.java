package com.csscaps.tcs.presenter;

import com.alibaba.fastjson.JSON;
import com.csscaps.tcs.SdcardDBUtil;
import com.csscaps.tcs.ServerConstants;
import com.csscaps.tcs.database.SDInvoiceDatabase;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.Invoice_Table;
import com.csscaps.tcs.database.table.ProductModel;
import com.csscaps.tcs.database.table.ProductModel_Table;
import com.csscaps.tcs.model.RequestUploadInvoice;
import com.tax.fcr.library.network.Api;
import com.tax.fcr.library.network.IPresenter1;
import com.tax.fcr.library.network.RequestModel;
import com.tax.fcr.library.utils.SecurityUtil;

import java.util.List;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/6/20.
 */

public class InvoiceApprovePresenter implements IPresenter1 {

    @Override
    public void onSuccess(String requestPath, String objectString, Object O) {
        requestResult(O, "1");
    }

    @Override
    public void onFailure(String requestPath, String errorMes, Object O) {
        requestResult(O, "0");
    }

    private void requestResult(Object O, String status) {
        RequestModel requestModel = (RequestModel) O;
        String data = requestModel.getData();
        data= SecurityUtil.getFromBase64(data);
        RequestUploadInvoice requestUploadInvoice = JSON.parseObject(data, RequestUploadInvoice.class);
        String invoiceNo = requestUploadInvoice.getInvoice_data().get(0).getInvoice_no();
        Invoice invoice = select().from(Invoice.class).where(Invoice_Table.invoice_no.eq(invoiceNo)).querySingle();
        invoice.setRequestStatus(status);
        invoice.update();
        SdcardDBUtil.saveSDDB(invoice,SDInvoiceDatabase.class);
    }

    /**
     * 作废发票申请
     *
     * @param invoice
     */
    public void cancellation(Invoice invoice) {
        request(invoice, ServerConstants.ATCS018);
    }

    /**
     * 负数发票申请
     *
     * @param invoice
     */
    public void negative(Invoice invoice) {
        request(invoice, ServerConstants.ATCS016);
    }

    /**
     * 申请
     *
     * @param invoice
     * @param funId
     */
    private void request(Invoice invoice, String funId) {
        String invoiceNo = invoice.getInvoice_no();
        List<ProductModel> productModels = select().from(ProductModel.class).where(ProductModel_Table.invoice_no.eq(invoiceNo)).queryList();
        invoice.setGoods(productModels);
        RequestUploadInvoice uploadInvoice = new RequestUploadInvoice();
        uploadInvoice.setFuncid(funId);
        uploadInvoice.getInvoice_data().add(invoice);
        RequestModel requestModel = new RequestModel();
        requestModel.setFuncid(uploadInvoice.getFuncid());
        uploadInvoice.setDevicesn(requestModel.getDevicesn());
        requestModel.setData(JSON.toJSONString(uploadInvoice));
        Api.post1(this, requestModel);
    }

}
