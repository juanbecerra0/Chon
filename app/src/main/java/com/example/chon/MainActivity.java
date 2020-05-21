package com.example.chon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {

    private WheelData wheel;
    private Button wheelMenuButton;
    private TextView menuTestText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Config UI
        configButtons();

        // Load current wheel
        wheel = loadCurrentWheel();

        // TODO TEST load current wheel information
        menuTestText = (TextView) findViewById(R.id.menu_test);
        menuTestText.setText(wheel.toString());

        wheelMenuButton.setText(wheel.getWheelName());
    }

    private void configButtons() {
        wheelMenuButton = (Button) findViewById(R.id.wheelMenuButton);
        wheelMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WheelMenu.class);
                startActivity(intent);
            }
        });
    }

    private WheelData loadCurrentWheel() {
        WheelSaveDataManager saveDataManager = new WheelSaveDataManager(getBaseContext());
        WheelData currentWheel = saveDataManager.LoadCurrentWheel();

        // Check if a new wheel needs to be selected or created
        if (currentWheel == null) {
            LinkedHashMap<String, WheelData> wheelList = saveDataManager.LoadWheelList();
            if (!wheelList.isEmpty()) {
                for (WheelData i : wheelList.values()) {
                    currentWheel = i;
                    break;
                }
            } else {
                currentWheel = new WheelData("NewWheel1");
            }

            // Save as current
            saveDataManager.SaveCurrentWheel(currentWheel);
        }

        // Check if current needs to be added to wheel list
        saveDataManager.AddToWheelList(currentWheel);

        return currentWheel;
    }

}
