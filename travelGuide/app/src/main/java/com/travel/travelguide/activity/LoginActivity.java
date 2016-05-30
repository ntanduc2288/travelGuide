package com.travel.travelguide.activity;

import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.server.BaseService;
import com.travel.travelguide.R;
import com.travel.travelguide.Ulti.Constants;
import com.travel.travelguide.fragment.LoginFragment;
import com.travel.travelguide.manager.TransactionManager;

/**
 * Created by user on 4/22/16.
 */
public class LoginActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_container;
    }

    @Override
    protected void setupViews() {

        TransactionManager.getInstance().replaceFragment(getSupportFragmentManager(), LoginFragment.newInstance());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    QBSession qbSession = QBAuth.getSession();
                    String token = qbSession.getToken();
                    BaseService.getBaseService().getToken();
                } catch (QBResponseException e) {
                    e.printStackTrace();
                } catch (BaseServiceException e){
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }
}
