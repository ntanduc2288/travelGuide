package com.travel.travelguide.presenter.chat;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.quickblox.chat.QBChat;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBPrivateChat;
import com.quickblox.chat.QBPrivateChatManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBPrivateChatManagerListener;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.manager.QBManager;
import com.travel.travelguide.manager.UserManager;

import org.jivesoftware.smack.SmackException;

/**
 * Created by user on 5/30/16.
 */
public class ChatPresenterImpl extends ChatPresenter {
    ChatView chatView;
    QBPrivateChatManager qbPrivateChatManager;
    QBPrivateChat qbPrivateChat;
    QBChat qbChat;
    private int occupantID = 13210849; //Duc@gmail.com
//    private final int occupantID = 13214162; //Duc2@gmail.com
    User userChat;

    QBPrivateChatManagerListener privateChatManagerListener = new QBPrivateChatManagerListener() {
        @Override
        public void chatCreated(QBPrivateChat qbPrivateChat, boolean createdLocally) {
            if(!createdLocally){
                qbPrivateChat.addMessageListener(ChatPresenterImpl.this);

            }
            qbPrivateChat.addMessageListener(ChatPresenterImpl.this);
        }
    };

    public ChatPresenterImpl(final ChatView chatView, User userChat) {
        this.chatView = chatView;
        chatView.showLoading();
        occupantID = userChat.getQbUserId();
        Log.d(ChatPresenterImpl.class.getSimpleName(), "Chat With: " + userChat.getName() + ". BQUserId: " + occupantID);



        if(!QBManager.getInstance().isLoggedIn()){
            User currentUserLoggedin = UserManager.getInstance().getCurrentUser();
            QBUser qbUser = new QBUser(currentUserLoggedin.getEmail(), currentUserLoggedin.getEmail());
            QBManager.getInstance().signInQBUser(qbUser, new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser o, Bundle bundle) {
                    initQbPrivateChatManager();
                }

                @Override
                public void onError(QBResponseException e) {
                    Log.e(ChatPresenterImpl.class.getSimpleName(), e.toString());
                    chatView.hideLoading();
                    Toast.makeText(chatView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }else {
            initQbPrivateChatManager();

        }
    }

    private void initQbPrivateChatManager(){
        if(qbPrivateChatManager == null){
            qbPrivateChatManager = QBChatService.getInstance().getPrivateChatManager();
            if(qbPrivateChatManager != null){
                qbPrivateChatManager.addPrivateChatManagerListener(privateChatManagerListener);
            }

        }

        qbPrivateChat = qbPrivateChatManager.getChat(occupantID);
        if(qbPrivateChat == null){
            qbPrivateChat = qbPrivateChatManager.createChat(occupantID, ChatPresenterImpl.this);
        }else {
            qbPrivateChat.addMessageListener(ChatPresenterImpl.this);
        }
        qbPrivateChat.addMessageSentListener(ChatPresenterImpl.this);
        chatView.hideLoading();
    }

    @Override
    public void sendMessage(final User user, final String message) {

        qbPrivateChatManager.createDialog(occupantID, new QBEntityCallback<QBDialog>() {
            @Override
            public void onSuccess(QBDialog qbDialog, Bundle bundle) {
                QBChatMessage chatMessage = new QBChatMessage();
                chatMessage.setBody(message);
                chatMessage.setProperty("save_to_history", "1");
                try {
                    qbPrivateChat.sendMessage(chatMessage);
                    Log.d(ChatPresenterImpl.class.getSimpleName(), "Send message: " + message + ". To: " + occupantID);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                    Log.d(ChatPresenterImpl.class.getSimpleName(), "Error Send message: " + message + ". To: " + occupantID +". Error: " + e.getMessage());
                }
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e(ChatPresenterImpl.class.getSimpleName(), e.toString());
            }
        });



    }

    @Override
    public void processMessage(QBChat qbChat, QBChatMessage qbChatMessage) {
        Log.d(ChatPresenterImpl.class.getSimpleName(), "processMessage(): " + qbChatMessage.getBody());
    }

    @Override
    public void processError(QBChat qbChat, QBChatException e, QBChatMessage qbChatMessage) {
        Log.d(ChatPresenterImpl.class.getSimpleName(), "processError(): " + qbChatMessage.getBody());
    }

    @Override
    public void processMessageSent(QBChat qbChat, QBChatMessage qbChatMessage) {
        Log.d(ChatPresenterImpl.class.getSimpleName(), "processMessageSent(): " + qbChatMessage.getBody());
    }

    @Override
    public void processMessageFailed(QBChat qbChat, QBChatMessage qbChatMessage) {
        Log.d(ChatPresenterImpl.class.getSimpleName(), "processMessageFailed(): " + qbChatMessage.getBody());
    }

    @Override
    public void processMessageDelivered(String s, String s1, Integer integer) {
        Log.d(ChatPresenterImpl.class.getSimpleName(), "processMessageDelivered(): " + s);
    }

    @Override
    public void processMessageRead(String s, String s1, Integer integer) {
        Log.d(ChatPresenterImpl.class.getSimpleName(), "processMessageRead(): " + s);
    }

    @Override
    public void destroy() {
//        qbPrivateChatManager.removePrivateChatManagerListener(privateChatManagerListener);
        qbPrivateChat.removeMessageListener(this);
//        qbPrivateChat.removeMessageSentListener(this);
        super.destroy();

    }
}
