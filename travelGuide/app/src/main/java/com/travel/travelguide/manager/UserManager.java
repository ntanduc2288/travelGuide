package com.travel.travelguide.manager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBPrivateChatManager;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.Ulti.Constants;
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
            public void handleResponse(BackendlessUser response) {
                LogUtils.logD(TAG, "handle response login: " + response.toString());
                UserManager.getInstance().setCurrentUser(new User(response));

                signInQBUser(context, UserManager.getInstance().getCurrentUser(), new GeneralCallback<QBUser>(context) {
                    @Override
                    public void success(QBUser qbUser) {
                        UserManager.getInstance().getCurrentUser().setQbUserId(qbUser.getId());
                        UserManager.getInstance().saveUserToDatabase(context);
                        generalCallback.success(UserManager.getInstance().getCurrentUser());
                    }

                    @Override
                    public void error(String errorMessage) {
                        generalCallback.error(errorMessage);
                    }
                });
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
                logoutQBUser(context, callback);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                callback.error(fault.getMessage());
            }
        });
    }

    public void signInQBUser(final Context context, final User user, final GeneralCallback generalCallback){
        QBUser qbUser = new QBUser(user.getEmail(), user.getbackendlessUserId());
        QBManager.getInstance().signInQBUser(qbUser, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                generalCallback.success(qbUser);
            }

            @Override
            public void onError(QBResponseException e) {
                LogUtils.logD(TAG, "handleFault login: " + e.getMessage());

                if(e.getMessage().equalsIgnoreCase(Constants.UNAUTHORIZE_ERROR)){
                    signUpQBUser( user, generalCallback);
                }else {
                    generalCallback.error(e.getMessage());
                }
            }
        });
    }

    public void signUpBKUser(final Context context, User bkUser, final GeneralCallback generalCallback){
        Backendless.UserService.register(bkUser, new BackendlessCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                LogUtils.logD(TAG, response.toString());
                User userTmp = new User(response);
                UserManager.getInstance().setCurrentUser(userTmp);

                signUpQBUser(UserManager.getInstance().getCurrentUser(), new GeneralCallback<QBUser>(context) {
                    @Override
                    public void success(QBUser qbUser) {
                        UserManager.getInstance().getCurrentUser().setQbUserId(qbUser.getId());
                        UserManager.getInstance().saveUserToDatabase(context);
                        generalCallback.success(UserManager.getInstance().getCurrentUser());
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                generalCallback.error(fault.getMessage());
            }
        });
    }

    public void signUpQBUser(final User user, final GeneralCallback callback){
        final QBUser qbUser = new QBUser(user.getEmail(), user.getbackendlessUserId());
        QBManager.getInstance().signUpQBUser(qbUser, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser o, Bundle bundle) {
                callback.success(o);
            }

            @Override
            public void onError(QBResponseException e) {
                callback.error(e.getMessage());
            }
        });
    }

    private void logoutQBUser(final Context context, final GeneralCallback generalCallback){
        QBUsers.signOut(new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                generalCallback.success(null);
                clearLocalUserData(context, generalCallback);
            }

            @Override
            public void onError(QBResponseException e) {
                generalCallback.success(null);
                clearLocalUserData(context, generalCallback);
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

    public void chat(){
        if(QBChatService.getInstance().isLoggedIn()){

        }else {
            QBChatService.getInstance().login(new QBUser(UserManager.getInstance().getCurrentUser().getEmail(),
                    UserManager.getInstance().getCurrentUser().getbackendlessUserId()), new QBEntityCallback() {
                @Override
                public void onSuccess(Object o, Bundle bundle) {

                }

                @Override
                public void onError(QBResponseException e) {

                }
            });
        }
        QBPrivateChatManager privateChatManager = QBChatService.getInstance().getPrivateChatManager();
//        privateChatManager.create
    }

}
