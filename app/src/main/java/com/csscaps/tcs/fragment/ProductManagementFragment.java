package com.csscaps.tcs.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.csscaps.common.base.BaseFragment;
import com.csscaps.tcs.R;
import com.csscaps.tcs.dialog.AddProductDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tl on 2018/5/8.
 */

public class ProductManagementFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

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

    private AddProductDialog mAddProductDialog;


    @Override
    protected int getLayoutResId() {
        return R.layout.product_mangement_fragment;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBack.setTag(0);
        mSelect.setTag(0);
        mAllSelect.setOnCheckedChangeListener(this);
        mAddProductDialog=new AddProductDialog();
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
                    case 0:
                        getActivity().finish();
                        break;
                    case 1:
                        mBack.setTag(0);
                        mBack.setText(getResources().getString(R.string.back));
                        mAllSelect.setButtonDrawable(ContextCompat.getDrawable(mContext,android.R.color.transparent));
                        mAllSelect.setText(getString(R.string.no));
                        mAllSelect.setChecked(false);
                        break;
                }
                break;
            case R.id.add:
                mAddProductDialog.show(getChildFragmentManager(),"AddProductDialog");

                break;
            case R.id.select:
                switch (tag) {
                    case 0:
                        mBack.setTag(1);
                        mBack.setText(getResources().getString(R.string.cancel));
                        mAllSelect.setButtonDrawable(ContextCompat.getDrawable(mContext,R.drawable.cb_check_selector));
                        mAllSelect.setText(null);
                        break;
                    case 1:
                        break;
                }
                break;
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }
}
