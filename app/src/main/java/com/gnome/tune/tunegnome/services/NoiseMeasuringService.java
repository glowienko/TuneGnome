package com.gnome.tune.tunegnome.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Patryk Głowienko on 2017-05-03.
 */

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class NoiseMeasuringService extends IntentService {


    private static final String ACTION_MEASURE_NOISE = "com.gnome.tune.tunegnome.action.MEASURE_NOISE";
    private static final double AMPLITUDE_REFERENCE = 0.1;
    private double currentNoiseAvg = 0;

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
        intent.setAction(ACTION_MEASURE_NOISE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_MEASURE_NOISE.equals(action)) {
                handleActionMeasureNoise();
            }
        }
    }

    /**
     * Handle action MeasureNoise in the provided background thread with the provided
     * parameters.
     */
    private void handleActionMeasureNoise() {
        // TODO: Handle action MeasureNoise
        throw new UnsupportedOperationException("Not yet implemented");
    }


    //============================================

    private double getNoiseLevelDecibels() {
        return (20 * Math.log10(mediaRecorder.getMaxAmplitude() / AMPLITUDE_REFERENCE));
    }

    //gets current noise level in decibels from last 1s, sampling - each 200 ms
    public double getCurrentNoiseLevelAvg() {
        currentNoiseAvg = 0.0;

        for(int i=0; i<5; i++) {

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return 0;
            }
            currentNoiseAvg += currentNoiseAvg + getNoiseLevelDecibels();
        }
        Log.d("NOISE LEVEL", "noise="+ currentNoiseAvg/5);
        return currentNoiseAvg/5;
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
    }

    public void stopMeasurement() {
        if(mediaRecorder != null) {
            mediaRecorder.stop();
            resetSoundRecorder();
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