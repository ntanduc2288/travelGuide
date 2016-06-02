package com.travel.travelguide.fragment;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.quickblox.chat.QBChat;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBMessageListener;
import com.quickblox.chat.listeners.QBMessageSentListener;
import com.quickblox.chat.listeners.QBMessageStatusListener;
import com.quickblox.chat.model.QBChatMessage;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.R;
import com.travel.travelguide.presenter.chat.ChatPresenter;
import com.travel.travelguide.presenter.chat.ChatPresenterImpl;
import com.travel.travelguide.presenter.chat.ChatView;

import butterknife.Bind;

/**
 * Created by user on 5/30/16.
 */
public class ChatFragment extends BaseFragment implements ChatView, View.OnClickListener, QBMessageSentListener, QBMessageStatusListener, QBMessageListener{
    @Bind(R.id.title)
    AppCompatTextView lblTitle;
    @Bind(R.id.back_button)
    AppCompatButton btnBack;
    @Bind(R.id.button_send)
    AppCompatButton btnSend;
    @Bind(R.id.edittext_chat)
    AppCompatEditText txtChatView;

    User userChat;
    private MaterialDialog dialog;
    ChatPresenter chatPresenter;
    public static ChatFragment newInstance(User bkUser){
        ChatFragment chatFragment = new ChatFragment();
        chatFragment.setUserChat(bkUser);
        return chatFragment;
    }

    private void setUserChat(User userChat) {
        this.userChat = userChat;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.chat_fragment;
    }

    @Override
    protected void setupViews() {
        btnBack.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        bindUser(userChat);
        chatPresenter = new ChatPresenterImpl(this, userChat);

    }

    @Override
    public void showLoading() {
        if (dialog == null) {
            dialog = new MaterialDialog.Builder(getActivity())
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
    public void bindUser(User user) {
        lblTitle.setText(user.getName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                getActivity().onBackPressed();
                break;
            case R.id.button_send:
                chatPresenter.sendMessage(userChat, txtChatView.getText().toString().trim()

                );
                break;
        }
    }

    @Override
    public void onDestroyView() {
        chatPresenter.destroy();
        chatPresenter = null;
        super.onDestroyView();
    }

    @Override
    public void processMessage(QBChat qbChat, QBChatMessage qbChatMessage) {
        Log.d(ChatFragment.class.getSimpleName(), qbChatMessage.getBody());
    }

    @Override
    public void processError(QBChat qbChat, QBChatException e, QBChatMessage qbChatMessage) {
        Log.d(ChatFragment.class.getSimpleName(), qbChatMessage.getBody());
    }

    @Override
    public void processMessageSent(QBChat qbChat, QBChatMessage qbChatMessage) {
        Log.d(ChatFragment.class.getSimpleName(), qbChatMessage.getBody());
    }

    @Override
    public void processMessageFailed(QBChat qbChat, QBChatMessage qbChatMessage) {
        Log.d(ChatFragment.class.getSimpleName(), qbChatMessage.getBody());
    }

    @Override
    public void processMessageDelivered(String s, String s1, Integer integer) {
        Log.d(ChatFragment.class.getSimpleName(), s);
    }

    @Override
    public void processMessageRead(String s, String s1, Integer integer) {
        Log.d(ChatFragment.class.getSimpleName(), s);
    }
}
