package com.travel.travelguide.Ulti;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.travel.travelguide.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;

/**
 * Created by user on 5/5/16.
 */
public class CropImageUlti {
    public static Bitmap getPic(int targetW, int targetH, String photoPath) {

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        return BitmapFactory.decodeFile(photoPath, bmOptions);
    }

    public static void startCamera(Fragment fragment, String imageFile, int activityResultCode) {
        File photoFile = null;
        try {
            photoFile = new File(imageFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        if (takePictureIntent.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
            fragment.startActivityForResult(takePictureIntent, activityResultCode);
        }
    }

    public static void startCropActivity(Fragment fragment, @NonNull Uri uri, String imageFile) {

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(imageFile)));
        uCrop = uCrop.withAspectRatio(1, 1);
        uCrop = uCrop.withMaxResultSize(700, 700);
        UCrop.Options options = new UCrop.Options();
        options.setHideBottomControls(true);
        uCrop.withOptions(options);

        uCrop.start(fragment.getActivity());
    }

    public static void pickFromGallery(Fragment fragment, int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        fragment.startActivityForResult(Intent.createChooser(intent, fragment.getString(R.string.label_select_picture)), requestCode);
    }
}
