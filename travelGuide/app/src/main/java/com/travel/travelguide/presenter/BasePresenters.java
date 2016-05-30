package com.travel.travelguide.presenter;

/**
 * Created by user on 5/27/16.
 */
public abstract class BasePresenters<T> {
    T t;
    public boolean viewIsValid(T basePresenter) {
        t = basePresenter;
        if (basePresenter != null) {
            return true;
        } else {
            return false;
        }
    }

    public void destroy(){
        t = null;
    }
}
