package com.jixstreet.temanusahapartner;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.jixstreet.temanusahapartner.util.APIAgent;
import com.jixstreet.temanusahapartner.util.CommonConstants;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by satryaway on 11/12/2015.
 * activity for forgot password
 */
public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText emailET;
    private Button sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        setCallBack();
    }

    private void initUI() {
        setContentView(R.layout.forgot_password_layout);

        emailET = (EditText) findViewById(R.id.email_et);
        sendBtn = (Button) findViewById(R.id.send_btn);
    }

    private void setCallBack() {
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailET.getText().toString().isEmpty()) {
                    emailET.setError(getString(R.string.should_not_be_empty_error));
                } else {
                    sendNewPasswordRequest();
                }
            }
        });
    }

    private void sendNewPasswordRequest() {
        String url = CommonConstants.SERVICE_FORGOT_PASSWORD;

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.TYPE, CommonConstants.USER);
        requestParams.put(CommonConstants.EMAIL, emailET.getText().toString());

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));

        APIAgent.post(url, requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                super.onStart();
                progressDialog.show();
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getInt(CommonConstants.STATUS) == CommonConstants.STATUS_OK) {
                        finish();
                    }

                    Toast.makeText(ForgotPasswordActivity.this, response.getString(CommonConstants.MESSAGE), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ForgotPasswordActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(ForgotPasswordActivity.this, R.string.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.dismiss();
            }
        });
    }
}
