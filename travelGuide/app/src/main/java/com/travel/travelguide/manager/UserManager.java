package com.travel.travelguide.manager;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.Ulti.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by user on 4/23/16.
 */
public class UserManager {
    private static UserManager instance;
    User user;
    DatabaseHelper databaseHelper;

    public static UserManager getInstance(){
        if(instance == null){
            synchronized (UserManager.class){
                if(instance == null){
                    instance = new UserManager();
                }
            }
        }

        return instance;
    }

    private DatabaseHelper getDatabaseHelper(Context context){
        if(databaseHelper == null){
            databaseHelper = OpenHelperManager.getHelper(context.getApplicationContext(), DatabaseHelper.class);
        }
        return databaseHelper;
    }

    private UserManager(){}

    public User getCurrentUser() {
        return user;
    }

    public User getCurrentUser(Context context) {
        if(user == null){
            try {
                ArrayList<User> users = (ArrayList<User>) getDatabaseHelper(context).getUser().queryForAll();
                if(users != null && users.size() > 0){
                    user = users.get(0);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(user != null){
            user.setbackendlessUserId(user.getbackendlessUserId());
        }
        return user;
    }

    public void setCurrentUser(User user) {
        this.user = user;
    }

    public void saveUserToDatabase(final Context context){

//        Observable.create(new Observable.OnSubscribe<Object>() {
//            @Override
//            public void call(Subscriber<? super Object> subscriber) {
//                try {
//                    getDatabaseHelper(context).getUser().create(user);
//
//                    if(databaseHelper != null){
//                        OpenHelperManager.releaseHelper();
//                        databaseHelper = null;
//                    }
//
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                    Toast.makeText(context, "Could not save user data. " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//                subscriber.onNext(null);
//                subscriber.onCompleted();
//            }
//        }).subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(new Action1<Object>() {
//            @Override
//            public void call(Object o) {
//
//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//
//            }
//        });

        try {
            getDatabaseHelper(context).getUser().create(user);

            if(databaseHelper != null){
                OpenHelperManager.releaseHelper();
                databaseHelper = null;
            }

            Toast.makeText(context, "Save data successful DB. ", Toast.LENGTH_SHORT).show();

        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(context, "Could not save user data. " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(UserManager.class.getSimpleName(), e.toString());
        }



    }

    public void updateUserToDatabase(final Context context){

//        Observable.create(new Observable.OnSubscribe<Object>() {
//            @Override
//            public void call(Subscriber<? super Object> subscriber) {
//                try {
//                    getDatabaseHelper(context).getUser().create(user);
//
//                    if(databaseHelper != null){
//                        OpenHelperManager.releaseHelper();
//                        databaseHelper = null;
//                    }
//
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                    Toast.makeText(context, "Could not save user data. " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//                subscriber.onNext(null);
//                subscriber.onCompleted();
//            }
//        }).subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(new Action1<Object>() {
//            @Override
//            public void call(Object o) {
//
//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//
//            }
//        });

        try {
            getDatabaseHelper(context).getUser().update(user);

            if(databaseHelper != null){
                OpenHelperManager.releaseHelper();
                databaseHelper = null;
            }

            Toast.makeText(context, "Update data successful DB. ", Toast.LENGTH_SHORT).show();

        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(context, "Could not Update user data. " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(UserManager.class.getSimpleName(), e.toString());
        }



    }

    public boolean clearCurrentUserInfo(Context context){
        int result = 0;
        try {
            Dao<User, Long> dao =  getDatabaseHelper(context).getUser();
            DeleteBuilder<User, Long> deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq("id", user.getbackendlessUserId());
            result = deleteBuilder.delete();
            if(result > 0){
                user = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result > 0 ? true : false;
    }

    public boolean haveTravelDate(User user){
        if(0 != user.getTravelDateFrom() && 0 != user.getTravelDateTo()){
            return true;
        }

        return false;
    }

}
