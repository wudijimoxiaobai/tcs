package com.tax.fcr.library;

import android.content.Context;

import com.tax.fcr.library.network.Api;
import com.tax.fcr.library.utils.NetworkUtils;

/**
 * Created by tl on 2017/12/25.
 */

public class TCSInvoice {

    /**
     * 初始化
     * @param context
     * @param url
     */
    public static void init(Context context, String url){
        NetworkUtils.mContext=context;
        Api.setBaseUrl(url);
    }

    public String getVersion() {
        ///版本规则：主版本号.次版本号.修订版.日期
        ///其中日期格式为yyyyMMdd
        String _ver = BuildConfig.VERSION_NAME +"."+BuildConfig.DATE;
        return _ver;
    }


    public String request(String funcID, String param) {
        String _result = "";
        switch (funcID) {
            case "ATSI001"://发票上传
                _result = uploadInvoice(param);
                break;
            case "ATSI002"://发票查询
                _result = queryInvoice(param);
                break;
            case "ATSI003"://发票打印
                _result = printInvoice(param);
                break;
            case "ATSI004"://发票开具
                _result = issuingInvoice(param);
                break;
        }
        return _result;
    }


    /**
     * 上传发票
     *
     * @param param
     * @return
     */
    private String uploadInvoice(String param) {


        return "";
    }

    /**
     * 查询发票
     *
     * @param param
     * @return
     */
    private String queryInvoice(String param) {

        return "";
    }

    /**
     * 打印发票
     *
     * @param param
     * @return
     */
    private String printInvoice(String param) {

        return "";
    }

    /**
     * 开发票
     *
     * @param param
     * @return
     */
    private String issuingInvoice(String param) {

        return "";
    }

}
