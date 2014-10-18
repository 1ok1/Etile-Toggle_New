package com.EliteToggle.Elite_Toggle;

import Util.ResUtil;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    public static Context context;

    ListView colorSelectorList;

    ResUtil resUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        context = getApplicationContext();

        Intent iconServiceIntent= new Intent(context,IconService.class);
        Intent widgetServiceIntent= new Intent(context,widgetService.class);
//        createShortCut();
        colorSelectorList = (ListView) findViewById(R.id.colorSelector);
        resUtil = new ResUtil(context);

        String[] values = new String[] { "Blue", "Violet", "Red",
                "Pink", "Dark Green", "Black" };

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        colorSelectorList.setAdapter(adapter);

        if(!(isMyServiceRunning(IconService.class,context) && isMyServiceRunning(widgetService.class,context))){
            context.startService(iconServiceIntent);
            context.startService(widgetServiceIntent);
        }

        colorSelectorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    resUtil.setIntPref("resource color", R.drawable.round_blue);
                    setLayout();
                }else if(position == 1){
                    resUtil.setIntPref("resource color", R.drawable.round_violet);
                    setLayout();
                }else if(position == 2){
                    resUtil.setIntPref("resource color", R.drawable.round_red);
                    setLayout();
                }else if(position == 3){
                    resUtil.setIntPref("resource color", R.drawable.round_pink);
                    setLayout();
                }else if(position == 4){
                    resUtil.setIntPref("resource color", R.drawable.round_dark_green);
                    setLayout();
                }else if(position == 5){
                    resUtil.setIntPref("resource color", R.drawable.round_black);
                    setLayout();
                }
                finish();
            }

        });
    }
    private boolean isMyServiceRunning(Class<?> serviceClass,Context context) {
        ActivityManager manager = (ActivityManager)context. getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service already","running");
                return true;
            }
        }
        Log.i("Service not","running");
        return false;
    }
    private void setLayout(){
        widgetService.menuOuterLayout.setBackgroundResource(resUtil.getIntPref("resource color"));
        IconService.icon.setBackgroundResource(resUtil.getIntPref("resource color"));
    }
    private void createShortCut(){

        Intent shortcutIntent = new Intent();
        ComponentName name = new ComponentName(getPackageName(), ".MyActivity");
        shortcutIntent.setClassName("ELITE_TOGGLE", "Elite Toggle");
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        shortcutIntent.setComponent(name);
        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Elite Toggle");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context, R.drawable.ic_launcher));
        addIntent.putExtra("duplicate", false);
        addIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        context.sendBroadcast(addIntent);
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(addIntent);
    }
}
