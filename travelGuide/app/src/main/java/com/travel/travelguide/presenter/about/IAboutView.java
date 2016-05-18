package com.travel.travelguide.presenter.about;

import android.content.Context;

import com.travel.travelguide.Object.User;

/**
 * Created by user on 4/29/16.
 */
public interface IAboutView {
    void bindData(User user);
    void showMessage(String message);
    Context getContext();
}
