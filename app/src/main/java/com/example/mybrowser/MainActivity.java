package com.example.mybrowser;

import android.app.Activity;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class MainActivity extends AppCompatActivity implements
        OnClickListener {

    WebView wv;
    ImageButton bAddress, bKeyword;
    Button bBack, bForward, bView, bRefresh;
    EditText addr, keyword;
    SwipeRefreshLayout swipeRefresh;
    private int desktopmodesValue = 0;
    // for setting WebView Client to wv rather than opera browser
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // progresss Bar initialisation
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_main);
        initialise();


        // to set go back and go forward
        wv.canGoBack();
        wv.canGoForward();

        // enable javascript for webview and flash
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);

        //webSettings.setPluginsEnabled(true);

        // setting new WebViewClient so at to prevent opera from opening the
        // site
        wv.setWebViewClient(new MyWebViewClient());

        bAddress.setOnClickListener(this);
        bKeyword.setOnClickListener(this);

        bBack.setOnClickListener(this);
        bForward.setOnClickListener(this);
        bView.setOnClickListener(this);
        bRefresh.setOnClickListener(this);

        // setTitle(wv.getTitle());
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to make your refresh action
                // CallYourRefreshingMethod();
               wv.reload();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(swipeRefresh.isRefreshing()) {
                            swipeRefresh.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });

    }

    public void initialise() {
        wv = (WebView) findViewById(R.id.wvContent);

        bAddress = (ImageButton) findViewById(R.id.bAddr);
        bKeyword = (ImageButton) findViewById(R.id.bKeyword);
        bBack = (Button) findViewById(R.id.Back);
        bForward = (Button) findViewById(R.id.Forward);
        bView = (Button) findViewById(R.id.desktopview);
        bRefresh = (Button) findViewById(R.id.Refresh);

        addr = (EditText) findViewById(R.id.etAddr);
        keyword = (EditText) findViewById(R.id.etKeyword);
        swipeRefresh = findViewById(R.id.swipeRefresh);

    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        try {

            // <<<<<<<<<<<<<<<<<PROGRESS BAR>>>>>>>>>>>>
            // set a webChromeClient to track progress
            wv.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    // update the progressBar
                    MainActivity.this.setTitle("Loading...");
                    MainActivity.this.setProgress(progress * 100);

                    //set the title of the layout
                    if(progress == 100)
                        MainActivity.this.setTitle(R.string.app_name);
                }
            });



            switch (v.getId()) {
                case R.id.bAddr:
                    String url = addr.getText().toString();
                    if (url != "") {
                        url = URLUtil.isValidUrl(url) ? addr.getText().toString()   : "http://" + addr.getText().toString();
                        wv.loadUrl(url);
                        addr.setText(url);
                        wv.requestFocus();
                    }
                    break;
                case R.id.bKeyword:
                    String key = keyword.getText().toString();
                    if (key != "") {
                        key = "http://www.google.com/search?q="+ keyword.getText().toString();
                        wv.loadUrl(key);
                        addr.setText(key);
                        wv.requestFocus();
                    }
                    break;

                case R.id.Back:
                    wv.goBack();
                    break;

                case R.id.Forward:
                    wv.goForward();
                    break;

                case R.id.desktopview:

                    if (desktopmodesValue == 0)
                    {
                        setDesktopMode(wv,true);
                        desktopmodesValue = 1;
                    }
                    else
                    {
                        setDesktopMode(wv,false);
                        desktopmodesValue = 0;
                    }

                case R.id.Refresh:
                    wv.reload();
                    break;

            }

        } catch (Exception e) {
            // TODO: handle exception]
            e.printStackTrace();
        }

    }


    //..................DESKTOP MODE............................
    private void setDesktopMode(WebView searchWebAddress, boolean enabled) {
        String newUserAgent = searchWebAddress.getSettings().getUserAgentString();
        if (enabled) {
            try {
                String ua = searchWebAddress.getSettings().getUserAgentString();
                String androidDosString = wv.getSettings().getUserAgentString().substring(ua.indexOf("("), ua.indexOf(")") + 1);
                newUserAgent = searchWebAddress.getSettings().getUserAgentString().replace(androidDosString, "X11; Linux x86_84");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else {
            newUserAgent = null;
        }
        searchWebAddress.getSettings().setUserAgentString(newUserAgent);
        searchWebAddress.getSettings().setUseWideViewPort(enabled);
        searchWebAddress.getSettings().setLoadWithOverviewMode(enabled);
        searchWebAddress.reload();
    }
}


