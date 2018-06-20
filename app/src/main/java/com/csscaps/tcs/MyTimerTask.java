package com.csscaps.tcs;

import com.alibaba.fastjson.JSON;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.Invoice_Table;
import com.csscaps.tcs.model.RequestInvoiceResult;
import com.tax.fcr.library.network.Api;
import com.tax.fcr.library.network.IPresenter;
import com.tax.fcr.library.network.RequestModel;

import java.util.List;
import java.util.TimerTask;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/6/20.
 */

public class MyTimerTask extends TimerTask implements IPresenter {

    private final String DISA = "DISA";
    private final String NEG = "NEG";

    @Override
    public void run() {
        List<Invoice> disas = select().from(Invoice.class).where(Invoice_Table.status.eq(DISA)).and(Invoice_Table.approveFlag.eq("4")).and(Invoice_Table.requestStatus.eq("1")).queryList();
        List<Invoice> engs = select().from(Invoice.class).where(Invoice_Table.status.eq(NEG)).and(Invoice_Table.approveFlag.eq("4")).and(Invoice_Table.requestStatus.eq("1")).queryList();
        request(disas,ServerConstants.ATCS019);
        request(engs,ServerConstants.ATCS017);
    }

    private void request(List<Invoice> list,String funId){
        RequestInvoiceResult requestInvoiceResult=new RequestInvoiceResult();
        requestInvoiceResult.setFuncid(funId);
        requestInvoiceResult.setInvoice(list);
        RequestModel requestModel = new RequestModel();
        requestModel.setFuncid(requestInvoiceResult.getFuncid());
        requestInvoiceResult.setDevicesn(requestModel.getDevicesn());
        requestModel.setData(JSON.toJSONString(requestInvoiceResult));
        Api.post(this,requestModel);
    }

    @Override
    public void onSuccess(String requestPath, String objectString) {
         switch (requestPath) {
                     case ServerConstants.ATCS019:

                         break;
                     case ServerConstants.ATCS017:

                         break;
                 }
    }

    @Override
    public void onFailure(String requestPath, String errorMes) {

    }


}
