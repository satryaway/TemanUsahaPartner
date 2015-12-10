package com.jixstreet.temanusahapartner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jixstreet.temanusahapartner.util.CommonConstants;
import com.jixstreet.temanusahapartner.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by satryaway on 11/2/2015.
 * activity to handle signing up
 */
public class SignUpActivity extends Activity {
    private EditText firstNameET, lastNameET, emailET, passwordET, confirmPasswordET;
    private Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        setCallBack();
    }

    private void initUI() {
        setContentView(R.layout.sign_up_layout);
        firstNameET = (EditText) findViewById(R.id.first_name_et);
        lastNameET = (EditText) findViewById(R.id.last_name_et);
        emailET = (EditText) findViewById(R.id.email_et);
        passwordET = (EditText) findViewById(R.id.password_et);
        confirmPasswordET = (EditText) findViewById(R.id.confirm_password_et);
        signUpBtn = (Button) findViewById(R.id.sign_up_btn);
    }

    private void setCallBack() {
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormVerified()) {
                    register();
                }
            }
        });
    }

    private void register() {
        String url = CommonConstants.SERVICE_DO_REGISTER_PARTNER;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.hide();
                            JSONObject jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt(CommonConstants.STATUS);
                            if (status == CommonConstants.STATUS_OK) {
                                saveDataInPreferences(jsonObject.getJSONObject(CommonConstants.RETURN_DATA));
                                finish();
                            }
                            Toast.makeText(SignUpActivity.this, jsonObject.getString(CommonConstants.MESSAGE), Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(CommonConstants.FIRST_NAME, firstNameET.getText().toString());
                params.put(CommonConstants.LAST_NAME, lastNameET.getText().toString());
                params.put(CommonConstants.EMAIL, emailET.getText().toString());
                params.put(CommonConstants.PASSWORD, passwordET.getText().toString());
                params.put(CommonConstants.PASSWORD_CONFIRMATION, confirmPasswordET.getText().toString());
                params.put(CommonConstants.DEVICE_ID, TemanUsahaApplication.getInstance().getSharedPreferences().getString(CommonConstants.GCM_TOKEN, ""));
                params.put(CommonConstants.LATITUDE, TemanUsahaApplication.getInstance().getSharedPreferences().getString(CommonConstants.LATITUDE, "0.0"));
                params.put(CommonConstants.LONGITUDE, TemanUsahaApplication.getInstance().getSharedPreferences().getString(CommonConstants.LONGITUDE, "0.0"));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }

    private void saveDataInPreferences(JSONObject jsonObject) {
        SharedPreferences.Editor editor = TemanUsahaApplication.getInstance().getSharedPreferences().edit();

        try {
            editor.putInt(CommonConstants.ID, jsonObject.getInt(CommonConstants.ID));
            editor.putString(CommonConstants.FIRST_NAME, jsonObject.getString(CommonConstants.FIRST_NAME));
            editor.putString(CommonConstants.LAST_NAME, jsonObject.getString(CommonConstants.LAST_NAME));
            editor.putString(CommonConstants.EMAIL, jsonObject.getString(CommonConstants.EMAIL));
            editor.putString(CommonConstants.EMAIL, jsonObject.getString(CommonConstants.EMAIL));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editor.apply();
    }

    private boolean isFormVerified() {
        int filledFormTotal = 0;

        if (firstNameET.getText().length() < 2)
            firstNameET.setError(getString(R.string.minimum_two_char_error));
        else
            filledFormTotal++;

        if (lastNameET.getText().length() < 2)
            lastNameET.setError(getString(R.string.minimum_two_char_error));
        else
            filledFormTotal++;

        if (!Utility.isEmailValid(emailET.getText().toString()))
            emailET.setError(getString(R.string.email_validation_error));
        else
            filledFormTotal++;

        if (passwordET.getText().length() < 2) {
            passwordET.setError(getString(R.string.minimum_two_char_error));
        } else {
            filledFormTotal++;
        }

        if (!passwordET.getText().toString().equals(confirmPasswordET.getText().toString())) {
            confirmPasswordET.setError(getString(R.string.confirm_password_not_matched));
        } else {
            filledFormTotal++;
        }

        return filledFormTotal == 5;
    }
}
