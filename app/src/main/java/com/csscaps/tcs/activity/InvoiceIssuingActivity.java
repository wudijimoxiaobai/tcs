package com.csscaps.tcs.activity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.csscaps.common.base.BaseActivity;
import com.csscaps.common.utils.AppSP;
import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.InvoiceType;
import com.csscaps.tcs.database.table.InvoiceType_Table;
import com.csscaps.tcs.model.MyTaxpayer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/5/22.
 */

public class InvoiceIssuingActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    @BindView(R.id.invoice_type)
    AppCompatSpinner mInvoiceType;
    @BindView(R.id.invoice_no_from)
    TextView mInvoiceNoFrom;
    @BindView(R.id.remaining_invoice)
    TextView mRemainingInvoice;
    @BindView(R.id.object)
    AppCompatSpinner mObject;
    @BindView(R.id.language_in_invoice)
    AppCompatSpinner mLanguageInInvoice;
    @BindView(R.id.tin)
    EditText mTin;
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.national_id)
    TextView mNationalId;
    @BindView(R.id.passport)
    TextView mPassport;
    @BindView(R.id.address)
    TextView mAddress;
    @BindView(R.id.tel)
    TextView mTel;

    private List<InvoiceType> mTaxTypes;
    private ArrayList<String> invoiceTypeNames;
    private Invoice mInvoice;
    private MyTaxpayer myTaxpayer;

    @Override
    protected int getLayoutResId() {
        return R.layout.invoice_issuing_activity;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mInvoice=new Invoice();
        String myTaxpayerString = AppSP.getString("MyTaxpayer");
        myTaxpayer = JSON.parseObject(myTaxpayerString, MyTaxpayer.class);
        mInvoice.setSeller_tin(myTaxpayer.getTin());
        mInvoice.setSeller_address(myTaxpayer.getPhysical_address());
        mInvoice.setSeller_name(myTaxpayer.getName_in_english());
        mInvoice.setSeller_phone(myTaxpayer.getTelphone());
        int offset = DeviceUtils.dip2Px(this, 50);
        mInvoiceType.setDropDownVerticalOffset(offset);
        mLanguageInInvoice.setDropDownVerticalOffset(offset);
        mObject.setDropDownVerticalOffset(offset);
        mTaxTypes = select().from(InvoiceType.class).queryList();
        invoiceTypeNames = new ArrayList<>();
        for (InvoiceType it : mTaxTypes) {
            invoiceTypeNames.add(it.getInvoice_type_name());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, invoiceTypeNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mInvoiceType.setAdapter(adapter);
        mInvoiceType.setOnItemSelectedListener(this);
        mLanguageInInvoice.setOnItemSelectedListener(this);
        mObject.setOnItemSelectedListener(this);
    }


    @OnClick({R.id.back, R.id.next, R.id.tin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.next:
                break;
            case R.id.tin:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == mInvoiceType) {
            String str=  invoiceTypeNames.get(i);
            InvoiceType invoiceType=  select().from(InvoiceType.class).where(InvoiceType_Table.invoice_type_name.eq(str)).querySingle();
            mInvoice.setInvoice_type_name(str);
            mInvoice.setInvoice_type_code(invoiceType.getInvoice_type_code());

        } else if (adapterView == mLanguageInInvoice) {
            String str[] = getResources().getStringArray(R.array.language_no);
            String s = str[i];
        } else if (adapterView == mObject) {
            String str[] = getResources().getStringArray(R.array.objects);
            String s = str[i];
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
