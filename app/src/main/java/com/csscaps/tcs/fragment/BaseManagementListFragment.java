package com.csscaps.tcs.fragment;

import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.csscaps.common.base.BaseFragment;
import com.csscaps.tcs.R;

import java.util.List;

import butterknife.BindView;

/**
 * Created by tl on 2018/5/16.
 */

public abstract class BaseManagementListFragment<T> extends BaseFragment {
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

    private List<T> data;

}
