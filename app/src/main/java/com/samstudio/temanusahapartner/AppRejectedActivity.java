package com.samstudio.temanusahapartner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by satryaway on 10/25/2015.
 * rejected app activity
 */
public class AppRejectedActivity extends AppCompatActivity {
    private Button okBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent();
        initUI();
        setCallBack();
        putData();
    }

    private void handleIntent() {
    }

    private void initUI() {
        setContentView(R.layout.app_rejected_layout);
        okBtn = (Button) findViewById(R.id.ok_btn);
    }

    private void setCallBack() {
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void putData() {
    }
}
