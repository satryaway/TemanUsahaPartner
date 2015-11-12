package com.samstudio.temanusahapartner;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.samstudio.temanusahapartner.entities.Partner;
import com.samstudio.temanusahapartner.util.APIAgent;
import com.samstudio.temanusahapartner.util.CommonConstants;
import com.samstudio.temanusahapartner.util.UniversalImageLoader;
import com.samstudio.temanusahapartner.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by satryaway on 10/10/2015.
 * activity to confirm the loan on customer side
 */
public class PartnerConfirmationActivity extends AppCompatActivity {
    private int partnerId, loanType, loanSegment, loanPeriod, personalityShape;
    private ImageView profilePicIV;
    private TextView partnerNameTV, ptNameTV, ptBranchTV, strengthTV;
    private Partner partner = new Partner();
    private UniversalImageLoader imageLoader;
    private Button sendAppBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = new UniversalImageLoader(this);
        imageLoader.initImageLoader();

        handleIntent();
        getData();
        initUI();
        setCallBack();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        partnerId = intent.getIntExtra(CommonConstants.ID, 0);
        loanType = intent.getIntExtra(CommonConstants.LOAN_TYPE, 0);
        loanSegment = intent.getIntExtra(CommonConstants.LOAN_SEGMENT, 0);
        loanPeriod = intent.getIntExtra(CommonConstants.LOAN_PERIOD, 0);
        personalityShape = intent.getIntExtra(CommonConstants.SHAPE_CODE, 0);
    }

    private void initUI() {
        setContentView(R.layout.partner_confirmation_layout);

        profilePicIV = (ImageView) findViewById(R.id.profile_picture_iv);
        partnerNameTV = (TextView) findViewById(R.id.partner_name_tv);
        ptNameTV = (TextView) findViewById(R.id.pt_name_tv);
        ptBranchTV = (TextView) findViewById(R.id.kcp_name_tv);
        strengthTV = (TextView) findViewById(R.id.pt_strength_tv);
        sendAppBtn = (Button) findViewById(R.id.send_app_btn);
    }

    private void setCallBack() {
        sendAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendApp();
            }
        });
    }

    private void sendApp() {
        String url = CommonConstants.SERVICE_SAVE_APPLICATION;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.USER_ID, TemanUsahaApplication.getInstance().getSharedPreferences().getInt(CommonConstants.ID, 0));
        requestParams.put(CommonConstants.PARTNER_ID, partnerId);
        requestParams.put(CommonConstants.PERSONALITY_SHAPE, personalityShape);
        requestParams.put(CommonConstants.LOAN_TYPE, loanType);
        requestParams.put(CommonConstants.LOAN_SEGMENT, loanSegment);
        requestParams.put(CommonConstants.LOAN_PERIOD, loanPeriod);

        APIAgent.post(url, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                progressDialog.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.hide();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int status = response.getInt(CommonConstants.STATUS);
                    if (status == CommonConstants.STATUS_OK) {
                        showDialog();
                    } else {
                        Toast.makeText(PartnerConfirmationActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(PartnerConfirmationActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(PartnerConfirmationActivity.this, R.string.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData() {
        String url = CommonConstants.SERVICE_GET_PARTNER_DETAIL + partnerId;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));

        APIAgent.get(url, null, new JsonHttpResponseHandler(){

            @Override
            public void onStart() {
                super.onStart();
                progressDialog.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.hide();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int status = response.getInt(CommonConstants.STATUS);
                    if (status == CommonConstants.STATUS_OK) {
                        partner = Utility.parsePartner(response);
                        putData();
                    } else {
                        Toast.makeText(PartnerConfirmationActivity.this, R.string.common_error_msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(PartnerConfirmationActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(PartnerConfirmationActivity.this, R.string.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.your_loan_app_has_been_sent))
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(PartnerConfirmationActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void putData() {
        imageLoader.display(profilePicIV, CommonConstants.SERVICE_PROFILE_PIC_PARTNER + partner.getProfilePicture());
        partnerNameTV.setText(partner.getFirstName() + " " + partner.getLastName());
        ptNameTV.setText(partner.getCompany());
        ptBranchTV.setText(partner.getBranch());
        strengthTV.setText(partner.getDescription());
    }
}
