package com.gnome.tune.tunegnome.services;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaRecorder;
import android.util.Log;

import com.gnome.tune.tunegnome.actions.TuneGnomeActions;
import com.gnome.tune.tunegnome.utils.NoiseLevelNotification;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static com.gnome.tune.tunegnome.actions.TuneGnomeActions.NOISE_PARAM;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class NoiseMeasuringService extends IntentService {

    private static final String LOG_TAG = "NOISE_SERVICE: ";
    private MediaRecorder recorder;
    private short[] buffer;
    private Timer timer;

    public NoiseMeasuringService() {
        super("NoiseMeasuringService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            if (TuneGnomeActions.ACTION_START_MEASURE_NOISE.equals(action)) {
                handleActionMeasureNoise();
            }
        }
    }

    private void handleActionMeasureNoise() {
        setupMediaRecorder();
        startMeasurement();
    }

    private void startMeasurement() {

        try {
            recorder.prepare();
            recorder.start();
        } catch(IllegalStateException | IOException e) {
            e.printStackTrace();
        }

        timer = new Timer(LOG_TAG, true);
        timer.scheduleAtFixedRate(createTimerTaskForMeasurement(), 0, 500);
    }

    private TimerTask createTimerTaskForMeasurement() {
        return new TimerTask() {
            public void run() {

                double currentNoiseLevel = 0;
                double maxAmplitude = 0;


                if (maxAmplitude != 0) {
                    currentNoiseLevel = 20.0 * Math.log10(maxAmplitude / 32767.0) + 90;
                }
                Log.d(LOG_TAG, Double.toString(currentNoiseLevel));

                broadcastNoiseValue(currentNoiseLevel);
                NoiseLevelNotification.createOrUpdate(getApplicationContext(), Double.toString(currentNoiseLevel));
            }
        };
    }

    private void broadcastNoiseValue(double noiseValue) {
        Intent intent = new Intent(getApplicationContext(), VolumeAdjusterService.class);
        intent.setAction(TuneGnomeActions.ACTION_NOISE_VALUE);
        intent.putExtra(NOISE_PARAM, noiseValue);

        getApplicationContext().startService(intent);
        Log.d(LOG_TAG, "noise passed to another service");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        NoiseLevelNotification.cancel(getApplicationContext());

        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        } else {
            throw new IllegalStateException("Cannot destroy, recorder is not initialized");
        }
    }

    private void setupMediaRecorder() {
        MediaRecorder recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile("/dev/null");
    }

}