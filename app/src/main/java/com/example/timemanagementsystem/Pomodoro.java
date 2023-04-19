package com.example.timemanagementsystem;

import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class Pomodoro extends AppCompatActivity {

    private static final long START_TIME_IN_MILLIS = 1500000;

    private TextView tvCountDown;
    private Button btnStartPause, btnReset;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pomodoro);

        tvCountDown = findViewById(R.id.textViewCountDown);
        btnStartPause = findViewById(R.id.button_startPause);
        btnReset = findViewById(R.id.button_reset);

        btnStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        updateCountDownText();

    }
    private void startTimer(){
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                btnStartPause.setText("Start");
                btnStartPause.setVisibility(View.INVISIBLE);
                btnReset.setVisibility(View.VISIBLE);

            }
        }.start();

        mTimerRunning = true;
        btnStartPause.setText("Pause");
        btnReset.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer(){
        mCountDownTimer.cancel();
        mTimerRunning = false;
        btnStartPause.setText("Start");
        btnReset.setVisibility(View.VISIBLE);

    }

    private void resetTimer(){
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        btnReset.setVisibility(View.INVISIBLE);
        btnStartPause.setVisibility(View.VISIBLE);

    }

    private void updateCountDownText(){
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeft = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);

        tvCountDown.setText(timeLeft);
    }
}
