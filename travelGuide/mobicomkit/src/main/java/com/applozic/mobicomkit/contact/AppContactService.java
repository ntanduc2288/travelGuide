package com.applozic.mobicomkit.contact;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.applozic.mobicomkit.api.MobiComKitClientService;
import com.applozic.mobicomkit.api.attachment.FileClientService;
import com.applozic.mobicomkit.broadcast.BroadcastService;
import com.applozic.mobicomkit.contact.database.ContactDatabase;

import com.applozic.mobicommons.commons.image.ImageUtils;
import com.applozic.mobicommons.people.contact.Contact;

import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.List;

/**
 * Created by adarsh on 7/7/15.
 */
public class AppContactService implements BaseContactService {

    private static final String TAG = "AppContactService";
    ContactDatabase contactDatabase;
    Context context;

    public AppContactService(Context context) {
        this.context = context;
        this.contactDatabase = new ContactDatabase(context);
    }

    @Override
    public void add(Contact contact) {
        contactDatabase.addContact(contact);
    }

    @Override
    public void addAll(List<Contact> contactList) {
        for (Contact contact: contactList) {
            upsert(contact);
        }
    }

    @Override
    public void deleteContact(Contact contact) {
        contactDatabase.deleteContact(contact);
    }

    @Override
    public void deleteContactById(String contactId) {
        contactDatabase.deleteContactById(contactId);
    }

    @Override
    public List<Contact> getAll() {
        return contactDatabase.getAllContact();
    }

    @Override
    public Contact getContactById(String contactId) {
        Contact contact = contactDatabase.getContactById(contactId);
        if (contact != null) {
            contact.processContactNumbers(context);
        } else {
            contact = new Contact(context, contactId);
            add(contact);
        }
        return contact;
    }

    @Override
    public void updateContact(Contact contact) {
        contactDatabase.updateContact(contact);
    }

    @Override
    public void upsert(Contact contact) {
        if (contactDatabase.getContactById(contact.getUserId()) == null) {
            contactDatabase.addContact(contact);
        } else {
            contactDatabase.updateContact(contact);
        }

    }

    @Override
    public List<Contact> getAllContactListExcludingLoggedInUser() {
        return contactDatabase.getAllContactListExcludingLoggedInUser();
    }

    @Override
    public Bitmap downloadContactImage(Context context, Contact contact) {
        try {
            if (TextUtils.isEmpty(contact.getImageURL()) && TextUtils.isEmpty(contact.getLocalImageUrl())) {
                return null;
            }
            Bitmap attachedImage = null;
            String contactImageURL = contact.getImageURL();
            String contentType = "image";
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            String imageLocalPath = contact.getLocalImageUrl();
            if (imageLocalPath != null) {
                try {
                    attachedImage = BitmapFactory.decodeFile(imageLocalPath);
                } catch (Exception ex) {
                    Log.e(TAG, "File not found on local storage: " + ex.getMessage());
                }
            }
            if (attachedImage == null) {
                HttpURLConnection connection = new MobiComKitClientService(context).openHttpConnection(contactImageURL);
                if (connection.getResponseCode() == 200) {
                    attachedImage = BitmapFactory.decodeStream(connection.getInputStream());
                    if(attachedImage != null){
                        imageLocalPath = new FileClientService(context).saveImageToInternalStorage(attachedImage, contact.getUserId(), context, "image");
                        contact.setLocalImageUrl(imageLocalPath);
                        updateContact(contact);
                    }
                } else {
                    Log.w(TAG, "Download is failed response code is ...." + connection.getResponseCode());
                }
            }
            // Calculate inSampleSize
            options.inSampleSize = ImageUtils.calculateInSampleSize(options, 100, 50);
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            attachedImage = BitmapFactory.decodeFile(imageLocalPath, options);
            return attachedImage;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            Log.e(TAG, "File not found on server: " + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "Exception fetching file from server: " + ex.getMessage());
        }

        return null;
    }


    public Contact getContactReceiver(List<String> items, List<String> userIds) {
        if (userIds != null && !userIds.isEmpty()) {
            return getContactById(userIds.get(0));
        } else if (items != null && !items.isEmpty()) {
            return getContactById(items.get(0));
        }

        return null;
    }

    @Override
    public boolean isContactExists(String contactId) {
        return contactDatabase.getContactById(contactId) != null;
    }

    @Override
    public void updateConnectedStatus(String contactId, Date date, boolean connected) {
        Contact contact = getContactById(contactId);
            if(contact != null && contact.isConnected() != connected){
                contactDatabase.updateConnectedOrDisconnectedStatus(contactId, date, connected);
                BroadcastService.sendUpdateLastSeenAtTimeBroadcast(context, BroadcastService.INTENT_ACTIONS.UPDATE_LAST_SEEN_AT_TIME.toString(), contactId);
            }
    }

    @Override
    public void updateUserBlocked(String userId,boolean userBlocked) {
        if(!TextUtils.isEmpty(userId)){
            contactDatabase.updateUserBlockStatus(userId,userBlocked);
            BroadcastService.sendUpdateLastSeenAtTimeBroadcast(context, BroadcastService.INTENT_ACTIONS.UPDATE_LAST_SEEN_AT_TIME.toString(), userId);
        }
    }

    @Override
    public void updateUserBlockedBy(String userId, boolean userBlockedBy) {
        if(!TextUtils.isEmpty(userId)){
            contactDatabase.updateUserBlockByStatus(userId, userBlockedBy);
            BroadcastService.sendUpdateLastSeenAtTimeBroadcast(context, BroadcastService.INTENT_ACTIONS.UPDATE_LAST_SEEN_AT_TIME.toString(), userId);
        }
    }

    @Override
    public boolean isContactPresent(String userId) {
        return contactDatabase.isContactPresent(userId);
    }

}
