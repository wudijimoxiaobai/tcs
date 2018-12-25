package com.csscaps.tcs.model;

import com.csscaps.tcs.database.table.Taxpayer;

import java.util.List;

/**
 * Created by tl on 2018/12/25.
 */

public class ReceiveAllTaxpayer extends ReceiveData {

    private List<Taxpayer> data;
    private String last_update_time;

    public List<Taxpayer> getData() {
        return data;
    }

    public void setData(List<Taxpayer> data) {
        this.data = data;
    }

    public String getLast_update_time() {
        return last_update_time;
    }

    public void setLast_update_time(String last_update_time) {
        this.last_update_time = last_update_time;
    }
}
