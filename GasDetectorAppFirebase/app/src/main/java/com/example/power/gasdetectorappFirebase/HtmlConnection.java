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

        String myUrl = "file:///android_asset/index.html";// the path of the source file
        WebView webView = (WebView) findViewById(R.id.webView);
        WebView.setWebContentsDebuggingEnabled(true); // set the possibility to debug using chrome://inspec
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);// enable javascript communication between front end and back ends
        JavaScriptInterface jsInterface = new JavaScriptInterface(this);// add the javascript for the projcet
        webView.addJavascriptInterface(jsInterface, "JSInterface");// add the javascript interface for the project
        webView.loadUrl(myUrl);// load the front end in the  project in the webview




    }
}

