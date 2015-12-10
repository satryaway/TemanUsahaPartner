package com.jixstreet.temanusahapartner;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.jixstreet.temanusahapartner.gcm.QuickstartPreferences;
import com.jixstreet.temanusahapartner.gcm.RegistrationIntentService;
import com.jixstreet.temanusahapartner.util.CommonConstants;
import com.jixstreet.temanusahapartner.util.MyLocation;

/**
 * Created by satryaway on 9/10/2015.
 * Splash screen for about 2 seconds
 */
public class SplashScreen extends AppCompatActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "GCMMainActivity";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);

        sharedPreferences = TemanUsahaApplication.getInstance().getSharedPreferences();

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                String token = sharedPreferences.getString(CommonConstants.GCM_TOKEN, "");
                if (sentToken && !token.equals("")) {
                    getLocation();
                    //Toast.makeText(SplashScreen.this, sharedPreferences.getString(CommonConstants.GCM_TOKEN, ""), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SplashScreen.this, R.string.common_error_msg, Toast.LENGTH_SHORT).show();
                }
            }
        };

        if (!sharedPreferences.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false)) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
        } else
            getLocation();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CommonConstants.GPS_CODE) {
            getLocation();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), CommonConstants.GPS_CODE);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void getLocation() {
        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                if (location != null) {
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(CommonConstants.LATITUDE, String.valueOf(lat));
                    editor.putString(CommonConstants.LONGITUDE, String.valueOf(lng));
                    editor.apply();

                    runApp();
                }
            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(this, locationResult);
    }

    private void runApp() {
        Handler mHandler = new Handler(Looper.getMainLooper());

        int SPLASH_TIME_OUT = 2000;
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i;

                if (sharedPreferences.getBoolean(CommonConstants.IS_LOGGED_IN, false))
                    i = new Intent(SplashScreen.this, MainActivity.class);
                else
                    i = new Intent(SplashScreen.this, LoginActivity.class);

                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
