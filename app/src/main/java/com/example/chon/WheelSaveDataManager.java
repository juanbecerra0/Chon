package com.example.chon;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class WheelSaveDataManager extends AppCompatActivity {

    // Strings for saving and loading from preferences
    private final static String WHEEL_CURRENT = "wheelCurrentPref";
    private final static String WHEEL_CURRENT_KEY = "Wheel_Current_Pref";

    private final static String WHEEL_LIST = "wheelListPref";
    private final static String WHEEL_LIST_KEY = "Wheel_List_Pref";

    private final static String WHEEL_EDIT = "wheelEditPref";
    private final static String WHEEL_EDIT_KEY = "Wheel_Edit_Pref";

    // Context
    private Context ctx;

    /**
     * Constructor for save data manager, requires current context
     *
     * @param ctx
     */
    public WheelSaveDataManager(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * Load the last selected WheelData object. Intended for MainActivity (load selected info).
     *
     * @return
     */
    public WheelData LoadCurrentWheel () {
        // Create shared preferences
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
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(WHEEL_LIST, MODE_PRIVATE);

        // Get saved string
        String wheelListJSON = sharedPreferences.getString(WHEEL_LIST_KEY, "");

        // Create GSON object, and turn back into WheelData object
        Gson gson = new Gson();
        Type type = new TypeToken<LinkedHashMap<String, WheelData>>(){}.getType();
        LinkedHashMap<String, WheelData> wdList = gson.fromJson(wheelListJSON, type);

        if (wdList == null) {
            Log.v("SaveManager", "Loaded null list, making new empty list");
            wdList = new LinkedHashMap<String, WheelData>();
        }

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
    public void SaveWheelList(LinkedHashMap<String, WheelData> wdList) {
        Gson gson = new Gson();

        // Get Gson string
        String currentWheelJSON = gson.toJson(wdList);

        // Create shared preferences
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(WHEEL_LIST, MODE_PRIVATE);

        // Put json string into shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(WHEEL_LIST_KEY, currentWheelJSON);
        editor.commit();
    }

    public WheelData LoadWheelToEdit() {
        // Create shared preferences
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(WHEEL_EDIT, MODE_PRIVATE);

        // Get saved string
        String currentWheelJSON = sharedPreferences.getString(WHEEL_EDIT_KEY, "");

        // Create GSON object, and turn back into WheelData object
        Gson gson = new Gson();
        WheelData wd = gson.fromJson(currentWheelJSON, WheelData.class);

        // Clear this disk space
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(WHEEL_EDIT_KEY, "");
        editor.commit();

        return wd;
    }

    public void SaveWheelToEdit(WheelData wd) {
        Gson gson = new Gson();

        // Get Gson string
        String currentWheelJSON = gson.toJson(wd);

        // Create shared preferences
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(WHEEL_EDIT, MODE_PRIVATE);

        // Put json string into shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(WHEEL_EDIT_KEY, currentWheelJSON);
        editor.commit();
    }

    public boolean AddToWheelList(WheelData wd) {
        LinkedHashMap<String, WheelData> wheelList = LoadWheelList();
        if (wheelList.containsKey(wd.getWheelName())) {
            return false;
        } else {
            wheelList.put(wd.getWheelName(), wd);
            SaveWheelList(wheelList);
            return true;
        }
    }

    public boolean RemoveFromWheelList(String oldName) {
        LinkedHashMap<String, WheelData> wheelList = LoadWheelList();
        if (!wheelList.containsKey(oldName)) {
            return false;
        } else {
            wheelList.remove(oldName);
            SaveWheelList(wheelList);
            return true;
        }
    }

    public Set<String> getSavedWheelNames() {
        return LoadWheelList().keySet();
    }

}
