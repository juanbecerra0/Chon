package com.example.chon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Set;

public class WheelEditor extends AppCompatActivity {

    // This wheel data
    public WheelData thisWheel;

    // Buttons
    private Button saveWheelButton;
    private Button newItemButton;

    // Text fields
    private TextView nameError;
    private EditText wheelName;

    // Item list
    public LinearLayout wheelItemsUI;

    // List of all saved wheels (to check names)
    private WheelSaveDataManager saveDataManager;
    private Set<String> wheelNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_editor);

        // Get set of all wheel names
        saveDataManager = new WheelSaveDataManager(getBaseContext());
        wheelNames = saveDataManager.getSavedWheelNames();

        // Check if a wheel was loaded in
        WheelData loadedWheel = saveDataManager.LoadWheelToEdit();

        if (loadedWheel == null) {
            // Generate new wheel
            // Generate new wheel name
            String wheelAutoName = "NewWheel1";
            int nameInt = 1;
            while(wheelNames.contains(wheelAutoName)) {
                wheelAutoName = "NewWheel" + (++nameInt);
            }

            thisWheel = new WheelData(wheelAutoName);
        } else {
            // Load saved wheel
            thisWheel = loadedWheel;

            // Initialize UI and parent wheel for each wheel item
            for (WheelDataItem i : thisWheel.getHashMap().values()) {
                i.loadParentWheel(thisWheel);
                i.initUiElement(getBaseContext());
            }

        }

        // Config UI
        configButtons();
        configText();

        // Add specific UI elements
        for (WheelDataItem i : thisWheel.getHashMap().values()) {
            LoadUIElement(i);
        }
        UpdateWheelUI();

    }

    private void configButtons() {
        // Hide support bar
        this.getSupportActionBar().hide();

        // Discard changes button
        Button wheelMenuButton = (Button) findViewById(R.id.wheelMenuButton);
        wheelMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WheelEditor.this, WheelMenu.class));
                finish();
            }
        });

        // Save wheel button
        saveWheelButton = (Button) findViewById(R.id.saveWheelButton);
        saveWheelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove from object from save data (if it exists), then add to save data
                saveDataManager.RemoveFromWheelList(thisWheel.getWheelName());
                thisWheel.setWheelName(wheelName.getText().toString());
                saveDataManager.AddToWheelList(thisWheel);

                // Save as current wheel
                saveDataManager.SaveCurrentWheel(thisWheel);

                // Finally, return to main menu
                startActivity(new Intent(WheelEditor.this, MainActivity.class));
                finish();
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
        nameError = (TextView) findViewById(R.id.nameWarning);

        // Wheel name
        wheelName = (EditText) findViewById(R.id.wheelNameField);
        wheelName.setText(thisWheel.getWheelName());
        wheelName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (wheelNames.contains(s.toString()) && !s.toString().equals(thisWheel.getWheelName())) {
                    nameError.setText("Name taken");
                    saveWheelButton.setEnabled(false);
                } else if (s.toString().equals("")) {
                    nameError.setText("Name cannot be blank");
                    saveWheelButton.setEnabled(false);
                } else {
                    nameError.setText("");
                    saveWheelButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        wheelName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                UpdateWheelUI();
            }
        });

        // Wheel items list
        wheelItemsUI = (LinearLayout) findViewById(R.id.wheelItemsUI);
    }

    public void LoadUIElement(WheelDataItem i) {
        i.initUiElement(getBaseContext());
        wheelItemsUI.addView(i.getUIElement());
    }

    /**
     * Updates the user interface to display information
     */
    public void UpdateWheelUI() {
        // Update info on all elements
        boolean allSavable = true;
        for (WheelDataItem i : thisWheel.getHashMap().values()) {
            if(!i.updateUI()) {
                allSavable = false;
            }
        }

        // Check if wheel is savable
        saveWheelButton.setEnabled(thisWheel.getTotalItemChance() == 100 && allSavable && nameError.getText().equals(""));
    }

    @Override
    public void onBackPressed() {
        // Discard changes and return to wheel menu
        startActivity(new Intent(WheelEditor.this, WheelMenu.class));
        finish();
    }
}
