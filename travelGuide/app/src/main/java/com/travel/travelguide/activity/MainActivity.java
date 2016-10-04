package com.travel.travelguide.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.applozic.mobicomkit.api.conversation.Message;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.MessageCommunicator;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.squareup.otto.Subscribe;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.R;
import com.travel.travelguide.Ulti.EvenBusHelper;
import com.travel.travelguide.Ulti.GCMRegistrationUtils;
import com.travel.travelguide.fragment.EditProfileFragment;
import com.travel.travelguide.fragment.MapGuideFragment;
import com.travel.travelguide.manager.TransactionManager;
import com.travel.travelguide.manager.UserManager;
import com.travel.travelguide.presenter.LeftMenu.LeftMenuPresenter;
import com.travel.travelguide.presenter.LeftMenu.LeftMenuPresenterImpl;
import com.travel.travelguide.presenter.main.MainPresenter;
import com.travel.travelguide.presenter.main.MainPresenterImpl;
import com.yalantis.ucrop.UCrop;

import java.util.Calendar;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by user on 4/23/16.
 */
public class MainActivity extends BaseActivity implements MessageCommunicator, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener,
        MainPresenter.MainView, LeftMenuPresenter.ILeftMenuView {
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.avatar_left_menu)
    CircleImageView imgAvatar;
    @Bind(R.id.username_left_menu)
    TextView lblUsername;
    @Bind(R.id.calendarView)
    MaterialCalendarView materialCalendarView;
    MapGuideFragment mapGuideFragment;
    MaterialDialog dialog;
    MainPresenter.Presenter presenter;
    @Bind(R.id.edit_information)
    AppCompatButton btnEditInformation;
    @Bind(R.id.messages)
    AppCompatButton btnMessages;
    @Bind(R.id.button_add_edit_travel_itinerary)
    AppCompatButton btnAddEditTravelItinerary;
    @Bind(R.id.invite_friends)
    AppCompatButton btnInviteFriends;
    @Bind(R.id.logout)
    AppCompatButton btnLogout;
    @Bind(R.id.about_travetrot)
    AppCompatButton btnAboutTravetrot;
    @Bind(R.id.calendar_container)
    LinearLayout lnCalendarContainer;
    @Bind(R.id.scrollview_left_menu_container)
    NestedScrollView leftMenuContainer;
    @Bind(R.id.back_calendar)
    AppCompatButton btnBackCalendar;
    @Bind(R.id.save_calendar)
    AppCompatButton btnSaveItinerary;
    @Bind(R.id.number_of_people_test)
    AppCompatEditText txtNumberOfPeople;
    @Bind(R.id.destination)
    AppCompatEditText txtDestination;

    LeftMenuPresenter.Presenter leftMenuPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        presenter.subscribeChat();

    }

    @Override
    protected void onPause() {
        super.onPause();
//        presenter.unSubscribeChat();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.main_activity;
    }

    @Override
    protected void setupViews() {
        EvenBusHelper.getInstance().registerEventBus(this);
        presenter = new MainPresenterImpl(this);
        leftMenuPresenter = new LeftMenuPresenterImpl(this);
        mapGuideFragment = MapGuideFragment.newInstance();
        mapGuideFragment.setDrawerClickedListener(this);
        TransactionManager.getInstance().replaceFragment(getSupportFragmentManager(), mapGuideFragment, R.id.container);
        navigationView.setNavigationItemSelectedListener(this);

        btnAboutTravetrot.setOnClickListener(this);
        btnAddEditTravelItinerary.setOnClickListener(this);
        btnEditInformation.setOnClickListener(this);
        btnInviteFriends.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnMessages.setOnClickListener(this);
        btnBackCalendar.setOnClickListener(this);
        btnSaveItinerary.setOnClickListener(this);

        setupCalendarView();
        GCMRegistrationUtils gcmRegistrationUtils = new GCMRegistrationUtils(this);
        gcmRegistrationUtils.setUpGcmNotification();

    }

    private void setupCalendarView() {
        lnCalendarContainer.setVisibility(View.GONE);
        materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_RANGE);

