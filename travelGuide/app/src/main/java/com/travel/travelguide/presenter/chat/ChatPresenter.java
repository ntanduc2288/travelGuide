package com.travel.travelguide.presenter.chat;

import com.travel.travelguide.Object.User;
import com.travel.travelguide.presenter.BasePresenters;

/**
 * Created by user on 5/30/16.
 */
public abstract class ChatPresenter extends BasePresenters{
    public abstract void sendMessage(User user, String message);

}
