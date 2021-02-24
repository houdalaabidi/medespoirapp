package com.touchlink.medespoir.MVP.views.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AsyncPlayer;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.touchlink.medespoir.MVP.models.network.MedespoirResponse;
import com.touchlink.medespoir.MVP.views.adapters.HomeAdapter;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.WebServices.retrofit.WebServiceFactory;
import com.touchlink.medespoir.session.FirebaseIdService;
import com.touchlink.medespoir.session.MedespoirSession;
import com.touchlink.medespoir.session.MedespoirTokenSession;
import com.touchlink.medespoir.session.SharedPreferencesUserFactory;
import com.touchlink.medespoir.utils.MEConstants;

import java.util.ArrayList;


public class MainFragment extends Fragment {
    private FragmentActivity myContext;
    private ArrayList<SlideModel> imageList = new ArrayList<SlideModel>();
    private ImageSlider imageSlider;
    private TabLayout tableLayout ;
    private TabLayout tableLayout2 ;
    private ViewPager viewPager ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        imageSlider = view.findViewById(R.id.image_slider);
        viewPager = view.findViewById(R.id.viewpager);
        tableLayout = view.findViewById(R.id.tabLayout);
        Log.i(MEConstants.TAG , "password " +SharedPreferencesUserFactory.retrieveUPW()+" !");
        imageList.clear();
        imageList.add(new SlideModel(R.drawable.slid_app_medespoir, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.slid_app_medespoir2, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.slid_app_medespoir3, ScaleTypes.CENTER_CROP));
        imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP);
        if (MedespoirTokenSession.getLocalStorage(getContext()) != null){
            sendFirebaseToken();
        }



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                tableLayout.addTab(tableLayout.newTab().setText("Article"));
                tableLayout.addTab(tableLayout.newTab().setText("Interventions"));
                tableLayout.addTab(tableLayout.newTab().setText("Videos"));
                tableLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                final HomeAdapter adapter = new HomeAdapter( getChildFragmentManager(),
                        3, imageSlider);
                viewPager.setAdapter(adapter);
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tableLayout));
                tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager.setCurrentItem(tab.getPosition());
                    }
                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }
                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                    }
                });

            }
        },300);

        return view;
    }


    private void sendFirebaseToken(){

        JsonObject postParams = new JsonObject();
        if (FirebaseIdService.refreshedToken != null) {


            postParams.addProperty("token",FirebaseIdService.refreshedToken);
            Log.i(MEConstants.TAG , "token = "+ FirebaseIdService.refreshedToken);

        } else {
            postParams.addProperty("token", FirebaseInstanceId.getInstance().getToken()+"");
            Log.i(MEConstants.TAG , "token = "+  FirebaseInstanceId.getInstance().getToken()+"");
        }
        Call<MedespoirResponse> call = WebServiceFactory.getInstance().getApi().sendFirebaseToken("Bearer "+ MedespoirTokenSession.getUsergestToken(getContext()) ,postParams );
        call.enqueue(new Callback<MedespoirResponse>() {
            @Override
            public void onResponse(Call<MedespoirResponse> call, Response<MedespoirResponse> response) {
                if (response.code() == MEConstants.CODE_TOKEN_EXPIRED || response.code() == MEConstants.CODE_NO_TOKEN) {

                    Log.i(MEConstants.TAG , "Expired");
                    MedespoirSession.Reconnecter(getContext());
                    //consumeWebService();
                    sendFirebaseToken();
                } else {
                    if (response != null) {
                        if (response.body() != null) {
                            if (response.body().getStatus() == 1) {
                                Log.i(MEConstants.TAG , "success send firebase token ");

                            }

                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<MedespoirResponse> call, Throwable t) {

            }
        });
    }
}