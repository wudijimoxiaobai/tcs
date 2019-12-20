package com.csscaps.tcs.model;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Created by tl on 2018/7/18.
 */

public class ItemName {


    private String name;

    @XStreamAsAttribute()
    private String extendSize = "false";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtendSize() {
        return extendSize;
    }

    public void setExtendSize(String extendSize) {
        this.extendSize = extendSize;
    }
}
