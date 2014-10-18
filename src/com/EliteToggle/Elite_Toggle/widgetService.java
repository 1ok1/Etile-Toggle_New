package com.EliteToggle.Elite_Toggle;

import Util.ResUtil;
import Util.ToggleConstants;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.*;

import java.lang.reflect.Method;

/**
 * Created by LOKESH on 11-09-2014.
 */
public class widgetService extends Service {
    Context context;
    public static WindowManager windowManager;
    public static WindowManager.LayoutParams params;
    public static View view;
    Animation fade_in;
    Camera cam = null;
    public static RelativeLayout menuOuterLayout;
    public static ImageView launcher,home ,
            silent ,
    camera,
    internet,
    torch ,
    wifi,
    bluetooth,
     music ;
    boolean flash =false,VIBRATE = true;
    Camera.Parameters p;
    AudioManager audioManager;
    ConnectivityManager connectivityManager;
    public static RotateAnimation rotateAnimation;
    public static AnimationSet animationSet;
    IconService iconService;
    TextView widgetBatteryLevel;
    BluetoothAdapter bluetoothAdapter;
    private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            WifiManager wifiNetwork = (WifiManager)getSystemService(Context.WIFI_SERVICE);
            Log.i("widget service ","wifi change detacted"+wifiNetwork.isWifiEnabled());
            Log.i("widget service","wifi availability"+wifiNetwork.isWifiEnabled());
            if (wifiNetwork.isWifiEnabled()) {
                wifi.setImageResource(R.drawable.wifi);
                // Do something
            }else {
                wifi.setImageResource(R.drawable.wifi_off);
            }
        }
    };
    private BroadcastReceiver SilentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (audioManager.getRingerMode()){
                case AudioManager.RINGER_MODE_NORMAL:
                    silent.setImageResource(R.drawable.loud);
                    break;
                case AudioManager.RINGER_MODE_VIBRATE:
                    silent.setImageResource(R.drawable.silent);
                    break;
                case AudioManager.RINGER_MODE_SILENT:
                    silent.setImageResource(R.drawable.silent);
                    break;
            }
//            final android.net.NetworkInfo wifiNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//            final android.net.NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        }
    };
    private BroadcastReceiver netReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            connectivityManager  = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo()  ;
            if(activeNetwork != null&& activeNetwork.isAvailable() && activeNetwork.isConnected()) {
                    internet.setImageResource(R.drawable.dataon);
            }else{
                    internet.setImageResource(R.drawable.dataoff);
            }
        }
    };
    private BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            boolean isEnabled = bluetoothAdapter.isEnabled();
            Log.i("widget service ","bluetooth change detacted"+ isEnabled);
            Log.i("widget service","bluetoothAdapter availability"+ isEnabled);
            if ( !isEnabled) {
                bluetooth.setImageResource(R.drawable.bluetooth_off);
            }
            else if(isEnabled) {
                bluetooth.setImageResource(R.drawable.bluetooth_on);
            }
        }
    } ;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        fade_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        context = getApplicationContext();
        registerReceiver(wifiReceiver,new IntentFilter( ToggleConstants.WIFI_BROADCAST));
        registerReceiver(SilentReceiver,new IntentFilter( ToggleConstants.SILENT_BROADCAST));
        registerReceiver(netReceiver,new IntentFilter( ToggleConstants.NET_BROADCAST));
        registerReceiver(bluetoothReceiver,new IntentFilter( ToggleConstants.BLUETOOTH_BROADCAST));
        audioManager= (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING|WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.widget,null);
        widgetBatteryLevel = (TextView) view.findViewById(R.id.widgetBatteryLevel);
        iconService = new IconService();
        menuOuterLayout = (RelativeLayout) view.findViewById(R.id.menuOuterLayout);

        launcher = (ImageView) view.findViewById(R.id.launcher);
        home = (ImageView) view.findViewById(R.id.home);
        silent = (ImageView) view.findViewById(R.id.silent);
        camera = (ImageView) view.findViewById(R.id.camera);
        internet = (ImageView) view.findViewById(R.id.internet);
        torch = (ImageView) view.findViewById(R.id.torch);
        wifi = (ImageView) view.findViewById(R.id.wifi);
        bluetooth = (ImageView) view.findViewById(R.id.bluetooth);
        music = (ImageView) view.findViewById(R.id.music);

        widgetBatteryLevel.setText(String.valueOf(Math.round(getBatteryLevel())));
        params.gravity = Gravity.CENTER;
        windowManager.addView(view, params);
        rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setRepeatCount(0);
        rotateAnimation.setDuration(400);
        animationSet = new AnimationSet(true);
        animationSet.addAnimation(rotateAnimation);
//        animationSet.addAnimation(fade_in);
        Log.i("menu","param"+params);
