package com.example.power.gasdetectorappFirebase;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class HtmlConnection extends Activity {


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_html_test);
        String myUrl = "file:///android_asset/index.html";
        WebView webView = (WebView) findViewById(R.id.webView);

        WebView.setWebContentsDebuggingEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        JavaScriptInterface jsInterface = new JavaScriptInterface(this);
        webView.addJavascriptInterface(jsInterface, "JSInterface");
        webView.loadUrl(myUrl);



        //setContentView(webView);

    }
}

