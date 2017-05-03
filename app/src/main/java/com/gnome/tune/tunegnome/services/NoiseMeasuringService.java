package com.gnome.tune.tunegnome.services;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.media.MediaRecorder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.gnome.tune.tunegnome.actions.TuneGnomeActions;
import com.gnome.tune.tunegnome.utils.NoiseLevelNotification;

import java.io.IOException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class NoiseMeasuringService extends IntentService {

    private static final double AMPLITUDE_REFERENCE = 0.1;

    MediaRecorder mediaRecorder;


    public NoiseMeasuringService() {
        super("NoiseMeasuringService");
    }

    /**
     * Starts this service to perform action MeasureNoise with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionMeasureNoise(Context context) {
        Intent intent = new Intent(context, NoiseMeasuringService.class);
        intent.setAction(TuneGnomeActions.ACTION_START_MEASURE_NOISE);
        context.startService(intent);
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

    /**
     * Handle action MeasureNoise in the provided background thread with the provided
     * parameters.
     */
    private void handleActionMeasureNoise() {
        setupMediaRecorder();
        double currentNoiseLevel = getAveragedNoiseValue();
        broadcastNoiseValue(currentNoiseLevel);
        NoiseLevelNotification.createOrUpdate(getApplicationContext(), Double.toString(currentNoiseLevel));
    }



    //=======================

    private void broadcastNoiseValue(double noiseValue) {
        Intent intent = new Intent();
        intent.setAction(TuneGnomeActions.ACTION_BROADCAST_NOISE_VALUE);
        intent.putExtra("data", noiseValue);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        Log.d("BROADCAST_NOISE_VALUE", "Current noise level broadcasted successfully");
    }





    //============================================

    private double getNoiseLevelDecibels() {
        return (20 * Math.log10(mediaRecorder.getMaxAmplitude() / AMPLITUDE_REFERENCE));
    }

    //gets current noise level in decibels from last 1s, sampling - each 200 ms
    public double getAveragedNoiseValue() {
        double currentAveragedNoise = 0.0;

        for(int i=0; i<5; i++) {
            currentAveragedNoise += currentAveragedNoise + getNoiseLevelDecibels();

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return 0;
            }
        }
        Log.d("NOISE LEVEL", "noise= "+ currentAveragedNoise/5);
        return currentAveragedNoise/5;
    }



    public void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile("/dev/null");

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("MEDIA_RECORDER_SETUP", "MediaRecorder setup success");
    }

    public void stopMeasurement() {
        if(mediaRecorder != null) {
            mediaRecorder.stop();
            resetSoundRecorder();
            Log.d("STOP_MEASUREMENT", "Sound measurement stop success");
        }
    }

    public void resetSoundRecorder() {
        mediaRecorder.release();
        mediaRecorder = null;
    }




//        /**
//     * Starts this service to perform action MeasureNoise with the given parameters. If
//     * the service is already performing a task this action will be queued.
//     *
//     * @see IntentService
//     */
//    // TODO: Customize helper method
//    public static void startActionMeasureNoise(Context context, String param1, String param2) {
//        Intent intent = new Intent(context, NoiseMeasuringService.class);
//        intent.setAction(ACTION_FOO);
//        intent.putExtra(EXTRA_PARAM1, param1);
//        intent.putExtra(EXTRA_PARAM2, param2);
//        context.startService(intent);
//    }


}