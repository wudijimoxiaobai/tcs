package com.csscaps.tcs.model;

import com.csscaps.tcs.database.table.Invoice;

import java.util.List;

/**
 * Created by tl on 2018/6/20.
 */

public class RequestInvoiceResult extends RequestData {
    List<Invoice> invoice;

    public List<Invoice> getInvoice() {
        return invoice;
    }

    public void setInvoice(List<Invoice> invoice) {
        this.invoice = invoice;
    }
}
