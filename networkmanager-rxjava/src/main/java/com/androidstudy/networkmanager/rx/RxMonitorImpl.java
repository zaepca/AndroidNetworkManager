package com.androidstudy.networkmanager.rx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.androidstudy.networkmanager.NetworkUtil;
import com.androidstudy.networkmanager.Status;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

/**
 * Created by chweya on 10/10/17.
 */

public class RxMonitorImpl extends RxMonitor {
    private final Context context;

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

    public RxMonitorImpl(Context context, int connectionType) {
        this.context = context;
        this.connectionType = connectionType;
    }

    public RxMonitorImpl(Context context) {
        this(context, -1);
    }

    @Override
    public Flowable<Status> stream() {
        return subject.toFlowable(BackpressureStrategy.LATEST);
    }

    private void emitEvent() {
        subject.onNext(new Status(isConnected, connectionType, isConnected && NetworkUtil.isConnectionFast(context)));
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
        register();
    }

    @Override
    public void onStop() {
        unregister();
    }
}
