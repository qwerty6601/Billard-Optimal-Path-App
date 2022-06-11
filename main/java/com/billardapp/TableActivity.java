package com.sep.billardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

public class TableActivity extends AppCompatActivity {

    int distance;

    Animation scaleUp, scaleDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        Button btnSevenFeet = findViewById(R.id.btn_seven_feet);
        Button btnEightFeet = findViewById(R.id.btn_eight_feet);
        Button btnNineFeet = findViewById(R.id.btn_nine_feet);
        Button btnTenFeet = findViewById(R.id.btn_ten_feet);

        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        btnSevenFeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int distance = 39;

                btnSevenFeet.startAnimation(scaleUp);
                btnSevenFeet.startAnimation(scaleDown);

                try {
                    Intent intent = new Intent(TableActivity.this, BreakSpeedActivity.class);
                    intent.putExtra("EXTRA_DISTANCE", distance);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(TableActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnEightFeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                distance = 44;

                btnEightFeet.startAnimation(scaleUp);
                btnEightFeet.startAnimation(scaleDown);

                try {
                    Intent intent = new Intent(TableActivity.this, BreakSpeedActivity.class);
                    intent.putExtra("EXTRA_DISTANCE", distance);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(TableActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnNineFeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                distance = 50;

                btnNineFeet.startAnimation(scaleUp);
                btnNineFeet.startAnimation(scaleDown);

                try {
                    Intent intent = new Intent(TableActivity.this, BreakSpeedActivity.class);
                    intent.putExtra("EXTRA_DISTANCE", distance);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(TableActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnTenFeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                distance = 56;

                btnTenFeet.startAnimation(scaleUp);
                btnTenFeet.startAnimation(scaleDown);

                try {
                    Intent intent = new Intent(TableActivity.this, BreakSpeedActivity.class);
                    intent.putExtra("EXTRA_DISTANCE", distance);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(TableActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}