package com.samstudio.temanusahapartner;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

/**
 * Created by satryaway on 10/9/2015.
 * count down timer activity to wait partner to grab
 */
public class CountDownActivity extends AppCompatActivity {
    private TextView countDownTV;
    private static final String FORMAT = "%02d:%02d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        setCallBack();
    }

    private void initUI() {
        setContentView(R.layout.count_down_layout);

        countDownTV = (TextView) findViewById(R.id.count_down_timer_tv);

        countDownStart();
    }

    private void setCallBack() {

    }

    public void countDownStart() {
        new CountDownTimer(600000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                countDownTV.setText("" + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                countDownTV.setText("done!");
            }
        }.start();


    }
}
