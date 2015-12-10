package com.jixstreet.temanusahapartner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.jixstreet.temanusahapartner.entities.Application;
import com.jixstreet.temanusahapartner.util.APIAgent;
import com.jixstreet.temanusahapartner.util.CommonConstants;
import com.jixstreet.temanusahapartner.util.Seeder;
import com.jixstreet.temanusahapartner.util.UniversalImageLoader;
import com.jixstreet.temanusahapartner.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by satryaway on 10/6/2015.
 * activity to show the list of available partner nearby by using google Map
 */
public class ShowMapActivity extends AppCompatActivity {
    private List<Application> applicationList = new ArrayList<>();
    private GoogleMap googleMap;
    private int chosenId = 1;
    private ImageView grabIV;
    private ImageView profilePictureIV;
    private TextView customerNameTV;
    private TextView loanTypeTV;
    private TextView loanSegmentTV;
    private TextView timeRangeTV;
    private UniversalImageLoader imageLoader;
    private List<String> creditPurposeList = new ArrayList<>();
    private List<String> creditCeilingList = new ArrayList<>();
    private List<String> timeRangeList = new ArrayList<>();
    private int position;
    private HashMap<Marker, Integer> hashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = new UniversalImageLoader(this);
        imageLoader.initImageLoader();
        collectData();
        handleIntent();
        initUI();
        setCallBack();
        createMapView();
        putData();
    }

    private void collectData() {
        creditPurposeList = Seeder.getCreditPurposeList(this);
        creditCeilingList = Seeder.getCreditCeilingList(this);
        timeRangeList = Seeder.getTimeRangeList(this);
    }

    private void handleIntent() {
    }

    private void initUI() {
        setContentView(R.layout.show_map_layout);

        profilePictureIV = (ImageView) findViewById(R.id.profile_picture_iv);
        customerNameTV = (TextView) findViewById(R.id.customer_name_tv);
        loanTypeTV = (TextView) findViewById(R.id.loan_type_tv);
        loanSegmentTV = (TextView) findViewById(R.id.loan_segment_tv);
        timeRangeTV = (TextView) findViewById(R.id.time_range_tv);
        grabIV = (ImageView) findViewById(R.id.grab_iv);
    }

    private void setCallBack() {
        grabIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(ShowMapActivity.this, WaitingForProcessActivity.class);
                intent.putExtra(CommonConstants.DATE, applicationList.get(position).getDatetime());
                intent.putExtra(CommonConstants.APP_ID, applicationList.get(position).getId());
                intent.putExtra(CommonConstants.FIRST_NAME, applicationList.get(position).getCustomer().getFirstName() + "  " + applicationList.get(position).getCustomer().getLastName());
                intent.putExtra(CommonConstants.LOAN_TYPE, applicationList.get(position).getLoanType());
                intent.putExtra(CommonConstants.LOAN_SEGMENT, applicationList.get(position).getLoanSegment());
                intent.putExtra(CommonConstants.LOAN_PERIOD, applicationList.get(position).getTimeRange());
                intent.putExtra(CommonConstants.JOB, applicationList.get(position).getCustomer().getJob());
                intent.putExtra(CommonConstants.COMPANY_NAME, applicationList.get(position).getCustomer().getCompanyName());
                intent.putExtra(CommonConstants.DATE_OF_BIRTH, applicationList.get(position).getCustomer().getDateOfBirth());
                intent.putExtra(CommonConstants.MARITAL_STATUS, applicationList.get(position).getCustomer().getMaritalStatus());
                startActivity(intent);
            }
        });
    }

    private void createMapView() {
        try {
            if (null == googleMap) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
                if (null == googleMap)
                    Toast.makeText(getApplicationContext(), "Error creating map", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException exception) {
            Log.e("mapApp", exception.toString());
        }

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void addCurrentLocation() {
        double lat = Double.valueOf(TemanUsahaApplication.getInstance().getSharedPreferences().getString(CommonConstants.LATITUDE, "0.0"));
        double lng = Double.valueOf(TemanUsahaApplication.getInstance().getSharedPreferences().getString(CommonConstants.LONGITUDE, "0.0"));
        LatLng latLng = new LatLng(lat, lng);
        if (null != googleMap) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(getString(R.string.your_location))
                    .draggable(false)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.tu_white)));
            hashMap.put(marker, -1);
        }
    }

    private void addMarkers() {
        addCurrentLocation();
        int i = 0;
        for (Application application : applicationList) {
            LatLng latLng = new LatLng(application.getCustomer().getLatitude(), application.getCustomer().getLongitude());
            hashMap.put(addMarker(latLng, application.getCustomer().getCompanyName()), i);

            if (i == 0) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            }

            i++;
        }

        final HashMap<Marker, Integer> markerMap = hashMap;

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (markerMap.get(marker) != -1)
                    showCustomerInformation(markerMap.get(marker));
                return false;
            }
        });
    }

    private Marker addMarker(final LatLng pos, String name) {
        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(pos)
                .title(name)
                .draggable(false)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon)));

        return marker;
    }

    private void putData() {
        String url = CommonConstants.SERVICE_GET_APPLICANT_LIST + TemanUsahaApplication.getInstance().getSharedPreferences().getInt(CommonConstants.ID, 0);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();

        APIAgent.get(url, new RequestParams(), new JsonHttpResponseHandler() {
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
                        JSONArray jsonArray = response.getJSONArray(CommonConstants.RETURN_DATA);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            applicationList.add(Utility.parseApplicants(object));
                        }
                        addMarkers();
                        showCustomerInformation(0);
                    } else {
                        Toast.makeText(ShowMapActivity.this, response.getString(CommonConstants.MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ShowMapActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(ShowMapActivity.this, R.string.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.dismiss();
            }
        });
    }

    private void showCustomerInformation(int position) {
        Application application = applicationList.get(position);
        imageLoader.display(profilePictureIV, CommonConstants.SERVICE_PROFILE_PIC + application.getCustomer().getProfilePicture());
        customerNameTV.setText(application.getCustomer().getFirstName() + " " + application.getCustomer().getLastName());
        loanTypeTV.setText(creditPurposeList.get(Integer.valueOf(applicationList.get(position).getLoanType())-1));
        loanSegmentTV.setText(creditCeilingList.get(Integer.valueOf(applicationList.get(position).getLoanSegment())-1));
        timeRangeTV.setText(timeRangeList.get(Integer.valueOf(applicationList.get(position).getTimeRange())-1));
        this.position = position;
    }
}
