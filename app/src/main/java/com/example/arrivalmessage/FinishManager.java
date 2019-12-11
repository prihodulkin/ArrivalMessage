package com.example.arrivalmessage;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

public class FinishManager {
    private static final Map<Class<? extends Activity>, Activity> list = new HashMap<Class<? extends Activity>, Activity>();

    public static void addActivity(Activity activity) {
        list.put(activity.getClass(), activity);
    }

    public static void finishActivity(Class<? extends Activity> clazz) {
        Activity activity = list.remove(clazz);
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
    }

}