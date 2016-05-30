package com.travel.travelguide.presenter.chat;

import com.travel.travelguide.Object.User;

/**
 * Created by user on 5/30/16.
 */
public class ChatPresenterImpl extends ChatPresenter {
    ChatView chatView;
    public ChatPresenterImpl(ChatView chatView) {
        this.chatView = chatView;
    }

    @Override
    public void sendMessage(User user, String message) {

    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
