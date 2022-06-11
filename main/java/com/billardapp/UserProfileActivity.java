package com.sep.billardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.sep.billardapp.Helpers.DatabaseHelper;

import java.util.Calendar;

public class UserProfileActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button datebutton;
    Animation scaleUp, scaleDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Button btnNutzerspeichern = findViewById(R.id.btnNutzerspeichern);
        Button btnWeiter = findViewById(R.id.btnweiter);
        Button btnGeburtsdatum = findViewById(R.id.btnGeburtsdatum);

        EditText vorname = findViewById(R.id.ptVorname);
        EditText nachname = findViewById(R.id.ptNachname);
        EditText nickname = findViewById(R.id.ptNickname);

        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        btnNutzerspeichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                btnNutzerspeichern.startAnimation(scaleUp);
                btnNutzerspeichern.startAnimation(scaleDown);
                
                PlayerDataModel playerDataModel;

                try {
                    playerDataModel = new PlayerDataModel(-1, vorname.getText().toString(), nachname.getText().toString(), nickname.getText().toString(), btnGeburtsdatum.getText().toString());
                    Toast.makeText(UserProfileActivity.this, playerDataModel.toString(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    playerDataModel = new PlayerDataModel(-1, "/", "/", "/", "/");
                }

                DatabaseHelper databaseHelper = new DatabaseHelper(UserProfileActivity.this);

                boolean success = databaseHelper.addPlayer(playerDataModel);

                try {
                    Intent intent = new Intent(UserProfileActivity.this, MainMenuActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(UserProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }

            }
        });


        btnWeiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnWeiter.startAnimation(scaleUp);
                btnWeiter.startAnimation(scaleDown);

                try {
                    //setContentView(R.layout.activity_main);
                    startActivity(new Intent(UserProfileActivity.this, MainMenuActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(UserProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });



        btnGeburtsdatum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatePicker();
                datebutton = findViewById(R.id.btnGeburtsdatum);
                datebutton.setText(getTodaysDate());
                openDate(v);
            }
        });
    }

    private String getTodaysDate()

    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month +1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year);

    }

    public String makeDateString(int dayOfMonth, int month, int year)

    {
        return getMonthFormat(month) + " " + dayOfMonth + " " + year;
    }

    public String getMonthFormat(int month)

    {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAI";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";

        return "JAN";
    }


    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = makeDateString(dayOfMonth, month, year);
                datebutton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog (this, style, dateSetListener, year, month, dayOfMonth);

    }

    public void openDate(View view)

    {
        datePickerDialog.show();
    }

}
