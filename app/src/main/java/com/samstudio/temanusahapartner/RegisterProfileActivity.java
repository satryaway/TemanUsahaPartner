package com.samstudio.temanusahapartner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.samstudio.temanusahapartner.util.CommonConstants;
import com.samstudio.temanusahapartner.util.Seeder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satryaway on 9/30/2015.
 * activity to register applicant
 */
public class RegisterProfileActivity extends AppCompatActivity {
    private Spinner creditPurposeSP, creditCeilingSP, timeRangeSP;
    private List<String> creditPurposeList = new ArrayList<>();
    private List<String> creditCeilingList = new ArrayList<>();
    private List<String> timeRangeList = new ArrayList<>();
    private int creditPurpose = 0, creditCeiling = 0, timeRange = 0;
    private int shapeCode;
    private ImageView searchPartnerIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent();
        initUI();
        setCallBack();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        shapeCode = intent.getIntExtra(CommonConstants.SHAPE_CODE, 1);
    }

    private void initUI() {
        setContentView(R.layout.get_loan_layout);

        searchPartnerIV = (ImageView) findViewById(R.id.search_partner_iv);

        creditPurposeSP = (Spinner) findViewById(R.id.tujuan_kredit_sp);
        creditCeilingSP = (Spinner) findViewById(R.id.plafon_kredit_sp);
        timeRangeSP = (Spinner) findViewById(R.id.jangka_waktu_sp);

        creditPurposeList = Seeder.getCreditPurposeList(this);
        creditCeilingList = Seeder.getCreditCeilingList(this);
        timeRangeList = Seeder.getTimeRangeList(this);

        ArrayAdapter<String> dataAdapter;

        dataAdapter = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_item, creditPurposeList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        creditPurposeSP.setAdapter(dataAdapter);
        creditPurposeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                creditPurpose = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dataAdapter = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_item, creditCeilingList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        creditCeilingSP.setAdapter(dataAdapter);
        creditCeilingSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                creditCeiling = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dataAdapter = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_item, timeRangeList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        timeRangeSP.setAdapter(dataAdapter);
        timeRangeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeRange = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void setCallBack() {
        searchPartnerIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterProfileActivity.this, ShowMapActivity.class);
                intent.putExtra(CommonConstants.SHAPE_CODE, shapeCode);
                intent.putExtra(CommonConstants.LOAN_TYPE, creditPurpose);
                intent.putExtra(CommonConstants.LOAN_SEGMENT, creditCeiling);
                intent.putExtra(CommonConstants.LOAN_PERIOD, timeRange);
                startActivity(intent);
            }
        });
    }
}
