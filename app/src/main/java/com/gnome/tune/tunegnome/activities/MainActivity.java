package com.gnome.tune.tunegnome.activities;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.media.VolumeProvider;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gnome.tune.tunegnome.R;
import com.gnome.tune.tunegnome.actions.TuneGnomeActions;
import com.gnome.tune.tunegnome.services.NoiseMeasuringService;

import java.io.IOException;

import static android.R.attr.level;

public class MainActivity extends AppCompatActivity {

    TextView currentSoundLvlTextView;

    VolumeProvider volumeProvider;
    NoiseMeasuringService noiseMeasuringService;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        currentSoundLvlTextView = (TextView) findViewById(R.id.currentSoundLvlTextView);
        currentSoundLvlTextView.setText(volumeProvider.getCurrentVolume());
    }

    public void startMeasureNoise() {
        noiseMeasuringService.startActionMeasureNoise(getApplicationContext());
    }


}
