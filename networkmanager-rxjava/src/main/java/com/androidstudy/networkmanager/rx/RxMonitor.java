package com.androidstudy.networkmanager.rx;

import com.androidstudy.networkmanager.Monitor;
import com.androidstudy.networkmanager.Status;

import io.reactivex.Flowable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by chweya on 18/10/17.
 */

public abstract class RxMonitor implements Monitor {
    protected Subject<Status> subject = BehaviorSubject.<Status>create().toSerialized();

    public abstract Flowable<Status> stream();
}
