package com.samstudio.temanusahapartner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.samstudio.temanusahapartner.util.CommonConstants;

/**
 * Created by satryaway on 10/17/2015.
 * activity to display meet up process
 */
public class MeetUpProcessActivity extends AppCompatActivity {
    private TextView dateTV, venueTV;
    private String date, phoneNumber, meetupDatetime, meetupVenue;
    private ImageView callPartnerIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleIntent();
        initUI();
        setCallBack();
        putData();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        date = intent.getStringExtra(CommonConstants.DATE);
        phoneNumber = intent.getStringExtra(CommonConstants.PHONE_NUMBER);
        meetupDatetime = intent.getStringExtra(CommonConstants.MEETUP_DATETIME);
        meetupVenue = intent.getStringExtra(CommonConstants.MEETUP_VENUE);
    }

    private void initUI() {
        setContentView(R.layout.meet_up_process_layout);
        dateTV = (TextView) findViewById(R.id.date_tv);
        venueTV = (TextView) findViewById(R.id.venue_tv);
        callPartnerIV = (ImageView) findViewById(R.id.call_partner_iv);
    }

    private void setCallBack() {
        callPartnerIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + phoneNumber.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                }
                startActivity(intent);
            }
        });
    }

    private void putData() {
        dateTV.setText(meetupDatetime);
        venueTV.setText(meetupVenue);
    }
}
