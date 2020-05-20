package com.example.chon;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

public class WheelDataItem {

    // ---------------------------------------------------------------
    // Variables and constructor
    // ---------------------------------------------------------------

    // Basic variables for handling name, chance (1 -> 99), and static status
    private String name;
    private int chance;
    private boolean isStatic;

    // UI elements and corresponding parent object
    private WheelDataUIElement uiElement;

    /**
     * Constructs a WheelDataItem object, which can be found in a WheelData object
     * @param name of item
     * @param chance chance of receiving item
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
     * @param name of item
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * Gets name of this item
     *
     * @return name of item
     */
    String getName() {
        return name;
    }

    /**
     * Sets chance of receiving this item
     *
     * Chance should be between 1 and 99 inclusive
     *
     * @param chance of receiving item
     */
    void setChance(int chance) {
        if (chance < 1 || chance > 99)
            return;

        this.chance = chance;
    }

    /**
     * Gets chance of receiving this item
     *
     * @return chance of receiving item
     */
    int getChance() {
        return chance;
    }

    /**
     * Sets static status of item
     *
     * @param isStatic status
     */
    void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    /**
     * Returns whether or not item is static
     *
     * @return isStatis status
     */
    boolean isStatic() { return isStatic; }

    /**
     * Returns whether or not item is dynamic
     *
     * @return isDynamic
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

    // ---------------------------------------------------------------
    // GUI element
    // ---------------------------------------------------------------

    /**
     * Initializes UI object for this item
     * @param context
     */
    public void initUiElement(Context context) {
        uiElement = new WheelDataUIElement(context);
    }

    /**
     * Returns the UI object of this item
     * @return
     */
    public WheelDataUIElement getUiElement() {
        return uiElement;
    }

    /**
     * Updates the internal UI strings of this item for the WHeelEditor GUI
     */
    public void updateUI() {
        uiElement.internalUIUpdate();
    }

    /**
     * Represents the listable lines that represent wheel items.
     */
    private class WheelDataUIElement extends LinearLayout {
        private LinearLayout layout;
        private EditText itemName;
        private EditText itemChance;
        private CheckBox isStaticBox;
        private Button removeButton;

        /**
         * Constructs the UI element object
         *
         * @param context
         */
        public WheelDataUIElement(Context context) {
            super(context);
            initControl(context);
        }

        /**
         * Initializes subviews of the UI element
         *
         * @param context
         */
        private void initControl(Context context) {
            // Create a layout blueprint
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.wheel_item_editor, this);
            layout = (LinearLayout) findViewById(R.id.wheelItemEditorLayout);

            // Item name box
            itemName = (EditText) findViewById(R.id.itemName);
            itemName.setText(name);
            // TODO
            itemName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            // Item chance box
            itemChance = (EditText) findViewById(R.id.itemChance);
            itemChance.setText(String.valueOf(chance));
            itemChance.setInputType(InputType.TYPE_NULL);
            itemChance.setTextColor(Color.LTGRAY);
            // TODO
            itemChance.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            // Is static swap
            isStaticBox = (CheckBox) findViewById(R.id.isStaticBox);
            isStaticBox.setChecked(isStatic);
            isStaticBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO parentWheel.ToggleStatic(name, isChecked);
                    isStatic = isChecked;
                    if (isChecked) {
                        // static
                        itemChance.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
                        itemChance.setTextColor(Color.BLACK);
                    } else {
                        // dynamic
                        itemChance.setInputType(InputType.TYPE_NULL);
                        itemChance.setTextColor(Color.LTGRAY);
                    }
                }
            });

            // Remove button
            removeButton = (Button) findViewById(R.id.removeButton);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SelfDestruct();
                }
            });
        }

        /**
         * Internally updates UI name and chance
         */
        public void internalUIUpdate() {
            itemName.setText(name);
            itemChance.setText(String.valueOf(chance));
        }

        /**
         * Removes this UI object from the parent component
         */
        private void SelfDestruct() {
            // TODO
        }
    }

}
