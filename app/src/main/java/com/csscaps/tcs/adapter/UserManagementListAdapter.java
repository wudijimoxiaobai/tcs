package com.csscaps.tcs.adapter;

import android.content.Context;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.tcs.R;
import com.csscaps.tcs.TCSApplication;
import com.csscaps.tcs.database.table.User;

import java.util.ArrayList;
import java.util.List;

public class UserManagementListAdapter extends BaseManagementListAdapter<User> {

    public UserManagementListAdapter(Context context, int layoutResId, List<User> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, final User item, int position) {
        helper.setText(R.id.name, item.getUserName());
        helper.setText(R.id.role, item.getRole() == 0 ? context.getString(R.string.admin) : context.getString(R.string.operator));
    }

    public void setAllSelect(boolean allSelect) {
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
