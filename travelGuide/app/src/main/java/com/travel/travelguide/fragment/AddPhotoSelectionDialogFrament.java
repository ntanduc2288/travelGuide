package com.travel.travelguide.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.travel.travelguide.R;

/**
 * Created by user on 5/5/16.
 */
public class AddPhotoSelectionDialogFrament extends DialogFragment implements View.OnClickListener{

    public interface IAddPhotoSelelectionListener {
        public void onPressChooosePhoto();
        public void onPressTakePhoto();
    }

    private View mRootView;
    private Button btnTakePhoto;
    private Button btnChoosePhoto;
    private Dialog mDialog;
    private IAddPhotoSelelectionListener mListenter;

    public static AddPhotoSelectionDialogFrament newInstance() {
        AddPhotoSelectionDialogFrament fragment = new AddPhotoSelectionDialogFrament();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_FRAME, R.style.Dialog_No_Border);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_add_photo_selection, container, false);

        btnTakePhoto = (Button) mRootView.findViewById(R.id.btn_take_photo);
        btnChoosePhoto = (Button) mRootView.findViewById(R.id.btn_choose_photo);

        btnTakePhoto.setOnClickListener(this);
        btnChoosePhoto.setOnClickListener(this);

        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM|Gravity.LEFT);

        return mRootView;
    }

    public void setOnAddPhotoSelectionListener(IAddPhotoSelelectionListener listener) {
        mListenter = listener;
    }



    @Override
    public void onClick(View v) {
        this.dismiss();
        if (mListenter == null)
            return;

        switch (v.getId()) {
            case R.id.btn_take_photo:
                mListenter.onPressTakePhoto();
                break;
            case R.id.btn_choose_photo:
                mListenter.onPressChooosePhoto();;
                break;

            default:
                break;
        }
    }
}
