package com.csscaps.tcs.model;

import com.csscaps.tcs.database.table.TaxItem;

import java.util.List;

/**
 * Created by tl on 2018/5/9.
 */

public class ReceiveTaxItem extends ReceiveData {

    private List<TaxItem> taxitem_info;

    public List<TaxItem> getTaxitem_info() {
        return taxitem_info;
    }

    public void setTaxitem_info(List<TaxItem> taxitem_info) {
        this.taxitem_info = taxitem_info;
    }

}
