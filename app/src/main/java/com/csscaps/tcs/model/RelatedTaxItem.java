package com.csscaps.tcs.model;

import com.csscaps.tcs.database.table.TaxItem;
import com.csscaps.tcs.database.table.TaxType;

import java.util.List;

/**
 * Created by tl on 2018/5/15.
 */

public class RelatedTaxItem {
    private List<TaxItem> mTaxItemList;
    private List<TaxType> mTaxTypeList;

    public List<TaxItem> getTaxItemList() {
        return mTaxItemList;
    }

    public void setTaxItemList(List<TaxItem> taxItemList) {
        mTaxItemList = taxItemList;
    }

    public List<TaxType> getTaxTypeList() {
        return mTaxTypeList;
    }

    public void setTaxTypeList(List<TaxType> taxTypeList) {
        mTaxTypeList = taxTypeList;
    }
}
