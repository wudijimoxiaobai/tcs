package com.csscaps.tcs.model;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tl on 2018/7/18.
 */

public class QD1 {

    @XStreamImplicit
    private List<QUA> QUAs=new ArrayList<>();

    public List<QUA> getQUAs() {
        return QUAs;
    }

    public void setQUAs(List<QUA> QUAs) {
        this.QUAs = QUAs;
    }
}
