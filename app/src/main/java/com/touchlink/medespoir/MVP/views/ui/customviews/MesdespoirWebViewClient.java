package com.touchlink.medespoir.MVP.views.ui.customviews;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.airbnb.lottie.LottieAnimationView;
import com.touchlink.medespoir.WebServices.retrofit.Urls;



public class  MesdespoirWebViewClient extends WebViewClient {
    LottieAnimationView loader ;


    public MesdespoirWebViewClient(LottieAnimationView loader) {
        this.loader=loader;
        loader.setVisibility(View.VISIBLE);
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // TODO Auto-generated method stub
        view.loadUrl(Urls.URL_MEDESPOIRTV);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // TODO Auto-generated method stub
        super.onPageFinished(view, url);
        loader.setVisibility(View.GONE);
    }
}
