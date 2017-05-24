package com.gnome.tune.tunegnome.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.gnome.tune.tunegnome.R;
import com.gnome.tune.tunegnome.storage.ProfilesDatabaseHelper;
import com.gnome.tune.tunegnome.utils.UserProfile;

public class AddProfileActivity extends AppCompatActivity {

    private NumberPicker ringtoneNoiseLevel;
    private TimePicker startTime;
    private TimePicker endTime;

    private Integer ringtoneLevel;
    private UserProfile newProfile;


    private ProfilesDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_profile);
        dbHelper = new ProfilesDatabaseHelper(this);

        newProfile = new UserProfile();

        ringtoneNoiseLevel = (NumberPicker) findViewById(R.id.ringtoneNoiseLevel);
        ringtoneNoiseLevel.setMinValue(1);
        ringtoneNoiseLevel.setMaxValue(15);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addProfile(View view) {
        newProfile.setEndTimeHours(endTime.getHour());
        newProfile.setEndTimeMinutes(endTime.getMinute());

        dbHelper.addProfile(newProfile);
        startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
    }

    public void goToStartTime(View view) {
        newProfile.setCallRingtoneSoundLevel(ringtoneNoiseLevel.getValue());

        setContentView(R.layout.start_time);
        startTime = (TimePicker) findViewById(R.id.startTime);
        startTime.setIs24HourView(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void goToEndTime(View view) {
        newProfile.setStartTimeHours(startTime.getHour());
        newProfile.setStartTimeMinutes(startTime.getMinute());

        setContentView(R.layout.end_time);
        endTime = (TimePicker) findViewById(R.id.endTime);
        endTime.setIs24HourView(true);
    }


}
