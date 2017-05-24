package com.gnome.tune.tunegnome.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.util.Log;

import com.gnome.tune.tunegnome.actions.TuneGnomeActions;
import com.gnome.tune.tunegnome.utils.NoiseLevelNotification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class NoiseMeasuringService extends IntentService {

    private static final String LOG_TAG = "NOISE_SERVICE: ";
    private MediaRecorder recorder;
    private Timer timer;
    //Lista, w której będzie przechowywanych 10 ostatnich pomiarów głośności
    ArrayList<Double> noiseLevels;
    private AudioManager audioManager;

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
        noiseLevels = new ArrayList<>();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        setupMediaRecorder();
        startMeasurement();
    }

    private void startMeasurement() {
        try {
            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

        timer = new Timer(LOG_TAG, true);
        timer.scheduleAtFixedRate(createTimerTaskForMeasurement(), 0, 500);
    }

    private TimerTask createTimerTaskForMeasurement() {
        return new TimerTask() {
            public void run() {
                double currentNoiseLevel = 20 * Math.log10(Math.abs(recorder.getMaxAmplitude()));
//               double currentNoiseLevel = 60;
                Log.d(LOG_TAG, Double.toString(currentNoiseLevel));

                if(noiseLevels.size() == 10) {
                    proceedingAlgorithm(calculateAverageNoiseLevel(noiseLevels));
                    NoiseLevelNotification.createOrUpdate(getApplicationContext(), Double.toString(currentNoiseLevel));
                    noiseLevels.clear();
                }
                noiseLevels.add(currentNoiseLevel);
            }
        };
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        NoiseLevelNotification.cancel(getApplicationContext());

        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder.reset();
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

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile("/dev/null");
    }

    //Metoda, która zwraca wyliczony średni poziom głośności
    //Algorytm działania:
    //1. Usuwa dwie najmniejsze i dwie największe wartości z listy ostatnich pomiarów
    //2. Wylicza średnią arytmetyczną z pozostałych wartości
    protected Double calculateAverageNoiseLevel(ArrayList<Double> NoiseArray) {
        Double sum = 0.0;
        List<Double> tmpNoiseArray = NoiseArray;
        tmpNoiseArray.remove(tmpNoiseArray.indexOf(Collections.min(tmpNoiseArray)));
        tmpNoiseArray.remove(tmpNoiseArray.indexOf(Collections.min(tmpNoiseArray)));
        tmpNoiseArray.remove(tmpNoiseArray.indexOf(Collections.max(tmpNoiseArray)));
        tmpNoiseArray.remove(tmpNoiseArray.indexOf(Collections.max(tmpNoiseArray)));
        for (int i = 0; i < tmpNoiseArray.size(); i++) {
            Double currentNumber = tmpNoiseArray.get(i).doubleValue();
            sum += currentNumber;
        }
        return sum / tmpNoiseArray.size();
    }


    //Metoda przetwarzająca średni pomiar algorytmu
    protected boolean proceedingAlgorithm(Double averagedNoiseLevel) {
        if (isBetween(averagedNoiseLevel, 1, 35))
        {
            audioManager.setStreamVolume(audioManager.STREAM_RING, 1, 0);
            return true;
        }
        else if (isBetween(averagedNoiseLevel, 35, 43))
        {
            audioManager.setStreamVolume(audioManager.STREAM_RING, 1, 0);
            return true;
        }
        else if (isBetween(averagedNoiseLevel, 43, 47))
        {
            audioManager.setStreamVolume(audioManager.STREAM_RING, 1, 0);
            return true;
        }
        else if (isBetween(averagedNoiseLevel, 43, 47))
        {
            audioManager.setStreamVolume(audioManager.STREAM_RING, 1, 0);
            return true;
        }
        else if (isBetween(averagedNoiseLevel, 43, 47))
        {
            audioManager.setStreamVolume(audioManager.STREAM_RING, 1, 0);
            return true;
        }
        else if (isBetween(averagedNoiseLevel, 47, 48))
        {
            audioManager.setStreamVolume(audioManager.STREAM_RING, 1, 0);
            return true;
        }
        else if (isBetween(averagedNoiseLevel, 83, 90))
        {
            audioManager.setStreamVolume(audioManager.STREAM_RING, 1, 0);
            return true;
        }
        else
            return false;


    }

    public static boolean isBetween(Double x, int floorValue, int cellValue) {
        return floorValue <= x && x <= cellValue;
    }

}