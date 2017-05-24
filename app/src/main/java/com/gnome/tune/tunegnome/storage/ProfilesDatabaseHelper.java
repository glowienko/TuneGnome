package com.gnome.tune.tunegnome.storage;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gnome.tune.tunegnome.utils.UserProfile;

import java.util.ArrayList;

public class ProfilesDatabaseHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Profiles.db";
    public static final String TABLE_NAME = "user_profiles";
    public static final String RINGTONE_LEVEL = "ringtone_level";
    public static final String START_TIME_HOURS = "start_hours";
    public static final String START_TIME_MINUTES = "start_minutes";
    public static final String END_TIME_HOURS = "end_hours";
    public static final String END_TIME_MINUTES = "end_minutes";

    public ProfilesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + TABLE_NAME +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT, ringtone_level INTEGER, start_hours INTEGER, start_minutes INTEGER, end_hours INTEGER, end_minutes INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addProfile(UserProfile newUserProfile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(RINGTONE_LEVEL, newUserProfile.getCallRingtoneSoundLevel());
        contentValues.put(START_TIME_HOURS, newUserProfile.getStartTimeHours());
        contentValues.put(START_TIME_MINUTES, newUserProfile.getStartTimeMinutes());
        contentValues.put(END_TIME_HOURS, newUserProfile.getEndTimeHours());
        contentValues.put(END_TIME_MINUTES, newUserProfile.getEndTimeMinutes());
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public Integer deleteProfile(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<UserProfile> getAllProfiles() {
        ArrayList<UserProfile> profiles = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            UserProfile newUserProfile = new UserProfile();

            newUserProfile.setCallRingtoneSoundLevel(res.getInt(res.getColumnIndex(RINGTONE_LEVEL)));
            newUserProfile.setStartTimeHours(res.getInt(res.getColumnIndex(START_TIME_HOURS)));
            newUserProfile.setStartTimeMinutes(res.getInt(res.getColumnIndex(START_TIME_MINUTES)));
            newUserProfile.setEndTimeHours(res.getInt(res.getColumnIndex(END_TIME_HOURS)));
            newUserProfile.setEndTimeMinutes(res.getInt(res.getColumnIndex(END_TIME_MINUTES)));
            newUserProfile.setId(res.getInt(res.getColumnIndex("id")));

            profiles.add(newUserProfile);
            res.moveToNext();
        }
        return profiles;
    }


    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
