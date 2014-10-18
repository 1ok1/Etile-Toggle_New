package Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class ResUtil {
	Context context;
	public ResUtil(Context context){
		this.context = context.getApplicationContext();
	}
	
	public static SharedPreferences getSharedPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
	public void setIntPref(String key, int value){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(key, value);
		editor.commit();

	}
	public int getIntPref(String key){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        Log.i("resUtil","setting pref"+settings+"    "+settings.getInt(key, 0));
		return settings.getInt(key, 0);
	}
}
