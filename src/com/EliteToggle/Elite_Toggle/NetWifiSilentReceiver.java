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
                sendSilentBroadCast();
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                sendSilentBroadCast();
                break;
            case AudioManager.RINGER_MODE_SILENT:
                sendSilentBroadCast();
                break;
        }
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if( mobile.isAvailable()){
            sendNetBroadcast();
        }else if (wifi.isAvailable() ) {
            // Do something
            sendWifiBroadCast();
        }else{
            sendNetBroadcast();
            sendWifiBroadCast();
        }
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if ( !isEnabled) {
            sendBlutoothBroadcast();
        }
        else if(isEnabled) {
            sendBlutoothBroadcast();
        }
    }
    public void sendSilentBroadCast(){
        Intent netWifiSilentIntent1 = new Intent(ToggleConstants.SILENT_BROADCAST);
        Log.i("NetWifiSilentReceiver", "broadcast intiated");
        context.sendBroadcast(netWifiSilentIntent1);
    }
    public void sendWifiBroadCast(){
        Intent netWifiSilentIntent1 = new Intent(ToggleConstants.WIFI_BROADCAST);
        Log.i("NetWifiSilentReceiver", " wifi broadcast intiated");
        context.sendBroadcast(netWifiSilentIntent1);
    }
    public void sendBlutoothBroadcast(){
        Intent netWifiSilentIntent1 = new Intent(ToggleConstants.BLUETOOTH_BROADCAST);
        Log.i("NetWifiSilentReceiver", " bluetooth broadcast intiated");
        context.sendBroadcast(netWifiSilentIntent1);
    }
    public void sendNetBroadcast(){
        Intent netWifiSilentIntent1 = new Intent(ToggleConstants.NET_BROADCAST);
        Log.i("NetWifiSilentReceiver", " NET broadcast intiated");
        context.sendBroadcast(netWifiSilentIntent1);
    }
}
