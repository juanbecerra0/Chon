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

        thisWheel = new WheelData("New wheel", false);

        configButtons();
        configText();

        for (WheelDataItem i : thisWheel.getHashMap().values()) {
            LoadUIElement(i);
        }
        UpdateWheelUI();
    }

    // TODO temp
    int numItems = 2;

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
                // TODO
            }
        });

        // New wheel item button
        newItemButton = (Button) findViewById(R.id.newItemButton);
        newItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add item to wheel, then load UI element
                WheelDataItem item = thisWheel.AddToWheel("Item " + String.valueOf(++numItems));
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
        wheelItemsUI.addView(i.getUiElement());
        i.updateUI();
    }

    /**
     * Updates the user interface to display information
     */
    private void UpdateWheelUI() {
        // Update current total chance
        totalChance.setText(thisWheel.getTotalChancePercent());

        // Update info on all elements
        for (WheelDataItem i : thisWheel.getHashMap().values()) {
            i.updateUI();
        }

        // Check if wheel is savable
        saveWheelButton.setEnabled(thisWheel.getTotalItemChance() == 100);
    }

}
