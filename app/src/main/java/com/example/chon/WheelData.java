package com.example.chon;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class WheelData {

    // ---------------------------------------------------------------
    // Variables and constructor
    // ---------------------------------------------------------------

    private String wheelName;
    private HashMap<String, WheelDataItem> wheelItems;

    private int totalItemCount;
    private int totalItemChance;

    private int dynamicCount;
    private int dynamicPortion;

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

    public WheelData(String wheelName, LinkedHashMap<String, WheelDataItem> wheelItems) {
        this.wheelName = wheelName;
        this.wheelItems = wheelItems;

        totalItemCount = 0;
        totalItemChance = 0;

        dynamicCount = 0;
        dynamicPortion = 0;

        for (WheelDataItem i : wheelItems.values()) {
            totalItemCount++;
            totalItemChance++;
            if (!i.isStatic()) {
                dynamicCount++;
                dynamicPortion += i.getChance();
            }
        }

    }

    public WheelData(WheelData wd) {
        // TODO
    }

    // ---------------------------------------------------------------
    // Utility
    // ---------------------------------------------------------------

    // TODO handle static items

    void AddToWheel(String name) {
        if (wheelItems.containsKey(name))
            return;

        int staticBaseChance = dynamicPortion / ++dynamicCount;
        int staticLeftover = dynamicPortion % dynamicCount;

        for (WheelDataItem i : wheelItems.values()) {
            if (!i.isStatic()) {
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

    void RemoveFromWheel(String name) {
        WheelDataItem itemToRemove = wheelItems.get(name);
        if (itemToRemove == null)
            return;

        if (!itemToRemove.isStatic()) {
            int staticBaseChance = dynamicPortion / --dynamicCount;
            int staticLeftover = dynamicPortion % dynamicCount;

            for (WheelDataItem i : wheelItems.values()) {
                if (!i.isStatic()) {
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

    // ---------------------------------------------------------------
    // Setters and getters
    // ---------------------------------------------------------------

    public void setWheelName(String name) {
        this.wheelName = wheelName;
    }

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
