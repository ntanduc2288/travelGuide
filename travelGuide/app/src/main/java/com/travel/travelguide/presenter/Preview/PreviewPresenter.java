package com.travel.travelguide.presenter.Preview;

import com.travel.travelguide.Object.Preview;
import com.travel.travelguide.presenter.BasePresenters;

import java.util.ArrayList;

/**
 * Created by user on 6/15/16.
 */
public interface PreviewPresenter {
    public interface PreviewView{
        public void showLoading();
        public void hideLoading();
        public void showProgressbar();
        public void hideProgressbar();
        public void bindPreviewContentOnView(Preview preview);
        public void bindPreviewsOnView(ArrayList<Preview> previews);
        public void showAler(String alertMessage);
    }

    public abstract class Presenter extends BasePresenters {
        public abstract void sendPreview(String content);
        public abstract void getPreviewList(String toUserId);
    }

}
