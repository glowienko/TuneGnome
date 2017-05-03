package com.gnome.tune.tunegnome.activities;

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

import java.io.IOException;

import static android.R.attr.level;
/**
 * Created by Patryk Głowienko on 2017-05-03.
 */
public class MainActivity extends AppCompatActivity {

    TextView noiseValueTextView;
    TextView currentSoundLvlTextView;

    VolumeProvider volumeProvider;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noiseValueTextView = (TextView) findViewById(R.id.noiseValueTextView);
        currentSoundLvlTextView = (TextView) findViewById(R.id.currentSoundLvlTextView);
        currentSoundLvlTextView.setText(volumeProvider.getCurrentVolume());
    }



}
