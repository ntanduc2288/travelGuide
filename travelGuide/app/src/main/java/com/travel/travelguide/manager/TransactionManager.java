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
        replaceFragment(fragmentManager, fragment, R.id.container);
    }

    public void replaceFragment(FragmentManager fragmentManager, Fragment fragment, int resContainerId){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(resContainerId, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());

        fragmentTransaction.commit();
    }

    public void addFragment(FragmentManager fragmentManager, Fragment fragment){
        addFragment(fragmentManager, fragment, R.id.container);
    }

    public void addFragment(FragmentManager fragmentManager, Fragment fragment, int resContainerId){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(resContainerId, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }



    public void gotoActivity(Activity activity, Class activityClass, Bundle bundle, boolean shouldClearAllActivityInTask){
        Intent intent = new Intent(activity, activityClass);
        if(bundle != null){
            intent.putExtras(bundle);
        }

        if(shouldClearAllActivityInTask){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        activity.startActivity(intent);

    }
}
