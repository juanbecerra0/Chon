package com.example.chon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.provider.SelfDestructiveThread;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
                // TODO
            }
        });

        // New wheel item button
        newItemButton = (Button) findViewById(R.id.newItemButton);
        newItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewWheelItem();
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

    private void AddNewWheelItem() {
        wheelItemsUI.addView(new WheelDataUIElement(getBaseContext()));
    }

    /**
     * Updates the user interface to display information
     */
    private void Update() {
        boolean canSave = true;

        // Update current total chance
        totalChance.setText(thisWheel.getTotalChancePercent());

        // Check if wheel is savable
        saveWheelButton.setEnabled(canSave);
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

        public WheelDataUIElement(Context context) {
            super(context);
            initControl(context);
        }

        private void initControl(Context context) {
            // Create a layout blueprint
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.wheel_item_editor, this);
            layout = (LinearLayout) findViewById(R.id.wheelItemEditorLayout);

            // Item name box
            itemName = (EditText) findViewById(R.id.itemName);

            // Item chance box
            itemChance = (EditText) findViewById(R.id.itemChance);

            // Is static swap
            isStaticBox = (CheckBox) findViewById(R.id.isStaticBox);

            // Remove button
            removeButton = (Button) findViewById(R.id.removeButton);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SelfDestruct();
                }
            });
        }

        private void SelfDestruct() {
            wheelItemsUI.removeView((View) this);
        }
    }

}
