package com.travel.travelguide.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.applozic.mobicomkit.api.conversation.Message;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.MessageCommunicator;
import com.travel.travelguide.R;
import com.travel.travelguide.fragment.EditProfileFragment;
import com.travel.travelguide.fragment.MapGuideFragment;
import com.travel.travelguide.manager.TransactionManager;
import com.travel.travelguide.manager.UserManager;
import com.travel.travelguide.presenter.main.MainPresenter;
import com.travel.travelguide.presenter.main.MainPresenterImpl;
import com.yalantis.ucrop.UCrop;

import butterknife.Bind;

/**
 * Created by user on 4/23/16.
 */
public class MainActivity extends BaseActivity implements MessageCommunicator, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener,
        MainPresenter.MainView{
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    MapGuideFragment mapGuideFragment;
    MaterialDialog dialog;
    MainPresenter.Presenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.main_activity;
    }

    @Override
    protected void setupViews() {
        presenter = new MainPresenterImpl(this);
        mapGuideFragment = MapGuideFragment.newInstance();
        mapGuideFragment.setDrawerClickedListener(this);
        TransactionManager.getInstance().replaceFragment(getSupportFragmentManager(), mapGuideFragment, R.id.container);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UCrop.REQUEST_CROP) {
            for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
                try {
                    android.support.v4.app.Fragment fragment = getSupportFragmentManager().getFragments().get(i);
                    if (fragment != null) {
                        fragment.onActivityResult(requestCode, resultCode, data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_information:
                TransactionManager.getInstance().addFragment(getSupportFragmentManager(), EditProfileFragment.newInstance(UserManager.getInstance().getCurrentUser()));
                break;
            case R.id.messages:

                break;
            case R.id.button_add_edit_travel_itinerary:

                break;
            case R.id.invite_friends:

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

        if(dialog.isShowing()){
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
    public void gotoLoginScreen() {
        TransactionManager.getInstance().gotoActivity(this, LoginActivity.class, null, true);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
