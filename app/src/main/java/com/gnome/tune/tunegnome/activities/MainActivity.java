package com.gnome.tune.tunegnome.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gnome.tune.tunegnome.R;
import com.gnome.tune.tunegnome.actions.TuneGnomeActions;
import com.gnome.tune.tunegnome.services.NoiseMeasuringService;
import com.gnome.tune.tunegnome.storage.ProfilesDatabaseHelper;
import com.gnome.tune.tunegnome.utils.NoiseLevelNotification;

public class MainActivity extends AppCompatActivity {

    private AudioManager audioManager;
    private ProfilesDatabaseHelper dbHelper;


    int maxPhoneVolume;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String permissionName = Manifest.permission.RECORD_AUDIO;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};
    private boolean isProfileActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        isProfileActive = false;
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxPhoneVolume = audioManager.getStreamMaxVolume(audioManager.STREAM_RING);
        dbHelper = new ProfilesDatabaseHelper(this);

        permissionToRecordAccepted = ContextCompat.checkSelfPermission(this, permissionName) == PackageManager.PERMISSION_GRANTED;
    }

    public void startMeasureNoise(View view) {
//        checkIfProfileActive();

        if (permissionToRecordAccepted && !isProfileActive) {
            startMeasureService();
        } else {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }

    public void stopMeasureNoise(View view) {
        Intent serviceIntent = new Intent(this, NoiseMeasuringService.class);
        getApplicationContext().stopService(serviceIntent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionToRecordAccepted = true;
                    startMeasureService();

                } else {
                    NoiseLevelNotification.createOrUpdate(getApplicationContext(), "Cannot measure noise");
                }
            }
        }
    }

    private void startMeasureService() {
        Intent serviceIntent = new Intent(getApplicationContext(), NoiseMeasuringService.class);
        serviceIntent.setAction(TuneGnomeActions.ACTION_START_MEASURE_NOISE);
        getApplicationContext().startService(serviceIntent);
    }

    public void startUserProfileActivity(View view) {
        startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
    }


    //Metoda, która ma za zadanie sprawdzenie, czy aktualny czas jest uwzględniony w profilowaniu
    public void checkIfProfileActive() {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Date());
//        Double hours = (double) cal.get(Calendar.HOUR_OF_DAY);
//
//
//
//        //  float currTime = (float)new Date().getTime();
//        if ((isBetween((hours), beginSleepTime, endSleepTime)))
//            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//        else
//            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }

    public static boolean isBetween(Double x, int floorValue, int cellValue) {
        return floorValue <= x && x <= cellValue;
    }
}
