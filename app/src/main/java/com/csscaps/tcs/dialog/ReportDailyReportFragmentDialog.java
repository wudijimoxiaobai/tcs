package com.csscaps.tcs.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.csscaps.common.utils.DateUtils;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.R;
import com.csscaps.tcs.activity.DailyInvoiceDialogActivity;
import com.csscaps.tcs.adapter.PeriodReportAdapter;
import com.csscaps.tcs.adapter.PeriodReportAdapter2;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.Invoice_Table;
import com.csscaps.tcs.model.InvoiveSectionDaily;
import com.csscaps.tcs.model.PeriodReportSection;
import com.csscaps.tcs.utils.DailyDatePickerDialogFragment;
import com.csscaps.tcs.utils.ExcelUtil;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

public class ReportDailyReportFragmentDialog extends DialogFragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.data_to_data)
    TextView mDataToData;
    @BindView(R.id.list_view)
    ListView mListView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.list_view2)
    ListView mListView2;
    @BindView(R.id.c_period)
    ConstraintLayout mCperiod;
    @BindView(R.id.c_qty)
    ConstraintLayout mCqty;

    String years1;
    private PeriodReportAdapter mPeriodReportAdapter;
    private PeriodReportAdapter2 periodReportAdapter2;
    private SynDataDialog mSynDataDialog = new SynDataDialog();
    List<PeriodReportSection> listPrs;
    private String mData;

    public ReportDailyReportFragmentDialog(String data) {
        mData = data;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 1), (int) (dm.heightPixels * 1));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_theme3);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.report_daily_report_fragment, null);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
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

                        String fileName = file.toString() + "/" + "DailyReportFragment" + time + ".xls";

                        ExcelUtil.initExcel(fileName, title);
                        ExcelUtil.writeObjListToExcel(getRecordData(), fileName, getActivity());
                        break;
                    case R.id.id_filter_item:
                        final DailyDatePickerDialogFragment dailyDatePickerDialogFragment = new DailyDatePickerDialogFragment();
                        dailyDatePickerDialogFragment.setOnDateChooseListener(new DailyDatePickerDialogFragment.OnDateChooseListener() {
                            @Override
                            public void onDateChoose(String s1) {
                                years1 = "";
                                if (Integer.parseInt(s1.replace("-", "")) <= Integer.parseInt(s1.replace("-", ""))) {
                                    years1 = s1;
                                    List<PeriodReportSection> list = getFinalData(years1);
                                    mPeriodReportAdapter = new PeriodReportAdapter(getContext(), R.layout.period_report_list_item, list);
                                    mListView.setAdapter(mPeriodReportAdapter);
                                    periodReportAdapter2 = new PeriodReportAdapter2(getContext(), R.layout.period_report_list_item2, listPrs);
                                    mListView2.setAdapter(periodReportAdapter2);
                                    mDataToData.setText(s1.replace("-", ""));
                                    dailyDatePickerDialogFragment.dismiss();
                                } else {
                                    ToastUtil.showLong("Start time must be greater than end time");
                                }
                            }
                        });
                        dailyDatePickerDialogFragment.show(getFragmentManager(), "DatePickerDialogFragment");
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
        mListView.setOnItemClickListener(this);
        mListView2.setOnItemClickListener(this);
        mSynDataDialog.show(getFragmentManager(), "SynDataDialog");
        threadhandler();
        setyear();
        return view;
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
                    mPeriodReportAdapter = new PeriodReportAdapter(getContext(), R.layout.period_report_list_item, listPrs);
                    periodReportAdapter2 = new PeriodReportAdapter2(getContext(), R.layout.period_report_list_item2, listPrs);
                    mListView.setAdapter(mPeriodReportAdapter);
                    mListView2.setAdapter(periodReportAdapter2);
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
                listPrs = getFinalData(mData);
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
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        if (month < 10) {
            months = "0" + String.valueOf(month + 1);
        } else {
            months = String.valueOf(month + 1);
        }
        years = String.valueOf(year);
        years1 = years + "-" + months;
        mDataToData.setText(years + months);
    }

    private List<PeriodReportSection> getFinalData(String s) {
        int no = 0;
        double Normal_Amount = 0.00;
        int qty = 0;
        double Cancellation_Amount = 0.00;
        int qty2 = 0;
        double NI_amount = 0.00;
        int qty3 = 0;
        List<InvoiveSectionDaily> invoiveSections = setNewDate(s);
        List<PeriodReportSection> groupList = new ArrayList<>();
        int size = invoiveSections.size();
        for (int i = 0; i < size; i++) {
            InvoiveSectionDaily section = invoiveSections.get(i);
            Invoice t = (Invoice) section.getT();
            if (i < size - 1) {
                InvoiveSectionDaily section2 = invoiveSections.get(i + 1);
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
                    PeriodReportSection periodReportSection = new PeriodReportSection();
                    periodReportSection.setNo(String.valueOf(no));
                    periodReportSection.setPeriod(section.getHanderContent());
                    periodReportSection.setNormal_Amount(String.valueOf(new DecimalFormat("0.00").format(Normal_Amount)));
                    periodReportSection.setQty(String.valueOf(qty));
                    periodReportSection.setCancellation_Amount(String.valueOf(new DecimalFormat("0.00").format(Cancellation_Amount)));
                    periodReportSection.setQty2(String.valueOf(qty2));
                    periodReportSection.setNegative_Amount(String.valueOf(new DecimalFormat("0.00").format(NI_amount)));
                    periodReportSection.setQty3(String.valueOf(qty3));
                    groupList.add(periodReportSection);
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
                PeriodReportSection periodReportSection = new PeriodReportSection();
                periodReportSection.setNo(String.valueOf(no));
                periodReportSection.setPeriod(section.getHanderContent());
                periodReportSection.setNormal_Amount(String.valueOf(new DecimalFormat("0.00").format(Normal_Amount)));
                periodReportSection.setQty(String.valueOf(qty));
                periodReportSection.setCancellation_Amount(String.valueOf(new DecimalFormat("0.00").format(Cancellation_Amount)));
                periodReportSection.setQty2(String.valueOf(qty2));
                periodReportSection.setNegative_Amount(String.valueOf(new DecimalFormat("0.00").format(NI_amount)));
                periodReportSection.setQty3(String.valueOf(qty3));
                groupList.add(periodReportSection);
            }
        }
        return groupList;
    }

    private List<InvoiveSectionDaily> setNewDate(String s) {
        List<InvoiveSectionDaily> date = setDate();
        int count = date.size();
        List<InvoiveSectionDaily> groupList1 = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            InvoiveSectionDaily section = date.get(i);
            //String timeformat = DateUtils.dateToStr(DateUtils.getStringToDate(section.getHanderContent(), DateUtils.format_yyyyMMddHHmmss_24_EN), DateUtils.format_yyyy_MM_EN);
            String timeformat = section.getHanderContent().substring(0, 7);
            int t = timeCompare(s, timeformat);
            if (t == 2) {
                groupList1.add(section);
            }
        }
        return groupList1;
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

    private List<InvoiveSectionDaily> setDate() {
        List<Invoice> date = select().from(Invoice.class).orderBy(Invoice_Table.client_invoice_datetime, false).queryList();
        int count = date.size();
        List<InvoiveSectionDaily> groupList = new ArrayList<>();
        String header = "";
        for (int i = 0; i < count; i++) {
            Invoice invoice = date.get(i);
            String time = invoice.getClient_invoice_datetime();
            String timeformat = DateUtils.dateToStr(DateUtils.getStringToDate(time, DateUtils.format_yyyyMMddHHmmss_24_EN), DateUtils.format_yyyy_MM_dd_EN);
            date.get(i).setTimeFormat(timeformat);
            InvoiveSectionDaily<Invoice> mSection;
            if (TextUtils.equals(timeformat, header)) {
                mSection = new InvoiveSectionDaily<>(timeformat, invoice);
                groupList.add(mSection);
            } else {
                mSection = new InvoiveSectionDaily<>(true, timeformat);
                groupList.add(mSection);
                InvoiveSectionDaily<Invoice> item = new InvoiveSectionDaily<>(timeformat, invoice);
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
            beanList.add(listPrs.get(i).getPeriod());
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getContext(), DailyInvoiceDialogActivity.class);
        //可传递多种类型的数据
        intent.putExtra("years", listPrs.get(i).getPeriod());
        startActivity(intent);
    }
}
