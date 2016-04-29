package com.travel.travelguide.presenter;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.travel.travelguide.Ulti.DatabaseHelper;

/**
 * Created by user on 4/27/16.
 */
public class BasePresenter {
    DatabaseHelper databaseHelper;
    protected DatabaseHelper getDatabaseHelper(Context context){
        if(databaseHelper == null){
            databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }

        return databaseHelper;
    }

    protected void releaseDatabaseHelper(){
        if(databaseHelper != null){
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
