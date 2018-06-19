package com.csscaps.tcs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.common.utils.ObserverActionUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.fragment.RequestInvoiceFragment;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;

/**
 * Created by tl on 2018/6/12.
 */

public class InvoiceDetailsActivity extends BaseActivity {
    @BindView(R.id.back)
    TextView mBack;
    @BindView(R.id.next)
    TextView mNext;
    @BindView(R.id.upload)
    TextView mUpload;

    @Override
    protected int getLayoutResId() {
        return R.layout.invoice_details_activity;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    protected void parseArgumentsFromIntent(Intent argIntent) {
        Invoice  invoice= (Invoice) argIntent.getSerializableExtra("invoice");
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }


    @OnClick({R.id.back, R.id.next, R.id.upload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.next:
                Subscription subscription= ObserverActionUtils.subscribe(0, RequestInvoiceFragment.class);
                if(subscription!=null)subscription.unsubscribe();
                startActivity(new Intent(this,ApplicationListActivity.class));
                break;
            case R.id.upload:
                break;
        }
    }
}
