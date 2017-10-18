package com.androidstudy.androidnetworkmanager;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.androidstudy.androidnetworkmanager.databinding.ActivityMainBinding;
import com.androidstudy.networkmanager.Monitor;
import com.androidstudy.networkmanager.MonitorFactory;
import com.androidstudy.networkmanager.Status;
import com.androidstudy.networkmanager.Tovuti;
import com.androidstudy.networkmanager.internal.DefaultMonitor;
import com.androidstudy.networkmanager.internal.NoopMonitor;

public class MainActivity extends AppCompatActivity {
    public static final int RQ_ACCESS_NET_STATE = 101;
    private ActivityMainBinding binding;
    private Tovuti tovuti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_NETWORK_STATE)) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Network State Checking")
                        .setMessage("Allow me to monitor network state changes to modify behavior such as; minimizing data usage on mobile data")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                requestPermission(RQ_ACCESS_NET_STATE, Manifest.permission.ACCESS_NETWORK_STATE);
                            }
                        })
                        .create()
                        .show();
            } else {
                requestPermission(RQ_ACCESS_NET_STATE, Manifest.permission.ACCESS_NETWORK_STATE);
            }
        } else {
            startNetworkMonitoring();
        }
    }

    public void startNetworkMonitoring() {
        // in case we had started monitoring already
        if (tovuti != null) {
            tovuti.stop();
            tovuti = null;
        }
        tovuti = Tovuti.builder(this)
                // use below Tovuti.Builder#factory(factory) setter to customize how the Monitor is created;
                // and/or provide your own implementation of Monitor. See DefaultMonitorFactory and DefaultMonitor
                // classes for implementation examples
                .factory(new MonitorFactory() {
                    @NonNull
                    @Override
                    public Monitor create(@NonNull Context context, int connectionType, @NonNull Monitor.ConnectivityListener listener) {
                        // Check for android N and above if ACCESS_NETWORK_STATE permission is granted
                        // if not return a No-op Monitor that basically does nothing
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_NETWORK_STATE)
                                == PackageManager.PERMISSION_GRANTED) {
                            return new DefaultMonitor(context, connectionType, listener);
                        } else {
                            return new NoopMonitor();
                        }
                    }
                })
                .build();

        // Monitor Wifi state changes
        tovuti.monitor(ConnectivityManager.TYPE_WIFI, new Monitor.ConnectivityListener() {
            @Override
            public void onConnectivityChanged(Status status) {
                binding.connectionWifiConnected.setText(status.isConnected() ? "Connected" : "Disconnected");
                binding.connectionWifiOther.setText(status.isConnected() ? (status.isFast() ? "Fast" : "Slow") : "N/A");
            }
        });
        // Monitor mobile data state changes
        tovuti.monitor(ConnectivityManager.TYPE_MOBILE, new Monitor.ConnectivityListener() {
            @Override
            public void onConnectivityChanged(Status status) {
                binding.connectionMobileConnected.setText(status.isConnected() ? "Connected" : "Disconnected");
                binding.connectionMobileOther.setText(status.isConnected() ? (status.isFast() ? "Fast" : "Slow") : "N/A");
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (tovuti != null) {
            // you can call Tovuti#stop() at any point to stop monitoring
            tovuti.stop();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RQ_ACCESS_NET_STATE) {
            for (int i = 0, length = permissions.length; i < length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_NETWORK_STATE)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        startNetworkMonitoring();
                    }
                    break;
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void requestPermission(int requestCode, String permission) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
    }
}
