package com.travel.travelguide.manager;

import android.content.Context;
import android.util.Log;

import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.UserLoginTask;
import com.applozic.mobicomkit.uiwidgets.ApplozicSetting;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.Ulti.DatabaseHelper;
import com.travel.travelguide.Ulti.GeneralCallback;
import com.travel.travelguide.Ulti.LogUtils;

import java.sql.SQLException;
import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by bkUser on 4/23/16.
 */
public class UserManager {
    private final String TAG = UserManager.class.getSimpleName();
    private static UserManager instance;
    User bkUser;
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
        return bkUser;
    }

    public User getCurrentUser(Context context) {
        if(bkUser == null){
            try {
                ArrayList<User> users = (ArrayList<User>) getDatabaseHelper(context).getUser().queryForAll();
                if(users != null && users.size() > 0){
                    bkUser = users.get(0);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(bkUser != null){
            bkUser.setbackendlessUserId(bkUser.getbackendlessUserId());
        }
        return bkUser;
    }

    public void setCurrentUser(User user) {
        this.bkUser = user;
    }

    public void saveUserToDatabase(final Context context){

        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                try {
                    getDatabaseHelper(context).getUser().create(bkUser);

                    if(databaseHelper != null){
                        OpenHelperManager.releaseHelper();
                        databaseHelper = null;
                    }

                    Log.e(UserManager.class.getSimpleName(), "Save data successful DB.");
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.e(UserManager.class.getSimpleName(), e.toString());
                }
                subscriber.onNext(null);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });





    }

    public void updateUserToDatabase(final Context context){

        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                try {
                    getDatabaseHelper(context).getUser().update(bkUser);

                    if(databaseHelper != null){
                        OpenHelperManager.releaseHelper();
                        databaseHelper = null;
                    }

                    Log.e(UserManager.class.getSimpleName(), "update bkUser into db successful");
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.e(UserManager.class.getSimpleName(), e.toString());
                }
                subscriber.onNext(null);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });





    }

    public boolean haveTravelDate(User user){
        if(0 != user.getTravelDateFrom() && 0 != user.getTravelDateTo()){
            return true;
        }

        return false;
    }


    public void signInBKUser(final Context context, String email, String password, final GeneralCallback generalCallback) {
        Backendless.UserService.login(email, password, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(final BackendlessUser response) {
                LogUtils.logD(TAG, "handle response login: " + response.toString());
                User userTmp = new User(response);
                UserManager.getInstance().setCurrentUser(userTmp);
                UserManager.getInstance().saveUserToDatabase(context);
//                generalCallback.success(UserManager.getInstance().getCurrentUser());


                loginAppzolicUser(context, UserManager.getInstance().getCurrentUser(), generalCallback);


            }


            @Override
            public void handleFault(BackendlessFault fault) {
                LogUtils.logD(TAG, "handleFault login: " + fault.getMessage());
                generalCallback.error(fault.getMessage());
            }
        });

    }

    public void logoutBKUser(final Context context, final GeneralCallback callback){
        Backendless.UserService.logout(new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void response) {
                clearLocalUserData(context, callback);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                clearLocalUserData(context, callback);
            }
        });
    }



    public void signUpBKUser(final Context context, User bkUser, final GeneralCallback generalCallback){
        Backendless.UserService.register(bkUser, new BackendlessCallback<BackendlessUser>() {
            @Override
            public void handleResponse(final BackendlessUser response) {
                LogUtils.logD(TAG, response.toString());
                User userTmp = new User(response);
                UserManager.getInstance().setCurrentUser(userTmp);
                UserManager.getInstance().saveUserToDatabase(context);
                generalCallback.success(UserManager.getInstance().getCurrentUser());
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                generalCallback.error(fault.getMessage());
            }
        });
    }

    private void clearLocalUserData(final Context context, final GeneralCallback generalCallback) {

        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                int result = 0;
                try {
                    Dao<User, Long> dao =  getDatabaseHelper(context).getUser();
                    DeleteBuilder<User, Long> deleteBuilder = dao.deleteBuilder();
                    deleteBuilder.where().eq("backendlessUserId", bkUser.getbackendlessUserId());
                    result = deleteBuilder.delete();
                    if(result > 0){
                        bkUser = null;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                subscriber.onNext(result > 0 ? true : false);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean o) {
                        generalCallback.success(o);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        generalCallback.success(null);
                    }
                });
    }

    private void loginAppzolicUser(Context context, User user, final GeneralCallback callback){
        UserLoginTask.TaskListener listener = new UserLoginTask.TaskListener() {
            @Override
            public void onSuccess(RegistrationResponse registrationResponse, Context context) {
                // After successful registration with Applozic server the callback will come here
                ApplozicSetting.getInstance(context).showStartNewButton();//To show contact list.
                ApplozicSetting.getInstance(context).enableRegisteredUsersContactCall();//To enable the applozic Registered Users Contact Note:for disable that you can comment this line of code
                callback.success(registrationResponse);
            }

            @Override
            public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                callback.error(exception.getMessage());
            }
        };

        com.applozic.mobicomkit.api.account.user.User userAppzolic = new com.applozic.mobicomkit.api.account.user.User();
        userAppzolic.setUserId(user.getbackendlessUserId());
        userAppzolic.setPassword(user.getbackendlessUserId());
        userAppzolic.setEmail(user.getEmail());
        userAppzolic.setImageLink(user.getAvatar());
        userAppzolic.setDisplayName(user.getName());
        new UserLoginTask(userAppzolic, listener, context).execute((Void) null);
    }

}
