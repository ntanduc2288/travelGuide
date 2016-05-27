package com.travel.travelguide.presenter;

/**
 * Created by user on 5/27/16.
 */
public abstract class BasePresenters<T> {
    public boolean viewIsValid(T basePresenter) {
        if (basePresenter != null) {
            return true;
        } else {
            return false;
        }
    }
}
