package com.gnome.tune.tunegnome.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import static android.media.AudioManager.*;
import static com.gnome.tune.tunegnome.actions.TuneGnomeActions.ACTION_NOISE_VALUE;
import static com.gnome.tune.tunegnome.actions.TuneGnomeActions.NOISE_PARAM;

public class VolumeAdjusterService extends IntentService {

    private AudioManager audioManager;


    public VolumeAdjusterService() {
        super("VolumeAdjusterService");
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_NOISE_VALUE.equals(action)) {
                handleActionNoiseValue(intent.getStringExtra(NOISE_PARAM));
            }
        }
    }

    private void handleActionNoiseValue(String noiseLevelString) {
        double noiseLevel = Double.valueOf(noiseLevelString);

        //todo: zapis wartości ? albo otrzymanie 10 pomiarów i decyzja?

        //TODO: DECYZJA CO DO ZMIANY GŁOŚNOŚCI NA PODSTAWIE POMIARÓW

    }

    private void callSoundDown() {
        audioManager.setStreamVolume(STREAM_RING, audioManager.getStreamVolume(STREAM_RING) - 1 , 0);

    }

    private void callSoundUp() {
        audioManager.setStreamVolume(STREAM_RING, audioManager.getStreamVolume(STREAM_RING) + 1 , 0);
    }

}
