package com.csscaps.tcs.model;

import com.csscaps.tcs.database.table.TaxMethod;

import java.util.List;

/**
 * Created by tl on 2018/5/9.
 */

public class ReceiveTaxMethod extends ReceiveData {

    private List<TaxMethod> calc_rule_info;

    public List<TaxMethod> getCalc_rule_info() {
        return calc_rule_info;
    }

    public void setCalc_rule_info(List<TaxMethod> calc_rule_info) {
        this.calc_rule_info = calc_rule_info;
    }
}
