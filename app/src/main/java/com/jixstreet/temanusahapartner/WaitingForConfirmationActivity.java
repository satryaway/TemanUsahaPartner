package com.jixstreet.temanusahapartner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jixstreet.temanusahapartner.util.CommonConstants;

/**
 * Created by satryaway on 10/25/2015.
 * waiting for approval page
 */
public class WaitingForConfirmationActivity extends AppCompatActivity {
    private String date;
    private TextView dateTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent();
        initUI();
        setCallBack();
        putData();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        date = intent.getStringExtra(CommonConstants.DATE);
    }

    private void initUI() {
        setContentView(R.layout.waiting_for_approval_layout);
        dateTV = (TextView) findViewById(R.id.date_tv);
    }

    private void setCallBack() {
    }

    private void putData() {
        dateTV.setText(date);
    }
}
