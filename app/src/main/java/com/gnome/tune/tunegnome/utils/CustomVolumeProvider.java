package com.gnome.tune.tunegnome.utils;

import android.media.VolumeProvider;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * This class should implement VolumeProvider abstract class
 * todo: make it work in activity, or change way of initializing VolumeProvider
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CustomVolumeProvider extends VolumeProvider {
    public CustomVolumeProvider(int volumeControl, int maxVolume, int currentVolume) {
        super(volumeControl, maxVolume, currentVolume);
    }

    @Override
    public void onSetVolumeTo(int volume) {
        super.onSetVolumeTo(volume);
    }


}
