package com.androidstudy.networkmanager.rx;

/**
 * Created by chweya on 18/10/17.
 */

public class RxNoopMonitor extends RxMonitor {
    @Override
    public void onStart() {
        //no-op
    }

    @Override
    public void onStop() {
        //no-op
    }
}