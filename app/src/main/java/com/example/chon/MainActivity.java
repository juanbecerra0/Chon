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
import java.util.Random;

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
    private long funhausSongDuration;

    // Wheel animation constants
    private final float SPIN_DEGREES = 5 * 360f;
    private final float THREAD_OFFSET = 0.4f;

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

        // Funhaus song
        funhausSong = MediaPlayer.create(this, R.raw.wheelhaus);
        funhausSongDuration = funhausSong.getDuration();
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
        }, funhausSongDuration);

        // Animate the wheel on its own thread
        Thread animateWheel = new Thread() {
            public void run () {
                try {

                    // Determine the end angle
                    Random rand = new Random();
                    float endAngle = 0f;

                    Object[] items = wheel.getWheelDataItemsAsArray();
                    int prevChance = 0;
                    for (Object i : items) {
                        if ( !((WheelDataItem)i).getName().equals(winningItem) ) {
                            prevChance += ((WheelDataItem)i).getChance();
                        } else {
                            endAngle = ((float)prevChance / 100 +
                                    (rand.nextFloat() * ((WheelDataItem)i).getChance()) / 100) * 360f;
                        }
                    }

                    for (int i = 0; i < (funhausSongDuration * THREAD_OFFSET); i++) {
                        float lerpAmount = (float) (((float)i) / (funhausSongDuration * THREAD_OFFSET));

                        ((GLWheelView) glView).setRotationAngle( inOutQuadBlend(lerpAmount) * (SPIN_DEGREES + endAngle));
                        Thread.sleep(1);
                    }

                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        };

        animateWheel.start();

    }

    private float inOutQuadBlend(float t) {
        if (t <= 0.5f)
            return 2.0f * t * t;

        t -= 0.5f;
        return 2.0f * t * (1.0f - t) + 0.5f;
    }

    @Override
    public void onBackPressed() {
        // Exit program
        finish();
    }

}
