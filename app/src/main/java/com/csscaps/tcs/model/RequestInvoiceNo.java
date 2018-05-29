package com.csscaps.tcs.model;

import java.util.List;

/**
 * Created by tl on 2018/5/22.
 */

public class RequestInvoiceNo extends RequestData{
    private List<InvoiceNoModel> invoice_type_info;
    public List<InvoiceNoModel> getInvoice_type_info() {
        return invoice_type_info;
    }

    public void setInvoice_type_info(List<InvoiceNoModel> invoice_type_info) {
        this.invoice_type_info = invoice_type_info;
    }


}
