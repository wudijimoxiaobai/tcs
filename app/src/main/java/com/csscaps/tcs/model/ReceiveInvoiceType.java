package com.csscaps.tcs.model;

import com.csscaps.tcs.database.table.InvoiceType;

import java.util.List;

/**
 * Created by tl on 2018/5/9.
 */

public class ReceiveInvoiceType extends ReceiveData {

    private List<InvoiceType> invoice_type_info;

    public List<InvoiceType> getInvoice_type_info() {
        return invoice_type_info;
    }

    public void setInvoice_type_info(List<InvoiceType> invoice_type_info) {
        this.invoice_type_info = invoice_type_info;
    }
}
