package com.example.chon;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class WheelData {

    // ---------------------------------------------------------------
    // Variables and constructor
    // ---------------------------------------------------------------

    // Name of wheel, and hashmap to access elements of wheel
    private String wheelName;
    private HashMap<String, WheelDataItem> wheelItems;

    // Keeps track of all items
    private int totalItemCount;
    private int totalItemChance;

    // Keeps track of only dynamic item information for dynamic chance calculations
    private int dynamicCount;
    private int dynamicPortion;

    /**
     * Constructor for WheelData, a data structure that keeps track of items and their chances
     *
     * @param wheelName
     */
    WheelData(String wheelName) {
        this.wheelName = wheelName;
        wheelItems = new LinkedHashMap<String, WheelDataItem>();
        wheelItems.put("Item 1", new WheelDataItem("Item 1", 50));
        wheelItems.put("Item 2", new WheelDataItem("Item 2", 50));

        totalItemCount = 2;
        totalItemChance = 100;

        dynamicCount = 2;
        dynamicPortion = 100;
    }

    /**
     * Constructor for WheelData, a data structure that keeps track of items and their chances
     *
     * @param wheelName
     * @param wheelItems
     */
    WheelData(String wheelName, LinkedHashMap<String, WheelDataItem> wheelItems) {
        this.wheelName = wheelName;
        this.wheelItems = wheelItems;

        totalItemCount = 0;
        totalItemChance = 0;

        dynamicCount = 0;
        dynamicPortion = 0;

        for (WheelDataItem i : wheelItems.values()) {
            totalItemCount++;
            totalItemChance++;
            if (i.isDynamic()) {
                dynamicCount++;
                dynamicPortion += i.getChance();
            }
        }

    }

    /**
     * Constructor for WheelData, a data structure that keeps track of items and their chances
     *
     * @param wd
     */
    WheelData(WheelData wd) {
        // TODO
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
    void AddToWheel(String name) {
        if (wheelItems.containsKey(name))
            return;

        int staticBaseChance = dynamicPortion / ++dynamicCount;
        int staticLeftover = dynamicPortion % dynamicCount;

        for (WheelDataItem i : wheelItems.values()) {
            if (i.isDynamic()) {
                if (staticLeftover > 0) {
                    i.setChance(staticBaseChance + 1);
                    staticLeftover--;
                } else {
                    i.setChance(staticBaseChance);
                }
            }
        }

        wheelItems.put(name, new WheelDataItem(name, staticBaseChance));
        totalItemCount++;
    }

    /**
     * Removes an item from the wheel
     *
     * TODO return error message if item cannot be found
     * TODO handle static removals
     *
     * @param name
     */
    void RemoveFromWheel(String name) {
        WheelDataItem itemToRemove = wheelItems.get(name);
        if (itemToRemove == null)
            return;

        if (itemToRemove.isDynamic()) {
            int staticBaseChance = dynamicPortion / --dynamicCount;
            int staticLeftover = dynamicPortion % dynamicCount;

            for (WheelDataItem i : wheelItems.values()) {
                if (i.isDynamic()) {
                    if (staticLeftover > 0) {
                        i.setChance(staticBaseChance + 1);
                        staticLeftover--;
                    } else {
                        i.setChance(staticBaseChance);
                    }
                }
            }
        }

        wheelItems.remove(name);
        totalItemCount--;
    }

    /**
     * Toggles static status of an item
     *
     * TODO return error message if item cannot be found
     * TODO handle static -> dynamic change
     *
     * @param name
     */
    void ToggleStatic(String name) {
        WheelDataItem itemToToggle = wheelItems.get(name);
        if (itemToToggle == null)
            return;

        int staticBaseChance;
        int staticLeftover;
        wheelItems.get(name).toggleStatic();

        if (itemToToggle.isDynamic()) {
            // static -> dynamic
            // TODO handle difference in transition
            staticBaseChance = dynamicPortion / ++dynamicCount;
        } else {
            // dynamic -> static
            staticBaseChance = dynamicPortion / --dynamicCount;
        }

        staticLeftover = dynamicPortion % dynamicCount;

        for (WheelDataItem i : wheelItems.values()) {
            if (i.isDynamic()) {
                if (staticLeftover > 0) {
                    i.setChance(staticBaseChance + 1);
                    staticLeftover--;
                } else {
                    i.setChance(staticBaseChance);
                }
            }
        }
    }

    /**
     * Sets the chance of a static item
     *
     * TODO return error message if item cannot be found
     * TODO return error message if item is dynamic
     * TODO handle changing dynamic chances based on this static diff
     *
     * @param name
     */
    void SetChance(String name) {
        WheelDataItem itemToSet = wheelItems.get(name);
        if (itemToSet == null || itemToSet.isDynamic())
            return;

        // TODO handle difference in transition

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
    public String getWheelName() {
        return wheelName;
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
        sb.append("Item Chance: " + totalItemChance + ", Item Count: " + totalItemCount + "\n");
        sb.append("--------------------------------------\n\n");

        return sb.toString();
    }
}
