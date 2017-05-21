package com.gnome.tune.tunegnome.services;

import android.app.IntentService;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

import com.gnome.tune.tunegnome.actions.TuneGnomeActions;
import com.gnome.tune.tunegnome.utils.NoiseLevelNotification;

import java.util.Timer;
import java.util.TimerTask;

import static com.gnome.tune.tunegnome.actions.TuneGnomeActions.NOISE_PARAM;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class NoiseMeasuringService extends IntentService {

    private static final String LOG_TAG = "NOISE_SERVICE: ";
    private AudioRecord audioRecorder;
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
        setupAudioRecorder();

        if (audioRecorder != null && audioRecorder.getState() == AudioRecord.STATE_INITIALIZED) {
            startMeasurement();
        }
    }

    private void startMeasurement() {
        audioRecorder.startRecording();

        timer = new Timer(LOG_TAG, true);
        timer.scheduleAtFixedRate(createTimerTaskForMeasurement(), 0, 500);
    }

    private TimerTask createTimerTaskForMeasurement() {
        return new TimerTask() {
            public void run() {
                int readSize = audioRecorder.read(buffer, 0, buffer.length);
                double currentNoiseLevel = 0;
                double maxAmplitude = 0;

                for (int i = 0; i < readSize; i++) {
                    if (Math.abs(buffer[i]) > maxAmplitude) {
                        maxAmplitude = Math.abs(buffer[i]);
                    }
                }

                if (maxAmplitude != 0) {
                    currentNoiseLevel = 20.0 * Math.log10(maxAmplitude / 32767.0) + 90;
                }
                Log.d(LOG_TAG, Double.toString(currentNoiseLevel));

                broadcastNoiseValue(currentNoiseLevel); //todo: powinno być najpierw uśrenienie z okresu 1 s np. czy coś, może :D
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

        buffer = null;
        if (audioRecorder != null) {
            audioRecorder.stop();
            audioRecorder.release();
            audioRecorder = null;
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        } else {
            throw new IllegalStateException("Cannot destroy, audioRecorder is not initialized");
        }
    }

    private void setupAudioRecorder() {
        int rate = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_SYSTEM);
        int bufferSize = AudioRecord.getMinBufferSize(rate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        buffer = new short[bufferSize*2];

        audioRecorder = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                rate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize);
    }

}