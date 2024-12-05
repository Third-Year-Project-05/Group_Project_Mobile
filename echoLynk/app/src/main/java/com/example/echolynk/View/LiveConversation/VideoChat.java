package com.example.echolynk.View.LiveConversation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.echolynk.R;

public class VideoChat extends AppCompatActivity {

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video_chat);

         webView = findViewById(R.id.webView);

        // Enable JavaScript in the WebView
        webView.getSettings().setJavaScriptEnabled(true);

        // Ensure links open within the WebView
        webView.setWebViewClient(new WebViewClient());

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        if (url != null) {
            webView.loadUrl(url);
        }

    }
}