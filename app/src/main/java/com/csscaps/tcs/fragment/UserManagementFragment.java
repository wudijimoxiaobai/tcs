package com.csscaps.tcs.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.csscaps.tcs.R;
import com.csscaps.tcs.TCSApplication;
import com.csscaps.tcs.activity.ChangePasswordActivity;
import com.csscaps.tcs.activity.UserDetailsActivity;
import com.csscaps.tcs.adapter.BaseManagementListAdapter;
import com.csscaps.tcs.adapter.UserManagementListAdapter;
import com.csscaps.tcs.database.table.User;
import com.csscaps.tcs.database.table.User_Table;
import com.csscaps.tcs.dialog.AddUserDialog;
import com.csscaps.tcs.dialog.BaseAddDialog;

import java.util.List;

import butterknife.BindView;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/6/1.
 */

public class UserManagementFragment extends BaseManagementListFragment<User> {

    @BindView(R.id.add_line)
    View mAddLine;
    @BindView(R.id.select_line)
    View mSelectLine;

    private User user;

    @Override
    protected int getLayoutResId() {
        return R.layout.user_management_fragment;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (TCSApplication.currentUser.getRole() == 1) {
            mAdd.setVisibility(View.GONE);
            mSelect.setVisibility(View.GONE);
            mAddLine.setVisibility(View.GONE);
            mSelectLine.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getPopupWindowLayout() {
        if (TCSApplication.currentUser.getRole() == 1) {
            return R.layout.user_list_popuwindow1_layout;
        }
        return R.layout.user_list_popuwindow_layout;
    }

    @Override
    protected List<User> getData() {
        if (TCSApplication.currentUser.getRole() == 1) {
            return select().from(User.class).where(User_Table.id.eq(TCSApplication.currentUser.getId())).queryList();
        }
        return select().from(User.class).queryList();
    }

    @Override
    protected BaseManagementListAdapter getAdapter(List<User> data) {
        return new UserManagementListAdapter(mContext, R.layout.uer_management_list_item, data);
    }

    @Override
    protected BaseAddDialog getDialog() {
        return new AddUserDialog();
    }

    @Override
    protected void toDetails(User user) {
        Intent intent = new Intent(mContext, UserDetailsActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_password:
                popupWindow.dismiss();
                user = (User) view.getTag();
                Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                intent.putExtra("pwd", user.getPassword());
                startActivityForResult(intent, 1001);
                break;
            case R.id.active:
                popupWindow.dismiss();
                User user = (User) view.getTag();
                user.setStatus(0);
                user.update();
                mBaseManagementListAdapter.notifyDataSetChanged();
                break;
        }
        super.onClick(view);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        super.onItemClick(adapterView, view, i, l);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK & data != null) {
            String pwd = data.getStringExtra("pwd");
            user.setPassword(pwd);
            user.update();
        }
    }


}
