package com.csscaps.tcs.model;

import java.util.List;

public class StatisticsReportModel {

    List<String> type;

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "StatisticsReportModel{" +
                "type=" + type +
                '}';
    }
}
