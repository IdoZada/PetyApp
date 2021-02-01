package com.example.pety.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.pety.objects.Family;
import com.example.pety.objects.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MySP {
    private static final String TAG = MySP.class.getName();
    private static SharedPreferences prefs;
    public static final String KEY_USER = "KEY_USER";
    public static final String FAMILIES_KEY = "FAMILIES_KEY";
    private static MySP uniqueInstance;
    public static final String PREF_NAME = "my_app";
    private User user;
    private ArrayList<Family> families;

    private MySP(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Throws IllegalStateException if this class is not initialized
     *
     * @return unique SharedPrefsManager instance
     */
    public static MySP getInstance() {
        if (uniqueInstance == null) {
            throw new IllegalStateException(
                    "SharedPrefsManager is not initialized, call initialize(applicationContext) " +
                            "static method first");
        }
        return uniqueInstance;
    }

    /**
     * Initialize this class using application Context,
     * should be called once in the beginning by any application Component
     *
     * @param appContext application context
     */
    public static void initialize(Context appContext) {
        if (appContext == null) {
            throw new NullPointerException("Provided application context is null");
        }
        if (uniqueInstance == null) {
            synchronized (MySP.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new MySP(appContext);
                }
            }
        }
    }

    private SharedPreferences getPrefs() {
        return prefs;
    }


    /**
     * This function write data to storage
     *
     * @param
     */
    public void writeDataToStorage(User user) {
        SharedPreferences.Editor editor = prefs.edit();
        String json = createJSONStringFromObject(user);
        editor.putString(MySP.KEY_USER, json);
        editor.apply();
    }

    /**
     * This function read data from storage
     *
     * @return ArrayList of WinnerPlayer objects
     */
    public User readDataFromStorage() {
        Gson gson = new Gson();
        String json = prefs.getString(MySP.KEY_USER, null);
        Type type = new TypeToken<User>() {
        }.getType();
        if ((user = gson.fromJson(json, type)) == null)
            user = new User();
        return user;
    }


    private static String createJSONStringFromObject(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }


    public void writeFamiliesToStorage(ArrayList<Family> families){
        SharedPreferences.Editor editor = prefs.edit();
        String json = createJSONStringFromObject(families);
        editor.putString(MySP.FAMILIES_KEY, json);
        editor.commit();
    }

    public ArrayList<Family> readFamiliesFromStorage(){
        Gson gson = new Gson();
        String json = prefs.getString(MySP.FAMILIES_KEY, null);
        Type type = new TypeToken<ArrayList<Family>>() {
        }.getType();
        if ((families = gson.fromJson(json, type)) == null)
            families = new ArrayList<>();
        return families;
    }

    public SharedPreferences.Editor getEditor() {
        return getPrefs().edit();
    }


    /**
     * This function clear all memory from shared preference
     */
    public void clearSharedPreferences() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * This function add String to shared preference
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference
     */
    public void putString(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * This function read String from shared preference
     *
     * @param key The name of the preference to retrieve.
     * @param def Value to return if this preference does not exist.
     * @return preference value if it exists, or defValue.
     */
    public String getString(String key, String def) {
        return prefs.getString(key, def);
    }

    /**
     * This function add Integer to shared preference
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference
     */
    public void putInt(String key, Integer value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * This function read Integer from shared preference
     *
     * @param key The name of the preference to retrieve.
     * @param def Value to return if this preference does not exist.
     * @return preference value if it exists, or defValue.
     */
    public Integer getInt(String key, Integer def) {
        return prefs.getInt(key, def);
    }

    /**
     * This function add Float to shared preference
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference
     */
    public void putFloat(String key, Float value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    /**
     * This function read Float from shared preference
     *
     * @param key The name of the preference to retrieve.
     * @param def Value to return if this preference does not exist.
     * @return preference value if it exists, or defValue.
     */
    public Float getFloat(String key, Float def) {
        return prefs.getFloat(key, def);
    }

    /**
     * This function remove the name from the shared preference
     *
     * @param key The name of the preference to remove.
     */
    public void removeKey(String key) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key).apply();
    }
}