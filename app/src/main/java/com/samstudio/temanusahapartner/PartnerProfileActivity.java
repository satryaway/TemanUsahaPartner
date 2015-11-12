package com.samstudio.temanusahapartner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.samstudio.temanusahapartner.util.Utility;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by satryaway on 9/17/2015.
 * activity to input profile
 */
public class PartnerProfileActivity extends AppCompatActivity {
    private ImageView profilePictureIV;
    private EditText firstNameET, lastNameET, placeOfBirthET, dateOfBirthET, idCardNumberET, expiredIdET,
            emailET, addressET, phoneNumberET;
    private RadioButton maleRB, femaleRB, marriedRB, singleRB;
    private Button saveBtn;
    private boolean isPickDateOfBirth;
    private DatePickerDialog datePickerDialog;
    private RadioButton hundredMillionRB;
    private EditText kantorCabangET;
    private EditText perusahaanET;
    private EditText companyStrengthET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        setCallBack();
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
        hundredMillionRB = (RadioButton) findViewById(R.id.hundred_million_rb);
        saveBtn = (Button) findViewById(R.id.save_btn);
        maleRB.setChecked(true);
        hundredMillionRB.setChecked(true);

        dateOfBirthET.setFocusable(false);
        datePickerDialog = new DatePickerDialog(this, dateListener, 1990, 1, 1);
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
                    Intent intent = new Intent(PartnerProfileActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        dateOfBirthET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPickDateOfBirth = true;
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
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
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
