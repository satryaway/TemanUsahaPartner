package com.jixstreet.temanusahapartner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by satryaway on 10/10/2015.
 * data confirmation page
 */
public class DeliveryDataConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        setCallBack();
    }

    private void initUI() {
        setContentView(R.layout.delivery_data_confirmation_layout);
    }

    private void setCallBack() {

    }
}
