package com.travel.travelguide.manager;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.travel.travelguide.R;

/**
 * Created by user on 4/22/16.
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
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());


        fragmentTransaction.commit();
    }

    public void addFragment(FragmentManager fragmentManager, Fragment fragment){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    public void gotoActivity(Activity activity, Class activityClass, Bundle bundle, boolean isRootActivity){
        Intent intent = new Intent(activity, activityClass);
        if(bundle != null){
            intent.putExtras(bundle);
        }

        if(isRootActivity){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        activity.startActivity(intent);

    }
}
