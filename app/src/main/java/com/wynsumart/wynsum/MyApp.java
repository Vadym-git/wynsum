package com.wynsumart.wynsum;

import android.app.Application;

import com.wynsumart.wynsum.models.DBHelper;
import com.wynsumart.wynsum.models.DataBase;

public class MyApp extends Application {
    private DBHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = DBHelper.getInstance(DataBase.getInstance(this).getReadableDatabase());
    }

    public DBHelper getDbHelper() {
        return dbHelper;
    }
}
