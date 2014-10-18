package com.EliteToggle.Elite_Toggle;

import Util.ToggleConstants;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by LOKESH on 07-09-2014.
 */
public class BlueetoothService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("bluetooth service", "service started");
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if ( !isEnabled) {
            bluetoothAdapter.enable();
        }
        else if(isEnabled) {
            bluetoothAdapter.disable();
        }
        Intent netWifiSilentIntent1 = new Intent(ToggleConstants.BLUETOOTH_BROADCAST);
        getApplicationContext().sendBroadcast(netWifiSilentIntent1);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
