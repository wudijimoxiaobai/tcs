package com.csscaps.tcs.model;

import java.util.List;

/**
 * Created by tl on 2018/6/21.
 */

public class ReceiveRequestResult extends ReceiveData {
    List<RequestResultModel> invoice_data;

    public List<RequestResultModel> getInvoice_data() {
        return invoice_data;
    }

    public void setInvoice_data(List<RequestResultModel> invoice_data) {
        this.invoice_data = invoice_data;
    }
}
