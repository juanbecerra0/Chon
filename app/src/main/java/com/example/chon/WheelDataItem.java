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
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class WheelDataItem {

    // ---------------------------------------------------------------
    // Variables and constructor
    // ---------------------------------------------------------------

    // Basic variables for handling name, chance (1 -> 99), and static status
    private String name;
    private int chance;
    private boolean isStatic;

    // UI elements and corresponding parent object (excluded from JSON serialization)
    private transient WheelDataUIElement uiElement;
    private transient WheelData parentWheel;

    /**
     * Constructs a WheelDataItem object, which can be found in a WheelData object
     * @param name of item
     * @param chance chance of receiving item
     * @param parentWheel WheelData object that this item is contained in
     */
    WheelDataItem(String name, int chance, WheelData parentWheel) {
        this.name = name;
        this.chance = chance;
        this.isStatic = false;
        this.parentWheel = parentWheel;
    }

    // ---------------------------------------------------------------
    // Utility
    // ---------------------------------------------------------------

    void ReloadJSON(WheelData parentWheel) {
        this.parentWheel = parentWheel;
    }

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
        // Layout elements
        private LinearLayout layout;
        private EditText itemName;
        private EditText itemChance;
        private CheckBox isStaticBox;
        private Button removeButton;

        // Warning messages for name
        private TextView warningTextName;
        private boolean nameChangedByProgram;

        // Warning messages for chance
        private TextView warningTextChance;
        private boolean chanceChangedByProgram;

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

            // Warning texts
            warningTextName = (TextView) findViewById(R.id.warningTextName);
            warningTextChance = (TextView) findViewById(R.id.warningTextChance);

            // Item name box
            nameChangedByProgram = true;
            itemName = (EditText) findViewById(R.id.itemName);
            itemName.setText(name);
            itemName.addTextChangedListener(new TextWatcher() {
                // Do nothing on these
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Check if name was automatically changed by the program rather than the user
                    if (nameChangedByProgram) {
                        nameChangedByProgram = false;
                        warningTextName.setText("");
                        return;
                    }

                    // Check if name has returned to the old name
                    if (s.toString().equals(name)) {
                        warningTextName.setText("");
                        return;
                    }

                    // Check if this name change would cause an error. If not, set the new name.
                    if (!parentWheel.setNewWheelItemName(name, s.toString())) {
                        warningTextName.setText("The name \"" + s.toString() + "\" is already being used!");
                    } else {
                        warningTextName.setText("");
                    }
                }
            });
            itemName.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        internalUIUpdate();
                    }
                }
            });


            // Item chance box
            itemChance = (EditText) findViewById(R.id.itemChance);
            itemChance.setText(String.valueOf(chance));
            itemChance.setInputType(InputType.TYPE_NULL);
            itemChance.setTextColor(Color.LTGRAY);
            // TODO verify number
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
                    parentWheel.ToggleStatic(name, isChecked);
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
            // Change name is needed
            nameChangedByProgram = true;
            itemName.setText(name);

            // Change chance if needed
            if (!isStatic) {
                chanceChangedByProgram = true;
                itemChance.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
                itemChance.setText(String.valueOf(chance));
                itemChance.setInputType(InputType.TYPE_NULL);
            }
        }

        /**
         * Removes this UI object from the parent component, then removes this object from
         * the Wheel object
         */
        private void SelfDestruct() {
            if (parentWheel.getTotalItemCount() <= 2) {
                Snackbar warning = Snackbar.make((View)layout, "You must have at least two wheel items", BaseTransientBottomBar.LENGTH_SHORT);
                warning.show();
                return;
            }

            // Remove from layout
            LinearLayout parentLayout = (LinearLayout) layout.getParent();
            parentLayout.removeView((View) layout);

            // Remove from wheel
            parentWheel.RemoveFromWheel(name);

            // Update UI of each other element
            for (WheelDataItem i : parentWheel.getHashMap().values()) {
                i.updateUI();
            }
        }
    }

}
