package com.EliteToggle.Elite_Toggle;

import Util.ToggleConstants;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by LOKESH on 07-09-2014.
 */
public class WifiService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("wifi service", "service started");
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        boolean isWifiEnabled = wifiManager.isWifiEnabled();
        if (isWifiEnabled){
            wifiManager.setWifiEnabled(false);
        } else {
            wifiManager.setWifiEnabled(true);
        }
        Intent netWifiSilentIntent1 = new Intent(ToggleConstants.NET_WIFI_SILENT);
        Log.i("NetWifiSilentReceiver", "broadcast intiated");
        getApplicationContext().sendBroadcast(netWifiSilentIntent1);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
