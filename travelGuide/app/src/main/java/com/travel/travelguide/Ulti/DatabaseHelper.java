package com.travel.travelguide.Ulti;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.travel.travelguide.Object.TestObject;
import com.travel.travelguide.Object.User;
import com.travel.travelguide.R;

import java.sql.SQLException;

/**
 * Created by user on 4/27/16.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "travel_guide_db";
    private static final int DATABASE_VERSION = 1;
    private Dao<User, Long> userDao;
    private Dao<TestObject, Long> testObjectDao;
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

//            TableUtils.clearTable(connectionSource, User.class);
            TableUtils.clearTable(connectionSource, TestObject.class);
        }catch (java.sql.SQLException e){
            e.printStackTrace();
        }

    }


    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public Dao<User, Long> getUser() throws SQLException{
        if(userDao == null){
            TableUtils.createTableIfNotExists(getConnectionSource(), User.class);
            userDao = getDao(User.class);
        }

        return userDao;
    }

    public Dao<TestObject, Long> getTestObject() throws SQLException{
        if(testObjectDao == null){
//            testObjectDao = getDao(TestObject.class);
            TableUtils.createTableIfNotExists(getConnectionSource(), TestObject.class);
            testObjectDao = DaoManager.createDao(getConnectionSource(), TestObject.class);
        }

        return testObjectDao;
    }

}
