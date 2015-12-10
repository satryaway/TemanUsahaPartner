package com.jixstreet.temanusahapartner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.jixstreet.temanusahapartner.entities.CreditCeiling;
import com.jixstreet.temanusahapartner.entities.CreditPurpose;
import com.jixstreet.temanusahapartner.entities.TimeRange;
import com.jixstreet.temanusahapartner.util.APIAgent;
import com.jixstreet.temanusahapartner.util.CommonConstants;
import com.jixstreet.temanusahapartner.util.Seeder;
import com.jixstreet.temanusahapartner.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * Created by satryaway on 10/17/2015.
 * activity for administration process
 */
public class WaitingForProcessActivity extends AppCompatActivity {
    private String date;
    private TextView dateTV, customerNameTV, loanTypeTV, loanSegmentTV, loanPeriodTV, jobTV, ptNameTV;
    private TextView ageTV, maritalStatusTV;
    private Button confirmAppIV;
    private Button cancelAppIV;
    private ImageView getCIFIV;
    private String appID, customerName, loanType, loanSegment, loanPeriod, job, ptName, age, maritalStatus;
    private List<CreditPurpose> loanTypeList = new ArrayList<>();
    private List<CreditCeiling> loanSegmentList = new ArrayList<>();
    private List<TimeRange> loanPeriodList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleIntent();
        collectData();
        initUI();
        setCallBack();
        setData();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        date = intent.getStringExtra(CommonConstants.DATE);
        appID = intent.getStringExtra(CommonConstants.APP_ID);
        customerName = intent.getStringExtra(CommonConstants.FIRST_NAME);
        loanType = intent.getStringExtra(CommonConstants.LOAN_TYPE);
        loanSegment = intent.getStringExtra(CommonConstants.LOAN_SEGMENT);
        loanPeriod = intent.getStringExtra(CommonConstants.LOAN_PERIOD);
        job = intent.getStringExtra(CommonConstants.JOB);
        ptName = intent.getStringExtra(CommonConstants.COMPANY_NAME);
        age = intent.getStringExtra(CommonConstants.DATE_OF_BIRTH);
        maritalStatus = intent.getStringExtra(CommonConstants.MARITAL_STATUS);
    }

    private void collectData() {
        loanTypeList = Seeder.getCreditPurpose(this);
        loanSegmentList = Seeder.getCreditCeiling(this);
        loanPeriodList = Seeder.getTimeRange(this);
    }

    private void initUI() {
        setContentView(R.layout.administration_process_layout);
        dateTV = (TextView) findViewById(R.id.date_tv);
        confirmAppIV = (Button) findViewById(R.id.confirm_app_btn);
        cancelAppIV = (Button) findViewById(R.id.cancel_app_btn);
        customerNameTV = (TextView) findViewById(R.id.customer_name_tv);
        loanTypeTV = (TextView) findViewById(R.id.loan_type_tv);
        loanSegmentTV = (TextView) findViewById(R.id.loan_segment_tv);
        loanPeriodTV = (TextView) findViewById(R.id.time_range_tv);
        jobTV = (TextView) findViewById(R.id.pekerjaan_tv);
        ptNameTV = (TextView) findViewById(R.id.pt_name_tv);
        ageTV = (TextView) findViewById(R.id.age_tv);
        maritalStatusTV = (TextView) findViewById(R.id.status_tv);
        getCIFIV = (ImageView) findViewById(R.id.get_cif_iv);
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

        getCIFIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(CommonConstants.SERVICE_DOWNLOAD_CIF + appID);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private void saveApplication(boolean b) {
        String url = CommonConstants.SERVICE_PROCESS_APPLICATION;

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.APPLICATION_ID, appID);
        requestParams.put(CommonConstants.STATUS, b ? CommonConstants.PROCESS : CommonConstants.REJECTED);

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
                        Toast.makeText(WaitingForProcessActivity.this, response.getString(CommonConstants.MESSAGE), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(WaitingForProcessActivity.this, AppStatusActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(WaitingForProcessActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(WaitingForProcessActivity.this, R.string.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData() {
        customerNameTV.setText(customerName);
        loanTypeTV.setText(loanTypeList.get(Integer.valueOf(loanType) - 1).getName());
        loanSegmentTV.setText(loanSegmentList.get(Integer.valueOf(loanSegment) - 1).getName());
        loanPeriodTV.setText(loanPeriodList.get(Integer.valueOf(loanPeriod) - 1).getName());
        jobTV.setText(job);
        ptNameTV.setText(ptName);
        ageTV.setText(Utility.getAge(age) + " Tahun");
        maritalStatusTV.setText(maritalStatus);
    }
}
