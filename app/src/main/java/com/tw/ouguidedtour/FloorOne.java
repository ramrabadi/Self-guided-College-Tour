package com.tw.ouguidedtour;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

public class FloorOne extends AppCompatActivity {

    private static final String TAG = "FloorOne";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_one);
        Button floor_two = (Button) findViewById(R.id.fp_btn_two);
        Button floor_three = (Button) findViewById(R.id.fp_btn_three);

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
        Intent intent = new Intent(this, FloorPlan.class);
        startActivity(intent);
    }
}