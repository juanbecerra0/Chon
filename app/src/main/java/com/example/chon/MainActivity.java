package com.example.chon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.opengl.GLWheelView;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {
    // Current wheel object
    private WheelData wheel;

    // UI elements
    private GLSurfaceView glView;
    private Button wheelMenuButton;
    private Button rollButton;
    private TextView resultText;

    // Sound media player
    private MediaPlayer funhausSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load current wheel
        wheel = loadCurrentWheel();

        // Config UI
        ConfigUI();

    }

    private void ConfigUI() {
        // Hide support bar
        this.getSupportActionBar().hide();

        // OpenGL rendering context
        glView = (GLWheelView) findViewById(R.id.glView);
        ((GLWheelView) glView).initRenderer(wheel);

        // Wheel selection button
        wheelMenuButton = (Button) findViewById(R.id.wheelMenuButton);
        wheelMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WheelMenu.class);
                startActivity(intent);
                finish();
            }
        });
        wheelMenuButton.setText(wheel.getWheelName());

        // music player
        funhausSong = MediaPlayer.create(this, R.raw.wheelhaus);
        float t = funhausSong.getDuration();

        // roll button
        rollButton = (Button) findViewById(R.id.rollButton);
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollWheel();
            }
        });

        // Result text
        resultText = (TextView) findViewById(R.id.result);

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

    private void rollWheel() {
        // Disable buttons and reset result
        rollButton.setEnabled(false);
        wheelMenuButton.setEnabled(false);
        resultText.setText("");

        // Determine winning string
        final String winningItem = wheel.getSpinItem();

        // Play the song
        funhausSong.start();

        // Sleep for length of song
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Show result, enable buttons
                resultText.setText(winningItem);
                rollButton.setEnabled(true);
                wheelMenuButton.setEnabled(true);
            }
        }, funhausSong.getDuration());

    }

    @Override
    public void onBackPressed() {
        // Exit program
        finish();
    }

}
