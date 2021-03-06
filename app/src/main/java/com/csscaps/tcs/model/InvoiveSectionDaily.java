package com.csscaps.tcs.model;

public class InvoiveSectionDaily<T> {

    private boolean isHeader;
    private String handerContent;
    private T t;

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    @Override
    public String toString() {
        return "InvoiveSection{" +
                "isHeader=" + isHeader +
                ", handerContent='" + handerContent + '\'' +
                ", t=" + t +
                '}';
    }

    public InvoiveSectionDaily() {
    }

    public InvoiveSectionDaily(String handerContent, T t) {
        this.handerContent = handerContent;
        this.t = t;
    }

    public InvoiveSectionDaily(boolean isHeader, String handerContent, T t) {
        this.isHeader = isHeader;
        this.handerContent = handerContent;
        this.t = t;
    }

    public InvoiveSectionDaily(boolean isHeader, String handerContent) {
        this.isHeader = isHeader;
        this.handerContent = handerContent;
    }



    public String getHanderContent() {
        return handerContent;
    }

    public void setHanderContent(String handerContent) {
        this.handerContent = handerContent;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
