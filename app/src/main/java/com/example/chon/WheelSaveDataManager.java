package com.example.chon;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class WheelSaveDataManager extends AppCompatActivity {

    // Strings for saving and loading from preferences
    private final static String WHEEL_CURRENT = "wheelCurrentPref";
    private final static String WHEEL_CURRENT_KEY = "Wheel_Current_Pref";
    private final static String WHEEL_LIST = "wheelListPref";
    private final static String WHEEL_LIST_KEY = "Wheel_List_Pref";

    /**
     * Load the last selected WheelData object. Intended for MainActivity (load selected info).
     *
     * @return
     */
    public WheelData LoadCurrentWheel () {
        // Create shared preferences
        Context ctx = getApplicationContext();
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(WHEEL_CURRENT, MODE_PRIVATE);

        // Get saved string
        String currentWheelJSON = sharedPreferences.getString(WHEEL_CURRENT_KEY, "");

        // Create GSON object, and turn back into WheelData object
        Gson gson = new Gson();
        WheelData wd = gson.fromJson(currentWheelJSON, WheelData.class);

        return wd;
    }

    /**
     * Save WheelData as last selected WheelData object. Intended for WheelMenu (set WheelData to load).
     *
     * @param wd
     */
    public void SaveCurrentWheel(WheelData wd) {
        Gson gson = new Gson();

        // Get Gson string
        String currentWheelJSON = gson.toJson(wd);

        // Create shared preferences
        Context ctx = getApplicationContext();
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(WHEEL_CURRENT, MODE_PRIVATE);

        // Put json string into shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(WHEEL_CURRENT_KEY, currentWheelJSON);
        editor.commit();
    }

    /**
     * Load WheelData list. Intended for WheelMenu (display list) and WheelEditor (add to list).
     *
     * @return
     */
    public LinkedHashMap<String, WheelData> LoadWheelList() {
        // Create shared preferences
        Context ctx = getApplicationContext();
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(WHEEL_LIST, MODE_PRIVATE);

        // Get saved string
        String wheelListJSON = sharedPreferences.getString(WHEEL_LIST_KEY, "");

        // Create GSON object, and turn back into WheelData object
        Gson gson = new Gson();
        LinkedHashMap<String, WheelData> wdList = gson.fromJson(wheelListJSON, LinkedHashMap.class);

        // For each of the wdList elements, load in vital child object info and UI elements
        for (WheelData wd : wdList.values()) {
            // TODO
        }

        return wdList;
    }

    /**
     * Save WheelData list. Intended for WheelMenu (delete) and WheelEditor (save)
     *
     * @param wdList
     */
    public void SaveWheelList(LinkedHashSet<WheelData> wdList) {
        Gson gson = new Gson();

        // Get Gson string
        String currentWheelJSON = gson.toJson(wdList);

        // Create shared preferences
        Context ctx = getApplicationContext();
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(WHEEL_LIST, MODE_PRIVATE);

        // Put json string into shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(WHEEL_LIST_KEY, currentWheelJSON);
        editor.commit();
    }

}
