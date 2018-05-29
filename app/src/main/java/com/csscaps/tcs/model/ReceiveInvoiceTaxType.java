package com.csscaps.tcs.model;

import com.csscaps.tcs.database.table.InvoiceTaxType;

import java.util.List;

/**
 * Created by tl on 2018/5/9.
 */

public class ReceiveInvoiceTaxType extends ReceiveData {

    private List<InvoiceTaxType> invoice_tax_info;

    public List<InvoiceTaxType> getInvoice_tax_info() {
        return invoice_tax_info;
    }

    public void setInvoice_tax_info(List<InvoiceTaxType> invoice_tax_info) {
        this.invoice_tax_info = invoice_tax_info;
    }
}
