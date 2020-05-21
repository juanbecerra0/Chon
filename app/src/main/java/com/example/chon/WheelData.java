package com.example.chon;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

public class WheelData {

    // ---------------------------------------------------------------
    // Variables and constructor
    // ---------------------------------------------------------------

    // Name of wheel, and hashmap to access elements of wheel
    private String wheelName;
    private LinkedHashMap<String, WheelDataItem> wheelItems;

    // Keeps track of all items
    private int totalItemCount;
    private int totalItemChance;

    // Keeps track of only dynamic item information for dynamic chance calculations
    private int dynamicCount;
    private int dynamicPortion;

    // Keeps track of max static int
    private int maxStaticChance;

    // UI elements
    private transient WheelDataUIElement uiElement;

    /**
     * Constructor for WheelData, a data structure that keeps track of items and their chances
     *
     * The fromJson tag is set to true if wheelName is the JSON string representation of
     * this object
     *
     * @param wheelName
     */
    WheelData(String wheelName) {
        // Init variables
        this.wheelName = wheelName;
        wheelItems = new LinkedHashMap<String, WheelDataItem>();

        // By default, add two dynamic items at 50/50
        wheelItems.put("NewItem1", new WheelDataItem("NewItem1", 50, this));
        wheelItems.put("NewItem2", new WheelDataItem("NewItem2", 50, this));

        // Set variables based on two inits
        totalItemCount = 2;
        totalItemChance = 100;

        dynamicCount = 2;
        dynamicPortion = 100;
    }

    // ---------------------------------------------------------------
    // Utility
    // ---------------------------------------------------------------

    /**
     * Adds an item to the wheel, which is dynamic by default
     *
     * @param name
     */
    WheelDataItem AddToWheel(String name) {
        // Check if item already exists
        if (wheelItems.containsKey(name))
            return null;

        // Add new item
        // Update static and dynamic rates
        int staticBaseChance = dynamicPortion / ++dynamicCount;
        int staticLeftover = dynamicPortion % dynamicCount;
        totalItemCount++;

        wheelItems.put(name, new WheelDataItem(name, staticBaseChance, this));
        UpdateDynamicRates(staticBaseChance, staticLeftover);

        return wheelItems.get(name);
    }

    /**
     * Removes an item from the wheel
     *
     * @param name
     */
    void RemoveFromWheel(String name) {
        // Check if item does not exist
        WheelDataItem itemToRemove = wheelItems.get(name);
        if (itemToRemove == null)
            return;

        // Init variables for later
        int staticBaseChance;
        int staticLeftover;

        // Check if item to remove is static or dynamic
        if (itemToRemove.isDynamic()) {
            staticBaseChance = dynamicPortion / --dynamicCount;
        } else {
            dynamicPortion += itemToRemove.getChance();
            staticBaseChance = dynamicPortion / dynamicCount;
        }

        // Calc leftover
        staticLeftover = dynamicPortion % dynamicCount;

        // Remove item, then update dynamic items
        wheelItems.remove(name);
        totalItemCount--;
        UpdateDynamicRates(staticBaseChance, staticLeftover);
    }

    /**
     * Toggles static status of an item
     *
     * @param name
     */
    void ToggleStatic(String name, boolean isStatic) {
        // Check if item exists
        WheelDataItem itemToToggle = wheelItems.get(name);
        if (itemToToggle == null)
            return;

        // Toggle static internally
        int staticBaseChance;
        int staticLeftover;
        wheelItems.get(name).setStatic(isStatic);

        // Check what the transition is to init variables
        if (itemToToggle.isDynamic()) {
            // static -> dynamic
            dynamicPortion += itemToToggle.getChance();
            staticBaseChance = dynamicPortion / ++dynamicCount;
        } else {
            // dynamic -> static
            dynamicPortion -= itemToToggle.getChance();
            staticBaseChance = dynamicPortion / --dynamicCount;
        }

        // Init second variable
        staticLeftover = dynamicPortion % dynamicCount;

        // Update dynamic items
        UpdateDynamicRates(staticBaseChance, staticLeftover);
    }

