package com.gnome.tune.tunegnome.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.gnome.tune.tunegnome.utils.CustomVolumeProvider;

import static com.gnome.tune.tunegnome.actions.TuneGnomeActions.ACTION_NOISE_VALUE;
import static com.gnome.tune.tunegnome.actions.TuneGnomeActions.NOISE_PARAM;

public class VolumeAdjusterService extends IntentService {


    CustomVolumeProvider volumeProvider;


    public VolumeAdjusterService() {
        super("VolumeAdjusterService");

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_NOISE_VALUE.equals(action)) {
                volumeProvider = new CustomVolumeProvider(1, 10, 1);
                handleActionNoiseValue(intent.getStringExtra(NOISE_PARAM));
            }
        }
    }

    private void handleActionNoiseValue(String noiseLevelString) {
        double noiseLevel = Double.valueOf(noiseLevelString);

        //todo: zapis wartości ? albo otrzymanie 10 pomiarów i decyzja?

        //TODO: DECYZJA CO DO ZMIANY GŁOŚNOŚCI NA PODSTAWIE POMIARÓW

    }

}
