package com.androidstudy.networkmanager.rx;

import com.androidstudy.networkmanager.Status;

import io.reactivex.Flowable;

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

    @Override
    public Flowable<Status> stream() {
        return Flowable.empty();
    }
}