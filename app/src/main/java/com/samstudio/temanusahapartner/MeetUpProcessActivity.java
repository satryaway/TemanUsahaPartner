package com.samstudio.temanusahapartner;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
 * activity to display meet up process
 */
public class MeetUpProcessActivity extends AppCompatActivity {
    private TextView dateTV, venueTV;
    private String date, phoneNumber, meetupDatetime, meetupVenue;
    private ImageView callPartnerIV;
    private String notes;
    private TextView notesTV;
    private Button confirmAppIV;
    private Button cancelAppIV;
    private String appID;

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
        phoneNumber = intent.getStringExtra(CommonConstants.PHONE_NUMBER);
        meetupDatetime = intent.getStringExtra(CommonConstants.MEETUP_DATETIME);
        meetupVenue = intent.getStringExtra(CommonConstants.MEETUP_VENUE);
        notes = intent.getStringExtra(CommonConstants.NOTES);
        appID = intent.getStringExtra(CommonConstants.APP_ID);
    }

    private void initUI() {
        setContentView(R.layout.meet_up_process_layout);
        dateTV = (TextView) findViewById(R.id.date_tv);
        venueTV = (TextView) findViewById(R.id.venue_tv);
        callPartnerIV = (ImageView) findViewById(R.id.call_partner_iv);
        notesTV = (TextView) findViewById(R.id.notes_tv);
        confirmAppIV = (Button) findViewById(R.id.confirm_app_btn);
        cancelAppIV = (Button) findViewById(R.id.cancel_app_btn);
    }

    private void setCallBack() {
        callPartnerIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + phoneNumber.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                }
                startActivity(intent);
            }
        });


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
                        Toast.makeText(MeetUpProcessActivity.this, response.getString(CommonConstants.MESSAGE), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MeetUpProcessActivity.this, AppStatusActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(MeetUpProcessActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(MeetUpProcessActivity.this, R.string.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void putData() {
        dateTV.setText(meetupDatetime);
        venueTV.setText(meetupVenue);
        notesTV.setText(notes);
    }
}
