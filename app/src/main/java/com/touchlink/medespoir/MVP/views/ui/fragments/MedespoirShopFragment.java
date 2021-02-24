package com.touchlink.medespoir.MVP.views.ui.fragments;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.WebServices.retrofit.Urls;
import com.touchlink.medespoir.utils.MEConstants;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MedespoirShopFragment  extends Fragment {



    WebView webView;

    LottieAnimationView loader;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medespoir_shop, container, false);
        webView = (WebView) view.findViewById(R.id.webView_medespoirdhop);
        loader = (LottieAnimationView) view.findViewById(R.id.loader);
        ButterKnife.bind(this, view);
        loader.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(MEConstants.URL_MEDESPOIRSHOP);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loader.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                loader.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
            }


        });

        return view;
    }

}

