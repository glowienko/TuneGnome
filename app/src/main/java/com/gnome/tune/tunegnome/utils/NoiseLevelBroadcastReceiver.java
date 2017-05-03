package com.gnome.tune.tunegnome.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


//klasa odbiera broadcast akcji typu : ACTION_BROADCAST_NOISE_VALUE, ustawiony ma taki filtr akcji Intentów
public class NoiseLevelBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}