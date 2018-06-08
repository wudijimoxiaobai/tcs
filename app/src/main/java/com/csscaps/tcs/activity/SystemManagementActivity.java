package com.csscaps.tcs.activity;

import android.support.v4.app.Fragment;

import com.csscaps.tcs.R;
import com.csscaps.tcs.fragment.NetworkConfigurationFragment;
import com.csscaps.tcs.fragment.UserManagementFragment;

/**
 * Created by tl on 2018/6/1.
 */

public class SystemManagementActivity extends BaseTabActivity {

    @Override
    protected Fragment[] getFragments() {
        NetworkConfigurationFragment mNetworkConfigurationFragment = new NetworkConfigurationFragment();
        UserManagementFragment  mUserManagementFragment = new UserManagementFragment();
        return new Fragment[]{mNetworkConfigurationFragment,mUserManagementFragment};
    }

    @Override
    protected int getTabTitleResourcesId() {
        return R.array.tab3;
    }
}
