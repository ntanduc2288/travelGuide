package com.travel.travelguide.activity;

import android.content.Intent;
import android.os.Handler;

import com.travel.travelguide.R;


/**
 * Created by user on 4/23/16.
 */
public class SplashActivity extends BaseActivity {

    Handler handler;
    @Override
    protected int getLayoutId() {
        return R.layout.splash_activity;
    }

    @Override
    protected void setupViews() {

        if(!isTaskRoot()){
            finish();
            return;
        }

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, 3 * 1000);

    }
}
