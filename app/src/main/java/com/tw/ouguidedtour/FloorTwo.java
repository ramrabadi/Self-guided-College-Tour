package com.tw.ouguidedtour;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class FloorTwo extends AppCompatActivity {

    private static final String TAG = "FloorTwo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_two);
        Button floor_one = (Button) findViewById(R.id.fp_btn_one);
        Button floor_three = (Button) findViewById(R.id.fp_btn_three);

        floor_one.setOnClickListener(v -> openFirstFloor());

        floor_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openThirdFloor();
            }
        });
    }
    public void openFirstFloor() {
        Intent intent = new Intent(this, FloorOne.class);
        startActivity(intent);
    }


    public void openThirdFloor() {
        Intent intent = new Intent(this, FloorThree.class);
        startActivity(intent);
    }
}
