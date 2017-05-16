package com.gnome.tune.tunegnome.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.gnome.tune.tunegnome.R;
import com.gnome.tune.tunegnome.actions.TuneGnomeActions;
import com.gnome.tune.tunegnome.services.NoiseMeasuringService;

public class MainActivity extends AppCompatActivity {

    TextView currentSoundLvlTextView;
    NoiseMeasuringService noiseMeasuringService;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};


//    CustomVolumeProvider volumeProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        if(permissionToRecordAccepted) {
            noiseMeasuringService = new NoiseMeasuringService();
            currentSoundLvlTextView = (TextView) findViewById(R.id.currentSoundLevel);
//
//            volumeProvider = new CustomVolumeProvider(1, 10, 1);
//            volumeProvider.onSetVolumeTo(2);
//            currentSoundLvlTextView.setText(volumeProvider.getCurrentVolume());  todo: make it work
        }
    }

    public void startMeasureNoise() {
        Intent serviceIntent = new Intent(this, NoiseMeasuringService.class);
        serviceIntent.setAction(TuneGnomeActions.ACTION_START_MEASURE_NOISE);
        getApplicationContext().startService(serviceIntent);
    }

    public void stopMeasureNoise() {
        Intent serviceIntent = new Intent(this, NoiseMeasuringService.class);
       getApplicationContext(). stopService(serviceIntent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) finish();

    }
}
