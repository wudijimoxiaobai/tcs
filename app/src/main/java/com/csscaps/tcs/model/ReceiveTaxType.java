package com.csscaps.tcs.model;

import com.csscaps.tcs.database.table.TaxType;

import java.util.List;

/**
 * Created by tl on 2018/5/9.
 */

public class ReceiveTaxType extends ReceiveData {
    private List<TaxType> taxtype_info;

    public List<TaxType> getTaxtype_info() {
        return taxtype_info;
    }

    public void setTaxtype_info(List<TaxType> taxtype_info) {
        this.taxtype_info = taxtype_info;
    }
}
