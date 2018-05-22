package com.csscaps.tcs.model;

import com.csscaps.tcs.database.table.Invoice;

import java.util.List;

/**
 * Created by tl on 2018/5/22.
 */

public class UpdateInvoice extends RequestData {
       private List<Invoice> invoice_data;

    public List<Invoice> getInvoice_data() {
        return invoice_data;
    }

    public void setInvoice_data(List<Invoice> invoice_data) {
        this.invoice_data = invoice_data;
    }
}
