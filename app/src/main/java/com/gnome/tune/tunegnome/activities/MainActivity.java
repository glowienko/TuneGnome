package com.gnome.tune.tunegnome.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gnome.tune.tunegnome.R;
import com.gnome.tune.tunegnome.actions.TuneGnomeActions;
import com.gnome.tune.tunegnome.services.NoiseMeasuringService;
import com.gnome.tune.tunegnome.utils.CustomVolumeProvider;

public class MainActivity extends AppCompatActivity {

    TextView currentSoundLvlTextView;

    CustomVolumeProvider volumeProvider;
    NoiseMeasuringService noiseMeasuringService;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        noiseMeasuringService = new NoiseMeasuringService();

        volumeProvider = new CustomVolumeProvider(1, 10, 1);
        volumeProvider.onSetVolumeTo(2);

        currentSoundLvlTextView = (TextView) findViewById(R.id.currentSoundLevel);
        //currentSoundLvlTextView.setText(volumeProvider.getCurrentVolume());

    }

    public void startMeasureNoise(View view) {
        noiseMeasuringService.startActionMeasureNoise(getApplicationContext());
    }

    public void stopMeasureNoise(View view) {
        broadcastStopMeasurementAction();
    }

    private void broadcastStopMeasurementAction() {
        Intent intent = new Intent();
        intent.setAction(TuneGnomeActions.ACTION_STOP_MEASURE_NOISE);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        Log.d("SEND_STOP_MEASUREMENT", "we want to stop noise measure service");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }


}
