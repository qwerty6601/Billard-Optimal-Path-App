package com.sep.billardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sep.billardapp.Helpers.DatabaseHelper;

import java.util.ArrayList;

public class ChartWinsActivity extends AppCompatActivity {

    Animation scaleUp, scaleDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_wins);

        Button btn_show = findViewById(R.id.btn_show);

        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText et_name_stats = findViewById(R.id.et_name_stats);

                btn_show.startAnimation(scaleUp);
                btn_show.startAnimation(scaleDown);

                winLossChart();
                et_name_stats.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });
    }

    private void winLossChart() {
        // fetch user's wins and losses from database and put them into pie chart

        PieChart piechart = findViewById(R.id.pie_chart);
        EditText et_name_stats = findViewById(R.id.et_name_stats);
        DatabaseHelper databaseHelper = new DatabaseHelper(ChartWinsActivity.this);

        ArrayList<PieEntry> games = new ArrayList<>();
        try {
            games.add(new PieEntry(databaseHelper.fetchWinsCount(et_name_stats), "Wins"));
            games.add(new PieEntry(databaseHelper.fetchLossesCount(et_name_stats), "Losses"));
        } catch (Exception e) {
            // player not found, cannot show chart
            e.printStackTrace();
        }


        PieDataSet pieDataSet = new PieDataSet(games, "Games");
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);

        piechart.setData(pieData);
        piechart.getDescription().setEnabled(false);
        piechart.setCenterText("Spiele");
        piechart.animate();
    }

}