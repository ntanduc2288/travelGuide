package com.travel.travelguide.manager;

import android.os.Bundle;
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
    public static QBManager getInstance(){
        synchronized (QBManager.class){
            if(instance == null){
                instance = new QBManager();
            }
        }

        return instance;
    }

    private QBManager(){}

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

        QBUsers.signIn(qbUser, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                userQBCallback.onSuccess(qbUser, bundle);
            }

            @Override
            public void onError(QBResponseException e) {
                userQBCallback.onError(e);
            }
        });
    }


}
