package com.travel.travelguide.manager;

import android.content.Context;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.Ulti.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by user on 4/23/16.
 */
public class UserManager {
    private static UserManager instance;
    User user;
    DatabaseHelper databaseHelper;

    public static UserManager getInstance(){
        if(instance == null){
            synchronized (UserManager.class){
                if(instance == null){
                    instance = new UserManager();
                }
            }
        }

        return instance;
    }

    private DatabaseHelper getDatabaseHelper(Context context){
        if(databaseHelper == null){
            databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    private UserManager(){}

    public User getCurrentUser() {
        return user;
    }

    public User getCurrentUser(Context context) {
        if(user == null){
            try {
                ArrayList<User> users = (ArrayList<User>) getDatabaseHelper(context).getUser().queryForAll();
                if(users != null && users.size() > 1){
                    user = users.get(0);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    public void setCurrentUser(User user) {
        this.user = user;
    }

    public void saveUserToDatabase(Context context){
        try {
//            getDatabaseHelper(context).getTestObject().createOrUpdate(new TestObject("1 id", "Name testing"));
            getDatabaseHelper(context).getUser().create(user);

            if(databaseHelper != null){
                OpenHelperManager.releaseHelper();
                databaseHelper = null;
            }

//            Toast.makeText(context, "Save data successful. ", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(context, "Could not save user data. " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean clearCurrentUserInfo(Context context){
        int result = 0;
        try {
            Dao<User, Long> dao =  getDatabaseHelper(context).getUser();
            DeleteBuilder<User, Long> deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq("id", user.getId());
            result = deleteBuilder.delete();
            if(result > 0){
                user = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result > 0 ? true : false;
    }

    public boolean haveTravelDate(User user){
        if(0 != user.getTravelDateFrom() && 0 != user.getTravelDateTo()){
            return true;
        }

        return false;
    }

}
