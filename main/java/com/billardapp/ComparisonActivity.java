package com.sep.billardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sep.billardapp.Helpers.DatabaseHelper;

import java.util.ArrayList;

public class ComparisonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparison);

        comparisonChart();
    }

    private void comparisonChart() {
        // fetch a list of all users who have won at least one game and count their wins, put them into the pie chart

        PieChart piechart = findViewById(R.id.pie_chart);
        DatabaseHelper databaseHelper = new DatabaseHelper(ComparisonActivity.this);

        ArrayList<PieEntry> games = new ArrayList<>();

        for (int i = 0; i <= databaseHelper.fetchWinnerList().size() - 1; i++) {
            try {
                games.add(new PieEntry(databaseHelper.fetchWinsPerName(i), "" + databaseHelper.fetchWinnerList().get(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        PieDataSet pieDataSet = new PieDataSet(games, "Won Games");
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);

        piechart.setData(pieData);
        piechart.getDescription().setEnabled(false);
        piechart.animate();
    }
}