package com.jixstreet.temanusahapartner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.jixstreet.temanusahapartner.util.CommonConstants;

/**
 * Created by satryaway on 9/25/2015.
 * picking preferre
 */
public class PickShapeActivity extends AppCompatActivity {
    private ImageView roundIV, triangleIV, hexagonIV, squareIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        setCallBack();
    }

    private void initUI() {
        setContentView(R.layout.pick_shape_layout);

        roundIV = (ImageView) findViewById(R.id.round_iv);
        triangleIV = (ImageView) findViewById(R.id.triangle_iv);
        hexagonIV = (ImageView) findViewById(R.id.hexagonal_iv);
        squareIV = (ImageView) findViewById(R.id.square_iv);
    }

    private void setCallBack() {
        triangleIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(1);
            }
        });

        hexagonIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(2);
            }
        });

        squareIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(3);
            }
        });

        roundIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(4);
            }
        });
    }

    private void startNewActivity(int pos) {
        Intent intent = new Intent(this, RegisterProfileActivity.class);
        intent.putExtra(CommonConstants.SHAPE_CODE, pos);
        startActivity(intent);
    }
}
