package com.touchlink.medespoir.MVP.views.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.fxn.cue.Cue;
import com.fxn.cue.enums.Duration;
import com.fxn.cue.enums.Type;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.touchlink.medespoir.MVP.models.network.MedespoirResponse;
import com.touchlink.medespoir.MVP.models.network.ProgramItem;
import com.touchlink.medespoir.MVP.models.network.Programs;
import com.touchlink.medespoir.MVP.models.network.Sousprogramme;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVBold;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVLight;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVRegular;
import com.touchlink.medespoir.WebServices.retrofit.WebServiceFactory;
import com.touchlink.medespoir.session.MedespoirSession;
import com.touchlink.medespoir.session.MedespoirTokenSession;
import com.touchlink.medespoir.utils.DeviceConnectivity;
import com.touchlink.medespoir.utils.MEConstants;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.utils.Utility;

public class ProgramItemActivity extends AppCompatActivity {

    private Call<MedespoirResponse> callPgrogram;
    @SuppressLint("id")
    @BindView(R.id.loader)
    LottieAnimationView loader ;
    @SuppressLint("id")
    @BindView(R.id.ll_connectivity_dialog)
    LinearLayout llConnectivityDialog ;
    @SuppressLint("id")
    @BindView(R.id.fab_program_details)
    ImageView fab ;
    @SuppressLint("id")
    @BindView(R.id.ll_program_details)
    LinearLayout llProgramDetails ;
    @SuppressLint("id")
    @BindView(R.id.label_program)
    MedEspoirTVBold label ;
    @SuppressLint("id")
    @BindView(R.id.Heure_program)
    MedEspoirTVRegular Heure ;
    @SuppressLint("id")
    @BindView(R.id.desc_program)
    MedEspoirTVLight description ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_item);
        init();

    }

    private void init(){
        ButterKnife.bind(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext() , MessageActivity.class);
                startActivity(intent);
            }
        });
        String id = getIntent().getStringExtra(MEConstants.PROGRAM_ID);
        if (!id.equalsIgnoreCase("")){

            if(DeviceConnectivity.isNetworkAvailable(getBaseContext())) {
                loadData(id);
            } else {
                llConnectivityDialog.setVisibility(View.VISIBLE);
                llProgramDetails.setVisibility(View.GONE);
                loader.setVisibility(View.GONE);

            }



        } else {
            llConnectivityDialog.setVisibility(View.GONE);
            llProgramDetails.setVisibility(View.GONE);
            loader.setVisibility(View.GONE);
            
            Cue.init()
                    .with(getBaseContext())
                    .setMessage(MEConstants.TECH_ERROR)
                    .setType(Type.CUSTOM)
                    .setDuration(Duration.LONG)
                    .setBorderWidth(5)
                    .setCornerRadius(10)
                    .setCustomFontColor(Color.parseColor("#ca994c"),
                            Color.parseColor("#ffffff"),
                            Color.parseColor("#b6843d"))
                    .setFontFace("fonts/nunitosansregular.ttf")
                    .setBorderWidth(1)
                    .setCornerRadius(20)
                    .setPadding(30)
                    .setTextSize(16)
                    .setGravity(Gravity.CENTER)
                    .show();
        }


    }

    private void loadData(String id){
        llConnectivityDialog.setVisibility(View.GONE);
        llProgramDetails.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
        final JsonObject postParams = new JsonObject();
        postParams.addProperty("id", id + "");
        callPgrogram =  WebServiceFactory.getInstance().getApi().getDailyProgramDetails("Bearer "+ MedespoirTokenSession.getUsergestToken(getBaseContext()),postParams);
        callPgrogram.enqueue(new Callback<MedespoirResponse>() {
            @Override
            public void onResponse(Call<MedespoirResponse> call, Response<MedespoirResponse> response) {
                if (response.code() == MEConstants.CODE_TOKEN_EXPIRED || response.code() == MEConstants.CODE_NO_TOKEN) {
                    MedespoirSession.Reconnecter(getBaseContext());
                    loadData(id);
                } else {
                    if (response != null) {
                        if (response.body() != null) {

                            if (response.body().getStatus() == 1) {
                                llConnectivityDialog.setVisibility(View.GONE);
                                llProgramDetails.setVisibility(View.VISIBLE);
                                loader.setVisibility(View.GONE);
                                Object data = null;
                                data = response.body().getData();

                                Gson gson = Utility.getGsonInstance();
                                try {
                                ProgramItem program = gson.fromJson(gson.toJson(data), ProgramItem.class);

                                label.setText(program.getLabel());
                                Heure.setText(program.getHeuredebut() + " -- " + program.getHeurefin());
                                description.setText( program.getDescription());
                                }catch (Exception e){
                                    llConnectivityDialog.setVisibility(View.GONE);
                                    llProgramDetails.setVisibility(View.GONE);
                                    loader.setVisibility(View.GONE);
                                    Cue.init()
                                            .with(getBaseContext())
                                            .setMessage(MEConstants.TECH_ERROR)
                                            .setType(Type.CUSTOM)
                                            .setDuration(Duration.LONG)
                                            .setBorderWidth(5)
                                            .setCornerRadius(10)
                                            .setCustomFontColor(Color.parseColor("#ca994c"),
                                                    Color.parseColor("#ffffff"),
                                                    Color.parseColor("#b6843d"))
                                            .setFontFace("fonts/nunitosansregular.ttf")
                                            .setBorderWidth(1)
                                            .setCornerRadius(20)
                                            .setPadding(30)
                                            .setTextSize(16)
                                            .setGravity(Gravity.CENTER)
                                            .show();
                                }

                            } else {
                                llConnectivityDialog.setVisibility(View.GONE);
                                llProgramDetails.setVisibility(View.GONE);
                                loader.setVisibility(View.GONE);
                                Cue.init()
                                        .with(getBaseContext())
                                        .setMessage(MEConstants.TECH_ERROR)
                                        .setType(Type.CUSTOM)
                                        .setDuration(Duration.LONG)
                                        .setBorderWidth(5)
                                        .setCornerRadius(10)
                                        .setCustomFontColor(Color.parseColor("#ca994c"),
                                                Color.parseColor("#ffffff"),
                                                Color.parseColor("#b6843d"))
                                        .setFontFace("fonts/nunitosansregular.ttf")
                                        .setBorderWidth(1)
                                        .setCornerRadius(20)
                                        .setPadding(30)
                                        .setTextSize(16)
                                        .setGravity(Gravity.CENTER)
                                        .show();
                            }
                        } else {
                            llConnectivityDialog.setVisibility(View.GONE);
                            llProgramDetails.setVisibility(View.GONE);
                            loader.setVisibility(View.GONE);
                            Cue.init()
                                    .with(getBaseContext())
                                    .setMessage(MEConstants.TECH_ERROR)
                                    .setType(Type.CUSTOM)
                                    .setDuration(Duration.LONG)
                                    .setBorderWidth(5)
                                    .setCornerRadius(10)
                                    .setCustomFontColor(Color.parseColor("#ca994c"),
                                            Color.parseColor("#ffffff"),
                                            Color.parseColor("#b6843d"))
                                    .setFontFace("fonts/nunitosansregular.ttf")
                                    .setBorderWidth(1)
                                    .setCornerRadius(20)
                                    .setPadding(30)
                                    .setTextSize(16)
                                    .setGravity(Gravity.CENTER)
                                    .show();

                        }

                    } else {
                        llConnectivityDialog.setVisibility(View.GONE);
                        llProgramDetails.setVisibility(View.GONE);
                        loader.setVisibility(View.GONE);
                        Cue.init()
                                .with(getBaseContext())
                                .setMessage(MEConstants.TECH_ERROR)
                                .setType(Type.CUSTOM)
                                .setDuration(Duration.LONG)
                                .setBorderWidth(5)
                                .setCornerRadius(10)
                                .setCustomFontColor(Color.parseColor("#ca994c"),
                                        Color.parseColor("#ffffff"),
                                        Color.parseColor("#b6843d"))
                                .setFontFace("fonts/nunitosansregular.ttf")
                                .setBorderWidth(1)
                                .setCornerRadius(20)
                                .setPadding(30)
                                .setTextSize(16)
                                .setGravity(Gravity.CENTER)
                                .show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MedespoirResponse> call, Throwable t) {

                llConnectivityDialog.setVisibility(View.GONE);
                llProgramDetails.setVisibility(View.GONE);
                loader.setVisibility(View.GONE);
                Cue.init()
                        .with(getBaseContext())
                        .setMessage(MEConstants.TECH_ERROR)
                        .setType(Type.CUSTOM)
                        .setDuration(Duration.LONG)
                        .setBorderWidth(5)
                        .setCornerRadius(10)
                        .setCustomFontColor(Color.parseColor("#ca994c"),
                                Color.parseColor("#ffffff"),
                                Color.parseColor("#b6843d"))
                        .setFontFace("fonts/nunitosansregular.ttf")
                        .setBorderWidth(1)
                        .setCornerRadius(20)
                        .setPadding(30)
                        .setTextSize(16)
                        .setGravity(Gravity.CENTER)
                        .show();

            }
        });


    }
}