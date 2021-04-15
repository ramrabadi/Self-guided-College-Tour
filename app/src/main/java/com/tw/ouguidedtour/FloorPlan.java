package com.tw.ouguidedtour;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class FloorPlan extends AppCompatActivity {

    private static final String TAG = "FloorPlan";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_plan);
        Button floor_one = (Button) findViewById(R.id.fp_btn_one);
        Button floor_two = (Button) findViewById(R.id.fp_btn_two);
        Button floor_three = (Button) findViewById(R.id.fp_btn_three);

        floor_one.setOnClickListener(v -> openFirstFloor());

        floor_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSecondFloor();
            }
        });

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

    public void openSecondFloor() {
        Intent intent = new Intent(this, FloorTwo.class);
        startActivity(intent);
    }

    public void openThirdFloor() {
        Intent intent = new Intent(this, FloorThree.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }
}
