package com.gnome.tune.tunegnome.services;

import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gnome.tune.tunegnome.utils.NoiseLevelNotification;

import java.io.IOException;

public class AudioRecordTest extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = "somefile";
    private MediaRecorder mRecorder = null;


    public void onRecord(boolean start) {
        if (start) {
            startRecording();


            NoiseLevelNotification.createOrUpdate(getApplicationContext(), Double.toString(mRecorder.getMaxAmplitude()) );
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
    }
}