package com.travel.travelguide.presenter.LeftMenu;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.R;
import com.travel.travelguide.Ulti.LogUtils;
import com.travel.travelguide.manager.UserManager;

import java.util.List;

/**
 * @author Duc Nguyen
 * @version 1.0
 * @since 6/22/16
 */
public class LeftMenuPresenterImpl implements LeftMenuPresenter.Presenter {
    private static final String TAG = LeftMenuPresenterImpl.class.getSimpleName();
    LeftMenuPresenter.ILeftMenuView leftMenuView;

    public LeftMenuPresenterImpl(LeftMenuPresenter.ILeftMenuView leftMenuView) {
        this.leftMenuView = leftMenuView;
    }

    @Override
    public void updateItineraryData(List<CalendarDay> calendarDays) {
        leftMenuView.showLoading();
        long startDate = 0;
        long endDate = 0;
        if(calendarDays != null && calendarDays.size() > 0){
            if(calendarDays.size() == 1){
                startDate = endDate = calendarDays.get(0).getCalendar().getTimeInMillis();
            }else {
                startDate = calendarDays.get(0).getCalendar().getTimeInMillis();
                endDate = calendarDays.get(calendarDays.size() - 1).getCalendar().getTimeInMillis();
            }
        }else {
            leftMenuView.hideLoading();
            leftMenuView.showMessage("Please select itinerary dates");
            return;
        }

        User user = new User(UserManager.getInstance().getCurrentUser());
        user.setTravelDateFrom(startDate);
        user.setTravelDateTo(endDate);

        Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                if(viewIsValid()){
                    leftMenuView.hideLoading();
                    leftMenuView.showMessage(leftMenuView.getContext().getString(R.string.your_account_has_been_updated));
                    UserManager.getInstance().setCurrentUser(new User(response));
                    UserManager.getInstance().updateUserToDatabase(leftMenuView.getContext());
//                    EvenBusHelper.getInstance().notifyUserDataChanged(user);
//                    leftMenuView.updateUserInfoSuccessfull(user);
                }
                LogUtils.logD(TAG, "update profile: " + response.toString());

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if(viewIsValid()){
                    leftMenuView.hideLoading();
                    leftMenuView.showMessage(fault.getMessage());
                }
                LogUtils.logD(TAG, "update profile error: " + fault.getMessage());
            }
        });

    }

    public boolean viewIsValid() {
        if (leftMenuView != null) {
            return true;
        } else {
            return false;
        }
    }
}
