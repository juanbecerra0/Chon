package com.example.chon;

public class WheelDataItem {

    // ---------------------------------------------------------------
    // Variables and constructor
    // ---------------------------------------------------------------

    // Basic variables for handling name, chance (1 -> 99), and static status
    private String name;
    private int chance;
    private boolean isStatic;

    /**
     * Constructs a WheelDataItem object, which can be found in a WheelData object
     * @param name
     * @param chance
     */
    WheelDataItem(String name, int chance) {
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

    /**
     * Sets name of this item
     *
     * @param name
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * Gets name of this item
     *
     * @return
     */
    String getName() {
        return name;
    }

    /**
     * Sets chance of receiving this item
     *
     * Chance should be between 1 and 99 inclusive
     *
     * TODO handle chance that is outside of allowed bounds (?)
     *      TODO this may be handled by WheelData class
     *
     * @param chance
     */
    void setChance(int chance) {
        if (chance < 1 || chance > 99)
            return;

        this.chance = chance;
    }

    /**
     * Gets chance of receiving this item
     *
     * @return
     */
    int getChance() {
        return chance;
    }

    /**
     * Toggles between static and dynamic item type
     *
     * Requires no parameters, as it simply swaps a boolean
     */
    void toggleStatic() { isStatic = !isStatic; }

    /**
     * Returns whether or not item is static
     *
     * @return
     */
    boolean isStatic() { return isStatic; }

    /**
     * Returns whether or not item is dynamic
     *
     * @return
     */
    boolean isDynamic() { return !isStatic; }

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
