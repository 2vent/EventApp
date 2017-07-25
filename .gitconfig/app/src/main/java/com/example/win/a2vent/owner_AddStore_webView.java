package com.example.win.a2vent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class owner_AddStore_webView extends AppCompatActivity {
    private static final int SERCH_ADDR = 4; //가져온 사진을 자르기 위한 변수
    //주소 검색
    WebView webview;
    private Handler handler;
    String v_com_addr=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner__addstore_webview);

        // WebView 초기화
        init_webView();

        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();




    }

    public void init_webView() {
        // WebView 설정
        webview = (WebView) findViewById(R.id.webView);
        // JavaScript 허용
        webview.getSettings().setJavaScriptEnabled(true);
        // JavaScript의 window.open 허용
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        // 두 번째 파라미터는 사용될 php에도 동일하게 사용해야함
        webview.addJavascriptInterface(new AndroidBridge(), "TestApp");
        // web client 를 chrome 으로 설정
        webview.setWebChromeClient(new WebChromeClient());
        // webview url load
        webview.loadUrl(GlobalData.getURL() +"YTest2.php");
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {


                    v_com_addr=String.format("%s %s", arg2, arg3);
                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    init_webView();

                    Intent i = new Intent();
                    i.putExtra("addr",v_com_addr);
                    setResult(RESULT_OK,i);

                    finish();


                }
            });
        }
    }
}
