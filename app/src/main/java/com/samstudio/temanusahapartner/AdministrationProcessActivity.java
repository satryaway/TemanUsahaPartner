package com.samstudio.temanusahapartner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.samstudio.temanusahapartner.util.CommonConstants;


/**
 * Created by satryaway on 10/17/2015.
 * activity for administration process
 */
public class AdministrationProcessActivity extends AppCompatActivity {
    private String date;
    private TextView dateTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleIntent();
        initUI();
        setCallBack();
        setData();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        date = intent.getStringExtra(CommonConstants.DATE);
    }

    private void initUI() {
        setContentView(R.layout.administration_process_layout);
        dateTV = (TextView) findViewById(R.id.date_tv);
    }

    private void setCallBack() {

    }

    private void setData() {
        dateTV.setText(date);
    }
}
