package com.samstudio.temanusahapartner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.samstudio.temanusahapartner.util.CommonConstants;

/**
 * Created by satryaway on 10/25/2015.
 * rejected app activity
 */
public class AppRejectedActivity extends AppCompatActivity {
    private Button okBtn;
    private boolean isCancelled;
    private TextView sorryTV;
    private TextView messageTV;
    private TextView pleaseSubmitAgainTV;

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
        isCancelled = intent.getBooleanExtra(CommonConstants.IS_CANCELLED, false);
    }

    private void initUI() {
        setContentView(R.layout.app_rejected_layout);
        okBtn = (Button) findViewById(R.id.ok_btn);
        sorryTV = (TextView) findViewById(R.id.sorry_tv);
        messageTV = (TextView) findViewById(R.id.message_tv);
        pleaseSubmitAgainTV = (TextView) findViewById(R.id.please_submit_again_tv);
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
        if (isCancelled) {
            sorryTV.setVisibility(View.GONE);
            okBtn.setVisibility(View.GONE);
            pleaseSubmitAgainTV.setVisibility(View.GONE);
            messageTV.setText(getResources().getString(R.string.customer_has_cancelled_an_app));
        }
    }
}
