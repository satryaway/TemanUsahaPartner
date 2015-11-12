package com.samstudio.temanusahapartner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.samstudio.temanusahapartner.util.CommonConstants;

/**
 * Created by satryaway on 9/12/2015.
 * Show the main activity if user has logged in
 */
public class MainActivity extends AppCompatActivity {
    private ImageView optionMenuIV, profileIV, applicationStatusIV;
    private Context context = MainActivity.this;
    private ImageView grabIV;
    private SharedPreferences sharedPreferences;
    private String deviceID, latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = TemanUsahaApplication.getInstance().getSharedPreferences();
        putData();
        initUI();
        setCallback();
    }

    private void initUI() {
        setContentView(R.layout.main_layout);
        optionMenuIV = (ImageView) findViewById(R.id.option_menu_iv);
        profileIV = (ImageView) findViewById(R.id.profile_iv);
        grabIV = (ImageView) findViewById(R.id.grab_iv);
        applicationStatusIV = (ImageView) findViewById(R.id.application_status_iv);
    }

    private void setCallback() {
        optionMenuIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, optionMenuIV);
                popup.getMenuInflater().inflate(R.menu.option_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = new Intent();
                        switch (item.getItemId()) {
                            case R.id.term_of_service:
                                intent.setClass(context, TermsActivity.class);
                                startActivity(intent);
                                break;

                            case R.id.change_password:
                                intent.setClass(context, ChangePasswordActivity.class);
                                startActivity(intent);
                                break;

                            case R.id.logout:
                                doLogOut();
                                break;

                            default:
                                break;
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });

        grabIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PickShapeActivity.class));
            }
        });

        profileIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CustomerProfileActivity.class));
            }
        });

        applicationStatusIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AppStatusActivity.class));
            }
        });
    }

    private void doLogOut() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(R.string.logout_confirmation_msg);
        alert.setTitle(R.string.logout);
        alert.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                editor.putString(CommonConstants.LATITUDE, latitude);
                editor.putString(CommonConstants.LONGITUDE, longitude);
                editor.putString(CommonConstants.GCM_TOKEN, deviceID);
                editor.apply();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        alert.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alert.show();
    }

    private void putData() {
        deviceID = sharedPreferences.getString(CommonConstants.GCM_TOKEN, "");
        latitude = sharedPreferences.getString(CommonConstants.LATITUDE, "");
        longitude = sharedPreferences.getString(CommonConstants.LONGITUDE, "");
    }
}
