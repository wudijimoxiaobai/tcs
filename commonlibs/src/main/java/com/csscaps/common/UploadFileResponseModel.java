package com.csscaps.common;

/**
 * Created by tanglei on 16/7/20.
 */
public class UploadFileResponseModel {
    private int code;
    private FileInfo data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public FileInfo getData() {
        return data;
    }

    public void setData(FileInfo data) {
        this.data = data;
    }
}

class FileInfo{
    private String url;
    private int height;
    private int state;
    private int width;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
