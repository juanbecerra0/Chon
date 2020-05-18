package com.example.chon;

public class WheelDataItem {

    // ---------------------------------------------------------------
    // Variables and constructor
    // ---------------------------------------------------------------

    private String name;
    private int chance;
    private boolean isStatic;

    public WheelDataItem(String name, int chance) {
        this.name = name;
        this.chance = chance;
        this.isStatic = false;
    }

    // ---------------------------------------------------------------
    // Utility
    // ---------------------------------------------------------------


    // ---------------------------------------------------------------
    // Setters and getters
    // ---------------------------------------------------------------

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public int getChance() {
        return chance;
    }

    public void toggleStatic() { isStatic = !isStatic; }

    public boolean isStatic() { return isStatic; }

    // ---------------------------------------------------------------
    // Overloads
    // ---------------------------------------------------------------
    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object item) {
        return this.hashCode() == item.hashCode();
    }

}
