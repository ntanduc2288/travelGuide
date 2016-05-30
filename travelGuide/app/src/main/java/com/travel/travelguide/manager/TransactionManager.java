package com.travel.travelguide.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.travel.travelguide.R;

/**
 * Created by bkUser on 4/22/16.
 */
public class TransactionManager {
    private static TransactionManager instance;
    public static TransactionManager getInstance(){
        if(instance == null){
            synchronized (TransactionManager.class){
                if(instance == null){
                    instance = new TransactionManager();
                }
            }
        }

        return instance;
    }

    private TransactionManager(){

    }

    public void replaceFragment(FragmentManager fragmentManager, Fragment fragment){
        replaceFragment(fragmentManager, fragment, R.id.container);
    }

    public void replaceFragment(FragmentManager fragmentManager, Fragment fragment, int resContainerId){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_right_to_left,
                0, 0, R.anim.slide_left_to_right);
        fragmentTransaction.replace(resContainerId, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());

        fragmentTransaction.commit();
    }

    public void addFragment(FragmentManager fragmentManager, Fragment fragment){
        addFragment(fragmentManager, fragment, R.id.container);
    }

    public void addFragment(FragmentManager fragmentManager, Fragment fragment, int resContainerId){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_right_to_left,
                0, 0, R.anim.slide_left_to_right);
        fragmentTransaction.add(resContainerId, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }



    public void gotoActivity(Activity activity, Class activityClass, Bundle bundle, boolean shouldClearAllActivityInTask){
        gotoActivity(activity, activityClass, bundle, shouldClearAllActivityInTask, null, null);

    }

    public void gotoActivity(Activity activity, Class activityClass, Bundle bundle, boolean shouldClearAllActivityInTask, View transitionView, String transitionName){
        Intent intent = new Intent(activity, activityClass);
        if(bundle != null){
            intent.putExtras(bundle);
        }

        if(shouldClearAllActivityInTask){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        ActivityOptionsCompat activityOptionsCompat;
        if(transitionView == null){
            activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, null);
        }else {
            activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionView, transitionName);
        }
        ActivityCompat.startActivity(activity, intent,  activityOptionsCompat.toBundle());


//        activity.overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
//        activity.startActivity(intent);


    }
}
