package com.samstudio.temanusahapartner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.samstudio.temanusahapartner.util.APIAgent;
import com.samstudio.temanusahapartner.util.CommonConstants;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


/**
 * Created by satryaway on 10/17/2015.
 * activity for administration process
 */
public class AdministrationProcessActivity extends AppCompatActivity {
    private String date;
    private TextView dateTV;
    private ImageView confirmAppIV, cancelAppIV;
    private String appID;
    private Button setUpMeetingBtn;


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
        appID = intent.getStringExtra(CommonConstants.APP_ID);
    }

    private void initUI() {
        setContentView(R.layout.administration_process_layout);
        dateTV = (TextView) findViewById(R.id.date_tv);
        confirmAppIV = (ImageView) findViewById(R.id.confirm_app_iv);
        cancelAppIV = (ImageView) findViewById(R.id.cancel_app_iv);
        setUpMeetingBtn = (Button) findViewById(R.id.meetup_btn);
    }

    private void setCallBack() {

        confirmAppIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveApplication(true);
            }
        });

        cancelAppIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveApplication(false);
            }
        });

        setUpMeetingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdministrationProcessActivity.this, SetUpMeetingActivity.class);
                intent.putExtra(CommonConstants.APP_ID, appID);
                startActivity(intent);
            }
        });
    }

    private void saveApplication(boolean b) {
        String url = CommonConstants.SERVICE_PROCESS_APPLICATION;

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.APPLICATION_ID, appID);
        requestParams.put(CommonConstants.STATUS, b ? CommonConstants.APPROVED : CommonConstants.REJECTED);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));

        APIAgent.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onFinish() {
                progressDialog.hide();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int status = response.getInt(CommonConstants.STATUS);
                    if (status == CommonConstants.STATUS_OK) {
                        Toast.makeText(AdministrationProcessActivity.this, response.getString(CommonConstants.MESSAGE), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdministrationProcessActivity.this, AppStatusActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(AdministrationProcessActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(AdministrationProcessActivity.this, R.string.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setData() {
        dateTV.setText(date);
    }
}
