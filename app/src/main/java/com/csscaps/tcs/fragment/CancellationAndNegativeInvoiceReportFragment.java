package com.csscaps.tcs.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
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
import com.csscaps.tcs.adapter.CancellationNegativeAdapter;
import com.csscaps.tcs.adapter.PeriodReportAdapter;
import com.csscaps.tcs.adapter.PeriodReportAdapter2;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.Invoice_Table;
import com.csscaps.tcs.dialog.SynDataDialog;
import com.csscaps.tcs.model.CancelNegativeNodel;
import com.csscaps.tcs.model.PeriodReportSection;
import com.csscaps.tcs.utils.DataHandleUtil;
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

public class CancellationAndNegativeInvoiceReportFragment extends BaseFragment {

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
    private CancellationNegativeAdapter cancellationNegativeAdapter;
    private SynDataDialog mSynDataDialog = new SynDataDialog();
    List<CancelNegativeNodel> listPrs;


    @Override
    protected int getLayoutResId() {
        return R.layout.cancel_nagative_fragment;
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
                        String[] title = {"No", "Type", "Amout", "Qty", "Percentage"};

                        File file = new File(ExcelUtil.getSDPath() + "/bluetooth");
                        ExcelUtil.makeDir(file);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = sdf.format(new Date());//Calendar.getInstance().toString();

                        String fileName = file.toString() + "/" + "CancellationAndNegativeInvoiceReportFragment" + time + ".xls";

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
                                    List<CancelNegativeNodel> list = getFinalData(years1, years2);
                                    cancellationNegativeAdapter = new CancellationNegativeAdapter(mContext, R.layout.cancellation_negative_list_item, list);
                                    mListView.setAdapter(cancellationNegativeAdapter);
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
                    cancellationNegativeAdapter = new CancellationNegativeAdapter(mContext, R.layout.cancellation_negative_list_item, listPrs);
                    mListView.setAdapter(cancellationNegativeAdapter);
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
                listPrs = getFinalData(years1, years2);
                if (listPrs.size() > 0) {
                    mHandler.sendEmptyMessage(0);
                } else {
                    mHandler.sendEmptyMessage(1);
                }
            }
        }).start();
    }

    private List<Invoice> setNewDate(String s, String s2) {
        List<Invoice> date = select().from(Invoice.class).orderBy(Invoice_Table.client_invoice_datetime, false).queryList();
        int count = date.size();
        List<Invoice> groupList1 = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Invoice invoice = date.get(i);
            String timeformat = DateUtils.dateToStr(DateUtils.getStringToDate(invoice.getClient_invoice_datetime(), DateUtils.format_yyyyMMddHHmmss_24_EN), DateUtils.format_YYYY_MM_EN);
            if (timeCompare(s, timeformat) >= 2) {
                if (timeCompare(s2, timeformat) <= 2) {
                    groupList1.add(invoice);
                }
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

    public void setyear() {
        String years = "2018", months = "3";
        String months2 = "3";
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        if (month < 10) {
            if (month + 1 < 10) {
                months = "0" + String.valueOf(month + 1);
                months2 = "0" + String.valueOf(month + 1);
            } else {
                months = "0" + String.valueOf(month + 1);
                months2 = String.valueOf(month + 1);
            }
        } else {
            months = String.valueOf(month + 1);
            months2 = String.valueOf(month + 1);
        }
        years = String.valueOf(year);
        years1 = years + "-" + months;
        years2 = years + "-" + months2;
        mDataFromDate.setText(years + months);
        mDataToData.setText(years + months2);
    }

    private List<CancelNegativeNodel> getFinalData(String s, String s2) {
        double Normal_Amount = 0.00;
        int qty = 0;
        double Cancellation_Amount = 0;
        int qty2 = 0;
        double NI_amount = 0;
        int qty3 = 0;
        List<Invoice> invoices = setNewDate(s, s2);
        List<CancelNegativeNodel> groupList = new ArrayList<>();
        int size = invoices.size();
        for (int i = 0; i < size; i++) {
            Invoice invoice = invoices.get(i);
            if ("AVL".equals(invoice.getStatus())) {
                try {
                    Normal_Amount += Double.parseDouble(invoice.getTotal_all());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                qty++;
            }
            if ("DISA".equals(invoice.getStatus())) {
                try {
                    Cancellation_Amount += Double.parseDouble(invoice.getTotal_all());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                qty2++;
            }
            if ("NEG".equals(invoice.getStatus())) {
                try {
                    NI_amount += Double.parseDouble(invoice.getTotal_all());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                qty3++;
            }
        }
        String division = DataHandleUtil.division(Normal_Amount, Normal_Amount + Cancellation_Amount + NI_amount);
        String division2 = DataHandleUtil.division(Cancellation_Amount, Normal_Amount + Cancellation_Amount + NI_amount);
        String division3 = DataHandleUtil.division(NI_amount, Normal_Amount + Cancellation_Amount + NI_amount);
        CancelNegativeNodel cancelNegativeNodel = null;
        if (Normal_Amount == 0 && Cancellation_Amount == 0 && NI_amount == 0) {

        }else {
        if (Normal_Amount != 0) {
            cancelNegativeNodel = new CancelNegativeNodel();
            cancelNegativeNodel.setType("Normal Amount");
            cancelNegativeNodel.setAmout(String.valueOf(new DecimalFormat("0.00").format(Normal_Amount)));
            cancelNegativeNodel.setQty(qty + "");
            cancelNegativeNodel.setPercentage(division);
            if (Cancellation_Amount != 0) {
                cancelNegativeNodel.setType("Cancellation Amount");
                cancelNegativeNodel.setAmout(String.valueOf(new DecimalFormat("0.00").format(Cancellation_Amount)));
                cancelNegativeNodel.setQty(qty2 + "");
                cancelNegativeNodel.setPercentage(division2);
            }
            if (NI_amount != 0) {
                cancelNegativeNodel.setType("Negative Invoice Amount");
                cancelNegativeNodel.setAmout(String.valueOf(new DecimalFormat("0.00").format(NI_amount)));
                cancelNegativeNodel.setQty(qty3 + "");
                cancelNegativeNodel.setPercentage(division3);
            }
        } else if (Cancellation_Amount != 0) {
            cancelNegativeNodel = new CancelNegativeNodel();
            cancelNegativeNodel.setType("Cancellation Amount");
            cancelNegativeNodel.setAmout(String.valueOf(new DecimalFormat("0.00").format(Cancellation_Amount)));
            cancelNegativeNodel.setQty(qty2 + "");
            cancelNegativeNodel.setPercentage(division2);
            if (NI_amount != 0) {
                cancelNegativeNodel.setType("Negative Invoice Amount");
                cancelNegativeNodel.setAmout(String.valueOf(new DecimalFormat("0.00").format(NI_amount)));
                cancelNegativeNodel.setQty(qty3 + "");
                cancelNegativeNodel.setPercentage(division3);
            }
        } else if (NI_amount != 0) {
            cancelNegativeNodel = new CancelNegativeNodel();
            cancelNegativeNodel = new CancelNegativeNodel();
            cancelNegativeNodel.setType("Negative Invoice Amount");
            cancelNegativeNodel.setAmout(String.valueOf(new DecimalFormat("0.00").format(NI_amount)));
            cancelNegativeNodel.setQty(qty3 + "");
            cancelNegativeNodel.setPercentage(division3);
        }
            groupList.add(cancelNegativeNodel);
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
            beanList.add(i + 1 + "");
            beanList.add(listPrs.get(i).getType());
            beanList.add(listPrs.get(i).getAmout());
            beanList.add(listPrs.get(i).getQty());
            beanList.add(listPrs.get(i).getPercentage());
            recordList.add(beanList);
        }
        return recordList;
    }

}
