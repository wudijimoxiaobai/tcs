package com.csscaps.tcs;

import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.csscaps.common.utils.AppSP;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.Invoice_Table;
import com.csscaps.tcs.database.table.ProductModel;
import com.csscaps.tcs.database.table.ProductModel_Table;
import com.csscaps.tcs.model.ReceiveRequestResult;
import com.csscaps.tcs.model.RequestInvoiceResult;
import com.csscaps.tcs.model.RequestResultModel;
import com.csscaps.tcs.model.RequestUploadInvoice;
import com.csscaps.tcs.service.ReportDataService;
import com.csscaps.tcs.service.UploadInvoiceService;
import com.tax.fcr.library.network.Api;
import com.tax.fcr.library.network.IPresenter;
import com.tax.fcr.library.network.RequestModel;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/6/20.
 */

public class MyTimerTask extends TimerTask implements IPresenter {

    private final String DISA = "DISA";
    private final String NEG = "NEG";
    private final String AP = "AP";
    private final String REJ = "REJ";

    @Override
    public void run() {
        List<Invoice> disas = select().from(Invoice.class).where(Invoice_Table.requestType.eq(DISA)).and(Invoice_Table.approveFlag.eq("4")).and(Invoice_Table.requestStatus.eq("1")).queryList();
        List<Invoice> engs = select().from(Invoice.class).where(Invoice_Table.requestType.eq(NEG)).and(Invoice_Table.approveFlag.eq("4")).and(Invoice_Table.requestStatus.eq("1")).queryList();
        List<Invoice> disas1 = select().from(Invoice.class).where(Invoice_Table.requestType.eq(DISA)).and(Invoice_Table.approveFlag.eq("4")).and(Invoice_Table.requestStatus.eq("0")).queryList();
        List<Invoice> engs1 = select().from(Invoice.class).where(Invoice_Table.requestType.eq(NEG)).and(Invoice_Table.approveFlag.eq("4")).and(Invoice_Table.requestStatus.eq("0")).queryList();
        requestResult(disas, ServerConstants.ATCS019);
        requestResult(engs, ServerConstants.ATCS017);
        request(disas1, ServerConstants.ATCS018);
        request(engs1, ServerConstants.ATCS016);
        TCSApplication.getAppContext().startService(new Intent(TCSApplication.getAppContext(), UploadInvoiceService.class));
        if (AppSP.getBoolean("automatic")) {
            TCSApplication.getAppContext().startService(new Intent(TCSApplication.getAppContext(), ReportDataService.class));
        }
    }

    private void requestResult(List<Invoice> list, String funId) {
        RequestInvoiceResult requestInvoiceResult = new RequestInvoiceResult();
        requestInvoiceResult.setFuncid(funId);
        requestInvoiceResult.setInvoice_data(list);
        RequestModel requestModel = new RequestModel();
        requestModel.setFuncid(requestInvoiceResult.getFuncid());
        requestInvoiceResult.setDevicesn(requestModel.getDevicesn());
        requestModel.setData(JSON.toJSONString(requestInvoiceResult));
        Api.post(this, requestModel);
    }

    private void request(List<Invoice> list, String funId) {
        RequestUploadInvoice uploadInvoice = new RequestUploadInvoice();
        for (Invoice invoice : list) {
            String invoiceNo = invoice.getInvoice_no();
            List<ProductModel> productModels = select().from(ProductModel.class).where(ProductModel_Table.invoice_no.eq(invoiceNo)).queryList();
            invoice.setGoods(productModels);
            uploadInvoice.getInvoice_data().add(invoice);
        }
        uploadInvoice.setFuncid(funId);
        RequestModel requestModel = new RequestModel();
        requestModel.setFuncid(uploadInvoice.getFuncid());
        uploadInvoice.setDevicesn(requestModel.getDevicesn());
        requestModel.setData(JSON.toJSONString(uploadInvoice));
        Api.post(this, requestModel);
    }

    @Override
    public void onSuccess(String requestPath, String objectString) {
        ReceiveRequestResult requestRust = JSON.parseObject(objectString, ReceiveRequestResult.class);
        List<RequestResultModel> invoiceData = requestRust.getInvoice_data();
        List<Invoice> disInvoices = new ArrayList<>();
        for (RequestResultModel resultModel : invoiceData) {
            String invoiceNo = resultModel.getInvoice_no();
            Invoice invoice = select().from(Invoice.class).where(Invoice_Table.invoice_no.eq(invoiceNo)).querySingle();
            String workStatus = resultModel.getWork_status();
            switch (requestPath) {
                case ServerConstants.ATCS016://负数发票申请
                case ServerConstants.ATCS018://作废发票申请
                    invoice.setRequestDate("1");
                    invoice.setUploadStatus("1");
                    break;
                case ServerConstants.ATCS017://负数发票申请结果
                case ServerConstants.ATCS019://作废发票申请结果
                    switch (workStatus) {
                        case AP:
                            invoice.setApproveFlag("0");
                            if (requestPath.equals(ServerConstants.ATCS019)) {
                                invoice.setStatus(DISA);
                                invoice.setInvalid_datetime(resultModel.getCancellation_date());
                                invoice.setInvalid_flag("Y");
                                invoice.setRequestDate("0");
                                disInvoices.add(invoice);
//                                ReportData reportData = select().from(ReportData.class).where(ReportData_Table.invoice_type_code.eq(invoice.getInvoice_type_code())).and(ReportData_Table.date.eq(date)).querySingle();
                            } else {
                                invoice.setStatus(NEG);
                            }
                            break;
                        case REJ:
                            invoice.setApproveFlag("1");
                            break;
                    }
                    break;
            }
            invoice.update();
        }

        if (disInvoices.size() > 0) {
            request(disInvoices, ServerConstants.ATCS018);
        }
    }

    @Override
    public void onFailure(String requestPath, String errorMes) {
    }


}
