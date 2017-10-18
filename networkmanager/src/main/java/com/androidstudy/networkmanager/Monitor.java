package com.androidstudy.networkmanager;

/**
 * Created by chweya on 29/08/17.
 */

public interface Monitor extends LifecycleListener {

    interface ConnectivityListener {
        void onConnectivityChanged(Status status);
    }
}
