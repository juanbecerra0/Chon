package com.example.chon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.opengl.GLWheelView;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

    // Chonisms
    private final String[] CHONISMS = {
            "Fix it, Chon!",
            "Ah well, typically, you know what they say",
            "I'm about to get F**KED",
            "Oh sh*t, ohohohoooooh sh*t",
            "*breathes heavily while running up stairs*",
            "They went... BACK IN TIME",
            "I've never smoked before in my life",
            "SISTER. MURDER.",
            "Israel is what Utah is to THE WORLD",
            "Oh God, my knees",
            "Nah"
    };

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

        // Chonism
        TextView chonsim = (TextView) findViewById(R.id.chonism);
        Random rand = new Random();
        chonsim.setText(CHONISMS[rand.nextInt(CHONISMS.length)]);

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

        // Add names to center layout
        LinearLayout colorKey = findViewById(R.id.colorKey);
        for(Object i : wheel.getWheelDataItemsAsArray()) {
            TextView label = new TextView(getBaseContext());
            label.setText( ((WheelDataItem)i).getName() );

            label.setTextColor(Color.parseColor( ((WheelDataItem)i).getColorAsHex() ));
            label.setTextSize(20);
            label.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            colorKey.addView(label);
        }
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

        //funhausSong.

        // Animate the wheel on its own thread
        Thread animateWheel = new Thread() {
            public void run () {
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

                Log.v("End angle: ", String.valueOf(endAngle));

                while(funhausSong.isPlaying()) {
                    float lerpAmount = (float)funhausSong.getCurrentPosition() / funhausSongDuration;

                    ((GLWheelView) glView).setRotationAngle( inOutQuadBlend(lerpAmount) * -(SPIN_DEGREES + endAngle));
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
