package com.androidstudy.networkmanager.livedata;

import android.arch.lifecycle.LiveData;

import com.androidstudy.networkmanager.Status;

/**
 * Created by chweya on 18/10/17.
 */

public class LiveDataNoopMonitor extends LiveDataMonitor {
    @Override
    public void onStart() {
        // do nothing
    }

    @Override
    public void onStop() {
        // do nothing
    }

    @Override
    public LiveData<Status> liveStatus() {
        return statusLiveData;
    }
}
