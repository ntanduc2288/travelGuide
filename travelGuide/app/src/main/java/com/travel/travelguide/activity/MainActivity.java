package com.travel.travelguide.activity;

import android.content.Intent;

import com.applozic.mobicomkit.api.conversation.Message;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.MessageCommunicator;
import com.travel.travelguide.R;
import com.travel.travelguide.fragment.MapGuideFragment;
import com.travel.travelguide.manager.TransactionManager;
import com.yalantis.ucrop.UCrop;

/**
 * Created by user on 4/23/16.
 */
public class MainActivity extends BaseActivity implements MessageCommunicator {
    @Override
    protected int getLayoutId() {
        return R.layout.main_activity;
    }

    @Override
    protected void setupViews() {
        TransactionManager.getInstance().replaceFragment(getSupportFragmentManager(), MapGuideFragment.newInstance(), R.id.container);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == UCrop.REQUEST_CROP){
            for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
                try{
                    android.support.v4.app.Fragment fragment = getSupportFragmentManager().getFragments().get(i);
                    if(fragment != null){
                        fragment.onActivityResult(requestCode, resultCode, data);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
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
}
