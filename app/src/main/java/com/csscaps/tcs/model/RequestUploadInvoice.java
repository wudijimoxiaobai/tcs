package com.csscaps.tcs.model;

import com.csscaps.tcs.database.table.Invoice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tl on 2018/5/30.
 */

public class RequestUploadInvoice extends RequestData {
    List<Invoice> invoice_data=new ArrayList<>();

    public List<Invoice> getInvoice_data() {
        return invoice_data;
    }

    public void setInvoice_data(List<Invoice> invoice_data) {
        this.invoice_data = invoice_data;
    }
}
