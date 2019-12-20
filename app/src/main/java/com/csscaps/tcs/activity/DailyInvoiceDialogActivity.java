package com.csscaps.tcs.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.adapter.DailyInvoiceAdapter;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.Invoice_Table;
import com.csscaps.tcs.dialog.PreviewInvoiceDialog;
import com.csscaps.tcs.dialog.SlideDayDialog;
import com.csscaps.tcs.dialog.SynDataDialog;
import com.csscaps.tcs.utils.ExcelUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

public class DailyInvoiceDialogActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.data_to_data)
    TextView mDataToData;
    @BindView(R.id.list_view)
    ListView mListView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private PreviewInvoiceDialog previewInvoiceDialog;
    private List<Invoice> setlist;
    private DailyInvoiceAdapter dailyInvoiceAdapter;
    private SynDataDialog mSynDataDialog = new SynDataDialog();
    private String mData = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.daily_report_dialog;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.id_export_item:
                        String[] title = {"No", "Period", "Normal_Amount", "Qty", "Cancellation_Amount", "Qty", "Negative_Amount", "Qty"};

                        File file = new File(ExcelUtil.getSDPath() + "/bluetooth");
                        ExcelUtil.makeDir(file);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = sdf.format(new Date());//Calendar.getInstance().toString();

                        String fileName = file.toString() + "/" + "DailyInvoiceDialogFragment" + time + ".xls";

                        ExcelUtil.initExcel(fileName, title);
                        ExcelUtil.writeObjListToExcel(getRecordData(), fileName, DailyInvoiceDialogActivity.this);
                        break;
                    case R.id.id_filter_item:
                        mData = "";
                        final SlideDayDialog slideDayDialog = new SlideDayDialog(DailyInvoiceDialogActivity.this, R.style.SlideDialog, getIntent().getStringExtra("years"));
                        slideDayDialog.setYesOnclickListener(new SlideDayDialog.onYesOnclickListener() {
                            @Override
                            public void onYesOnclick(String s) {
                                List<Invoice> list = setlist(s);
                                mData = mData + s;
                                mDataToData.setText(mData);
                                dailyInvoiceAdapter = new DailyInvoiceAdapter(DailyInvoiceDialogActivity.this, R.layout.daily_invoice_list_item, list);
                                mListView.setAdapter(dailyInvoiceAdapter);
                                slideDayDialog.dismiss();
                            }
                        });
                        slideDayDialog.setNoOnclickListener(new SlideDayDialog.onNoOnclickListener() {
                            @Override
                            public void onNoClick() {
                                slideDayDialog.dismiss();
                            }
                        });
                        slideDayDialog.show();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        mListView.setOnItemClickListener(this);
        mSynDataDialog.show(getSupportFragmentManager(), "SynDataDialog");
        threadhandler();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.all_report, menu);
        MenuItem item = menu.findItem(R.id.id_export_item);
        MenuItem item2 = menu.findItem(R.id.id_filter_item);
        setmenucolor(item);
        setmenucolor(item2);
        menu.setGroupVisible(R.menu.all_report, true);
        return true;
    }

    private void setmenucolor(MenuItem item) {
        SpannableString spannableString = new SpannableString(item.getTitle());
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableString.length(), 0);
        item.setTitle(spannableString);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    dailyInvoiceAdapter = new DailyInvoiceAdapter(DailyInvoiceDialogActivity.this, R.layout.daily_invoice_list_item, setlist);
                    mListView.setAdapter(dailyInvoiceAdapter);
                    mSynDataDialog.dismiss();
                }
                case 1: {
                    mSynDataDialog.dismiss();
                }
                break;
            }
        }
    };

    private void threadhandler() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String years = getIntent().getStringExtra("years");
                mData = years.substring(0,years.length() - 2);
                mDataToData.setText(years);
                setlist = setlist(getIntent().getStringExtra("years"));
                if (setlist.size() > 0) {
                    mHandler.sendEmptyMessage(0);
                } else {
                    mHandler.sendEmptyMessage(1);
                }
            }
        }).start();
    }

    private List<Invoice> setlist(String s) {
        List<Invoice> date = select().from(Invoice.class).orderBy(Invoice_Table.client_invoice_datetime, false).queryList();
        int count = date.size();
        List<Invoice> groupList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Invoice invoice = date.get(i);
            if ((s.replace("-", "")).equals(invoice.getClient_invoice_datetime().substring(0, 8))) {
                groupList.add(invoice);
            }
        }
        return groupList;
    }

    /**
     * 将数据集合 转化成ArrayList<ArrayList<String>>
     *
     * @return
     */
    private ArrayList<ArrayList<String>> getRecordData() {
        ArrayList<ArrayList<String>> recordList = new ArrayList<>();
        for (int i = 0; i < setlist.size(); i++) {
            ArrayList<String> beanList = new ArrayList<String>();
            beanList.add(i + 1 + "");
            beanList.add(DateUtils.dateToStr(DateUtils.getStringToDate(setlist.get(i).getClient_invoice_datetime(), DateUtils.format_yyyyMMddHHmmss_24_EN), DateUtils.format_yyyy_MM_dd_HH_mm_ss_24_EN));
            if ("AVL".equals(setlist.get(i).getStatus())) {
                beanList.add(setlist.get(i).getTotal_all());
                beanList.add("1");
                beanList.add("0");
                beanList.add("0");
                beanList.add("0");
                beanList.add("0");
            } else if ("DISA".equals(setlist.get(i).getStatus())) {
                beanList.add("0");
                beanList.add("0");
                beanList.add(setlist.get(i).getTotal_all());
                beanList.add("1");
                beanList.add("0");
                beanList.add("0");
            } else if ("NEG".equals(setlist.get(i).getStatus())) {
                beanList.add("0");
                beanList.add("0");
                beanList.add("0");
                beanList.add("0");
                beanList.add(setlist.get(i).getTotal_all());
                beanList.add("1");
            }
            recordList.add(beanList);
        }
        return recordList;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Invoice invoice = setlist.get(i);
        if (previewInvoiceDialog == null)
            previewInvoiceDialog = new PreviewInvoiceDialog(invoice);
        previewInvoiceDialog.show(getSupportFragmentManager(), "PreviewInvoiceDialog");
    }
}
