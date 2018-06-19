package com.csscaps.tcs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.csscaps.common.base.BaseActivity;
import com.csscaps.common.utils.AppSP;
import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.common.utils.ObserverActionUtils;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.R;
import com.csscaps.tcs.TCSApplication;
import com.csscaps.tcs.database.table.Customer;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.InvoiceNo;
import com.csscaps.tcs.database.table.InvoiceNo_Table;
import com.csscaps.tcs.database.table.InvoiceType;
import com.csscaps.tcs.database.table.InvoiceType_Table;
import com.csscaps.tcs.database.table.Product;
import com.csscaps.tcs.dialog.SelectCustomerDialog;
import com.csscaps.tcs.model.MyTaxpayer;
import com.raizlabs.android.dbflow.structure.database.FlowCursor;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/5/22.
 */

public class InvoiceIssuingActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, Action1<Customer> {
    @BindView(R.id.invoice_type)
    AppCompatSpinner mInvoiceType;
    @BindView(R.id.invoice_no_from)
    TextView mInvoiceNoFrom;
    @BindView(R.id.remaining_invoice)
    TextView mRemainingInvoice;
    @BindView(R.id.object)
    TextView mObject;
    @BindView(R.id.language_in_invoice)
    AppCompatSpinner mLanguageInInvoice;
    @BindView(R.id.tin)
    TextView mTin;
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

    private List<InvoiceType> mInvoiceTypes;
    private ArrayList<String> invoiceTypeNames;
    public static Invoice mInvoice;
    private MyTaxpayer myTaxpayer;
    private InvoiceType invoiceType;
    private int pObject;
    private String cInvoiceTypeCode;

    @Override
    protected int getLayoutResId() {
        return R.layout.invoice_issuing_activity;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mInvoice = new Invoice();
        mInvoice.setDrawer_name(TCSApplication.currentUser.getUserName());
        String myTaxpayerString = AppSP.getString("MyTaxpayer");
        myTaxpayer = JSON.parseObject(myTaxpayerString, MyTaxpayer.class);
        if (myTaxpayer != null) {
            mInvoice.setSeller_tin(myTaxpayer.getTin());
            mInvoice.setSeller_address(myTaxpayer.getPhysical_address());
            mInvoice.setSeller_name(myTaxpayer.getName_in_english());
            mInvoice.setSeller_phone(myTaxpayer.getTelphone());
        }
        int offset = DeviceUtils.dip2Px(this, 50);
        mInvoiceType.setDropDownVerticalOffset(offset);
        mLanguageInInvoice.setDropDownVerticalOffset(offset);
        mInvoiceTypes = select().from(InvoiceType.class).queryList();
        invoiceTypeNames = new ArrayList<>();
        for (InvoiceType it : mInvoiceTypes) {
            invoiceTypeNames.add(it.getInvoice_type_name());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, invoiceTypeNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mInvoiceType.setAdapter(adapter);
        mInvoiceType.setOnItemSelectedListener(this);
        mLanguageInInvoice.setOnItemSelectedListener(this);
        if (mInvoiceTypes.size() == 0) {
            ToastUtil.showShort(getString(R.string.hit22));
        }
        ObserverActionUtils.addAction(this);

    }


    @OnClick({R.id.back, R.id.next, R.id.tin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.next:
                if (pObject == 0 && TextUtils.isEmpty(mInvoice.getPurchaser_id_number())) {
                    ToastUtil.showShort(getString(R.string.hit23));
                    return;
                }
                if (pObject == 1 &&TextUtils.isEmpty(mInvoice.getPurchaser_tin()) ) {
                    ToastUtil.showShort(getString(R.string.hit23));
                    return;
                }
//                if (pObject == 2 && TextUtils.isEmpty(mInvoice.getPurchaser_id_number()) && TextUtils.isEmpty(mInvoice.getPurchaser_tin())) {
//                    ToastUtil.showShort(getString(R.string.hit23));
//                    return;
//                }
                Intent intent = new Intent(this, ProductListActivity.class);
                startActivity(intent);
                break;
            case R.id.tin:
                try {
                    SelectCustomerDialog selectCustomerDialog = new SelectCustomerDialog(invoiceType.getInvoiceObject());
                    selectCustomerDialog.show(getSupportFragmentManager(), "SelectCustomerDialog");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == mInvoiceType) {
            String str = invoiceTypeNames.get(i);
            initInvoiceType(str);
        } else if (adapterView == mLanguageInInvoice) {
            String str[] = getResources().getStringArray(R.array.language_no);
            String s = str[i];
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void initInvoiceType(String str) {
        invoiceType = select().from(InvoiceType.class).where(InvoiceType_Table.invoice_type_name.eq(str)).querySingle();
        String code = invoiceType.getInvoice_type_code();
        mInvoice.setInvoice_type_name(str);
        mInvoice.setInvoice_type_code(code);
        mInvoice.setInvoice_type_uid(invoiceType.getInvoice_type_uid());
        switch (invoiceType.getInvoiceObject()) {
            case 0:
                mObject.setText("B");
                break;
            case 1:
                mObject.setText("A");
                break;
            case 2:
                mObject.setText("C");
                break;
        }
        FlowCursor flowCursor = select().from(InvoiceNo.class).where(InvoiceNo_Table.invoice_type_code.eq(code)).orderBy(InvoiceNo_Table.id, true).query();
        flowCursor.moveToFirst();
        String invoice_num = flowCursor.getStringOrDefault("invoice_num");
        if (!TextUtils.isEmpty(invoice_num)) {
            mInvoiceNoFrom.setText(invoice_num);
            mInvoice.setInvoice_no(invoice_num);
            mRemainingInvoice.setText((flowCursor.getCount() - 1) + "");
        } else {
            ToastUtil.showShort(getString(R.string.hit24));
        }
        flowCursor.close();
        int cObject = invoiceType.getInvoiceObject();
        if (cObject != pObject) call(null);
        pObject = cObject;
        if (!code.equals(cInvoiceTypeCode)) {
            cInvoiceTypeCode = code;
            resetProductTax();
        }
    }

    private void resetProductTax() {
        List<Product> products = mInvoice.getProducts();
        if (products != null) {
            for (Product p : products) {
                p.setProductModel(null);
            }
        }
    }

    @Override
    public void call(Customer customer) {
        mTin.setText(customer != null ? customer.getTin() : null);
        mName.setText(customer != null ? customer.getName() : null);
        mNationalId.setText(customer != null ? customer.getNationalId() : null);
        mPassport.setText(customer != null ? customer.getPassport() : null);
        mAddress.setText(customer != null ? customer.getAddress() : null);
        mTel.setText(customer != null ? customer.getTel() : null);
        mInvoice.setPurchaser_tin(customer != null ? customer.getTin() : null);
        mInvoice.setPurchaser_phone(customer != null ? customer.getTel() : null);
        mInvoice.setPurchaser_address(customer != null ? customer.getAddress() : null);
        mInvoice.setPurchaser_name(customer != null ? customer.getName() : null);
        if (customer != null) {
            if (!TextUtils.isEmpty(customer.getNationalId())) {
                mInvoice.setPurchaser_id_type("ID");
                mInvoice.setPurchaser_id_number(customer.getNationalId());
            } else if (!TextUtils.isEmpty(customer.getPassport())) {
                mInvoice.setPurchaser_id_type("PASPT");
                mInvoice.setPurchaser_id_number(customer.getPassport());
            }
        } else {
            mInvoice.setPurchaser_id_type(null);
            mInvoice.setPurchaser_id_number(null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInvoice = null;
    }
}
