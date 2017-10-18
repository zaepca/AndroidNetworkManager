package com.androidstudy.networkmanager.livedata;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.androidstudy.networkmanager.NetworkUtil;
import com.androidstudy.networkmanager.Status;

/**
 * Created by chweya on 18/10/17.
 */

public class LiveDataMonitorImpl extends LiveDataMonitor {
    private final Context context;
    private final ConnectivityListener listener;
    private final int connectionType;

    private boolean isConnected;
    private boolean isRegistered;

    private final BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            boolean wasConnected = isConnected;
            isConnected = NetworkUtil.isConnected(context, connectionType);
            if (wasConnected != isConnected) {
                emitEvent();
            }
        }
    };

    public LiveDataMonitorImpl(Context context, int connectionType, ConnectivityListener listener) {
        this.context = context;
        this.listener = listener;
        this.connectionType = connectionType;
        statusLiveData = new MutableLiveData<>();
    }

    public LiveDataMonitorImpl(Context context, ConnectivityListener listener) {
        this(context, -1, listener);
    }

    private void emitEvent() {
        statusLiveData.setValue(new Status(isConnected, connectionType, isConnected && NetworkUtil.isConnectionFast(context)));
    }

    private void register() {
        if (isRegistered) {
            return;
        }

        isConnected = NetworkUtil.isConnected(context, connectionType);
        //emit last event
        emitEvent();
        context.registerReceiver(
                connectivityReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        isRegistered = true;
    }

    private void unregister() {
        if (!isRegistered) {
            return;
        }

        context.unregisterReceiver(connectivityReceiver);
        isRegistered = false;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public LiveData<Status> liveStatus() {
        return statusLiveData;
    }
}
