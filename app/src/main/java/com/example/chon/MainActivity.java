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

        configWheelMenuButton();

        WheelData wd = new WheelData("Wheel test");
        wd.AddToWheel("Item 2", 20);
        wd.AddToWheel("Item 3", 20);
        wd.AddToWheel("Item 4", 20);
        wd.AddToWheel("Item 5", 20);

        TextView text = (TextView) findViewById(R.id.menu_test);
        text.setText(wd.toString());

    }

    private void configWheelMenuButton() {
        Button wheelMenuButton = (Button) findViewById(R.id.wheelMenuButton);
        wheelMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, WheelMenu.class));
            }
        });
    }

}
