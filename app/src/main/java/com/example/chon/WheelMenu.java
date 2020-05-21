package com.example.chon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import java.util.LinkedHashMap;

public class WheelMenu extends AppCompatActivity {

    private LinearLayout wheelListUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_menu);

        ConfigUI();
        LoadUIElements();
    }

    private void ConfigUI() {
        // Return button
        Button mainMenuButton = (Button) findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WheelMenu.this, MainActivity.class));
            }
        });

        // New wheel button
        Button newWheelButton = (Button) findViewById(R.id.newWheelButton);
        newWheelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WheelMenu.this, WheelEditor.class));
            }
        });

        // wheelListUI
        wheelListUI = (LinearLayout) findViewById(R.id.wheelListUI);
    }

    private void LoadUIElements() {
        WheelSaveDataManager saveDataManager = new WheelSaveDataManager(getBaseContext());
        LinkedHashMap<String, WheelData> wheelList = saveDataManager.LoadWheelList();

        for (WheelData wd : wheelList.values()) {
            wd.initUIElement(getBaseContext(), WheelMenu.this);
            wheelListUI.addView(wd.getUIElement());
        }
    }
}
