package com.travel.travelguide.activity;

import com.travel.travelguide.R;
import com.travel.travelguide.fragment.LoginFragment;
import com.travel.travelguide.manager.TransactionManager;

/**
 * Created by user on 4/22/16.
 */
public class LoginActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void setupViews() {
        TransactionManager.getInstance().replaceFragment(getFragmentManager(), LoginFragment.newInstance());


    }


}
