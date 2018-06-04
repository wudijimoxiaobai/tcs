package com.csscaps.tcs.adapter;

import android.content.Context;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.User;

import java.util.List;

/**
 * Created by tl on 2018/6/4.
 */

public class UserManagementListAdapter extends BaseManagementListAdapter<User> {

    public UserManagementListAdapter(Context context, int layoutResId, List<User> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, User item, int position) {
        super.convert(helper, item, position);
        helper.setText(R.id.name, item.getUserName());
        helper.setText(R.id.code, item.getCode());
        helper.setText(R.id.tel, item.getTel());
        helper.setText(R.id.role, item.getRole() == 0 ? context.getString(R.string.admin) : context.getString(R.string.operator));
        helper.setText(R.id.address, item.getAddress());
        helper.setText(R.id.email, item.getEmail());
        helper.setText(R.id.status, item.getStatus() == 0 ? context.getString(R.string.active) : context.getString(R.string.not_active));
    }
}
