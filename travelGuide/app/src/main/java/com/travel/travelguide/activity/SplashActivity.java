package com.travel.travelguide.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.travel.travelguide.R;

import butterknife.Bind;


/**
 * Created by user on 4/23/16.
 */
public class SplashActivity extends BaseActivity {
    @Bind(R.id.image)
    ImageView imageView;
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


        imageView.post(new Runnable() {
            @Override
            public void run() {
                ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 20.0f, 1.0f, 20.0f, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
                scaleAnimation.setDuration(5 * 1000);
                imageView.startAnimation(scaleAnimation);
            }
        });



    }
}
