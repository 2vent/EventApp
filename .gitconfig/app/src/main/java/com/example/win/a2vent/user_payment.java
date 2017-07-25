package com.example.win.a2vent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 2017-07-13.
 */

public class user_payment extends AppCompatActivity {
    private WebView webView;
    private WebSettings webSettings;


    @Override
    public void onCreate(Bundle save) {
        super.onCreate(save);
        setContentView(R.layout.user_payment);
        webView = (WebView) findViewById(R.id.pay_webview);
        webView.setWebViewClient(new WebViewClient());
        webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(GlobalData.getURL() + "paytest.php");


    }
}
