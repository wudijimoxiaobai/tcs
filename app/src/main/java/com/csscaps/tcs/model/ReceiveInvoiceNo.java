package com.csscaps.tcs.model;

import java.util.List;

/**
 * Created by tl on 2018/5/22.
 */

public class ReceiveInvoiceNo extends ReceiveData {
    private List<InvoiceNoModel> invoice_data ;

    public List<InvoiceNoModel> getInvoice_data() {
        return invoice_data;
    }

    public void setInvoice_data(List<InvoiceNoModel> invoice_data) {
        this.invoice_data = invoice_data;
    }
}