    /**
     * Sets the chance of a static item
     *
     * Returns whether or not change was applied
     *
     * @param name
     * @param chance
     */
    boolean SetChance(String name, int chance) {
        WheelDataItem itemToSet = wheelItems.get(name);

        // Calculate max static chance size
        maxStaticChance = 100 - (dynamicCount) - (totalItemChance - dynamicPortion - itemToSet.getChance());

        if (itemToSet == null || itemToSet.isDynamic())
            return false;

        // Get difference of chance
        int chanceDiff = chance - itemToSet.getChance();

        if (chance > maxStaticChance)
            return false;

        dynamicPortion -= chanceDiff;
        int staticBaseChance = dynamicPortion / dynamicCount;
        int staticBasePortion = dynamicPortion % dynamicCount;

        // Update chance of item
        wheelItems.get(name).setChance(chance);

        // Update dynamic items
        UpdateDynamicRates(staticBaseChance, staticBasePortion);

        return true;
    }

    public void UpdateDynamicRates(int staticBaseChance, int staticLeftover) {
        int newTotalItemChance = 0;
        for (WheelDataItem i : wheelItems.values()) {
            if (i.isDynamic()) {
                if (staticLeftover > 0) {
                    i.setChance(staticBaseChance + 1);
                    staticLeftover--;
                } else {
                    i.setChance(staticBaseChance);
                }
            }
            newTotalItemChance += i.getChance();
        }
        totalItemChance = newTotalItemChance;
    }

    // ---------------------------------------------------------------
    // Setters and getters
    // ---------------------------------------------------------------

    /**
     * Sets the name of this wheel
     *
     * @param name
     */
    public void setWheelName(String name) {
        this.wheelName = name;
    }

    /**
     * Gets the name of this wheel
     *
     * @return
     */
    String getWheelName() {
        return wheelName;
    }

    /**
     * Sets a wheel item's name to newname
     *
     * Returns true if successful
     *
     * @param oldName
     * @param newName
     * @return whether changed or not
     */
    boolean setNewWheelItemName(String oldName, String newName) {
        if (!wheelItems.containsKey(oldName) || wheelItems.containsKey(newName))
            return false;
        else {
            wheelItems.put(newName, wheelItems.remove(oldName));
            wheelItems.get(newName).setName(newName);
            return true;
        }
    }

    WheelDataItem getWheelDataItem(String name) {
        return wheelItems.get(name);
    }

    /**
     * Returns the hashmap representing a list of wheel items
     *
     * @return
     */
    LinkedHashMap<String, WheelDataItem> getHashMap() {
        return wheelItems;
    }

    /**
     * Returns total item count
     *
     * @return
     */
    int getTotalItemCount() {
        return totalItemCount;
    }

    /**
     * Returns total item chance
     *
     * @return
     */
    int getTotalItemChance() {
        return totalItemChance;
    }

    /**
     * Returns total dynamic count
     *
     * @return
     */
    int getDynamicCount() {
        return dynamicCount;
    }

    /**
     * Returns total dynamic chance
     *
     * @return
     */
    int getDynamicPortion() {
        return dynamicPortion;
    }

    int getMaxStaticChance() {
        return maxStaticChance;
    }

    public String getSpinItem() {
        // Map items 1 -> 100
        HashMap<Integer, String> randomMap = new HashMap<Integer, String>();

        // Keep track of map index
        int mapIndex = 1;

        // Loop through each item's chances
        for (WheelDataItem item : wheelItems.values()) {
            // For this item's chance value, map inc index to item's name
            for (int i = 0; i < item.getChance(); i++) {
                randomMap.put(mapIndex++, item.getName());
            }
        }

        // Pick a random value between 1 and 100 to return
        Random rand = new Random();
        int chosenIndex = rand.nextInt(100) + 1;
        return randomMap.get(chosenIndex);
    }

