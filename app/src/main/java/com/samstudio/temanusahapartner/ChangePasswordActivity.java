package com.samstudio.temanusahapartner;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.samstudio.temanusahapartner.util.APIAgent;
import com.samstudio.temanusahapartner.util.CommonConstants;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by satryaway on 9/14/2015.
 * activity to change user's password
 */
public class ChangePasswordActivity extends AppCompatActivity {
    private EditText oldPasswordET, newPasswordET, confirmNewPasswordET;
    private ImageView saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        setCallBack();
    }

    private void initUI() {
        setContentView(R.layout.change_password_layout);

        oldPasswordET = (EditText) findViewById(R.id.old_password_et);
        newPasswordET = (EditText) findViewById(R.id.new_password_et);
        confirmNewPasswordET = (EditText) findViewById(R.id.confirm_new_password_et);
        saveBtn = (ImageView) findViewById(R.id.save_btn);
    }

    private void setCallBack() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    changePasswordRequest();
                }
            }
        });
    }

    private void changePasswordRequest() {
        String url = CommonConstants.SERVICE_CHANGE_PASSWORD;

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.ID, TemanUsahaApplication.getInstance().getSharedPreferences().getInt(CommonConstants.ID, 0));
        requestParams.put(CommonConstants.TYPE, CommonConstants.USER);
        requestParams.put(CommonConstants.CURRENT_PASSWORD, oldPasswordET.getText().toString());
        requestParams.put(CommonConstants.PASSWORD, newPasswordET.getText().toString());
        requestParams.put(CommonConstants.PASSWORD_CONFIRMATION, confirmNewPasswordET.getText().toString());

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));

        APIAgent.post(url, requestParams, new JsonHttpResponseHandler(){
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
                    if (status == CommonConstants.STATUS_OK)
                        finish();

                    Toast.makeText(ChangePasswordActivity.this, response.getString(CommonConstants.MESSAGE), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ChangePasswordActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(ChangePasswordActivity.this, R.string.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateForm() {
        int total = 0;

        if (oldPasswordET.getText().toString().isEmpty()){
            oldPasswordET.setError(getString(R.string.should_not_be_empty_error));
        } else {
            total++;
        }

        if (!newPasswordET.getText().toString().equals(confirmNewPasswordET.getText().toString())){
            newPasswordET.setError(getString(R.string.password_not_matched));
            confirmNewPasswordET.setError(getString(R.string.password_not_matched));
        } else {
            total++;
        }

        return total == 2;
    }
}
