package com.example.chon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WheelEditor extends AppCompatActivity {

    private WheelData thisWheel;

    // Buttons
    private Button saveWheelButton;

    // Text fields
    private TextView totalChance;
    private EditText wheelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_editor);

        thisWheel = new WheelData("New wheel", false);

        configButtons();
        configText();
    }

    private void configButtons() {
        Button wheelMenuButton = (Button) findViewById(R.id.wheelMenuButton);
        wheelMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WheelEditor.this, WheelMenu.class));
            }
        });

        saveWheelButton = (Button) findViewById(R.id.saveWheelButton);
        saveWheelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
            }
        });
    }

    private void configText() {
        totalChance = (TextView) findViewById(R.id.currentPercent);
        totalChance.setText(thisWheel.getTotalChancePercent());

        wheelName = (EditText) findViewById(R.id.wheelNameField);
        wheelName.setText(thisWheel.getWheelName());
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
}
