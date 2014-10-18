package com.EliteToggle.Elite_Toggle;

import Util.ToggleConstants;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.util.Log;

/**
 * Created by LOKESH on 18-10-2014.
 */
public class NetWifiSilentReceiver extends BroadcastReceiver {
    AudioManager audioManager;
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.i("NetWifiSilentReceiver", "broadcast reciver working");
        audioManager= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        switch (audioManager.getRingerMode()){
            case AudioManager.RINGER_MODE_NORMAL:
                sendNetWifiSilentBroadCast();
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                sendNetWifiSilentBroadCast();
                break;
            case AudioManager.RINGER_MODE_SILENT:
                sendNetWifiSilentBroadCast();
                break;
        }
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable() || mobile.isAvailable()) {
            // Do something
            sendNetWifiSilentBroadCast();
        }else{
            sendNetWifiSilentBroadCast();
        }
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if ( !isEnabled) {
            sendNetWifiSilentBroadCast();
        }
        else if(isEnabled) {
            sendNetWifiSilentBroadCast();
        }
    }
    public void sendNetWifiSilentBroadCast(){
        Intent netWifiSilentIntent1 = new Intent(ToggleConstants.NET_WIFI_SILENT);
        Log.i("NetWifiSilentReceiver", "broadcast intiated");
        context.sendBroadcast(netWifiSilentIntent1);
    }
}
