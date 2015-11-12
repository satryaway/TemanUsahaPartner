package com.samstudio.temanusahapartner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.samstudio.temanusahapartner.util.APIAgent;
import com.samstudio.temanusahapartner.util.CommonConstants;
import com.samstudio.temanusahapartner.util.UniversalImageLoader;
import com.samstudio.temanusahapartner.util.Utility;
import com.soundcloud.android.crop.Crop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cz.msebera.android.httpclient.Header;

/**
 * Created by satryaway on 9/17/2015.
 * activity to input profile
 */
public class CustomerProfileActivity extends AppCompatActivity {
    private ImageView profilePictureIV;
    private EditText firstNameET, lastNameET, placeOfBirthET, dateOfBirthET, idCardNumberET, expiredIdET,
            emailET, addressET, phoneNumberET;
    private RadioButton maleRB, femaleRB, marriedRB, singleRB, employeeRB;
    private Button saveBtn;
    private boolean isPickDateOfBirth;
    private DatePickerDialog datePickerDialog;
    private SharedPreferences sharedPreferences;
    private File userImageFile = null;
    private TextView jobPositionTV, monthlyIncomeTV, educationExpensesTV;
    private TextView transportExpensesTV, companyAddressTV, monthlyAssetTV;
    private TextView employeeWageTV;
    private EditText companyNameET, workSinceET, jobPositionET, monthlyIncomeET, educationExpensesET, householdExpensesET;
    private EditText transportExpensesET, waterAndElectricyExpensesET, miscExpensesET, companyAddressET, monthlyAssetET;
    private EditText employeeWageET;
    private RadioButton entrepreneurRB;
    private UniversalImageLoader imageLoader;
    private EditText kasSaatIniET, tabunganET, persediaanBarangET, rumahTanahET, kendaraanET, alatUsahaET, barangBerhargaET, piutangET, hutangBankET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        sharedPreferences = TemanUsahaApplication.getInstance().getSharedPreferences();
        imageLoader = new UniversalImageLoader(this);
        imageLoader.initImageLoader();
        initUI();
        setCallBack();
        getData();
        //putData();
    }

    private void getData() {
        String url = CommonConstants.SERVICE_GET_USER_DETAIL + TemanUsahaApplication.getInstance().getSharedPreferences().getInt(CommonConstants.ID, 1);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);

        APIAgent.get(url, null, new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getInt(CommonConstants.STATUS) == CommonConstants.STATUS_OK) {
                        putData(response);
                    } else {
                        Toast.makeText(CustomerProfileActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(CustomerProfileActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(CustomerProfileActivity.this, R.string.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                progressDialog.dismiss();
            }
        });
    }

    private void initUI() {
        setContentView(R.layout.customer_profile_layout);
        profilePictureIV = (ImageView) findViewById(R.id.profile_picture_iv);
        firstNameET = (EditText) findViewById(R.id.first_name_et);
        lastNameET = (EditText) findViewById(R.id.last_name_et);
        maleRB = (RadioButton) findViewById(R.id.male_rb);
        femaleRB = (RadioButton) findViewById(R.id.female_rb);
        placeOfBirthET = (EditText) findViewById(R.id.place_of_birth_et);
        dateOfBirthET = (EditText) findViewById(R.id.date_of_birth_et);
        idCardNumberET = (EditText) findViewById(R.id.id_card_et);
        expiredIdET = (EditText) findViewById(R.id.expired_id_et);
        emailET = (EditText) findViewById(R.id.email_et);
        addressET = (EditText) findViewById(R.id.address_et);
        phoneNumberET = (EditText) findViewById(R.id.phone_number_et);
        marriedRB = (RadioButton) findViewById(R.id.married_rb);
        singleRB = (RadioButton) findViewById(R.id.single_rb);
        saveBtn = (Button) findViewById(R.id.save_btn);
        maleRB.setChecked(true);
        marriedRB.setChecked(true);

        jobPositionTV = (TextView) findViewById(R.id.job_position_tv);
        monthlyIncomeTV = (TextView) findViewById(R.id.monthly_income_tv);
        educationExpensesTV = (TextView) findViewById(R.id.education_expenses_tv);
        transportExpensesTV = (TextView) findViewById(R.id.transport_expenses_tv);
        companyAddressTV = (TextView) findViewById(R.id.company_address_tv);
        monthlyAssetTV = (TextView) findViewById(R.id.monthly_asset_tv);
        employeeWageTV = (TextView) findViewById(R.id.employee_wage_tv);

        companyNameET = (EditText) findViewById(R.id.company_name_et);
        workSinceET = (EditText) findViewById(R.id.work_since_et);
        jobPositionET = (EditText) findViewById(R.id.job_position_et);
        monthlyIncomeET = (EditText) findViewById(R.id.monthly_income_et);
        educationExpensesET = (EditText) findViewById(R.id.education_expenses_et);
        householdExpensesET = (EditText) findViewById(R.id.household_expenses_et);
        transportExpensesET = (EditText) findViewById(R.id.transport_expenses_et);
        waterAndElectricyExpensesET = (EditText) findViewById(R.id.water_and_electricy_expenses_et);
        miscExpensesET = (EditText) findViewById(R.id.misc_expenses_et);
        companyAddressET = (EditText) findViewById(R.id.company_address_et);
        monthlyAssetET = (EditText) findViewById(R.id.monthly_asset_et);
        employeeWageET = (EditText) findViewById(R.id.employee_wage_et);

        kasSaatIniET = (EditText) findViewById(R.id.kas_saat_ini_et);
        tabunganET = (EditText) findViewById(R.id.tabungan_et);
        persediaanBarangET = (EditText) findViewById(R.id.persediaan_barang_et);
        rumahTanahET = (EditText) findViewById(R.id.rumah_tanah_yang_dimiliki_et);
        kendaraanET= (EditText) findViewById(R.id.kendaraan_yang_dimiliki_et);
        alatUsahaET = (EditText) findViewById(R.id.alat_usaha_et);
        barangBerhargaET = (EditText) findViewById(R.id.barang_berharga_et);
        piutangET = (EditText) findViewById(R.id.piutang_et);
        hutangBankET = (EditText) findViewById(R.id.hutang_bank_et);

        employeeRB = (RadioButton) findViewById(R.id.employee_rb);
        entrepreneurRB = (RadioButton) findViewById(R.id.entrepreneur_rb);

        employeeRB.setChecked(true);
        showFields(true);

        dateOfBirthET.setFocusable(false);
        expiredIdET.setFocusable(false);
        datePickerDialog = new DatePickerDialog(this, dateListener, 1990, 1, 1);
    }

    private void setCallBack() {
        profilePictureIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePopupDialog();
            }
        });

        employeeRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showFields(isChecked);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormVerified()) {
                    try {
                        updateProfile();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        monthlyIncomeET.addTextChangedListener(new TextWatcher() {

            boolean isManualChange = false;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (isManualChange) {
                    isManualChange = false;
                    return;
                }

                try {
                    String value = s.toString().replace(",", "");
                    isManualChange = true;
                    monthlyIncomeET.setText(addCommaToCurrency(value).reverse());
                    monthlyIncomeET.setSelection(addCommaToCurrency(value).length());
                } catch (Exception e) {
                    // Do nothing since not a number
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }
        });

        dateOfBirthET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPickDateOfBirth = true;
                datePickerDialog.show();
            }
        });

        expiredIdET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPickDateOfBirth = false;
                datePickerDialog.show();
            }
        });
    }

    private StringBuilder addCommaToCurrency(String value) {
        String reverseValue = new StringBuilder(value).reverse()
                .toString();
        StringBuilder finalValue = new StringBuilder();
        for (int i = 1; i <= reverseValue.length(); i++) {
            char val = reverseValue.charAt(i - 1);
            finalValue.append(val);
            if (i % 3 == 0 && i != reverseValue.length() && i > 0) {
                finalValue.append(",");
            }
        }

        return finalValue;
    }

    private void showFields(boolean isChecked) {
        companyAddressTV.setVisibility(isChecked ? View.GONE : View.VISIBLE);
        companyAddressET.setVisibility(isChecked ? View.GONE : View.VISIBLE);
        monthlyIncomeTV.setText(isChecked ? R.string.gaji_perbulan : R.string.omzet_perbulan);
        monthlyIncomeET.setHint(isChecked ? R.string.gaji_perbulan : R.string.omzet_perbulan);
        monthlyAssetTV.setVisibility(isChecked ? View.GONE : View.VISIBLE);
        monthlyAssetET.setVisibility(isChecked ? View.GONE : View.VISIBLE);
        employeeWageTV.setVisibility(isChecked ? View.GONE : View.VISIBLE);
        employeeWageET.setVisibility(isChecked ? View.GONE : View.VISIBLE);
        jobPositionTV.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        jobPositionET.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        educationExpensesTV.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        educationExpensesET.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        transportExpensesTV.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        transportExpensesET.setVisibility(isChecked ? View.VISIBLE : View.GONE);
    }

    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            showDate(year, monthOfYear, dayOfMonth);
        }
    };

    private void showDate(int year, int month, int day) {
        String fixedMonth = month < 10 ? "0" + month : "" + month;
        String fixedDay = day < 10 ? "0" + day : "" + day;
        if (isPickDateOfBirth)
            dateOfBirthET.setText(new StringBuilder().append(year).append("-").append(fixedMonth).append("-").append(fixedDay));
        else
            expiredIdET.setText(new StringBuilder().append(year).append("-").append(fixedMonth).append("-").append(fixedDay));
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

        if (placeOfBirthET.getText().length() == 0)
            placeOfBirthET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (dateOfBirthET.getText().length() == 0)
            dateOfBirthET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (idCardNumberET.getText().length() == 0)
            idCardNumberET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (phoneNumberET.getText().length() == 0)
            phoneNumberET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (expiredIdET.getText().length() == 0)
            expiredIdET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (!Utility.isEmailValid(emailET.getText().toString()))
            emailET.setError(getString(R.string.email_validation_error));
        else
            filledFormTotal++;

        if (addressET.getText().length() == 0)
            addressET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (phoneNumberET.getText().length() == 0)
            phoneNumberET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        /*if (userImageFile == null)
            Toast.makeText(CustomerProfileActivity.this, R.string.null_picture_error, Toast.LENGTH_SHORT).show();
        else
            filledFormTotal++;*/

        //11
        if (companyNameET.getText().length() == 0)
            companyNameET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (workSinceET.getText().length() == 0)
            workSinceET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (jobPositionET.getText().length() == 0 && jobPositionET.getVisibility() == View.VISIBLE)
            jobPositionET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (companyAddressET.getText().length() == 0 && companyAddressET.getVisibility() == View.VISIBLE)
            companyAddressET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (companyNameET.getText().length() == 0)
            companyNameET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (monthlyIncomeET.getText().length() == 0)
            monthlyIncomeET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (educationExpensesET.getText().length() == 0 && educationExpensesET.getVisibility() == View.VISIBLE)
            educationExpensesET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (monthlyAssetET.getText().length() == 0 && monthlyAssetET.getVisibility() == View.VISIBLE)
            monthlyAssetET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (householdExpensesET.getText().length() == 0 && householdExpensesET.getVisibility() == View.VISIBLE)
            householdExpensesET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (transportExpensesET.getText().length() == 0 && transportExpensesET.getVisibility() == View.VISIBLE)
            transportExpensesET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (employeeWageET.getText().length() == 0 && employeeWageET.getVisibility() == View.VISIBLE)
            employeeWageET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (waterAndElectricyExpensesET.getText().length() == 0)
            waterAndElectricyExpensesET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (miscExpensesET.getText().length() == 0)
            miscExpensesET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        //23

        if (kasSaatIniET.getText().toString().isEmpty())
            kasSaatIniET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (tabunganET.getText().toString().isEmpty())
            tabunganET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (persediaanBarangET.getText().toString().isEmpty())
            persediaanBarangET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (rumahTanahET.getText().toString().isEmpty())
            rumahTanahET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (kendaraanET.getText().toString().isEmpty())
            kendaraanET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (alatUsahaET.getText().toString().isEmpty())
            alatUsahaET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (barangBerhargaET.getText().toString().isEmpty())
            barangBerhargaET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (piutangET.getText().toString().isEmpty())
            piutangET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (hutangBankET.getText().toString().isEmpty())
            hutangBankET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        return filledFormTotal == 32;
    }

    private void updateProfile() throws FileNotFoundException {
        String url = CommonConstants.SERVICE_UPDATE_USER_PROFILE;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();

        String gender = maleRB.isChecked() ? CommonConstants.MALE : CommonConstants.FEMALE;
        String status = singleRB.isChecked() ? CommonConstants.SINGLE : CommonConstants.MARRIED;
        String job = employeeRB.isChecked() ? CommonConstants.EMPLOYEE : CommonConstants.ENTREPRENEUR;

        RequestParams parameters = new RequestParams();
        parameters.put(CommonConstants.ID, sharedPreferences.getInt(CommonConstants.ID, 1));
        parameters.put(CommonConstants.FIRST_NAME, firstNameET.getText().toString());
        parameters.put(CommonConstants.LAST_NAME, lastNameET.getText().toString());
        parameters.put(CommonConstants.EMAIL, emailET.getText().toString());
        parameters.put(CommonConstants.GENDER, gender);
        parameters.put(CommonConstants.DATE_OF_BIRTH, dateOfBirthET.getText().toString());
        parameters.put(CommonConstants.PLACE_OF_BIRTH, placeOfBirthET.getText().toString());
        parameters.put(CommonConstants.ID_CARD_NUMBER, idCardNumberET.getText().toString());
        parameters.put(CommonConstants.ID_CARD_EXP_DATE, expiredIdET.getText().toString());
        parameters.put(CommonConstants.ADDRESS, addressET.getText().toString());
        parameters.put(CommonConstants.PHONE, phoneNumberET.getText().toString());
        parameters.put(CommonConstants.MARITAL_STATUS, status);
        parameters.put(CommonConstants.JOB, job);
        parameters.put(CommonConstants.COMPANY_NAME, companyNameET.getText().toString());
        parameters.put(CommonConstants.COMPANY_ADDRESS, companyAddressET.getText().toString());
        parameters.put(CommonConstants.JOB_POSITION, jobPositionET.getText().toString());
        parameters.put(CommonConstants.WORK_SINCE, workSinceET.getText().toString());
        parameters.put(CommonConstants.MONTHLY_INCOME, removeComma(monthlyIncomeET.getText().toString()));
        parameters.put(CommonConstants.EDUCATION_EXPENSES, removeComma(educationExpensesET.getText().toString()));
        parameters.put(CommonConstants.HOUSEHOLD_EXPENSES, removeComma(householdExpensesET.getText().toString()));
        parameters.put(CommonConstants.TRANSPORTATION_EXPENSES, removeComma(transportExpensesET.getText().toString()));
        parameters.put(CommonConstants.WATER_ELECTRICITY_EXPENSES, removeComma(waterAndElectricyExpensesET.getText().toString()));
        parameters.put(CommonConstants.MISC_EXPENSES, removeComma(waterAndElectricyExpensesET.getText().toString()));
        parameters.put(CommonConstants.MONTHLY_ASSETS, removeComma(monthlyAssetET.getText().toString()));
        parameters.put(CommonConstants.EMPLOYEE_WAGE, removeComma(employeeWageET.getText().toString()));
        parameters.put(CommonConstants.LATITUDE, sharedPreferences.getString(CommonConstants.LATITUDE, "0.0"));
        parameters.put(CommonConstants.LONGITUDE, sharedPreferences.getString(CommonConstants.LONGITUDE, "0.0"));

        parameters.put(CommonConstants.KAS, kasSaatIniET.getText().toString());
        parameters.put(CommonConstants.TABUNGAN, tabunganET.getText().toString());
        parameters.put(CommonConstants.PERSEDIAAN, persediaanBarangET.getText().toString());
        parameters.put(CommonConstants.RUMAH, rumahTanahET.getText().toString());
        parameters.put(CommonConstants.KENDARAAN, kendaraanET.getText().toString());
        parameters.put(CommonConstants.ALAT, alatUsahaET.getText().toString());
        parameters.put(CommonConstants.BARANG, barangBerhargaET.getText().toString());
        parameters.put(CommonConstants.PIUTANG, piutangET.getText().toString());
        parameters.put(CommonConstants.HUTANG, hutangBankET.getText().toString());

        if (userImageFile != null)
            parameters.put(CommonConstants.PROFILE_PICTURE, userImageFile);

        APIAgent.post(url, parameters, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                progressDialog.setProgress(0);
                progressDialog.show();
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getInt(CommonConstants.STATUS) == CommonConstants.STATUS_OK) {
                        Toast.makeText(CustomerProfileActivity.this, R.string.profile_updated, Toast.LENGTH_SHORT).show();
                        changePreferences(response.getJSONObject(CommonConstants.RETURN_DATA));
                        Intent returnIntent = new Intent(CustomerProfileActivity.this, MainActivity.class);
                        returnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(returnIntent);
                    } else {
                        Toast.makeText(CustomerProfileActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(CustomerProfileActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(CustomerProfileActivity.this, R.string.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.dismiss();
            }
        });
    }

    private static String removeComma(String number) {
        return number.replace(",", "");
    }

    private void changePreferences(JSONObject jsonObject) {
        SharedPreferences.Editor editor = TemanUsahaApplication.getInstance().getSharedPreferences().edit();

        try {
            editor.putString(CommonConstants.FIRST_NAME, jsonObject.getString(CommonConstants.FIRST_NAME));
            editor.putString(CommonConstants.LAST_NAME, jsonObject.getString(CommonConstants.LAST_NAME));
            editor.putString(CommonConstants.EMAIL, jsonObject.getString(CommonConstants.EMAIL));
            editor.putString(CommonConstants.GENDER, jsonObject.getString(CommonConstants.GENDER));
            editor.putString(CommonConstants.DATE_OF_BIRTH, jsonObject.getString(CommonConstants.DATE_OF_BIRTH));
            editor.putString(CommonConstants.PLACE_OF_BIRTH, jsonObject.getString(CommonConstants.PLACE_OF_BIRTH));
            editor.putString(CommonConstants.ID_CARD_NUMBER, jsonObject.getString(CommonConstants.ID_CARD_NUMBER));
            editor.putString(CommonConstants.ID_CARD_EXP_DATE, jsonObject.getString(CommonConstants.ID_CARD_EXP_DATE));
            editor.putString(CommonConstants.ADDRESS, jsonObject.getString(CommonConstants.ADDRESS));
            editor.putString(CommonConstants.PHONE, jsonObject.getString(CommonConstants.PHONE));
            editor.putString(CommonConstants.MARITAL_STATUS, jsonObject.getString(CommonConstants.MARITAL_STATUS));
            editor.putString(CommonConstants.JOB, jsonObject.getString(CommonConstants.JOB));
            editor.putString(CommonConstants.COMPANY_NAME, jsonObject.getString(CommonConstants.COMPANY_NAME));
            editor.putString(CommonConstants.COMPANY_ADDRESS, jsonObject.getString(CommonConstants.COMPANY_ADDRESS));
            editor.putString(CommonConstants.JOB_POSITION, jsonObject.getString(CommonConstants.JOB_POSITION));
            editor.putString(CommonConstants.WORK_SINCE, jsonObject.getString(CommonConstants.WORK_SINCE));
            editor.putString(CommonConstants.MONTHLY_INCOME, jsonObject.getString(CommonConstants.MONTHLY_INCOME));
            editor.putString(CommonConstants.EDUCATION_EXPENSES, jsonObject.getString(CommonConstants.EDUCATION_EXPENSES));
            editor.putString(CommonConstants.HOUSEHOLD_EXPENSES, jsonObject.getString(CommonConstants.HOUSEHOLD_EXPENSES));
            editor.putString(CommonConstants.TRANSPORTATION_EXPENSES, jsonObject.getString(CommonConstants.TRANSPORTATION_EXPENSES));
            editor.putString(CommonConstants.WATER_ELECTRICITY_EXPENSES, jsonObject.getString(CommonConstants.WATER_ELECTRICITY_EXPENSES));
            editor.putString(CommonConstants.MISC_EXPENSES, jsonObject.getString(CommonConstants.MISC_EXPENSES));
            editor.putString(CommonConstants.MONTHLY_ASSETS, jsonObject.getString(CommonConstants.MONTHLY_ASSETS));
            editor.putString(CommonConstants.EMPLOYEE_WAGE, jsonObject.getString(CommonConstants.EMPLOYEE_WAGE));
            editor.putString(CommonConstants.PROFILE_PICTURE, jsonObject.getString(CommonConstants.PROFILE_PICTURE));
            editor.putString(CommonConstants.LATITUDE, jsonObject.getString(CommonConstants.LATITUDE));
            editor.putString(CommonConstants.LONGITUDE, jsonObject.getString(CommonConstants.LONGITUDE));

            editor.putString(CommonConstants.KAS, CommonConstants.KAS);
            editor.putString(CommonConstants.TABUNGAN, CommonConstants.TABUNGAN);
            editor.putString(CommonConstants.PERSEDIAAN, CommonConstants.PERSEDIAAN);
            editor.putString(CommonConstants.RUMAH, CommonConstants.RUMAH);
            editor.putString(CommonConstants.KENDARAAN, CommonConstants.KENDARAAN);
            editor.putString(CommonConstants.ALAT, CommonConstants.ALAT);
            editor.putString(CommonConstants.BARANG, CommonConstants.BARANG);
            editor.putString(CommonConstants.PIUTANG, CommonConstants.PIUTANG);
            editor.putString(CommonConstants.HUTANG, CommonConstants.HUTANG);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editor.apply();
    }


    private void putData(JSONObject jsonObject) throws JSONException {
        JSONObject object = jsonObject.getJSONObject(CommonConstants.RETURN_DATA);

        firstNameET.setText(object.getString(CommonConstants.FIRST_NAME));
        lastNameET.setText(object.getString(CommonConstants.LAST_NAME));
        placeOfBirthET.setText(object.getString(CommonConstants.PLACE_OF_BIRTH));
        dateOfBirthET.setText(object.getString(CommonConstants.DATE_OF_BIRTH));
        idCardNumberET.setText(object.getString(CommonConstants.ID_CARD_NUMBER));
        emailET.setText(object.getString(CommonConstants.EMAIL));
        expiredIdET.setText(object.getString(CommonConstants.ID_CARD_EXP_DATE));
        addressET.setText(object.getString(CommonConstants.ADDRESS));
        phoneNumberET.setText(object.getString(CommonConstants.PHONE));

        if (object.getString(CommonConstants.MARITAL_STATUS).equals(CommonConstants.SINGLE))
            singleRB.setChecked(true);
        else
            marriedRB.setChecked(true);

        if (object.getString(CommonConstants.GENDER).equals(CommonConstants.MALE))
            maleRB.setChecked(true);
        else
            femaleRB.setChecked(true);

        if (object.getString(CommonConstants.JOB).equals(CommonConstants.EMPLOYEE))
            employeeRB.setChecked(true);
        else
            entrepreneurRB.setChecked(true);

        companyNameET.setText(object.getString(CommonConstants.COMPANY_NAME));
        workSinceET.setText(object.getString(CommonConstants.WORK_SINCE));
        jobPositionET.setText(object.getString(CommonConstants.JOB_POSITION));
        monthlyIncomeET.setText(object.getString(CommonConstants.MONTHLY_INCOME));
        educationExpensesET.setText(object.getString(CommonConstants.EDUCATION_EXPENSES));
        householdExpensesET.setText(object.getString(CommonConstants.HOUSEHOLD_EXPENSES));
        transportExpensesET.setText(object.getString(CommonConstants.TRANSPORTATION_EXPENSES));
        waterAndElectricyExpensesET.setText(object.getString(CommonConstants.WATER_ELECTRICITY_EXPENSES));
        miscExpensesET.setText(object.getString(CommonConstants.MISC_EXPENSES));
        companyAddressET.setText(object.getString(CommonConstants.COMPANY_ADDRESS));
        monthlyAssetET.setText(object.getString(CommonConstants.MONTHLY_ASSETS));
        employeeWageET.setText(object.getString(CommonConstants.EMPLOYEE_WAGE));

        kasSaatIniET.setText(object.getString(CommonConstants.KAS));
        tabunganET.setText(object.getString(CommonConstants.TABUNGAN));
        persediaanBarangET.setText(object.getString(CommonConstants.PERSEDIAAN));
        rumahTanahET.setText(object.getString(CommonConstants.RUMAH));
        kendaraanET.setText(object.getString(CommonConstants.KENDARAAN));
        alatUsahaET.setText(object.getString(CommonConstants.ALAT));
        barangBerhargaET.setText(object.getString(CommonConstants.BARANG));
        piutangET.setText(object.getString(CommonConstants.PIUTANG));
        hutangBankET.setText(object.getString(CommonConstants.HUTANG));

        imageLoader.display(profilePictureIV, CommonConstants.SERVICE_PROFILE_PIC + object.getString(CommonConstants.PROFILE_PICTURE));
    }

    private void makePopupDialog() {
        final String[] option = getResources().getStringArray(R.array.set_picture_item);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(CustomerProfileActivity.this, android.R.layout.select_dialog_item, option);
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerProfileActivity.this);

        builder.setTitle(R.string.set_profile_picture);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                switch (which) {
                    case 0:
                        Crop.pickImage(CustomerProfileActivity.this);
                        break;
                    default:
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        startActivityForResult(intent, Crop.REQUEST_PICK);
                        break;
                }
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            profilePictureIV.setImageBitmap(null);
            profilePictureIV.setImageBitmap(setPic(Crop.getOutput(result)));
            Bitmap imageBitmap = setPic(Crop.getOutput(result));
            userImageFile = storeImage(imageBitmap);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private File storeImage(Bitmap image) {
        File f = getOutputMediaFile();

        String TAG = CommonConstants.PROFILE_PICTURE;
        if (f == null) {
            Log.d(TAG, "Error creating media file, check storage permissions: ");// e.getMessage());
            return null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(f);
            image.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.close();
            return f;
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }

        return null;
    }

    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Images");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "profile_pic" + ".jpg");
        return mediaFile;
    }

    private Bitmap setPic(Uri uri) {
        int targetW;
        int targetH;

        // Get the dimensions of the View
        targetW = profilePictureIV.getWidth();
        targetH = profilePictureIV.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(uri.getPath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath(), bmOptions);

        //Checking image orientation
        try {
            int orientation = getExifOrientation(uri.getPath());

            if (orientation == 1) {
                return bitmap;
            }
            boolean isRotated = false;
            Matrix matrix = new Matrix();
            switch (orientation) {
                case 2:
                    matrix.setScale(-1, 1);
                    break;
                case 3:
                    matrix.setRotate(180);
                    break;
                case 4:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case 5:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    isRotated = true;
                    break;
                case 6:
                    matrix.setRotate(90);
                    isRotated = true;
                    break;
                case 7:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    isRotated = true;
                    break;
                case 8:
                    matrix.setRotate(-90);
                    isRotated = true;
                    break;
                default:
                    return bitmap;
            }

            try {
                if (isRotated) {
                    Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    Bitmap result = Bitmap.createScaledBitmap(oriented, oriented.getHeight(), oriented.getWidth(), true);
                    bitmap.recycle();
                    return result;
                } else {
                    Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    bitmap.recycle();
                    return oriented;
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private static int getExifOrientation(String src) throws IOException {
        int orientation = 1;

        try {
            if (Build.VERSION.SDK_INT >= 5) {
                Class<?> exifClass = Class.forName("android.media.ExifInterface");
                Constructor<?> exifConstructor = exifClass.getConstructor(new Class[]{String.class});
                Object exifInstance = exifConstructor.newInstance(new Object[]{src});
                Method getAttributeInt = exifClass.getMethod("getAttributeInt", new Class[]{String.class, int.class});
                Field tagOrientationField = exifClass.getField("TAG_ORIENTATION");
                String tagOrientation = (String) tagOrientationField.get(null);
                orientation = (Integer) getAttributeInt.invoke(exifInstance, new Object[]{tagOrientation, 1});
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return orientation;
    }
}
