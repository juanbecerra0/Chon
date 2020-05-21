package com.example.chon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class WheelEditor extends AppCompatActivity {

    // This wheel data
    private WheelData thisWheel;

    // Buttons
    private Button saveWheelButton;
    private Button newItemButton;

    // Text fields
    private TextView totalChance;
    private EditText wheelName;

    // Item list
    private LinearLayout wheelItemsUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_editor);

        // TODO generate a unique name
        thisWheel = new WheelData("New wheel");

        configButtons();
        configText();

        for (WheelDataItem i : thisWheel.getHashMap().values()) {
            LoadUIElement(i);
        }
        UpdateWheelUI();
    }

    private void configButtons() {
        // Discard changes button
        Button wheelMenuButton = (Button) findViewById(R.id.wheelMenuButton);
        wheelMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WheelEditor.this, WheelMenu.class));
            }
        });

        // Save wheel button
        saveWheelButton = (Button) findViewById(R.id.saveWheelButton);
        saveWheelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Append this object to the end of the static wheel list

            }
        });

        // New wheel item button
        newItemButton = (Button) findViewById(R.id.newItemButton);
        newItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if there are greater than 100 items
                if (thisWheel.getHashMap().values().size() >= 100) {
                    Snackbar warning = Snackbar.make((View)wheelItemsUI, "You cannot have over 100 items", BaseTransientBottomBar.LENGTH_SHORT);
                    warning.show();
                    return;
                }

                // Generate a unique name for this new wheel item
                String name = "NewItem1";
                int nameAppend = 1;
                while(thisWheel.getHashMap().containsKey(name)) {
                    name = "NewItem" + (++nameAppend);
                }

                // Add item to wheel, then load UI element
                WheelDataItem item = thisWheel.AddToWheel(name);
                LoadUIElement(item);
                // Update all UI elements
                UpdateWheelUI();
            }
        });
    }

    private void configText() {
        // Total chance
        totalChance = (TextView) findViewById(R.id.currentPercent);
        totalChance.setText(thisWheel.getTotalChancePercent());

        // Wheel name
        wheelName = (EditText) findViewById(R.id.wheelNameField);
        wheelName.setText(thisWheel.getWheelName());
        wheelName.addTextChangedListener(new TextWatcher() {
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

        // Wheel items list
        wheelItemsUI = (LinearLayout) findViewById(R.id.wheelItemsUI);
    }

    private void LoadUIElement(WheelDataItem i) {
        i.initUiElement(getBaseContext());
        wheelItemsUI.addView(i.getUIElement());
    }

    /**
     * Updates the user interface to display information
     */
    private void UpdateWheelUI() {
        // Update info on all elements
        boolean allSavable = true;
        for (WheelDataItem i : thisWheel.getHashMap().values()) {
            if(!i.updateUI()) {
                allSavable = false;
            }
        }

        // Update current total chance
        totalChance.setText(thisWheel.getTotalChancePercent());

        // Check if wheel is savable
        // TODO check if name is taken
        saveWheelButton.setEnabled(thisWheel.getTotalItemChance() == 100 && allSavable);
    }

}