//        menuOuterLayout.startAnimation(animationSet);

        ResUtil resUtil = new ResUtil(getApplicationContext());
        int i = resUtil.getIntPref("resource color");
        Log.i("MyActivity","pref"+i);
        if(i == 0){
            widgetService.menuOuterLayout.setBackgroundResource(R.drawable.round_blue);
        }else{
            widgetService.menuOuterLayout.setBackgroundResource(resUtil.getIntPref("resource color"));
        }

        view.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (( visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    // TODO: The system bars are visible. Make any desired
                    // adjustments to your UI, such as showing the action bar or
                    // other navigational controls.

                } else {
                    Log.i("widget ","stopped");
//                    menuOuterLayout.setVisibility(View.GONE);
                    /* stopSelf();
                    android.os.Process.killProcess(android.os.Process.myPid());*/

                    // TODO: The system bars are NOT visible. Make any desired
                    // adjustments to your UI, such as hiding the action bar or
                    // other navigational controls.
                }
            }
        });
        launcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMenuVisibility();
            }
        });
        home.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                setMenuVisibility();
                return false;
            }
        });
        silent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (audioManager.getRingerMode()){
                    case AudioManager.RINGER_MODE_NORMAL:
                        silent.setImageResource(R.drawable.loud);
                        VIBRATE = false;
                        break;
                    case AudioManager.RINGER_MODE_VIBRATE:
                        silent.setImageResource(R.drawable.silent);
                        VIBRATE = true;
                        break;
                }
               if(VIBRATE == true){
                   audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                   silent.setImageResource(R.drawable.loud);
                   VIBRATE = false;
               }
                else{
                   VIBRATE = true;
                   audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                   silent.setImageResource(R.drawable.silent);
               }
                setMenuVisibility();
                return false;
            }
        });
        camera.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                setMenuVisibility();
                return false;
            }
        });
        internet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean isMobile = false;
                connectivityManager  = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo()  ;
                Method dataMtd = null;
                try {
                    dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
                    dataMtd.setAccessible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(activeNetwork != null&& activeNetwork.isAvailable() && activeNetwork.isConnected()) {
                    try {
                        internet.setImageResource(R.drawable.dataoff);
                        dataMtd.invoke(connectivityManager, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        internet.setImageResource(R.drawable.dataon);
                        dataMtd.invoke(connectivityManager, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                setMenuVisibility();
                return false;
            }
        });

        torch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                String packageManager = PackageManager.FEATURE_CAMERA_FLASH;
                Log.i("menu","panckageManager"+packageManager);
                Log.i("menu","context"+getApplicationContext());
                if(getApplicationContext().getPackageManager().hasSystemFeature(packageManager)){
                    if(flash == false){
                        try{
                            cam =  Camera.open();
                            p = cam.getParameters();
                            p.setFlashMode( Camera.Parameters.FLASH_MODE_TORCH);
                            torch.setImageResource(R.drawable.bulb_on);
                            cam.setParameters(p);
                            cam.startPreview();
                            flash = true;
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        try{
                            cam.stopPreview();
                            cam.release();
                            flash = false;
                            torch.setImageResource(R.drawable.bulb);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Flash Light Not Available", Toast.LENGTH_SHORT);
                }
                setMenuVisibility();
                return false;
            }
        });

        wifi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(), WifiService.class);
                        Log.i("wifi", "service started");
                        getApplicationContext().startService(i);
                    }
                });
                t.start();
                setMenuVisibility();
                return false;
            }
        });

        bluetooth.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent i= new Intent(getApplicationContext(),BlueetoothService.class);
                        Log.i("bluetooth", "service started");
                        getApplicationContext().startService(i);
                    }
                });
                t.start();
                setMenuVisibility();
                return false;
            }
        });
        music.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_APP_MUSIC);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                setMenuVisibility();
                return false;
            }
        });


        return START_STICKY;
    }
    public void setMenuVisibility(){
        IconService.MENU_VISIBILITY =false;
        menuOuterLayout.setVisibility(View.INVISIBLE);
        IconService.iconLayout.setVisibility(View.VISIBLE);
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING|WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        windowManager.updateViewLayout(view,params);
        Log.i("IconService","visible");
    }
    public void iconServiceCall(){
        Intent i= new Intent(context,IconService.class);
        context.startService(i);
        stopSelf();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    public float getBatteryLevel() {
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // Error checking that probably isn't needed but I added just in case.
        if(level == -1 || scale == -1) {
            return 50.0f;
        }

        return ((float)level / (float)scale) * 100.0f;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(SilentReceiver);
        unregisterReceiver(wifiReceiver);
        unregisterReceiver(netReceiver);
        unregisterReceiver(bluetoothReceiver);
    }
}
