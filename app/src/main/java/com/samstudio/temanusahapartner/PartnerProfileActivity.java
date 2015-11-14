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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.samstudio.temanusahapartner.util.APIAgent;
import com.samstudio.temanusahapartner.util.CommonConstants;
import com.samstudio.temanusahapartner.util.Seeder;
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
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by satryaway on 9/17/2015.
 * activity to input profile
 */
public class PartnerProfileActivity extends AppCompatActivity {
    private ImageView profilePictureIV;
    private EditText firstNameET, lastNameET, placeOfBirthET, dateOfBirthET,
            emailET, phoneNumberET;
    private Spinner creditPurposeSP, creditCeilingSP;
    private RadioButton maleRB, femaleRB;
    private Button saveBtn;
    private DatePickerDialog datePickerDialog;
    private EditText kantorCabangET;
    private EditText perusahaanET;
    private EditText companyStrengthET;
    private List<String> creditPurposeList = new ArrayList<>();
    private List<String> creditCeilingList = new ArrayList<>();
    private File userImageFile = null;

    private int creditPurpose = 1, creditCeiling = 1;
    private SharedPreferences sharedPreferences;
    private UniversalImageLoader imageLoader;

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
    }

    private void getData() {
        String url = CommonConstants.SERVICE_GET_PARTNER_DETAIL + sharedPreferences.getInt(CommonConstants.ID, 1);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);

        APIAgent.get(url, null, new JsonHttpResponseHandler() {
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
                        Toast.makeText(PartnerProfileActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(PartnerProfileActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(PartnerProfileActivity.this, R.string.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                progressDialog.dismiss();
            }
        });
    }

    private void putData(JSONObject jsonObject) throws JSONException {
        JSONObject object = jsonObject.getJSONObject(CommonConstants.RETURN_DATA);

        firstNameET.setText(object.getString(CommonConstants.FIRST_NAME));
        lastNameET.setText(object.getString(CommonConstants.LAST_NAME));
        placeOfBirthET.setText(object.getString(CommonConstants.PLACE_OF_BIRTH));
        dateOfBirthET.setText(object.getString(CommonConstants.DATE_OF_BIRTH));
        emailET.setText(object.getString(CommonConstants.EMAIL));
        perusahaanET.setText(object.getString(CommonConstants.COMPANY));
        kantorCabangET.setText(object.getString(CommonConstants.BRANCH));
        phoneNumberET.setText(object.getString(CommonConstants.PHONE));
        companyStrengthET.setText(object.getString(CommonConstants.DESCRIPTION));
        creditPurposeSP.setSelection(Integer.valueOf(object.getString(CommonConstants.LOAN_TYPE)));
        creditCeilingSP.setSelection(Integer.valueOf(object.getString(CommonConstants.LOAN_SEGMENT)));

        if (object.getString(CommonConstants.GENDER).equals(CommonConstants.MALE))
            maleRB.setChecked(true);
        else
            femaleRB.setChecked(true);

        imageLoader.display(profilePictureIV, CommonConstants.SERVICE_PROFILE_PIC_PARTNER + object.getString(CommonConstants.PROFILE_PICTURE));
    }

    private void initUI() {
        setContentView(R.layout.partner_profile_layout);
        profilePictureIV = (ImageView) findViewById(R.id.profile_picture_iv);
        firstNameET = (EditText) findViewById(R.id.first_name_et);
        lastNameET = (EditText) findViewById(R.id.last_name_et);
        maleRB = (RadioButton) findViewById(R.id.male_rb);
        femaleRB = (RadioButton) findViewById(R.id.female_rb);
        placeOfBirthET = (EditText) findViewById(R.id.place_of_birth_et);
        dateOfBirthET = (EditText) findViewById(R.id.date_of_birth_et);
        perusahaanET = (EditText) findViewById(R.id.perusahaan_et);
        kantorCabangET = (EditText) findViewById(R.id.kantor_cabang_et);
        companyStrengthET = (EditText) findViewById(R.id.company_strength_et);
        emailET = (EditText) findViewById(R.id.email_et);
        phoneNumberET = (EditText) findViewById(R.id.phone_number_et);
        saveBtn = (Button) findViewById(R.id.save_btn);
        maleRB.setChecked(true);

        creditPurposeSP = (Spinner) findViewById(R.id.tujuan_kredit_sp);
        creditCeilingSP = (Spinner) findViewById(R.id.plafon_kredit_sp);

        creditPurposeList = Seeder.getCreditPurposeList(this);
        creditCeilingList = Seeder.getCreditCeilingList(this);

        dateOfBirthET.setFocusable(false);
        datePickerDialog = new DatePickerDialog(this, dateListener, 1990, 1, 1);

        ArrayAdapter<String> dataAdapter;

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, creditPurposeList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        creditPurposeSP.setAdapter(dataAdapter);
        creditPurposeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                creditPurpose = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, creditCeilingList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        creditCeilingSP.setAdapter(dataAdapter);
        creditCeilingSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                creditCeiling = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setCallBack() {
        profilePictureIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePopupDialog();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormVerified()) {
                    doModify();
                }
            }
        });

        dateOfBirthET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
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
        dateOfBirthET.setText(new StringBuilder().append(year).append("-").append(fixedMonth).append("-").append(fixedDay));
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

        if (phoneNumberET.getText().length() == 0)
            phoneNumberET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (perusahaanET.getText().length() == 0)
            perusahaanET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (kantorCabangET.getText().length() == 0)
            kantorCabangET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (companyStrengthET.getText().length() == 0)
            companyStrengthET.setError(getString(R.string.should_not_be_empty_error));
        else
            filledFormTotal++;

        if (!Utility.isEmailValid(emailET.getText().toString()))
            emailET.setError(getString(R.string.email_validation_error));
        else
            filledFormTotal++;

        return filledFormTotal == 9;
    }

    private void doModify() {
        String url = CommonConstants.SERVICE_DO_MODIFY_PARTNER;

        String gender = maleRB.isChecked() ? CommonConstants.MALE : CommonConstants.FEMALE;

        RequestParams parameters = new RequestParams();
        parameters.put(CommonConstants.ID, sharedPreferences.getInt(CommonConstants.ID, 1));
        parameters.put(CommonConstants.FIRST_NAME, firstNameET.getText().toString());
        parameters.put(CommonConstants.LAST_NAME, lastNameET.getText().toString());
        parameters.put(CommonConstants.EMAIL, emailET.getText().toString());
        parameters.put(CommonConstants.GENDER, gender);
        parameters.put(CommonConstants.DATE_OF_BIRTH, dateOfBirthET.getText().toString());
        parameters.put(CommonConstants.PLACE_OF_BIRTH, placeOfBirthET.getText().toString());
        parameters.put(CommonConstants.PHONE, phoneNumberET.getText().toString());
        parameters.put(CommonConstants.COMPANY, perusahaanET.getText().toString());
        parameters.put(CommonConstants.BRANCH, kantorCabangET.getText().toString());
        parameters.put(CommonConstants.DESCRIPTION, companyStrengthET.getText().toString());
        parameters.put(CommonConstants.LOAN_TYPE, creditPurpose);
        parameters.put(CommonConstants.LOAN_SEGMENT, creditCeiling);
        parameters.put(CommonConstants.LATITUDE, sharedPreferences.getString(CommonConstants.LATITUDE, "0.0"));
        parameters.put(CommonConstants.LONGITUDE, sharedPreferences.getString(CommonConstants.LONGITUDE, "0.0"));

        if (userImageFile != null)
            try {
                parameters.put(CommonConstants.PROFILE_PICTURE, userImageFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));

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
                        Toast.makeText(PartnerProfileActivity.this, R.string.profile_updated, Toast.LENGTH_SHORT).show();
                        changePreferences(response.getJSONObject(CommonConstants.RETURN_DATA));
                        Intent returnIntent = new Intent(PartnerProfileActivity.this, MainActivity.class);
                        returnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(returnIntent);
                    } else {
                        Toast.makeText(PartnerProfileActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(PartnerProfileActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(PartnerProfileActivity.this, R.string.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.dismiss();
            }
        });

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
            editor.putString(CommonConstants.PHONE, jsonObject.getString(CommonConstants.PHONE));
            editor.putString(CommonConstants.COMPANY, jsonObject.getString(CommonConstants.COMPANY));
            editor.putString(CommonConstants.BRANCH, jsonObject.getString(CommonConstants.BRANCH));
            editor.putString(CommonConstants.DESCRIPTION, jsonObject.getString(CommonConstants.DESCRIPTION));
            editor.putString(CommonConstants.PROFILE_PICTURE, jsonObject.getString(CommonConstants.PROFILE_PICTURE));
            editor.putString(CommonConstants.LOAN_TYPE, jsonObject.getString(CommonConstants.LOAN_TYPE));
            editor.putString(CommonConstants.LOAN_SEGMENT, jsonObject.getString(CommonConstants.LOAN_SEGMENT));
            editor.putString(CommonConstants.LATITUDE, jsonObject.getString(CommonConstants.LATITUDE));
            editor.putString(CommonConstants.LONGITUDE, jsonObject.getString(CommonConstants.LONGITUDE));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editor.apply();
    }

    private void makePopupDialog() {
        final String[] option = getResources().getStringArray(R.array.set_picture_item);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(PartnerProfileActivity.this, android.R.layout.select_dialog_item, option);
        AlertDialog.Builder builder = new AlertDialog.Builder(PartnerProfileActivity.this);

        builder.setTitle(R.string.set_profile_picture);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                switch (which) {
                    case 0:
                        Crop.pickImage(PartnerProfileActivity.this);
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
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "partner_profile_pic" + ".jpg");
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
