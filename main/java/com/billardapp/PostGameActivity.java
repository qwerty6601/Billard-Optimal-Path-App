package com.sep.billardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sep.billardapp.Helpers.DatabaseHelper;

public class PostGameActivity extends AppCompatActivity {

    // references to buttons and other controls on the layout
    Button btn_statistics, btn_add;
    EditText et_gamemode, et_number_of_players, et_player_1_name, et_player_2_name, et_player_3_name, et_player_4_name, et_winner, et_gametime;
    TimePickerDialog time_picker_dialog;
    ListView lv_statistics;

    ArrayAdapter gameArrayAdapter;
    DatabaseHelper databaseHelper;

    Animation scaleUp, scaleDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_game);

        btn_statistics = findViewById(R.id.btn_statistics);
        btn_add = findViewById(R.id.btn_add);
        et_gamemode = findViewById(R.id.et_gamemode);
        et_number_of_players = findViewById(R.id.et_number_of_players);
        et_player_1_name = findViewById(R.id.et_player_1_name);
        et_player_2_name = findViewById(R.id.et_player_2_name);
        et_player_3_name = findViewById(R.id.et_player_3_name);
        et_player_4_name = findViewById(R.id.et_player_4_name);
        et_winner = findViewById(R.id.et_winner);
        et_gametime = findViewById(R.id.et_gametime);
        lv_statistics = findViewById(R.id.lv_statistics);

        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        databaseHelper = new DatabaseHelper(PostGameActivity.this);

        showGamesInListView(databaseHelper);

        // listeners
        et_number_of_players.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            // configures visibilty of views that are not visible/not needed

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus && et_number_of_players.getText().toString().equals("1")) {
                    et_player_2_name.setVisibility(View.GONE);
                    et_player_3_name.setVisibility(View.GONE);
                    et_player_4_name.setVisibility(View.GONE);
                    et_winner.setVisibility(View.GONE);
                } else if (!hasFocus && et_number_of_players.getText().toString().equals("2")) {
                    et_player_2_name.setVisibility(View.VISIBLE);
                    et_player_3_name.setVisibility(View.GONE);
                    et_player_4_name.setVisibility(View.GONE);
                    et_winner.setVisibility(View.VISIBLE);
                } else if (!hasFocus && et_number_of_players.getText().toString().equals("3")) {
                    et_player_2_name.setVisibility(View.VISIBLE);
                    et_player_3_name.setVisibility(View.VISIBLE);
                    et_player_4_name.setVisibility(View.GONE);
                    et_winner.setVisibility(View.VISIBLE);
                } else if (!hasFocus && et_number_of_players.getText().toString().equals("4")) {
                    et_player_2_name.setVisibility(View.VISIBLE);
                    et_player_3_name.setVisibility(View.VISIBLE);
                    et_player_4_name.setVisibility(View.VISIBLE);
                    et_winner.setVisibility(View.VISIBLE);
                } else if (!hasFocus && !et_number_of_players.getText().toString().isEmpty()) {
                    Toast.makeText(PostGameActivity.this, "Maximale Spieleranzahl: 4", Toast.LENGTH_SHORT).show();
                }
            }
        });

        et_gametime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                time_picker_dialog = new TimePickerDialog(PostGameActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker time_picker, int hours, int minutes) {

                        int gametime = hours * 60 + minutes;
                        et_gametime.setText("" + gametime);
                    }
                }, 0, 0, true);

                time_picker_dialog.show();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_add.startAnimation(scaleUp);
                btn_add.startAnimation(scaleDown);

                GameDataModel gameDataModel;

                try {
                    gameDataModel = new GameDataModel(-1, et_gamemode.getText().toString(), Integer.parseInt(et_number_of_players.getText().toString()), et_player_1_name.getText().toString(), et_player_2_name.getText().toString(), et_player_3_name.getText().toString(), et_player_4_name.getText().toString(), et_winner.getText().toString(), et_gametime.getText().toString());
                    Toast.makeText(PostGameActivity.this, gameDataModel.toString(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(PostGameActivity.this, "Error creating game instance", Toast.LENGTH_SHORT).show();
                    gameDataModel = new GameDataModel(-1, "/", 0, "/", "/","/", "/", "/", "/");
                }

                boolean success = databaseHelper.addOne(gameDataModel);

                showGamesInListView(databaseHelper);
            }
        });

        btn_statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_statistics.startAnimation(scaleUp);
                btn_statistics.startAnimation(scaleDown);

                try {
                    startActivity(new Intent(getApplicationContext(), StatisticsActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(PostGameActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lv_statistics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                GameDataModel clickedGame = (GameDataModel) parent.getItemAtPosition(position);
                databaseHelper.deleteOne(clickedGame);
                showGamesInListView(databaseHelper);
                Toast.makeText(PostGameActivity.this, "Deleted " + clickedGame.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showGamesInListView(DatabaseHelper databaseHelper2) {
        gameArrayAdapter = new ArrayAdapter<GameDataModel>(PostGameActivity.this, android.R.layout.simple_list_item_1, databaseHelper2.getEveryGame());
        lv_statistics.setAdapter(gameArrayAdapter);
    }
}