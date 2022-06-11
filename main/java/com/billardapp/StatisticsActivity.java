package com.sep.billardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

public class StatisticsActivity extends AppCompatActivity {

    Animation scaleUp, scaleDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Button btn_chart_wins = findViewById(R.id.btn_chart_wins);
        Button btn_comparison = findViewById(R.id.btn_comparison);
        Button btn_average_time = findViewById(R.id.btn_average_time);

        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        btn_chart_wins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_chart_wins.startAnimation(scaleUp);
                btn_chart_wins.startAnimation(scaleDown);

                try {
                    startActivity(new Intent(getApplicationContext(), ChartWinsActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(StatisticsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_comparison.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_comparison.startAnimation(scaleUp);
                btn_comparison.startAnimation(scaleDown);

                try {
                    startActivity(new Intent(getApplicationContext(), ComparisonActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(StatisticsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_average_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_average_time.startAnimation(scaleUp);
                btn_average_time.startAnimation(scaleDown);

                try {
                    startActivity(new Intent(getApplicationContext(), AverageTimeActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(StatisticsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}