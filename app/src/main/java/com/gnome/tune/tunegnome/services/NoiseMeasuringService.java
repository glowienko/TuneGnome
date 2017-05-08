package com.gnome.tune.tunegnome.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
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
    private static final String RECORDER_OUTPUT_FILE = "/dev/null";
    private static final String DATA_INTENT_FIELD_NAME = "data";
    private static final String LOG_TAG = "NOISE_MEASURE_SERVICE";
    private boolean continueMeasurement = false;
    public MediaRecorder recorder;


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
                continueMeasurement = true;
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

      //  while(continueMeasurement) {
            double currentNoiseLevel = getAveragedNoiseValue();
            broadcastNoiseValue(currentNoiseLevel);
            NoiseLevelNotification.createOrUpdate(getApplicationContext(), Double.toString(currentNoiseLevel));
          // checkMeasurementStatusChange();

        stopMeasurement();
      //  }
    }



    //=======================

    private void broadcastNoiseValue(double noiseValue) {
        Intent intent = new Intent();
        intent.setAction(TuneGnomeActions.ACTION_BROADCAST_NOISE_VALUE);
        intent.putExtra(DATA_INTENT_FIELD_NAME, noiseValue);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        Log.d(LOG_TAG, "Current noise level broadcasted successfully");
    }





    //============================================

    private double getNoiseLevelDecibels() {
        double maxAmplitude = recorder.getMaxAmplitude();
        if(maxAmplitude != 0) return (20 * Math.log10(maxAmplitude / AMPLITUDE_REFERENCE));
        else return 0.00;
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
        Log.d(LOG_TAG, "current noise= "+ currentAveragedNoise/5);
        return currentAveragedNoise/5;
    }



    public void setupMediaRecorder() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);//MediaRecorder.AudioSource.MIC
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);//MediaRecorder.OutputFormat.THREE_GPP
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//MediaRecorder.AudioEncoder.AMR_NB
        recorder.setOutputFile(RECORDER_OUTPUT_FILE);

        try {
            recorder.prepare();//titaj jest udefined nie wiem dlaczego :(   i przez to się wywala, no i nie da się pomairu odpalić
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorder.start();
        Log.d(LOG_TAG, "MediaRecorder setup success");
    }

    public void stopMeasurement() {
        if(recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
            Log.d(LOG_TAG, "Sound measurement stop success");
        }
    }

}