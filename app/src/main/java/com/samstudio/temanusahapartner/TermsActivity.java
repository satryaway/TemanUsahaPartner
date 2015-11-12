package com.samstudio.temanusahapartner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

/**
 * Created by satryaway on 9/14/2015.
 * Terms activity to show the app's term and condition concept
 */
public class TermsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        setCallBack();
    }

    private void initUI() {
        setContentView(R.layout.term_of_service_layout);
        WebView webView = (WebView) findViewById(R.id.terms_wv);
        webView.loadUrl("file:///android_asset/terms.html");
    }

    private void setCallBack() {
    }
}
