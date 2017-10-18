package com.androidstudy.networkmanager;

import android.content.Context;
import android.support.annotation.Nullable;

import com.androidstudy.networkmanager.internal.DefaultMonitorFactory;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chweya on 29/08/17.
 */

public class Tovuti {
    private static final String TAG = "Tovuti";
    private static final Object lock = new Object();

    private static volatile Tovuti tovuti;
    private MonitorFactory factory;
    private WeakReference<Context> contextRef;
    private Set<Monitor> monitors;

    private Tovuti(Context context, @Nullable MonitorFactory factory) {
        this.factory = factory;
        this.contextRef = new WeakReference<>(context);

        monitors = new HashSet<>();

        if (this.factory == null) this.factory = new DefaultMonitorFactory();
    }

    private Tovuti(Context context) {
        this(context, null);
    }

    public static Builder builder(Context context) {
        return new Builder(context);
    }

    public static Tovuti from(Context context) {
        if (tovuti == null) {
            synchronized (lock) {
                if (tovuti == null) {
                    tovuti = new Tovuti(context);
                }
            }
        }
        return tovuti;
    }

    public Tovuti monitor(int connectionType, Monitor.ConnectivityListener listener) {
        Context context = contextRef.get();
        if (context != null)
            monitors.add(factory.create(context, connectionType, listener));

        start();
        return tovuti;
    }

    public Tovuti monitor(Monitor.ConnectivityListener listener) {
        return monitor(-1, listener);
    }

    public void start() {
        for (Monitor monitor : monitors) {
            monitor.onStart();
        }
    }

    public void stop() {
        for (Monitor monitor : monitors) {
            monitor.onStop();
        }
    }

    public static class Builder {
        private MonitorFactory factory;
        private Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder factory(MonitorFactory factory) {
            this.factory = factory;
            return this;
        }

        public Tovuti build() {
            return new Tovuti(context, factory);
        }
    }
}
