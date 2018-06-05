package com.csscaps.tcs.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.common.utils.AppTools;
import com.csscaps.tcs.R;
import com.csscaps.tcs.TCSApplication;
import com.csscaps.tcs.database.table.User;

import java.util.ArrayList;
import java.util.List;

import static com.tax.fcr.library.utils.NetworkUtils.mContext;

/**
 * Created by tl on 2018/6/4.
 */

public class UserManagementListAdapter extends BaseManagementListAdapter<User> {

    public UserManagementListAdapter(Context context, int layoutResId, List<User> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, final User item, int position) {
        helper.setText(R.id.name, item.getUserName());
        helper.setText(R.id.code, item.getCode());
        helper.setText(R.id.tel, item.getTel());
        helper.setText(R.id.role, item.getRole() == 0 ? context.getString(R.string.admin) : context.getString(R.string.operator));
        helper.setText(R.id.address, item.getAddress());
        helper.setText(R.id.email, item.getEmail());
        helper.setText(R.id.status, item.getStatus() == 0 ? context.getString(R.string.active) : context.getString(R.string.inactive));

        CheckBox no = helper.getView(R.id.no);
        AppTools.expandViewTouchDelegate(no, 100, 100, 50, 50);
        no.setText(String.valueOf(position + 1));
        no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (!checkedTList.contains(item)) {
                        checkedTList.add(item);
                    }
                } else {
                    checkedTList.remove(item);
                }
            }
        });


        if (isShowCheckBox) {
            if (item.getId() == 1 || TCSApplication.currentUser.getId() == item.getId()) {
                no.setButtonDrawable(ContextCompat.getDrawable(mContext, android.R.color.transparent));
                return;
            }
            no.setText(null);
            no.setButtonDrawable(ContextCompat.getDrawable(mContext, R.drawable.cb_check_selector));

            if (checkedTList.contains(item) || mAllSelect) {
                no.setChecked(true);
            }

            if (!checkedTList.contains(item)) {
                no.setChecked(false);
            }

        } else {
            no.setButtonDrawable(ContextCompat.getDrawable(mContext, android.R.color.transparent));
        }
    }

    @Override
    public void setAllSelect(boolean allSelect) {
        mAllSelect = allSelect;
        if (allSelect) {
            checkedTList.addAll(getAllSelect());
        } else {
            checkedTList.clear();
        }
        notifyDataSetChanged();
    }

    private List<User> getAllSelect() {
        List<User> list = new ArrayList<>();
        for (User user : data) {
            if (user.getId() == 1 || user.getId() == TCSApplication.currentUser.getId()) continue;
            list.add(user);
        }
        return list;
    }

}
