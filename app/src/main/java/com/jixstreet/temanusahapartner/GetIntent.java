package com.jixstreet.temanusahapartner;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by aneh on 8/16/2014.
 */
public class GetIntent extends GCMExample {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getintent);

        String get = getIntent().getStringExtra("Notif");

        Log.e("Msg", "---------------------------"+get);

        TextView txt = (TextView)findViewById(R.id.get);
        txt.setText(get);

    }
}
