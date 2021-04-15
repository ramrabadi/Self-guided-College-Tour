package com.tw.ouguidedtour;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class FloorThree extends AppCompatActivity {

    private static final String TAG = "FloorThree";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_three);
        Button floor_one = (Button) findViewById(R.id.fp_btn_one);
        Button floor_two = (Button) findViewById(R.id.fp_btn_two);

        floor_one.setOnClickListener(v -> openFirstFloor());

        floor_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSecondFloor();
            }
        });
    }
    public void openFirstFloor() {
        Intent intent = new Intent(this, FloorOne.class);
        startActivity(intent);
    }

    public void openSecondFloor() {
        Intent intent = new Intent(this, FloorTwo.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, FloorPlan.class);
        startActivity(intent);
    }
}