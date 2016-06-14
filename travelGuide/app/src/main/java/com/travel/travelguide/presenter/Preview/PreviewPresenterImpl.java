package com.travel.travelguide.presenter.Preview;

import android.text.TextUtils;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.travel.travelguide.Object.Preview;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.manager.UserManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 6/15/16.
 */
public class PreviewPresenterImpl extends PreviewPresenter.Presenter {
    PreviewPresenter.PreviewView previewView;
    User userReceivedPreview;

    public PreviewPresenterImpl(PreviewPresenter.PreviewView previewView, User userReceivedPreview) {
        this.previewView = previewView;
        this.userReceivedPreview = userReceivedPreview;
    }

    @Override
    public void getPreviewList(String toUserId) {
        previewView.showProgressbar();

        String queryClause = "toUserId = '" + toUserId +"'";
        BackendlessDataQuery backendlessDataQuery = new BackendlessDataQuery();
        QueryOptions queryOptions = new QueryOptions();
        backendlessDataQuery.setWhereClause(queryClause);
        backendlessDataQuery.setQueryOptions(queryOptions);

        Backendless.Persistence.of(Preview.class).find(backendlessDataQuery, new AsyncCallback<BackendlessCollection<Preview>>() {
            @Override
            public void handleResponse(BackendlessCollection<Preview> response) {
                List<Preview> previews = response.getCurrentPage();
                if(viewIsValid(previewView)){
                    previewView.hideProgressbar();
                    previewView.bindPreviewsOnView((ArrayList<Preview>) previews);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if(viewIsValid(previewView)){
                    previewView.hideProgressbar();
                    previewView.showAler(fault.getMessage());
                }
            }
        });
    }

    @Override
    public void sendPreview(String content) {
        previewView.showLoading();
        if(TextUtils.isEmpty(content)){
            previewView.hideLoading();
            return;
        }

        final Preview preview = new Preview();
        preview.setFromUserAvatar(UserManager.getInstance().getCurrentUser().getAvatar());
        preview.setFromUserId(UserManager.getInstance().getCurrentUser().getbackendlessUserId());
        preview.setFromUserName(UserManager.getInstance().getCurrentUser().getName());
        preview.setToUserId(userReceivedPreview.getUserId());
        preview.setMessage(content);

        Backendless.Persistence.save(preview, new AsyncCallback<Preview>() {
            @Override
            public void handleResponse(Preview response) {
                if(viewIsValid(previewView)){
                    previewView.hideLoading();
                    previewView.bindPreviewContentOnView(response);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if(viewIsValid(previewView)){
                    previewView.hideLoading();
                    previewView.showAler(fault.getMessage());
                }
            }
        });


    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
