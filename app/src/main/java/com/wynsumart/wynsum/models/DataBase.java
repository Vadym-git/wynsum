package com.wynsumart.wynsum.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBase extends SQLiteOpenHelper {
    private static DataBase instance;

    public static final String TARGETS_TABLE = "meditation_targets";
    public static final String TARGET_SESSIONS = "target_sessions";
    private static final String DATABASE_NAME = "wynsum.db";
    private static final int DATABASE_VERSION = 1;

    public static synchronized DataBase getInstance(Context context) {
        if (instance == null) {
            instance = new DataBase(context.getApplicationContext());
        }
        return instance;
    }

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the tables in the database
        targets(db);
        targetSessions(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade the database when the version changes
        // Here you could modify the existing tables or create new ones
    }

    private void targets(SQLiteDatabase db){
        // table for targets

        String createTableTargets = "CREATE TABLE " +
                TARGETS_TABLE +
                " (" +
                // start pats you request
                "id INTEGER PRIMARY KEY," +
                "name TEXT, " +
                "description TEXT, " +
                "short_description TEXT, " +
                "guide TEXT, " +
                "icon TEXT" +
                // end of your request
                ")";
        db.execSQL(createTableTargets);
    }

    private void targetSessions(SQLiteDatabase db){
        // table for target's sessions

        String createTableTargetSessions = "CREATE TABLE " +
                TARGET_SESSIONS +
                " (" +
                // start pats you request
                "target_id INTEGER REFERENCES " + TARGETS_TABLE + " (id), " +
                "time_finish INTEGER, " +
                "duration INTEGER" +
                // end of your request
                ")";
        db.execSQL(createTableTargetSessions);
    }
}
