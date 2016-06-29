package com.travel.travelguide.presenter.LeftMenu;

import android.content.Context;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

/**
 * @author Duc Nguyen
 * @version 1.0
 * @since 6/22/16
 */
public interface LeftMenuPresenter {
    public interface ILeftMenuView {
        void showLoading();
        void hideLoading();
        void showMessage(String message);
        Context getContext();
    }

    public interface Presenter{
        public void updateItineraryData(List<CalendarDay> calendarDays, String numberOfPeople);
    }
}
