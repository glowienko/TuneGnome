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
import android.widget.TextView;

import com.gnome.tune.tunegnome.R;
import com.gnome.tune.tunegnome.actions.TuneGnomeActions;
import com.gnome.tune.tunegnome.services.NoiseMeasuringService;
import com.gnome.tune.tunegnome.utils.NoiseLevelNotification;

import static android.media.AudioManager.STREAM_RING;

public class MainActivity extends AppCompatActivity {

    TextView callSoundLevel;
//    private AudioRecordTest audioRecord;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String permissionName = Manifest.permission.RECORD_AUDIO;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};


    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        permissionToRecordAccepted = ContextCompat.checkSelfPermission(this, permissionName) == PackageManager.PERMISSION_GRANTED;
        callSoundLevel = (TextView) findViewById(R.id.callSoundLevel);

//        audioRecord = new AudioRecordTest();

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        callSoundLevel.setText(Integer.toString(audioManager.getStreamVolume(STREAM_RING)));
    }

    public void startMeasureNoise(View view) {
        if (permissionToRecordAccepted) {
            startMeasureService();
//            audioRecord.onRecord(true);
        } else {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }

    public void stopMeasureNoise(View view) {
        Intent serviceIntent = new Intent(this, NoiseMeasuringService.class);
        getApplicationContext().stopService(serviceIntent);
//        audioRecord.onRecord(false);
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
                return;
            }
        }
    }

    private void startMeasureService() {
        Intent serviceIntent = new Intent(getApplicationContext(), NoiseMeasuringService.class);
        serviceIntent.setAction(TuneGnomeActions.ACTION_START_MEASURE_NOISE);
        getApplicationContext().startService(serviceIntent);
    }
}
