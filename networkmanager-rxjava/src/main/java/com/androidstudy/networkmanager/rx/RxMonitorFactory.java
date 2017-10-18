package com.androidstudy.networkmanager.rx;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.androidstudy.networkmanager.Monitor;
import com.androidstudy.networkmanager.MonitorFactory;

/**
 * Created by chweya on 10/10/17.
 */

public class RxMonitorFactory implements MonitorFactory {
    @NonNull
    @Override
    public RxMonitor create(@NonNull Context context, int connectionType, @NonNull Monitor.ConnectivityListener listener) {
        int permissionResult = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE);
        boolean hasPermission = permissionResult == PackageManager.PERMISSION_GRANTED;

        return hasPermission ? new RxMonitorImpl(context, connectionType)
                : new RxNoopMonitor();
    }
}
