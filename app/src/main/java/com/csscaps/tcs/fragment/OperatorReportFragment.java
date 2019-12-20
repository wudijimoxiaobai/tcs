package com.csscaps.tcs.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.csscaps.common.base.BaseFragment;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.R;
import com.csscaps.tcs.adapter.OPeratorReportAdapter;
import com.csscaps.tcs.adapter.OPeratorReportAdapter2;
import com.csscaps.tcs.adapter.PeriodReportAdapter;
import com.csscaps.tcs.adapter.PeriodReportAdapter2;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.Invoice_Table;
import com.csscaps.tcs.dialog.SynDataDialog;
import com.csscaps.tcs.model.InvoiveSection;
import com.csscaps.tcs.model.OperatorReportSection;
import com.csscaps.tcs.model.PeriodReportSection;
import com.csscaps.tcs.utils.DatePickerDialogFragment;
import com.csscaps.tcs.utils.ExcelUtil;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

public class OperatorReportFragment extends BaseFragment {
    @BindView(R.id.data_to_data)
    TextView mDataToData;
    @BindView(R.id.data_from_date)
    TextView mDataFromDate;
    @BindView(R.id.list_view)
    ListView mListView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.c_period)
    ConstraintLayout mCperiod;
    @BindView(R.id.list_view2)
    ListView mListView2;
    @BindView(R.id.c_qty)
    ConstraintLayout mCqty;

    String years1;
    String years2;
    private OPeratorReportAdapter oPeratorReportAdapter;
    private OPeratorReportAdapter2 oPeratorReportAdapter2;
    private SynDataDialog mSynDataDialog = new SynDataDialog();
    List<OperatorReportSection> listPrs;

    @Override
    protected int getLayoutResId() {
        return R.layout.operator_report_fragment;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.id_export_item:
                        String[] title = {"No", "Operator", "Normal_Amount", "Qty", "Cancellation_Amount", "Qty", "Negative_Amount", "Qty"};

                        File file = new File(ExcelUtil.getSDPath() + "/bluetooth");
                        ExcelUtil.makeDir(file);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = sdf.format(new Date());//Calendar.getInstance().toString();

                        String fileName = file.toString() + "/" + "OperatorReportFragment" + time + ".xls";

                        ExcelUtil.initExcel(fileName, title);
                        ExcelUtil.writeObjListToExcel(getRecordData(), fileName, getActivity());
                        break;
                    case R.id.id_filter_item:
                        final DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
                        datePickerDialogFragment.setOnDateChooseListener(new DatePickerDialogFragment.OnDateChooseListener() {
                            @Override
                            public void onDateChoose(String s, String s1) {
                                years1 = "";
                                years2 = "";
                                if (Integer.parseInt(s.replace("-", "")) <= Integer.parseInt(s1.replace("-", ""))) {
                                    years1 = s;
                                    years2 = s1;
                                    List<OperatorReportSection> listPrs = getFinalData(years1, years2);
                                    oPeratorReportAdapter = new OPeratorReportAdapter(mContext, R.layout.period_report_list_item, listPrs);
                                    mListView.setAdapter(oPeratorReportAdapter);
                                    oPeratorReportAdapter2 = new OPeratorReportAdapter2(mContext, R.layout.period_report_list_item2, listPrs);
                                    mListView2.setAdapter(oPeratorReportAdapter2);
                                    mDataFromDate.setText(s.replace("-", ""));
                                    mDataToData.setText(s1.replace("-", ""));
                                    datePickerDialogFragment.dismiss();
                                } else {
                                    ToastUtil.showLong("Start time must be greater than end time");
                                }

                            }
                        });
                        datePickerDialogFragment.show(getFragmentManager(), "DatePickerDialogFragment");
                        break;
                    case R.id.id_switch_item:
                        if (mCperiod.getVisibility() == View.VISIBLE) {
                            mCperiod.setVisibility(View.GONE);
                            mCqty.setVisibility(View.VISIBLE);
                        } else {
                            mCqty.setVisibility(View.GONE);
                            mCperiod.setVisibility(View.VISIBLE);
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        mSynDataDialog.show(getFragmentManager(), "SynDataDialog");
        threadhandler();
        setyear();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.period_report, menu);
        MenuItem item = menu.findItem(R.id.id_export_item);
        MenuItem item2 = menu.findItem(R.id.id_filter_item);
        MenuItem item3 = menu.findItem(R.id.id_switch_item);
        setmenucolor(item);
        setmenucolor(item2);
        setmenucolor(item3);
        menu.setGroupVisible(R.menu.period_report, true);
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
                    oPeratorReportAdapter = new OPeratorReportAdapter(mContext, R.layout.period_report_list_item, listPrs);
                    oPeratorReportAdapter2 = new OPeratorReportAdapter2(mContext, R.layout.period_report_list_item2, listPrs);
                    mListView.setAdapter(oPeratorReportAdapter);
                    mListView2.setAdapter(oPeratorReportAdapter2);
                    mSynDataDialog.dismiss();
                }
                break;
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
                listPrs = getFinalData(years1, years2);
                if (listPrs.size() > 0) {
                    mHandler.sendEmptyMessage(0);
                } else {
                    mHandler.sendEmptyMessage(1);
                }
            }
        }).start();
    }

    public void setyear() {
        String years = "2018", months = "3";
        String months2 = "3";
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        if (month < 10) {
            if (month + 1 < 10) {
                months = "0" + String.valueOf(month+1);
                months2 = "0" + String.valueOf(month+1);
            } else {
                months = "0" + String.valueOf(month+1);
                months2 =String.valueOf(month+1);
            }
        } else {
            months = String.valueOf(month+1);
            months2 = String.valueOf(month+1);
        }
        years = String.valueOf(year);
        years1 = years + "-" + months;
        years2 = years + "-" + months2;
        mDataFromDate.setText(years + months);
        mDataToData.setText(years + months2);
    }

    public static int timeCompare(String startTime, String endTime) {
        int i = 0;
        //注意：传过来的时间格式必须要和这里填入的时间格式相同
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        try {
            Date date1 = dateFormat.parse(startTime);//开始时间
            Date date2 = dateFormat.parse(endTime);//结束时间
            // 1 结束时间小于开始时间 2 开始时间与结束时间相同 3 结束时间大于开始时间
            if (date2.getTime() < date1.getTime()) {
                //结束时间小于开始时间
                i = 1;
            } else if (date2.getTime() == date1.getTime()) {
                //开始时间与结束时间相同
                i = 2;
            } else if (date2.getTime() > date1.getTime()) {
                //结束时间大于开始时间
                i = 3;
            }
        } catch (Exception e) {

        }
        return i;
    }

    private List<Invoice> setNewDate2(String s, String s2) {
        List<InvoiveSection> invoiveSections = setNewDate(s, s2);
        List<Invoice> groupList = new ArrayList<>();
        int size = invoiveSections.size();
        for (int i = 0; i < size; i++) {
            if (!invoiveSections.get(i).isHeader()){
                Invoice t = (Invoice) invoiveSections.get(i).getT();
                groupList.add(t);
            }
        }
        return groupList;
    }

    private List<InvoiveSection> setNewDate3(String s, String s2) {
        List<Invoice> date = setNewDate2(s, s2);
        int count = date.size();
        List<InvoiveSection> groupList = new ArrayList<>();
        String header = "";
        for (int i = 0; i < count; i++) {
            Invoice invoice = date.get(i);
            String drawer_name = invoice.getDrawer_name();
            date.get(i).setTimeFormat(drawer_name);
            InvoiveSection<Invoice> mSection;
            if (TextUtils.equals(drawer_name, header)) {
                mSection = new InvoiveSection<>(drawer_name, invoice);
                groupList.add(mSection);
            } else {
                mSection = new InvoiveSection<>(true, drawer_name);
                groupList.add(mSection);
                InvoiveSection<Invoice> item = new InvoiveSection<>(drawer_name, invoice);
                groupList.add(item);
            }
            header = drawer_name;
        }
        return groupList;
    }

    private List<OperatorReportSection> getFinalData(String s, String s2) {
        int no = 0;
        double Normal_Amount = 0.00;
        int qty = 0;
        double Cancellation_Amount = 0;
        int qty2 = 0;
        double NI_amount = 0;
        int qty3 = 0;
        List<InvoiveSection> invoiveSections = setNewDate3(s, s2);
        List<OperatorReportSection> groupList = new ArrayList<>();
        int size = invoiveSections.size();
        for (int i = 0; i < size; i++) {
            InvoiveSection section = invoiveSections.get(i);
            Invoice t = (Invoice) section.getT();
            if (i < size - 1) {
                InvoiveSection section2 = invoiveSections.get(i + 1);
                if (section.getHanderContent().equals(section2.getHanderContent())) {
                    if (!section.isHeader()) {
                        if ("AVL".equals(t.getStatus())) {
                            try {
                                Normal_Amount += Double.parseDouble(t.getTotal_all());
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            qty++;
                        }
                        if ("DISA".equals(t.getStatus())) {
                            try {
                                Cancellation_Amount += Double.parseDouble(t.getTotal_all());
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            qty2++;
                        }
                        if ("NEG".equals(t.getStatus())) {
                            try {
                                NI_amount += Double.parseDouble(t.getTotal_all());
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            qty3++;
                        }
                    }
                } else {
                    no++;
                    if ("AVL".equals(t.getStatus())) {
                        try {
                            Normal_Amount += Double.parseDouble(t.getTotal_all());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        qty++;
                    }
                    if ("DISA".equals(t.getStatus())) {
                        try {
                            Cancellation_Amount += Double.parseDouble(t.getTotal_all());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        qty2++;
                    }
                    if ("NEG".equals(t.getStatus())) {
                        try {
                            NI_amount += Double.parseDouble(t.getTotal_all());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        qty3++;
                    }
                    OperatorReportSection operatorReportSection = new OperatorReportSection();
                    operatorReportSection.setNo(String.valueOf(no));
                    operatorReportSection.setOperator(section.getHanderContent());
                    operatorReportSection.setNormal_Amount(String.valueOf(new DecimalFormat("0.00").format(Normal_Amount)));
                    operatorReportSection.setQty(String.valueOf(qty));
                    operatorReportSection.setCancellation_Amount(String.valueOf(new DecimalFormat("0.00").format(Cancellation_Amount)));
                    operatorReportSection.setQty2(String.valueOf(qty2));
                    operatorReportSection.setNegative_Amount(String.valueOf(new DecimalFormat("0.00").format(NI_amount)));
                    operatorReportSection.setQty3(String.valueOf(qty3));
                    groupList.add(operatorReportSection);
                    Normal_Amount = 0;
                    qty = 0;
                    Cancellation_Amount = 0;
                    qty2 = 0;
                    NI_amount = 0;
                    qty3 = 0;
                }
            } else {
                no++;
                if ("AVL".equals(t.getStatus())) {
                    try {
                        Normal_Amount += Double.parseDouble(t.getTotal_all());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    qty++;
                }
                if ("DISA".equals(t.getStatus())) {
                    try {
                        Cancellation_Amount += Double.parseDouble(t.getTotal_all());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    qty2++;
                }
                if ("NEG".equals(t.getStatus())) {
                    try {
                        NI_amount += Double.parseDouble(t.getTotal_all());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    qty3++;
                }
                OperatorReportSection operatorReportSection = new OperatorReportSection();
                operatorReportSection.setNo(String.valueOf(no));
                operatorReportSection.setOperator(section.getHanderContent());
                operatorReportSection.setNormal_Amount(String.valueOf(new DecimalFormat("0.00").format(Normal_Amount)));
                operatorReportSection.setQty(String.valueOf(qty));
                operatorReportSection.setCancellation_Amount(String.valueOf(new DecimalFormat("0.00").format(Cancellation_Amount)));
                operatorReportSection.setQty2(String.valueOf(qty2));
                operatorReportSection.setNegative_Amount(String.valueOf(new DecimalFormat("0.00").format(NI_amount)));
                operatorReportSection.setQty3(String.valueOf(qty3));
                groupList.add(operatorReportSection);
            }
        }
        return groupList;
    }

    private List<InvoiveSection> setNewDate(String s, String s2) {
        List<InvoiveSection> date = setDate();
        int count = date.size();
        List<InvoiveSection> groupList1 = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            InvoiveSection section = date.get(i);
            if (timeCompare(s, section.getHanderContent()) >= 2) {
                if (timeCompare(s2, section.getHanderContent()) <= 2) {
                    groupList1.add(section);
                }
            }
        }
        return groupList1;
    }

    private List<InvoiveSection> setDate() {
        List<Invoice> date = select().from(Invoice.class).orderBy(Invoice_Table.client_invoice_datetime, false).queryList();
        int count = date.size();
        List<InvoiveSection> groupList = new ArrayList<>();
        String header = "";
        for (int i = 0; i < count; i++) {
            Invoice invoice = date.get(i);
            String time = invoice.getClient_invoice_datetime();
            String timeformat = DateUtils.dateToStr(DateUtils.getStringToDate(time, DateUtils.format_yyyyMMddHHmmss_24_EN), DateUtils.format_YYYY_MM_EN);
            date.get(i).setTimeFormat(timeformat);
            InvoiveSection<Invoice> mSection;
            if (TextUtils.equals(timeformat, header)) {
                mSection = new InvoiveSection<>(timeformat, invoice);
                groupList.add(mSection);
            } else {
                mSection = new InvoiveSection<>(true, timeformat);
                groupList.add(mSection);
                InvoiveSection<Invoice> item = new InvoiveSection<>(timeformat, invoice);
                groupList.add(item);
            }
            header = timeformat;
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
        for (int i = 0; i < listPrs.size(); i++) {
            ArrayList<String> beanList = new ArrayList<String>();
            beanList.add(listPrs.get(i).getNo());
            beanList.add(listPrs.get(i).getOperator());
            beanList.add(listPrs.get(i).getNormal_Amount());
            beanList.add(listPrs.get(i).getQty());
            beanList.add(listPrs.get(i).getCancellation_Amount());
            beanList.add(listPrs.get(i).getQty2());
            beanList.add(listPrs.get(i).getNegative_Amount());
            beanList.add(listPrs.get(i).getQty3());
            recordList.add(beanList);
        }
        return recordList;
    }
}
