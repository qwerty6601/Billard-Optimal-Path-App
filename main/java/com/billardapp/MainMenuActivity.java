package com.sep.billardapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.sep.billardapp.example.billard_app_02.CameraActivity;

public class MainMenuActivity extends AppCompatActivity {

    CardView cvStatistics, cvBreakSpeed, cvBallPath;
    Animation scaleUp, scaleDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        cvStatistics = findViewById(R.id.cv_statistics);
        cvBreakSpeed = findViewById(R.id.cv_break_speed);
        cvBallPath = findViewById(R.id.cv_ball_path);

        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        cvStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cvStatistics.startAnimation(scaleUp);
                cvStatistics.startAnimation(scaleDown);

                try {
                    startActivity(new Intent(getApplicationContext(), PostGameActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainMenuActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cvBreakSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cvBreakSpeed.startAnimation(scaleUp);
                cvBreakSpeed.startAnimation(scaleDown);

                try {
                    startActivity(new Intent(getApplicationContext(), TableActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainMenuActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cvBallPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cvBallPath.startAnimation(scaleUp);
                cvBallPath.startAnimation(scaleDown);

                try {
                    startActivity(new Intent(getApplicationContext(), CameraActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainMenuActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}