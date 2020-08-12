package com.example.newsapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class Webcontent extends AppCompatActivity {
    String url;
    WebView webView;
    ProgressBar progressBar;
    ActionBar actionBar;
    Boolean source = false;
    int f = 0;
    SwipeRefreshLayout swiperefresh;
    int zvalue;
    String contenturl;
    Boolean internet;
    int saved;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webcontent);
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        swiperefresh = findViewById(R.id.swiperefresh);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Article Content");
        progressBar.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
        Intent intent = getIntent();
        zvalue = intent.getIntExtra("zvalue", 0);
        url = intent.getStringExtra("url");
        saved=intent.getIntExtra("saved",0);
        contenturl = intent.getStringExtra("contenturl");
        if(url!=null) {
            if (url.contains("everything")) {
                source = true;
            }
        }
        getconnection();
        swiperefresh.setColorSchemeColors(getResources().getColor(android.R.color.holo_red_dark));
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finish();
                startActivity(getIntent());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swiperefresh.setRefreshing(false);
                    }
                }, 1500);
            }
        });

    }
    private void getconnection() {
        internet=checkConnection();
        if (internet) {
            webview();
        } else {
            internetnotification();
        }
    }
    private void internetnotification()
    {
        new AlertDialog.Builder(Webcontent.this)
                .setIcon(R.drawable.nointernet)
                .setTitle("Oops! An error occurred while loading the page.")
                .setMessage("Make sure you have an active internet connection")
                .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        webView.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        swiperefresh.setVisibility(View.GONE);
                        getconnection();
                    }

                })
                .setNegativeButton("GO BACK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (zvalue == 0) {
                            Intent back = new Intent(Webcontent.this, NewsList.class);
                            back.putExtra("intvalue", 1);
                            back.putExtra("sourcevalue", source);
                            back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(back);
                        }
                    }
                })
                .setCancelable(false)
                .show();
        progressBar.setVisibility(View.GONE);
        swiperefresh.setVisibility(View.VISIBLE);
    }
    private void webview() {
        Log.i("sourcevalue", String.valueOf(source));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.setVisibility(View.INVISIBLE);
        swiperefresh.setVisibility(View.GONE);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(final WebView view, String url) {
                f=1;
                    progressBar.setVisibility(View.GONE);
                    view.setVisibility(View.VISIBLE);
                    swiperefresh.setVisibility(View.VISIBLE);
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if(f!=1) {
                    progressBar.setVisibility(View.VISIBLE);
                    view.setVisibility(View.INVISIBLE);
                    swiperefresh.setVisibility(View.GONE);
                }
               super.onReceivedError(view, request, error);
            }
        });
        webView.loadUrl(contenturl);
    }

    private boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (zvalue == 0) {
                    Intent back = new Intent(this, NewsList.class);
                    back.putExtra("intvalue", 1);
                    back.putExtra("sourcevalue", source);
                    back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(back);
                } else {
                    Intent back = new Intent(this, SavedArticles.class);
                    back.putExtra("intvalue", 1);
                    back.putExtra("sourcevalue", source);
                    back.putExtra("newsurl",url);
                    back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(back);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
