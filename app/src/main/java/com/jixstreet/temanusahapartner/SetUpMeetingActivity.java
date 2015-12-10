package com.jixstreet.temanusahapartner;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.jixstreet.temanusahapartner.util.APIAgent;
import com.jixstreet.temanusahapartner.util.CommonConstants;
import com.jixstreet.temanusahapartner.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

import cz.msebera.android.httpclient.Header;

/**
 * Created by satryaway on 11/14/2015.
 */
public class SetUpMeetingActivity extends AppCompatActivity {
    private EditText date, venue, notes;
    private Button submit;
    private String appID;
    private long time;
    private AlertDialog alertDialog;
    private View dialogView;
    private Button setDateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        handleIntent();
        initUI();
        setCallBack();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        appID = intent.getStringExtra(CommonConstants.APP_ID);
    }

    private void initUI() {
        setContentView(R.layout.set_a_meeting_layout);

        date = (EditText) findViewById(R.id.meetup_datetime_et);
        venue = (EditText) findViewById(R.id.meetup_venue_et);
        notes = (EditText) findViewById(R.id.notes_et);
        submit = (Button) findViewById(R.id.submit_btn);

        dialogView = View.inflate(this, R.layout.date_time_picker, null);
        alertDialog = new AlertDialog.Builder(this).create();

        setDateBtn = (Button) dialogView.findViewById(R.id.date_time_set);
        alertDialog.setView(dialogView);
    }

    private void setCallBack() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (formValidated()) {
                    sendForm();
                }
            }
        });

        setDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());

                time = calendar.getTimeInMillis();
                date.setText(Utility.getRealDate(time));
                alertDialog.dismiss();
            }});

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });
    }

    private void sendForm() {
        String url = CommonConstants.SERVICE_PROCESS_APPLICATION;

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.APPLICATION_ID, appID);
        requestParams.put(CommonConstants.STATUS, CommonConstants.MEET_UP);
        requestParams.put(CommonConstants.MEETUP_DATETIME, date.getText().toString());
        requestParams.put(CommonConstants.MEETUP_VENUE, venue.getText().toString());
        requestParams.put(CommonConstants.NOTES, notes.getText().toString());

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
                        Toast.makeText(SetUpMeetingActivity.this, R.string.succed_make_appointment, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SetUpMeetingActivity.this, AppStatusActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(SetUpMeetingActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(SetUpMeetingActivity.this, R.string.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean formValidated() {
        int total = 0;

        if(date.getText().toString().isEmpty())
            date.setError(getString(R.string.should_not_be_empty_error));
        else
            total++;

        if(venue.getText().toString().isEmpty() || venue.getText().toString().length() < 10)
            venue.setError(getString(R.string.please_provide_a_valid_venue));
        else
            total++;

        return total == 2;
    }
}
