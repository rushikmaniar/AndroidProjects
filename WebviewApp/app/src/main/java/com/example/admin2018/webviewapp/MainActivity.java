package com.example.admin2018.webviewapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webview1 = (WebView)findViewById(R.id.webview1);
        WebSettings websettings = webview1.getSettings();
        websettings.setBuiltInZoomControls(true);
        webview1.loadUrl("http://192.168.1.79:90/dakshina/backoffice");

    }
}
