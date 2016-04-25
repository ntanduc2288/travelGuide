package com.travel.travelguide.activity;

import com.travel.travelguide.R;
import com.travel.travelguide.fragment.MapGuideFragment;
import com.travel.travelguide.manager.TransactionManager;

/**
 * Created by user on 4/23/16.
 */
public class MainActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.main_activity;
    }

    @Override
    protected void setupViews() {
        TransactionManager.getInstance().replaceFragment(getSupportFragmentManager(), MapGuideFragment.newInstance(), R.id.container);
    }
}
