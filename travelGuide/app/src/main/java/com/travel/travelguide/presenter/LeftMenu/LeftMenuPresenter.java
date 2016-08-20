package com.travel.travelguide.presenter.LeftMenu;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import android.content.Context;

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
        void showHideCalendar(boolean show);
        void bindDestination(String destination);
        Context getContext();
    }

    public interface Presenter{
        public void updateItineraryData(List<CalendarDay> calendarDays, String numberOfPeople, String destination);
    }
}
