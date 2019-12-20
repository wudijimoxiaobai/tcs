package com.csscaps.tcs.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.csscaps.common.base.BaseFragment;
import com.csscaps.common.utils.AppTools;
import com.csscaps.common.utils.FastDoubleClickUtil;
import com.csscaps.common.utils.ObserverActionUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.adapter.BaseManagementListAdapter;
import com.csscaps.tcs.database.table.Customer;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.Invoice_Table;
import com.csscaps.tcs.database.table.Product;
import com.csscaps.tcs.dialog.AllCustomerDeleteDialog;
import com.csscaps.tcs.dialog.AllDeleteDialog;
import com.csscaps.tcs.dialog.BaseAddDialog;
import com.csscaps.tcs.dialog.InvoiceQuerySelectDialog;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/5/16.
 */

public abstract class BaseManagementListFragment<T extends BaseModel> extends BaseFragment implements CompoundButton.OnCheckedChangeListener, Action1<T>, AdapterView.OnItemClickListener, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.customer_add)
    FloatingActionButton mAdd;
    @BindView(R.id.list_view)
    ListView mListView;

    protected List<T> data;
    protected BaseManagementListAdapter mBaseManagementListAdapter;
    protected PopupWindow popupWindow;
    protected String format = "%%%s%%";
    //view
    int screenHeight;
    int screenWidth;
    int downX;
    int downY;
    /**
     * 是否删除数据库数据
     */
    public boolean isDataBase = true;

    @Override
    protected void onInitPresenters() {
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
//        mSelect.setTag(0);
//        AppTools.expandViewTouchDelegate(mAllSelect, 100, 100, 100, 100);
//        mAllSelect.setOnCheckedChangeListener(this);
        ObserverActionUtils.addAction(this);
        data = getData();
        mBaseManagementListAdapter = getAdapter(data);
        mListView.setAdapter(mBaseManagementListAdapter);
        mListView.setOnItemClickListener(this);

        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                downX = (int) motionEvent.getRawX();
                downY = (int) motionEvent.getRawY();
                return false;
            }
        });
    }

    @OnClick({R.id.customer_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.query_uplode:
                List<Invoice> queryList = select().from(Invoice.class).where(Invoice_Table.uploadStatus.eq("0")).orderBy(Invoice_Table.client_invoice_datetime, false).queryList();
                popupWindow.dismiss();
                InvoiceQuerySelectDialog invoiceQuerySelectDialog = new InvoiceQuerySelectDialog(queryList, isDataBase);
                invoiceQuerySelectDialog.setTargetFragment(BaseManagementListFragment.this, InvoiceQuerySelectDialog.FRAGMNET_query);
                List<Invoice> list3 = invoiceQuerySelectDialog.getList();
                data = (List<T>) list3;
                invoiceQuerySelectDialog.show(getFragmentManager(), "InvoiceQuerySelectDialog");
                break;
            case R.id.customer_delete:
                popupWindow.dismiss();
                AllCustomerDeleteDialog allCustomerDeleteDialog = new AllCustomerDeleteDialog((List<Customer>) data, isDataBase);
                allCustomerDeleteDialog.setTargetFragment(BaseManagementListFragment.this, AllCustomerDeleteDialog.FRAGMNET_customer);
                List<Customer> list = allCustomerDeleteDialog.getList();
                data = (List<T>) list;
                allCustomerDeleteDialog.show(getFragmentManager(), "AllCustomerDeleteDialog");
                break;
            case R.id.p_delete:
                popupWindow.dismiss();
                AllDeleteDialog deleteDialog = new AllDeleteDialog((List<Product>) data, isDataBase);
                deleteDialog.setTargetFragment(BaseManagementListFragment.this, AllDeleteDialog.FRAGMNET_A_2_Fragment_B_REQUEST_CODE);
                List<Product> list2 = deleteDialog.getList();
                data = (List<T>) list2;
                deleteDialog.show(getFragmentManager(), "AllDeleteDialog");
                break;
            case R.id.customer_add:
                if (FastDoubleClickUtil.isFastDoubleClick(R.id.customer_add)) break;
                BaseAddDialog dialog = getDialog();
                if (dialog != null)
                    dialog.show(getChildFragmentManager(), dialog.getClass().getName());
                break;
            case R.id.edit:
                if (FastDoubleClickUtil.isFastDoubleClick(R.id.edit)) break;
                popupWindow.dismiss();
                T t1 = (T) view.getTag();
                BaseAddDialog dialog1 = getDialog();
                if (dialog1 != null) {
                    dialog1.edit(t1);
                    dialog1.show(getChildFragmentManager(), dialog1.getClass().getName());
                }
                break;
            case R.id.details:
                popupWindow.dismiss();
                T t2 = (T) view.getTag();
                toDetails(t2);
                break;
        }
    }

    @Override
    public void call(T t) {
        if (!data.contains(t)) data.add(t);
        mBaseManagementListAdapter.notifyDataSetChanged();
    }

    protected PopupWindow getPopupWindow(View view, int layout) {
        // 获取屏幕的高宽
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(layout, null);
        AppTools.measureView(linearLayout);
        int w = linearLayout.getMeasuredWidth();
        int h = linearLayout.getMeasuredHeight();
//        int w = DeviceUtils.dip2px(mContext, 200);
        final PopupWindow popupWindow = AppTools.getPopupWindow(linearLayout, w, h);
        //计算view的位置
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        linearLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int popuHeight = linearLayout.getMeasuredHeight();
        final int popuWidth = linearLayout.getMeasuredWidth();
        // 判断Y坐标
        if (downY > screenHeight / 2) {
            //向上弹出
            windowPos[1] = downY - popuHeight;
        } else {
            //向下弹出
            windowPos[1] = downY;
        }
        // 判断X坐标
        if (downX > screenWidth / 2) {
            //向左弹出
            windowPos[0] = downX - popuWidth;
        } else {
            //向右弹出
            windowPos[0] = downX;
        }
        int xOff = -20; // 调整偏移
        windowPos[0] -= xOff;
        popupWindow.showAtLocation(view, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
        //  popupWindow.showAsDropDown(view, (int) ((view.getWidth() - w) / 2f), (int) -(view.getHeight() + h * 3f / 5));
        return popupWindow;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

        if (FastDoubleClickUtil.isFastDoubleClick()) return;
        popupWindow = getPopupWindow(view,
                getPopupWindowLayout());

        View contentView = popupWindow.getContentView();
        T t = data.get(i);
        TextView edit = contentView.findViewById(R.id.edit);
        TextView pDelete = contentView.findViewById(R.id.p_delete);
        TextView details = contentView.findViewById(R.id.details);
        TextView cDelete = contentView.findViewById(R.id.customer_delete);
        TextView qUplode = contentView.findViewById(R.id.query_uplode);
//        TextView changePassword = contentView.findViewById(R.id.change_password);
//        TextView active = contentView.findViewById(R.id.active);
        if (edit != null) {
            edit.setTag(t);
            edit.setOnClickListener(this);
        }
        if (pDelete != null) {
            pDelete.setTag(t);
            pDelete.setOnClickListener(this);
        }

        if (details != null) {
            details.setTag(t);
            details.setOnClickListener(this);
        }

        if (cDelete != null) {
            cDelete.setTag(t);
            cDelete.setOnClickListener(this);
        }

        if (qUplode != null) {
            qUplode.setTag(t);
            qUplode.setOnClickListener(this);
        }
//        if (changePassword != null) {
//            changePassword.setTag(t);
//            changePassword.setOnClickListener(this);
//        }
//
//        if (active != null) {
//            active.setTag(t);
//            active.setOnClickListener(this);
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AllDeleteDialog.FRAGMNET_A_2_Fragment_B_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK && data != null) {//获取从DialogFragmentB中传递的mB2A
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        List<Product> products = (List<Product>) bundle.get(AllDeleteDialog.ARGUMENTS_B_2_A_KEY);
                        this.data = (List<T>) products;
                        mBaseManagementListAdapter.notifyDataSetChanged();
                    }
                }
                break;
            case AllCustomerDeleteDialog.FRAGMNET_customer:
                if (resultCode == Activity.RESULT_OK && data != null) {//获取从DialogFragmentB中传递的mB2A
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        List<Customer> products = (List<Customer>) bundle.get(AllCustomerDeleteDialog.ARGUMENTS_B_2_A_KEY);
                        this.data = (List<T>) products;
                        mBaseManagementListAdapter.notifyDataSetChanged();
                    }
                }
                break;
            case InvoiceQuerySelectDialog.FRAGMNET_query:
                if (resultCode == Activity.RESULT_OK && data != null) {//获取从DialogFragmentB中传递的mB2A
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        List<Invoice> products = (List<Invoice>) bundle.get(InvoiceQuerySelectDialog.ARGUMENTS_B_2_A_KEY);
                        this.data = (List<T>) products;
                        mBaseManagementListAdapter.notifyDataSetChanged();
                    }
                }
                break;
            default:
                break;
        }
    }

    public void setDataBase(boolean dataBase) {
        isDataBase = dataBase;
    }

    protected void toDetails(T t) {
    }

    protected abstract int getPopupWindowLayout();

    protected abstract List<T> getData();

    protected abstract BaseManagementListAdapter getAdapter(List<T> data);

    protected abstract BaseAddDialog getDialog();

}
