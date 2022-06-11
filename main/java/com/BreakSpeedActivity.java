package com.sep.billardapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class BreakSpeedActivity extends AppCompatActivity {

    final int RECORD_AUDIO = 0;
    ImageButton btnStart;
    Spinner spinDuration;
    TextView tvBreakSpeed;
    int clipDuration = 0;
    LongOperation recordAudioSync = null;
    String[] durationItems = {"5", "10", "15", "20"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_break_speed);

        btnStart = findViewById(R.id.btn_start);
        spinDuration = findViewById(R.id.duration);
        tvBreakSpeed = findViewById(R.id.tv_break_speed);

        ArrayAdapter<String> spinDurationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, durationItems);
        spinDurationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDuration.setAdapter(spinDurationAdapter);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clipDuration = Integer.parseInt(spinDuration.getSelectedItem().toString());

                if (ActivityCompat.checkSelfPermission(BreakSpeedActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BreakSpeedActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO);
                } else {
                    recordAudioSync = new LongOperation();
                    recordAudioSync.execute("");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (recordAudioSync != null && recordAudioSync.getStatus() != AsyncTask.Status.FINISHED) {
            recordAudioSync.done();
            recordAudioSync.cancel(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (recordAudioSync != null && recordAudioSync.getStatus() != AsyncTask.Status.FINISHED) {
            recordAudioSync.done();
            recordAudioSync.cancel(true);
        }
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        MediaRecorder recorder;
        double difference;

        @Override
        protected String doInBackground(String... strings) {

            recordAudio();
            Intent intent = getIntent();
            int distance = intent.getIntExtra("EXTRA_DISTANCE", 0);
            float inchesPerSecond = (float) (distance / (difference / 1000000000));
            float velocity = (float) (inchesPerSecond / 10.9361);
            return "" + velocity;
        }

        @Override
        protected void onPreExecute() {

            btnStart.setImageResource(R.drawable.mic_off);
            btnStart.setEnabled(false);
        }

        @Override
        protected void onPostExecute(String s) {

            tvBreakSpeed.setText("Your break speed was " + s + " km/h");
            btnStart.setImageResource(R.drawable.mic_on);
            btnStart.setEnabled(true);
        }

        private void recordAudio() {

            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile("/data/data/" + getPackageName() + "/music.3gp");
            int startAmplitude = 0;
            int maxAmplitude;
            int finishAmplitude;
            int amplitudeThreshold = 18000;
            int counter = 0;
            long startTime = 0;
            long finishTime = 0;
            final String START_TAG = "Start amplitude";
            final String FINISH_TAG = "Finish amplitude";
            final String START_TIME_TAG = "Start time";
            final String FINISH_TIME_TAG = "Finish time";

            try {
                recorder.prepare();
                recorder.start();
                startAmplitude = recorder.getMaxAmplitude();
                Log.d(START_TAG,"First amplitude: " + startAmplitude);
            } catch (IOException e) {
                e.printStackTrace();
            }

            do {
                if(isCancelled()) {
                    break;
                }
                counter++;
                waitSome();
                maxAmplitude = recorder.getMaxAmplitude();
                if (maxAmplitude >= amplitudeThreshold && startTime == 0) {
                    Log.d(START_TAG, "Start amplitude: " + maxAmplitude);
                    startTime = System.nanoTime();
                    Log.d(START_TIME_TAG, "Starting time: " + startTime);
                } else if (maxAmplitude >= amplitudeThreshold && startTime != 0) {
                    Log.d(FINISH_TAG, "Finishing amplitude: " + maxAmplitude);
                    finishTime = System.nanoTime();
                    Log.d(FINISH_TIME_TAG, "Finishing time: " + finishTime);
                }
            } while (counter < (clipDuration * 4));
            difference = finishTime - startTime;
            done();
        }

        private void done() {
            if (recorder != null) {
                recorder.stop();
                recorder.release();
            }
        }

        private void waitSome() {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}