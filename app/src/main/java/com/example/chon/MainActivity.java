package com.example.chon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configButtons();

        WheelData wd = new WheelData("Wheel test", false);
        wd.AddToWheel("Item 3");

        WheelData wd2 = new WheelData(wd.getJsonString(), true);

        TextView text = (TextView) findViewById(R.id.menu_test);
        text.setText(wd2.toString() + "\n\n" + wd2.getJsonString());
    }

    private void configButtons() {
        Button wheelMenuButton = (Button) findViewById(R.id.wheelMenuButton);
        wheelMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, WheelMenu.class));
            }
        });
    }

}
