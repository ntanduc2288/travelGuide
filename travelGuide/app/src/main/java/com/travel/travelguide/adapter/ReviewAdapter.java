package com.travel.travelguide.adapter;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.travel.travelguide.Object.Preview;
import com.travel.travelguide.R;
import com.travel.travelguide.Ulti.DateHelper;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by user on 5/19/16.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.PreviewViewHolder> {
    ArrayList<Preview> previews;

    public void setPreviews(ArrayList<Preview> previews) {
        this.previews = previews;
        notifyDataSetChanged();
    }

    public void addPreview(Preview preview){
        if(previews == null){
            previews = new ArrayList<>();
        }

        previews.add(0, preview);
        notifyDataSetChanged();
    }

    @Override
    public PreviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_item, parent, false);
        PreviewViewHolder previewViewHolder = new PreviewViewHolder(view);
        return previewViewHolder;
    }

    @Override
    public void onBindViewHolder(PreviewViewHolder holder, int position) {
        Preview preview = previews.get(position);
        if(!TextUtils.isEmpty(preview.getFromUserAvatar())){
            ImageLoader.getInstance().displayImage(preview.getFromUserAvatar(), holder.imgAvatar);
        }

        holder.lblUsername.setText(preview.getFromUserName());
        holder.lblPreviewContent.setText(preview.getMessage());
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTime(preview.getCreated());
        localCalendar = DateHelper.convertUTCtoLocal(localCalendar);
        holder.lblTime.setText(DateHelper.convertCalendarToString(localCalendar, DateHelper.RFC_USA_1));
    }

    @Override
    public int getItemCount() {
        if(previews != null){
            return previews.size();
        }
        return 0;
    }

    public class PreviewViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.imageview_from_user_avatar)
        CircleImageView imgAvatar;
        @Bind(R.id.textview_user_name)
        AppCompatTextView lblUsername;
        @Bind(R.id.textview_preview_content)
        AppCompatTextView lblPreviewContent;
        @Bind(R.id.textview_time)
        AppCompatTextView lblTime;

        public PreviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
