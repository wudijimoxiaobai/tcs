package com.csscaps.tcs.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.csscaps.common.base.BaseFragment;
import com.csscaps.common.utils.AppTools;
import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.common.utils.ObserverActionUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.adapter.BaseManagementListAdapter;
import com.csscaps.tcs.database.TcsDatabase;
import com.csscaps.tcs.dialog.BaseAddDialog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by tl on 2018/5/16.
 */

public abstract class BaseManagementListFragment<T extends BaseModel> extends BaseFragment implements CompoundButton.OnCheckedChangeListener, Action1<T>, AdapterView.OnItemClickListener, View.OnClickListener {

    @BindView(R.id.back)
    TextView mBack;
    @BindView(R.id.add)
    TextView mAdd;
    @BindView(R.id.select)
    TextView mSelect;
    @BindView(R.id.all_select)
    CheckBox mAllSelect;
    @BindView(R.id.list_view)
    ListView mListView;

    protected List<T> data;
    protected BaseManagementListAdapter mBaseManagementListAdapter;
    protected PopupWindow popupWindow;
    /**
     * 是否删除数据库数据
     */
    public boolean isDataBase = true;

    @Override
    protected void onInitPresenters() {
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBack.setTag(0);
        mSelect.setTag(0);
        AppTools.expandViewTouchDelegate(mAllSelect, 100, 100, 100, 100);
        mAllSelect.setOnCheckedChangeListener(this);
        ObserverActionUtils.addAction(this);
        data = getData();
        mBaseManagementListAdapter = getAdapter(data);
        mListView.setAdapter(mBaseManagementListAdapter);
        mListView.setOnItemClickListener(this);
    }


    @OnClick({R.id.back, R.id.add, R.id.select})
    public void onClick(View view) {
        int tag = 0;
        try {
            tag = (int) view.getTag();
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (view.getId()) {
            case R.id.back:
                switch (tag) {
                    case 0://返回
                        getActivity().finish();
                        break;
                    case 1://取消
                        mBack.setTag(0);
                        mSelect.setTag(0);
                        mBack.setText(getString(R.string.back));
                        mSelect.setText((getString(R.string.select)));
                        mAllSelect.setButtonDrawable(ContextCompat.getDrawable(mContext, android.R.color.transparent));
                        mBaseManagementListAdapter.setShowCheckBox(false);
                        mAllSelect.setText(getString(R.string.no));
                        mAllSelect.setChecked(false);
                        mAdd.setVisibility(View.VISIBLE);
                        break;
                }
                break;
            case R.id.select:
                switch (tag) {
                    case 0://选择
                        mBack.setTag(1);
                        mSelect.setTag(1);
                        mBack.setText(getString(R.string.cancel));
                        mSelect.setText((getString(R.string.delete)));
                        mAllSelect.setButtonDrawable(ContextCompat.getDrawable(mContext, R.drawable.cb_check_selector));
                        mAllSelect.setText(null);
                        mBaseManagementListAdapter.setShowCheckBox(true);
                        mAdd.setVisibility(View.INVISIBLE);
                        break;
                    case 1://删除
                        List<T> list = mBaseManagementListAdapter.getCheckedList();
                        if (isDataBase) {
                            FlowManager.getDatabase(TcsDatabase.class)
                                    .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                                            new ProcessModelTransaction.ProcessModel<T>() {
                                                @Override
                                                public void processModel(T model, DatabaseWrapper wrapper) {
                                                    model.delete();
                                                }
                                            }).addAll(list).build())
                                    .build()
                                    .execute();
                        }
                        data.removeAll(list);
                        list.clear();
                        mBaseManagementListAdapter.notifyDataSetChanged();
                        break;
                }
                break;
            case R.id.p_delete:
                popupWindow.dismiss();
                T t = (T) view.getTag();
                if (isDataBase) {
                    t.delete();
                }
                data.remove(t);
                mBaseManagementListAdapter.notifyDataSetChanged();
                break;
            case R.id.add:
                BaseAddDialog dialog = getDialog();
                if (dialog != null)
                    dialog.show(getChildFragmentManager(), dialog.getClass().getName());
                break;
            case R.id.edit:
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
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            mBaseManagementListAdapter.setAllSelect(true);
        } else {
            mBaseManagementListAdapter.setAllSelect(false);
        }
    }

    @Override
    public void call(T t) {
        if (!data.contains(t)) data.add(t);
        mBaseManagementListAdapter.notifyDataSetChanged();
    }

    protected PopupWindow getPopupWindow(View view, int layout) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(layout, null);
        AppTools.measureView(linearLayout);
//        int w = linearLayout.getMeasuredWidth();
        int h = linearLayout.getMeasuredHeight();
        int w = DeviceUtils.dip2px(mContext, 200);
        final PopupWindow popupWindow = AppTools.getPopupWindow(linearLayout, w, h);
        popupWindow.showAsDropDown(view, (int) ((view.getWidth() - w) / 2f), (int) -(view.getHeight() + h * 3f / 5));
        return popupWindow;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
        popupWindow = getPopupWindow(view, getPopupWindowLayout());
        View contentView = popupWindow.getContentView();
        T t = data.get(i);
        TextView edit = contentView.findViewById(R.id.edit);
        TextView pDelete = contentView.findViewById(R.id.p_delete);
        TextView details = contentView.findViewById(R.id.details);
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
