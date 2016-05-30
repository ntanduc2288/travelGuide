package com.travel.travelguide.Ulti;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by user on 5/30/16.
 */
public abstract class GeneralCallback<T> {
    Context context;
    public GeneralCallback(Context context){
        this.context = context;
    }
    public abstract void success(T t);
    public void error(String errorMessage){
        if(context instanceof Activity && !((Activity) context).isFinishing()){
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

}
