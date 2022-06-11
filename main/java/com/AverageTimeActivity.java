package com.sep.billardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sep.billardapp.Helpers.DatabaseHelper;

public class AverageTimeActivity extends AppCompatActivity {

    Animation scaleUp, scaleDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_time);

        Button btn_show_average_time = findViewById(R.id.btn_show_average_time);

        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        btn_show_average_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText et_name_average_time = findViewById(R.id.et_name_average_time);

                btn_show_average_time.startAnimation(scaleUp);
                btn_show_average_time.startAnimation(scaleDown);

                fetchAverageTime();
                et_name_average_time.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });
    }

    private void fetchAverageTime() {

        EditText et_name_average_time = findViewById(R.id.et_name_average_time);
        TextView tv_average_time = findViewById(R.id.tv_average_time);
        DatabaseHelper databaseHelper = new DatabaseHelper(AverageTimeActivity.this);

        tv_average_time.setText("Your average playtime is: " + String.valueOf(databaseHelper.calculateAverageTime(et_name_average_time)) + " Minuten");
    }
}