    // ---------------------------------------------------------------
    // UI getters
    // ---------------------------------------------------------------

    String getTotalChancePercent() {
        return this.getTotalItemChance() + "%";
    }

    // ---------------------------------------------------------------
    // Overloads
    // ---------------------------------------------------------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--------------------------------------\n");
        sb.append("Wheel Name: " + this.wheelName + "\n");
        sb.append("# :: Name :: Chance :: Static?\n");
        sb.append("--------------------------------------\n");

        int itemIndex = 1;
        for (WheelDataItem i : wheelItems.values()) {
            sb.append(itemIndex++ + " :: " + i.getName() + " :: " + i.getChance() + " :: " + i.isStatic() + "\n");
        }

        sb.append("--------------------------------------\n");
        sb.append("Item Count: " + totalItemCount + ", Item Chance: " + totalItemChance + "\n");
        sb.append("Dynamic Count: " + dynamicCount + ", Dynamic Portion: " + dynamicPortion + "\n");
        sb.append("--------------------------------------\n\n");

        return sb.toString();
    }

    // ---------------------------------------------------------------
    // UI
    // ---------------------------------------------------------------

    public void initUIElement(Context context, WheelMenu wheelMenu) {
        uiElement = new WheelDataUIElement(context, wheelMenu);
    }

    public LinearLayout getUIElement() {
        return uiElement;
    }

    private class WheelDataUIElement extends LinearLayout {

        // Layout elements
        private LinearLayout layout;
        private TextView wheelNameTextView;
        private Button loadWheel;
        private Button editWheel;
        private Button removeWheel;

        WheelSaveDataManager saveDataManager;
        private WheelMenu wm;

        public WheelDataUIElement(Context context, WheelMenu wm) {
            super(context);
            initControl(context);
            saveDataManager = new WheelSaveDataManager(context);
            this.wm = wm;
        }

        private void initControl(final Context context) {
            // layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.activity_wheel_selection, this);
            layout = (LinearLayout) findViewById(R.id.wheelSelection);

            // name
            wheelNameTextView = (TextView) findViewById(R.id.wheelNameTextView);
            wheelNameTextView.setText(wheelName);

            // load wheel
            loadWheel = (Button) findViewById(R.id.loadWheel);
            loadWheel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Set current wheel to this wheel
                    saveDataManager.SaveCurrentWheel(saveDataManager.LoadWheelList().get(wheelName));

                    // Return to main menu
                    wm.startActivity(new Intent(wm, MainActivity.class));
                }
            });

            // edit wheel
            editWheel = (Button) findViewById(R.id.editWheel);
            editWheel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Save this wheel data into wheel edit slot
                    saveDataManager.SaveWheelToEdit(saveDataManager.LoadWheelList().get(wheelName));

                    // Switch to the WheelReEditor
                    wm.startActivity(new Intent(wm, WheelEditor.class));
                }
            });

            // remove wheel
            removeWheel = (Button) findViewById(R.id.removeWheel);
            removeWheel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Check if removable (more than one wheel)
                    if (saveDataManager.LoadWheelList().size() <= 1) {
                        Snackbar warning = Snackbar.make((View)layout, "You must have at least one wheel", BaseTransientBottomBar.LENGTH_SHORT);
                        warning.show();
                        return;
                    }

                    // Remove item from wheel list
                    saveDataManager.RemoveFromWheelList(wheelName);

                    // Remove current if wheel was current, then reset current
                    if (saveDataManager.LoadCurrentWheel().getWheelName().equals(wheelName)) {
                        LinkedHashMap<String, WheelData> wheelList = saveDataManager.LoadWheelList();
                        WheelData currentWheel = null;
                        for (WheelData i : wheelList.values()) {
                            currentWheel = i;
                            break;
                        }
                        saveDataManager.SaveCurrentWheel(currentWheel);
                    }

                    // Finally, reload this page
                    wm.startActivity(new Intent(wm, WheelMenu.class));
                }
            });

        }

    }
}
