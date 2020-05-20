package com.example.chon;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.LinkedHashMap;

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

    /**
     * Constructor for WheelData, a data structure that keeps track of items and their chances
     *
     * The fromJson tag is set to true if wheelName is the JSON string representation of
     * this object
     *
     * @param wheelName
     * @param fromJson
     */
    WheelData(String wheelName, boolean fromJson) {
        if (fromJson) {
            Gson gson = new Gson();
            WheelData wd = gson.fromJson(wheelName, this.getClass());

            this.wheelName = wd.getWheelName();
            this.wheelItems = wd.getHashMap();
            this.totalItemCount = wd.getTotalItemCount();
            this.totalItemChance = wd.getTotalItemChance();
            this.dynamicCount = wd.getDynamicCount();
            this.dynamicPortion = wd.getDynamicPortion();
        } else {
            // Init variables
            this.wheelName = wheelName;
            wheelItems = new LinkedHashMap<String, WheelDataItem>();

            // By default, add two dynamic items at 50/50
            wheelItems.put("Item 1", new WheelDataItem("Item 1", 50, this));
            wheelItems.put("Item 2", new WheelDataItem("Item 2", 50, this));

            // Set variables based on two inits
            totalItemCount = 2;
            totalItemChance = 100;

            dynamicCount = 2;
            dynamicPortion = 100;
        }
    }

    // ---------------------------------------------------------------
    // Utility
    // ---------------------------------------------------------------

    /**
     * Adds an item to the wheel, which is dynamic by default
     *
     * TODO return error message if item is duplicate
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
     * TODO return error message if item cannot be found
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
     * TODO return error message if item cannot be found
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
     * TODO return error message if item cannot be found
     * TODO return error message if item is dynamic
     * TODO return error message if item chance change is too large
     *
     * @param name
     * @param chance
     */
    void SetChance(String name, int chance) {
        WheelDataItem itemToSet = wheelItems.get(name);
        if (itemToSet == null || itemToSet.isDynamic())
            return;

        // Get difference of chance
        int chanceDiff = chance - itemToSet.getChance();

        if (((dynamicPortion - chanceDiff) / dynamicCount) < 1)
            return;

        dynamicPortion -= chanceDiff;
        int staticBaseChance = dynamicPortion / dynamicCount;
        int staticBasePortion = dynamicPortion % dynamicCount;

        // Update chance of item
        wheelItems.get(name).setChance(chance);

        // Update dynamic items
        UpdateDynamicRates(staticBaseChance, staticBasePortion);
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
        this.wheelName = wheelName;
    }

    /**
     * Gets the name of this wheel
     *
     * @return
     */
    String getWheelName() {
        return wheelName;
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
    private int getTotalItemCount() {
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
    private int getDynamicCount() {
        return dynamicCount;
    }

    /**
     * Returns total dynamic chance
     *
     * @return
     */
    private int getDynamicPortion() {
        return dynamicPortion;
    }

    // ---------------------------------------------------------------
    // UI getters
    // ---------------------------------------------------------------

    /**
     * Gets the Json string representation
     *
     * @return
     */
    String getJsonString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

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
}
