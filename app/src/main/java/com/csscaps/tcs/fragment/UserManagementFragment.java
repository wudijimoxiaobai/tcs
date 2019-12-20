package com.csscaps.tcs.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.csscaps.common.utils.ToastUtil;
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
import com.csscaps.tcs.dialog.SearchUserDialog;
import com.csscaps.tcs.model.SearchUserCondition;
import com.raizlabs.android.dbflow.sql.language.Where;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

public class UserManagementFragment extends BaseManagementListFragment<User> {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.search)
    ImageView mSearch;
    @BindView(R.id.list_view)
    ListView mListView;
    @BindView(R.id.customer_add)
    FloatingActionButton mAddLine;
    private User user;

    @Override
    protected int getLayoutResId() {
        return R.layout.user_management_fragment;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        if (TCSApplication.currentUser.getRole() == 1) {
            mAdd.setVisibility(View.GONE);
            //  mSelect.setVisibility(View.GONE);
            mAddLine.setVisibility(View.GONE);
            // mSelectLine.setVisibility(View.GONE);
            mSearch.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getPopupWindowLayout() {
        //普通操作员
        if (TCSApplication.currentUser.getRole() == 1) {
            return R.layout.user_list_popuwindow1_layout;
        }
        //当前用户
        if (user.getId() == TCSApplication.currentUser.getId()) {
            return R.layout.user_list_popuwindow3_layout;
        }
        // 点击admin  非admin用户登录
        if (user.getId() == 1 && TCSApplication.currentUser.getId() != 1) {
            return R.layout.user_list_popuwindow4_layout;
        }
        // 点击admin  admin用户登录
        if (user.getId() == 1 && TCSApplication.currentUser.getId() == 1) {
            return R.layout.user_list_popuwindow5_layout;
        }
        // 点击已激活用户
        if (user.getStatus() == 0 && user.getId() != TCSApplication.currentUser.getId()) {
            return R.layout.user_list_popuwindow2_layout;
        }
        // 点击未激活用户
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
    @OnClick({R.id.search})
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
                user.setStatus(Math.abs(user.getStatus() - 1));
                user.update();
                mBaseManagementListAdapter.notifyDataSetChanged();
                break;
            case R.id.p_delete:
                popupWindow.dismiss();
                User t = (User) view.getTag();
                if (t.getId() == 1 || TCSApplication.currentUser.getId() == t.getId()) {
                    ToastUtil.showShort(getString(R.string.hit36));
                    return;
                }
                if (isDataBase) {
                    t.delete();
                }
                data.remove(t);
                mBaseManagementListAdapter.notifyDataSetChanged();
                break;
            case R.id.search:
                SearchUserDialog searchUserDialog = new SearchUserDialog(mHandler);
                searchUserDialog.show(getChildFragmentManager(), "SearchUserDialog");
                break;
        }
        super.onClick(view);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        user = data.get(i);
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

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            SearchUserCondition searchUserCondition = (SearchUserCondition) msg.obj;
            Where where = select().from(User.class).where();
            if (!TextUtils.isEmpty(searchUserCondition.getUserName())) {
                where = where.and(User_Table.userName.like(String.format(format, searchUserCondition.getUserName())));
            }

            if (searchUserCondition.getRole() != -1) {
                where = where.and(User_Table.role.is(searchUserCondition.getRole()));
            }

            if (searchUserCondition.getStatus() != -1) {
                where = where.and(User_Table.status.is(searchUserCondition.getStatus()));
            }

            data.clear();
            List list = where.queryList();
            data.addAll(list);
            mBaseManagementListAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
    }
}