//        materialCalendarView.setOnRangeSelectedListener(new OnRangeSelectedListener() {
//            @Override
//            public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
//
//            }
//        });
//        txtNumberOfPeople.setText(UserManager.getInstance().getCurrentUser().getNumberOfPeople());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UCrop.REQUEST_CROP) {
            for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
                try {
                    Fragment fragment = getSupportFragmentManager().getFragments().get(i);
                    if (fragment != null) {
                        fragment.onActivityResult(requestCode, resultCode, data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(SOCIAL_NETWORK_TAG);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void updateLatestMessage(Message message, String formattedContactNumber) {
        new ConversationUIService(this).updateLatestMessage(message, formattedContactNumber);
    }

    @Override
    public void removeConversation(Message message, String formattedContactNumber) {
        new ConversationUIService(this).removeConversation(message, formattedContactNumber);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_my_profile:
            case R.id.textview_profile_name:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.edit_information:
                TransactionManager.getInstance().addFragment(getSupportFragmentManager(), EditProfileFragment.newInstance(UserManager.getInstance().getCurrentUser()));
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.messages:
                gotoConversationList();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.button_add_edit_travel_itinerary:
                showHideCalendar(true);
                bindDestination(UserManager.getInstance().getCurrentUser().getDestination());
                break;
            case R.id.invite_friends:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                startActivity(Intent.createChooser(sharingIntent, "Invite via:"));

                break;
            case R.id.logout:
                presenter.logout();

                break;
            case R.id.back_calendar:
                showHideCalendar(false);
                break;
            case R.id.save_calendar:
                leftMenuPresenter.updateItineraryData(materialCalendarView.getSelectedDates(), txtNumberOfPeople.getText().toString().trim(), txtDestination.getText().toString().trim());
                break;
        }


    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_information:
                TransactionManager.getInstance().addFragment(getSupportFragmentManager(), EditProfileFragment.newInstance(UserManager.getInstance().getCurrentUser()));
                break;
            case R.id.messages:
                gotoConversationList();
                break;
            case R.id.button_add_edit_travel_itinerary:

                break;
            case R.id.invite_friends:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                startActivity(Intent.createChooser(sharingIntent, "Invite via:"));
                break;
            case R.id.logout:
                presenter.logout();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        presenter.destroy();
        EvenBusHelper.getInstance().unRegisterEventBus(this);
        super.onDestroy();
    }

    @Override
    public void showLoading() {
        if (dialog == null) {
            dialog = new MaterialDialog.Builder(this)
                    .content(getString(R.string.loading_three_dot))
                    .progress(true, 0)
                    .cancelable(false)
                    .build();
        }

        if (dialog.isShowing()) {
            return;
        }

        dialog.show();

    }

    @Override
    public void hideLoading() {
        if (dialog != null)
            dialog.dismiss();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void gotoLoginScreen() {
        TransactionManager.getInstance().gotoActivity(this, LoginActivity.class, null, true);
    }

    @Override
    public void gotoConversationList() {
        Intent intent = new Intent(this, ConversationActivity.class);
        startActivity(intent);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Subscribe
    public void updateUserView(User user) {
        if (user != null && user.getbackendlessUserId().equalsIgnoreCase(UserManager.getInstance().getCurrentUser().getUserId())) {
            lblUsername.setText(user.getName());
            ImageLoader.getInstance().displayImage(user.getAvatar(), imgAvatar);
        }
    }

    @Override
    public void showHideCalendar(boolean show) {
        if (show) {
            lnCalendarContainer.setVisibility(View.VISIBLE);
            leftMenuContainer.setVisibility(View.GONE);
            long travelFrom = UserManager.getInstance().getCurrentUser().getTravelDateFrom();
            long travelTo = UserManager.getInstance().getCurrentUser().getTravelDateTo();

            if (travelFrom != 0 && travelTo != 0) {
                Calendar calendarStart = Calendar.getInstance();
                calendarStart.setTimeInMillis(travelFrom);
                Calendar calendarEnd = Calendar.getInstance();
                calendarEnd.setTimeInMillis(travelTo);
                materialCalendarView.selectRange(CalendarDay.from(calendarStart), CalendarDay.from(calendarEnd));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(travelFrom);
                materialCalendarView.setCurrentDate(calendar);
            }


            txtNumberOfPeople.setText(String.valueOf(UserManager.getInstance().getCurrentUser().getNumberOfPeople()));
        } else {
            lnCalendarContainer.setVisibility(View.GONE);
            leftMenuContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void bindDestination(String destination) {
        txtDestination.setText(destination);
    }
}
