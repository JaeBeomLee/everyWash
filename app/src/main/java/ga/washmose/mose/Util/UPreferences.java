package ga.washmose.mose.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by C on 15. 9. 2..
 */
public class UPreferences {


    public static void setLongPref(Context context, String pref, String key, long value) {
        SharedPreferences prefs = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static long getLongPref(Context context, String pref, String key, long defValue) {
        SharedPreferences prefs = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
        return prefs.getLong(key, defValue);
    }

    public static void setIntPref(Context context, String pref, String key, int value) {
        SharedPreferences prefs = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getIntPref(Context context, String pref, String key, int defValue) {
        SharedPreferences prefs = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
        return prefs.getInt(key, defValue);
    }

    public static void setBooleanPref(Context context, String pref, String key, boolean value) {
        SharedPreferences prefs = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBooleanPref(Context context, String pref, String key, boolean defValue) {
        SharedPreferences prefs = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, defValue);
    }

    public static void setStringPref(Context context, String pref, String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getStringPref(Context context, String pref, String key, String defValue) {
        SharedPreferences prefs = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
        return prefs.getString(key, defValue);
    }

    public static void setLongArrayPref(Context context, String pref, String key, ArrayList<Long> values) {
        SharedPreferences prefs = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }

    public static ArrayList<Long> getLongArrayPref(Context context, String pref, String key) {
        SharedPreferences prefs = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
        String json = prefs.getString(key, null);
        ArrayList<Long> arr = new ArrayList<>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    long value = a.optLong(i);
                    arr.add(value);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arr;
    }

    public static void setStringArrayPref(Context context, String key, ArrayList<String> values) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.commit();
    }

    public static ArrayList<String> getStringArrayPref(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    public static void removePref(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.commit();
    }
}
