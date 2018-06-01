package com.csscaps.tcs.fragment;

import com.csscaps.tcs.R;
import com.csscaps.tcs.adapter.BaseManagementListAdapter;
import com.csscaps.tcs.database.table.User;
import com.csscaps.tcs.dialog.BaseAddDialog;

import java.util.List;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/6/1.
 */

public class UserManagementFragment extends BaseManagementListFragment<User> {
    @Override
    protected int getLayoutResId() {
        return R.layout.user_management_fragment;
    }

    @Override
    protected int getPopupWindowLayout() {
        return 0;
    }

    @Override
    protected List<User> getData() {
        return select().from(User.class).queryList();
    }

    @Override
    protected BaseManagementListAdapter getAdapter(List<User> data) {
        return null;
    }

    @Override
    protected BaseAddDialog getDialog() {
        return null;
    }
}
