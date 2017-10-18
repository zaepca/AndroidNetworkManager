package com.androidstudy.networkmanager;

/**
 * Created by chweya on 10/10/17.
 */

public class Status {
    private final boolean isConnected;
    private final int type;
    private final boolean isFast;

    public Status(boolean isConnected, int type, boolean isFast) {
        this.isConnected = isConnected;
        this.type = type;
        this.isFast = isFast;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public int type() {
        return type;
    }

    public boolean isFast() {
        return isFast;
    }
}
