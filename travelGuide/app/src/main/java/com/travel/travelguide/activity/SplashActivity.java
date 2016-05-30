package com.travel.travelguide.activity;

import android.os.Handler;
import android.widget.ImageView;

import com.quickblox.auth.model.QBSession;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.R;
import com.travel.travelguide.Ulti.GeneralCallback;
import com.travel.travelguide.manager.QBManager;
import com.travel.travelguide.manager.TransactionManager;
import com.travel.travelguide.manager.UserManager;

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

//        handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                User currentUser = UserManager.getInstance().getCurrentUser(getApplicationContext());
//                if(currentUser != null){
//                    TransactionManager.getInstance().gotoActivity(SplashActivity.this, MainActivity.class, null, true);
//                }else {
//                    TransactionManager.getInstance().gotoActivity(SplashActivity.this, LoginActivity.class, null, true, imageView, "TEST");
//                }
//
//
//            }
//        }, 2 * 1000);

        createQBSession();


//        imageView.post(new Runnable() {
//            @Override
//            public void run() {
//                ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 20.0f, 1.0f, 20.0f, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
//                scaleAnimation.setDuration(5 * 1000);
//                imageView.startAnimation(scaleAnimation);
//            }
//        });

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//
//                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this, imageView, "TEST");
//                ActivityCompat.startActivity(SplashActivity.this, intent,  activityOptionsCompat.toBundle());
//            }
//        });




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handler != null){
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    private void gotoNextScreen(){
        User currentUser = UserManager.getInstance().getCurrentUser(getApplicationContext());
        if(currentUser != null){
            TransactionManager.getInstance().gotoActivity(SplashActivity.this, MainActivity.class, null, true);
        }else {
            TransactionManager.getInstance().gotoActivity(SplashActivity.this, LoginActivity.class, null, true, imageView, "TEST");
        }
    }

    private void createQBSession(){
        QBManager.getInstance().createSession(this, new GeneralCallback<QBSession>(this){

            @Override
            public void success(QBSession o) {
                gotoNextScreen();
            }

            @Override
            public void error(String errorMessage) {
                super.error(errorMessage);
            }
        });


    }
}
