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

    void loadParentWheel(WheelData parentWheel) { this.parentWheel = parentWheel; }

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
    public String getName() {
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
    public int getChance() {
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

    /**
     * Returns color of this object (calculated based off of name)
     *
     * @return float array representing color
     */
    public float[] getColor() {

        char[] charArr = name.toCharArray();

        if (charArr.length < 3) {
            float value = 0;
            for (char c : charArr) {
                value += c;
            }
            float rgb = (value % 256) / 256;

            return new float[] {rgb, rgb, rgb, 1.0f};
        } else {
            int partition = charArr.length / 3;
            int remainder = charArr.length % 3;

            float r = 0f, g = 0f, b = 0f;

            // Calc r
            if (remainder == 1 || remainder == 2) {
                float value = 0;
                for (int i = 0; i < partition + 1; i++) {
                    value += charArr[i] * 2;
                }
                r = (value % 256) / 256;
            } else {
                float value = 0;
                for (int i = 0; i < partition; i++) {
                    value += charArr[i] * 2;
                }
                r = (value % 256) / 256;
            }

            // calc g
            if (remainder == 1) {
                float value = 0;
                for (int i = partition + 1; i < (partition * 2) + 1; i++) {
                    value += charArr[i] * 2;
                }
                g = (value % 256) / 256;
            } else if (remainder == 2) {
                float value = 0;
                for (int i = partition + 1; i < (partition * 2) + 2; i++) {
                    value += charArr[i] * 2;
                }
                g = (value % 256) / 256;
            } else {
                float value = 0;
                for (int i = partition; i < partition * 2; i++) {
                    value += charArr[i] * 2;
                }
                g = (value % 256) / 256;
            }

            // calc b
            if (remainder == 1) {
                float value = 0;
                for (int i = (partition * 2) + 1; i < (partition * 3) + 1; i++) {
                    value += charArr[i] * 2;
                }
                b = (value % 256) / 256;
            } else if (remainder == 2) {
                float value = 0;
                for (int i = (partition * 2) + 2; i < (partition * 3) + 2; i++) {
                    value += charArr[i] * 2;
                }
                b = (value % 256) / 256;
            } else {
                float value = 0;
                for (int i = (partition * 2); i < (partition * 3); i++) {
                    value += charArr[i] * 2;
                }
                b = (value % 256) / 256;
            }

            return new float[] {r, g, b, 1.0f};
        }
    }

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
    public WheelDataUIElement getUIElement() {
        return uiElement;
    }

    /**
     * Updates the internal UI strings of this item for the WHeelEditor GUI
     */
    public boolean updateUI() {
        return uiElement.internalUIUpdate();
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

        // Warning messages for name and chance
        private TextView warningText;
        private boolean nameChangedByProgram;
        private boolean chanceChangedByProgram;
        private boolean canChangeChance = false;

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
            warningText = (TextView) findViewById(R.id.warningText);

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
                        warningText.setText("");
                        return;
                    }

                    // Check if name has returned to the old name
                    if (s.toString().equals(name)) {
                        warningText.setText("");
                        return;
                    }

                    // Check if name is blank
                    if (s.toString().equals("")) {
                        warningText.setText("Name cannot be blank");
                        return;
                    }

                    // Check if this name change would cause an error. If not, set the new name.
                    if (!parentWheel.setNewWheelItemName(name, s.toString())) {
                        warningText.setText("The name \"" + s.toString() + "\" is already being used!");
                    } else {
                        warningText.setText("");
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
            chanceChangedByProgram = true;
            itemChance = (EditText) findViewById(R.id.itemChance);
            itemChance.setText(String.valueOf(chance));
            itemChance.setInputType(InputType.TYPE_NULL);
            itemChance.setTextColor(Color.LTGRAY);
            itemChance.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Check if chance was automatically changed by the program rather than by user
                    if (chanceChangedByProgram) {
                        chanceChangedByProgram = false;
                        return;
                    }

                    // Check if value is null
                    String valueString = s.toString();
                    if(valueString.equals("")) {
                        warningText.setText("Chance must be greater than 1!");
                        return;
                    }

                    // Check if value is out of range
                    int valueInt = Integer.parseInt(valueString);
                    if(valueInt < 1) {
                        warningText.setText("Chance must be greater than 1!");
                        return;
                    }

                    // Check if change is viable
                    if(!parentWheel.SetChance(name, Integer.parseInt(s.toString()))) {
                        warningText.setText("Chance must be between 1 and " + parentWheel.getMaxStaticChance() + "!");
                        return;
                    } else {
                        // For each element, update UI (once we click off)
                        warningText.setText("");
                        canChangeChance = true;
                        for (WheelDataItem i : parentWheel.getHashMap().values()) {
                            i.updateUI();
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            itemChance.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus) {
                        internalUIUpdate();
                    }
                }
            });

            // Is static swap
            isStaticBox = (CheckBox) findViewById(R.id.isStaticBox);
            isStaticBox.setChecked(isStatic);
            isStaticBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    // Check if this is the last dynamic item
                    if (parentWheel.getDynamicCount() <= 1 && isChecked) {
                        Snackbar warning = Snackbar.make((View)layout, "You must have at least one dynamic item", BaseTransientBottomBar.LENGTH_SHORT);
                        warning.show();
                        isStaticBox.setChecked(false);
                        return;
                    }

                    // Toggle static
                    parentWheel.ToggleStatic(name, isChecked);
                    // Update whether or not field is editable
                    if (isStatic) {
                        // static
                        itemChance.setInputType(InputType.TYPE_CLASS_NUMBER);
                        itemChance.setTextColor(Color.BLACK);
                    } else {
                        // dynamic
                        itemChance.setInputType(InputType.TYPE_NULL);
                        itemChance.setTextColor(Color.LTGRAY);
                    }
                    // For each element, update UI
                    for (WheelDataItem i : parentWheel.getHashMap().values()) {
                        i.updateUI();
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
         *
         * Returns whether or not this item is savable
         */
        public boolean internalUIUpdate() {
            // Change name is needed
            nameChangedByProgram = true;
            itemName.setText(name);

            // Change chance if needed
            chanceChangedByProgram = true;
            itemChance.setInputType(InputType.TYPE_CLASS_NUMBER);
            itemChance.setText(String.valueOf(chance));
            if (!isStatic)
                itemChance.setInputType(InputType.TYPE_NULL);

            // If no warning text, then this is savable
            return warningText.getText().toString().equals("");
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
