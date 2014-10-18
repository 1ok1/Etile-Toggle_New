package com.EliteToggle.Elite_Toggle;

import android.content.Context;
import android.util.Log;

/**
 * Created by LOKESH on 13-09-2014.
 */
public class ContextUtil {
    Context context;

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
        Log.i("contextUti","------"+this.context+context);

    }
}
