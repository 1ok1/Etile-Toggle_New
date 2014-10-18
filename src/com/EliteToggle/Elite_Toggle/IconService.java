package com.EliteToggle.Elite_Toggle;

import Util.ResUtil;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by LOKESH on 11-09-2014.
 */
public class IconService extends Service{
    Context context;
    WindowManager windowManager;
    WindowManager.LayoutParams params,closeLayoutParams;
    View view,closeView;
    TextView batteryLevel;
    int width,height;
    DisplayMetrics displaymetrics;
    GestureDetector gestureDetector;
    Rect rect;
    public static ImageView icon;
    public static RelativeLayout iconLayout,closeLayout;
    int XPosition,YPosition,halfWidth;
    public static boolean MENU_VISIBILITY= false;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getApplicationContext();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING,
                PixelFormat.TRANSLUCENT);
        closeLayoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING,
                PixelFormat.TRANSLUCENT);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.icon,null);
//        closeView = inflater.inflate(R.layout.close_border,null);

        icon = (ImageView) view.findViewById(R.id.iconImage);
        iconLayout = (RelativeLayout) view.findViewById(R.id.iconLayout);
        batteryLevel = (TextView) view.findViewById(R.id.batteryLevel);

//        closeLayout = (RelativeLayout) closeView.findViewById(R.id.closeLayout);
        icon.setAlpha(0.7f);
        batteryLevel.setAlpha(0.8f);

        displaymetrics = new DisplayMetrics();
        gestureDetector = new GestureDetector(context,new SingleTapListener());
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;
        params.gravity = Gravity.LEFT;
        windowManager.addView(view, params);
//        closeLayoutParams.gravity = Gravity.BOTTOM;
//        closeLayoutParams.x = width/10;
//        closeLayoutParams.y = height /4;
//        windowManager.addView(closeView, closeLayoutParams);

        batteryLevel.setText(String.valueOf( Math.round(getBatteryLevel())));


        ResUtil resUtil = new ResUtil(getApplicationContext());
        int i = resUtil.getIntPref("resource color");
        Log.i("MyActivity","pref"+i);
        if(i == 0){
            IconService.icon.setBackgroundResource(R.drawable.round_blue);
        }else{
            IconService.icon.setBackgroundResource(resUtil.getIntPref("resource color"));
        }

        view.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        // Note that system bars will only be "visible" if none of the
                        // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                        if (( visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            // TODO: The system bars are visible. Make any desired
                            // adjustments to your UI, such as showing the action bar or
                            // other navigational controls.

                            iconLayout.setVisibility(View.VISIBLE);
                            Log.i("icon","icon layout status bar visible"+iconLayout.isShown());
                        } else {
                            iconLayout.setVisibility(View.GONE);
                            Log.i("icon","icon layout status bar invisible"+iconLayout.isShown());
                            /*stopSelf();
                            android.os.Process.killProcess(android.os.Process.myPid());*/
                            // TODO: The system bars are NOT visible. Make any desired
                            // adjustments to your UI, such as hiding the action bar or
                            // other navigational controls.
                        }
                    }
                });


        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);


                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    Log.i("iconservice","height"+height);
                    Log.i("iconservice","width"+width);
                    Log.i("iconservice","height/4"+height/4);
                    Log.i("iconservice","width/10"+width/10);
//                    rect = new Rect(width/10+closeView.getLeft(), height /4+closeView.getTop(), width/10+closeView.getRight(), height /4+closeView.getBottom());
//                    Log.i("iconservice","close view left"+(width/10+closeView.getLeft()));
//                    Log.i("iconservice","close view top"+(height /4+closeView.getTop()));
//                    Log.i("iconservice","close view right"+(width/10+closeView.getRight()));
//                    Log.i("iconservice","close view bottom"+(height /4+closeView.getBottom()));
                    icon.setAlpha(1.0f);
                    batteryLevel.setAlpha(1.0f);
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    int tempX,tempY;
                    tempX = (int) (event.getRawX() - (v.getWidth()  *0.25));
                    tempY = (int) (event.getRawY() - (v.getHeight()*5));

                    params.x = tempX ;
                    params.y = tempY;
                    Log.i("icon position","tempx->"+tempX+"tempy->"+tempY);
                    Log.i("icon","contains tempx->"+(v.getLeft() +tempX)+"contains tempy->"+(v.getTop() + tempY));
                    /*if(rect.contains(v.getLeft() +tempX, v.getTop() + tempY)){
                        // User moved inside bounds
                        stopSelf();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
*/
                    windowManager.updateViewLayout(view, params);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    XPosition = (int) (event.getRawX() - (v.getWidth() *2.25));
                    YPosition = (int) (event.getRawY() - v.getHeight()*5);
                    params.y = YPosition;
                    halfWidth = width/2;
                    Log.i("menu", "x" + XPosition);
                    Log.i("menu","y"+YPosition);
                    Log.i("menu","width"+width);
                    Log.i("menu","height"+height);
                    Log.i("menu","half width"+(halfWidth));
                    Log.i("menu","condition"+(XPosition>width/2));
                    if(XPosition>0){
                        params.x = width;
                    }else{
                        params.x = -width;
                    }
                    windowManager.updateViewLayout(view, params);
                    icon.setAlpha(0.7f);
                    batteryLevel.setAlpha(0.8f);
                }

                return true;
            }
        });

        return START_STICKY;
    }
    private class SingleTapListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            stopSelf();
            android.os.Process.killProcess(android.os.Process.myPid());
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if(MENU_VISIBILITY == false){
                iconLayout.setVisibility(View.GONE);
                MENU_VISIBILITY = true;
                widgetService.menuOuterLayout.startAnimation(widgetService.animationSet);
                widgetService.menuOuterLayout.setVisibility(View.VISIBLE);

                Log.i("IconService","invisible");
                widgetService.params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_PHONE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING,
                        PixelFormat.TRANSLUCENT);
                widgetService.windowManager.updateViewLayout(widgetService.view,widgetService.params);
            }
            return false;
        }
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

}
