package com.example.chon;

import java.util.HashSet;
import java.util.LinkedHashSet;

public class WheelData {

    // ---------------------------------------------------------------
    // Variables and constructor
    // ---------------------------------------------------------------

    private String wheelName;
    private HashSet<WheelDataItem> wheelItems;

    private int dynamicCount;
    private int dynamicPortion;

    public WheelData(String wheelName) {
        this.wheelName = wheelName;
        wheelItems = new LinkedHashSet<WheelDataItem>();
        wheelItems.add(new WheelDataItem("Item 1", 50));
        wheelItems.add(new WheelDataItem("Item 2", 50));

        dynamicCount = 2;
        dynamicPortion = 100;
    }

    public WheelData(String wheelName, LinkedHashSet<WheelDataItem> wheelItems) {
        this.wheelName = wheelName;
        this.wheelItems = wheelItems;

        dynamicCount = 0;
        dynamicPortion = 0;

        for (WheelDataItem i : wheelItems) {
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

    public void AddToWheel(String name) {
        /*
        // Sum up all dynamic items
        for (WheelDataItem i : wheelItems) {
            if (!i.isStatic()) {
                dynamicSum += i.getChance();
                dynamicCount++;
            }
        }

        WheelDataItem item = new WheelDataItem(name, );

        if (wheelItems.contains(item)) {
            System.err.println("Collision with item " + item.getName());
        return;
        } else {
            float remainingChance = 100f - item.getChance();
            for (WheelDataItem i : wheelItems) {
                i.setChance(remainingChance * (i.getChance() / 100));
            }
            wheelItems.add(item);
        }
        */
    }

    /*
    public void RemoveFromWheel(String name) {
        WheelDataItem item = new WheelDataItem(name);

        if (!wheelItems.contains(item)) {
            System.err.println("Could not find item " + item.getName());
        } else {
            float remainingChance = 100f - item.getChance();
            for (WheelDataItem i : wheelItems) {
                i.setChance(100f * (i.getChance() / remainingChance));
            }
            wheelItems.remove(item);
        }
    }
    */

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
        sb.append("--------------------------------------\n");

        int itemIndex = 1;
        for (WheelDataItem i : wheelItems) {
            sb.append(itemIndex++ + " :: " + i.getName() + " :: " + i.getChance() + "\n");
        }

        sb.append("--------------------------------------\n\n");

        return sb.toString();
    }
}
