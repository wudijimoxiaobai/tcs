package com.csscaps.tcs.model;

import java.util.List;

/**
 * Created by tl on 2018/6/26.
 */

public class ReceiveUploadInvoiceResult extends ReceiveData {

    List<RequestResultModel> invoice_result;

    public List<RequestResultModel> getInvoice_result() {
        return invoice_result;
    }

    public void setInvoice_result(List<RequestResultModel> invoice_result) {
        this.invoice_result = invoice_result;
    }
}
