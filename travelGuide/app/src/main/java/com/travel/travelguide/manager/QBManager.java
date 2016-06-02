package com.travel.travelguide.manager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.server.BaseService;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.travel.travelguide.Ulti.GeneralCallback;

/**
 * Created by bkUser on 5/27/16.
 */
public class QBManager {
    private static QBManager instance;
    private QBChatService qbChatService;
    protected static final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    public static QBManager getInstance(){
        synchronized (QBManager.class){
            if(instance == null){
                instance = new QBManager();
            }
        }

        return instance;
    }

    private QBManager(){
        qbChatService = QBChatService.getInstance();
    }

    public QBUser getCurrentUser(){
        return QBChatService.getInstance().getUser();
    }

    public void createSession(final GeneralCallback generalCallback){

        QBAuth.createSession(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                generalCallback.success(qbSession);
            }

            @Override
            public void onError(QBResponseException e) {
                generalCallback.error(e.toString());
            }
        });
    }

    public boolean isValidSession(){
        boolean result = false;
        try {
            if(!TextUtils.isEmpty(BaseService.getBaseService().getToken())){
                result = true;
            }else {
                result = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    public boolean isLoggedIn(){
        return QBChatService.getInstance().isLoggedIn();
    }

    public void signUpQBUser(QBUser user, final QBEntityCallback callback){

        QBUsers.signUp(user, callback);
    }

    public void signInQBUser(final QBUser qbUser, final QBEntityCallback userQBCallback){
        QBAuth.createSession(qbUser, new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(final QBSession qbSession, Bundle bundle) {
                qbUser.setId(qbSession.getUserId());
                loginToChat(qbUser, userQBCallback);
            }

            @Override
            public void onError(final QBResponseException e) {
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        userQBCallback.onError(e);
                    }
                });
            }
        });

    }

    private void loginToChat(final QBUser qbUser, final QBEntityCallback callback){
        if(qbChatService.isLoggedIn()){
            callback.onSuccess(qbUser, null);
            return;
        }

        qbChatService.login(qbUser, new QBEntityCallback() {
            @Override
            public void onSuccess(Object o, Bundle bundle) {
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(qbUser, null);
                    }
                });
            }

            @Override
            public void onError(final QBResponseException e) {
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError(e);
                    }
                });
            }
        });
    }

    public void logout(final QBEntityCallback callback){
        qbChatService.logout(new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                callback.onSuccess(aVoid, bundle);
                qbChatService.destroy();
            }

            @Override
            public void onError(QBResponseException e) {
                callback.onError(e);
            }
        });
    }


}
