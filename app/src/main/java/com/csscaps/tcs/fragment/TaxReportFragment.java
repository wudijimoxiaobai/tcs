package com.csscaps.tcs.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
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
import com.csscaps.tcs.adapter.CancellationNegativeAdapter;
import com.csscaps.tcs.adapter.TaxReportAdapter;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.Invoice_Table;
import com.csscaps.tcs.database.table.ProductModel;
import com.csscaps.tcs.dialog.SynDataDialog;
import com.csscaps.tcs.model.CancelNegativeNodel;
import com.csscaps.tcs.model.InvoiveSection;
import com.csscaps.tcs.model.TaxReportSection;
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

public class TaxReportFragment extends BaseFragment {

    @BindView(R.id.data_to_data)
    TextView mDataToData;
    @BindView(R.id.data_from_date)
    TextView mDataFromDate;
    @BindView(R.id.list_view)
    ListView mListView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    String years1;
    String years2;
    private TaxReportAdapter mPeriodReportAdapter;
    private SynDataDialog mSynDataDialog = new SynDataDialog();
    List<TaxReportSection> listPrs;

    @Override
    protected int getLayoutResId() {
        return R.layout.tax_report_fragment;
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
                        String[] title = {"Period", "TA", "VAT", "BPT_F", "BPT_P", "SD_F", "SD_L", "Fee"};

                        File file = new File(ExcelUtil.getSDPath() + "/bluetooth");
                        ExcelUtil.makeDir(file);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = sdf.format(new Date());//Calendar.getInstance().toString();

                        String fileName = file.toString() + "/" + "TaxReportFragment" + time + ".xls";

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
                                    List<TaxReportSection> listPrs = getfinalData2(years1, years2);
                                    Log.e("data",s + s1);
                                    mPeriodReportAdapter = new TaxReportAdapter(mContext, R.layout.tax_report_list_item, listPrs);
                                    mListView.setAdapter(mPeriodReportAdapter);
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
        inflater.inflate(R.menu.all_report, menu);
        MenuItem item = menu.findItem(R.id.id_export_item);
        MenuItem item2 = menu.findItem(R.id.id_filter_item);
        setmenucolor(item);
        setmenucolor(item2);
        menu.setGroupVisible(R.menu.all_report, true);
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
                    mPeriodReportAdapter = new TaxReportAdapter(mContext, R.layout.tax_report_list_item, listPrs);
                    mListView.setAdapter(mPeriodReportAdapter);
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
                listPrs = getfinalData2(years1, years2);
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

    private List<TaxReportSection> getfinalData2(String s, String s2) {
        int no = 0;
        double VAT = 0.00;
        double BPT_P = 0.00;
        double BPT_F = 0.00;
        double SD_L = 0.00;
        double SD_F = 0.00;
        double Fee = 0.00;
        List<InvoiveSection> invoiveSections = setNewDate(s, s2);
        List<TaxReportSection> groupList = new ArrayList<>();
        int size = invoiveSections.size();
        List<ProductModel> productModels = select().from(ProductModel.class).queryList();
        int size1 = productModels.size();
        for (int i = 0; i < size; i++) {
            InvoiveSection section = invoiveSections.get(i);
            Invoice t = (Invoice) section.getT();
            if (i < size - 1) {
                InvoiveSection section2 = invoiveSections.get(i + 1);
                if (section.getHanderContent().equals(section2.getHanderContent())) {
                    if (!section.isHeader()) {
                        for (int y = 0; y < size1; y++) {
                            ProductModel model = productModels.get(y);
                            if (t.getInvoice_no().equals(model.getInvoice_no())) {
                                if (model.getTaxtype().equals("96")) {
                                    try {
                                        VAT += Double.parseDouble(model.getTaxable_amount());
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                } else if (model.getTaxtype().equals("97")) {
                                    try {
                                        BPT_P += Double.parseDouble(model.getTaxable_amount());
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                } else if (model.getTaxtype().equals("98")) {
                                    try {
                                        BPT_F += Double.parseDouble(model.getTaxable_amount());
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                } else if (model.getTaxtype().equals("99")) {
                                    try {
                                        SD_L += Double.parseDouble(model.getTaxable_amount());
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                } else if (model.getTaxtype().equals("100")) {
                                    try {
                                        SD_F += Double.parseDouble(model.getTaxable_amount());
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                } else if (model.getTaxtype().equals("101")) {
                                    try {
                                        Fee += Double.parseDouble(model.getTaxable_amount());
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                } else {
                    no++;
                    for (int y = 0; y < size1; y++) {
                        ProductModel model = productModels.get(y);
                        if (t.getInvoice_no().equals(model.getInvoice_no())) {
                            if (model.getTaxtype().equals("96")) {
                                try {
                                    VAT += Double.parseDouble(model.getTaxable_amount());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            } else if (model.getTaxtype().equals("97")) {
                                try {
                                    BPT_P += Double.parseDouble(model.getTaxable_amount());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            } else if (model.getTaxtype().equals("98")) {
                                try {
                                    BPT_F += Double.parseDouble(model.getTaxable_amount());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            } else if (model.getTaxtype().equals("99")) {
                                try {
                                    SD_L += Double.parseDouble(model.getTaxable_amount());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            } else if (model.getTaxtype().equals("100")) {
                                try {
                                    SD_F += Double.parseDouble(model.getTaxable_amount());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            } else if (model.getTaxtype().equals("101")) {
                                try {
                                    Fee += Double.parseDouble(model.getTaxable_amount());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    TaxReportSection taxReportSection = new TaxReportSection();
                    taxReportSection.setPeriod(section.getHanderContent());
                    taxReportSection.setNo(String.valueOf(no));
                    taxReportSection.setTA(String.valueOf(new DecimalFormat("0.00").format(VAT + BPT_P + BPT_F + SD_L + SD_F + Fee)));
                    taxReportSection.setVAT(String.valueOf(new DecimalFormat("0.00").format(VAT)));
                    taxReportSection.setBPT_P(String.valueOf(new DecimalFormat("0.00").format(BPT_P)));
                    taxReportSection.setBPT_F(String.valueOf(new DecimalFormat("0.00").format(BPT_F)));
                    taxReportSection.setSD_L(String.valueOf(new DecimalFormat("0.00").format(SD_L)));
                    taxReportSection.setSD_F(String.valueOf(new DecimalFormat("0.00").format(SD_F)));
                    taxReportSection.setFee(String.valueOf(new DecimalFormat("0.00").format(Fee)));
                    groupList.add(taxReportSection);
                    VAT = 0.00;
                    BPT_P = 0.00;
                    BPT_F = 0.00;
                    SD_L = 0.00;
                    SD_F = 0.00;
                    Fee = 0.00;
                }
            } else {
                no++;
                for (int y = 0; y < size1; y++) {
                    ProductModel model = productModels.get(y);
                    if (t.getInvoice_no().equals(model.getInvoice_no())) {
                        if (model.getTaxtype().equals("96")) {
                            try {
                                VAT += Double.parseDouble(model.getTaxable_amount());
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        } else if (model.getTaxtype().equals("97")) {
                            try {
                                BPT_P += Double.parseDouble(model.getTaxable_amount());
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        } else if (model.getTaxtype().equals("98")) {
                            try {
                                BPT_F += Double.parseDouble(model.getTaxable_amount());
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        } else if (model.getTaxtype().equals("99")) {
                            try {
                                SD_L += Double.parseDouble(model.getTaxable_amount());
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        } else if (model.getTaxtype().equals("100")) {
                            try {
                                SD_F += Double.parseDouble(model.getTaxable_amount());
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        } else if (model.getTaxtype().equals("101")) {
                            try {
                                Fee += Double.parseDouble(model.getTaxable_amount());
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                TaxReportSection taxReportSection = new TaxReportSection();
                taxReportSection.setPeriod(section.getHanderContent());
                taxReportSection.setNo(String.valueOf(no));
                taxReportSection.setTA(String.valueOf(new DecimalFormat("0.00").format(VAT + BPT_P + BPT_F + SD_L + SD_F + Fee)));
                taxReportSection.setVAT(String.valueOf(new DecimalFormat("0.00").format(VAT)));
                taxReportSection.setBPT_P(String.valueOf(new DecimalFormat("0.00").format(BPT_P)));
                taxReportSection.setBPT_F(String.valueOf(new DecimalFormat("0.00").format(BPT_F)));
                taxReportSection.setSD_L(String.valueOf(new DecimalFormat("0.00").format(SD_L)));
                taxReportSection.setSD_F(String.valueOf(new DecimalFormat("0.00").format(SD_F)));
                taxReportSection.setFee(String.valueOf(new DecimalFormat("0.00").format(Fee)));
                groupList.add(taxReportSection);
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
            beanList.add(listPrs.get(i).getPeriod());
            beanList.add(listPrs.get(i).getTA());
            beanList.add(listPrs.get(i).getVAT());
            beanList.add(listPrs.get(i).getBPT_F());
            beanList.add(listPrs.get(i).getBPT_P());
            beanList.add(listPrs.get(i).getSD_F());
            beanList.add(listPrs.get(i).getSD_L());
            beanList.add(listPrs.get(i).getFee());
            recordList.add(beanList);
        }
        return recordList;
    }
}
