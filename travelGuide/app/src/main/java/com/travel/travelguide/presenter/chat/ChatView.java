package com.travel.travelguide.presenter.chat;

import android.content.Context;

import com.travel.travelguide.Object.User;

/**
 * Created by user on 5/30/16.
 */
public interface ChatView {
    void showLoading();
    void hideLoading();
    Context getContext();
    void bindUser(User user);
}
