package com.csscaps.tcs.model;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tl on 2018/7/18.
 */

public class NQD {
    @XStreamImplicit
    private List<String> mItemNames=new ArrayList<>();

    public List<String> getItemNames() {
        return mItemNames;
    }

    public void setItemNames(List<String> itemNames) {
        mItemNames = itemNames;
    }
}
