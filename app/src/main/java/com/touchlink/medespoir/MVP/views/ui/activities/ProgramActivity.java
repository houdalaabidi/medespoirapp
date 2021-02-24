package com.touchlink.medespoir.MVP.views.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.fxn.cue.Cue;
import com.fxn.cue.enums.Duration;
import com.fxn.cue.enums.Type;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;
import com.touchlink.medespoir.MVP.models.network.MedespoirResponse;
import com.touchlink.medespoir.MVP.models.network.ProgramItem;
import com.touchlink.medespoir.MVP.models.network.Programs;
import com.touchlink.medespoir.MVP.models.network.Reclamation;
import com.touchlink.medespoir.MVP.models.network.Reclamations;
import com.touchlink.medespoir.MVP.views.adapters.MesReclamationsAdapter;
import com.touchlink.medespoir.MVP.views.adapters.ProgramAdapter;
import com.touchlink.medespoir.MVP.views.ui.callbacks.ProgramListener;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVBold;
import com.touchlink.medespoir.MVP.views.ui.dialogfragments.ConnexionDialogFragment;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.WebServices.retrofit.WebServiceFactory;
import com.touchlink.medespoir.session.MedespoirSession;
import com.touchlink.medespoir.session.MedespoirTokenSession;
import com.touchlink.medespoir.utils.DeviceConnectivity;
import com.touchlink.medespoir.utils.MEConstants;
import com.touchlink.medespoir.utils.Utility;

import android.annotation.SuppressLint;
import android.content.Intent;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProgramActivity extends AppCompatActivity {
    @SuppressLint("id")
    @BindView(R.id.program_button_back)
    ImageView programButtonBack ;
    private Call<MedespoirResponse> callPgrogram;
    @SuppressLint("id")
    @BindView(R.id.program_calendar)
    CollapsibleCalendar programCalendar ;
    @SuppressLint("id")
    @BindView(R.id.recycler_view_pgrogram)
    RecyclerView recyclerView ;
    @SuppressLint("id")
    @BindView(R.id.loader)
    LottieAnimationView loader ;
    private ArrayList<ProgramItem> listOfItemsProgram = new ArrayList<ProgramItem>() ;
    ProgramListener listener ;
    @SuppressLint("id")
    @BindView(R.id.fab_program)
    ImageView fabProgram ;
    @SuppressLint("id")
    @BindView(R.id.text_no_programs)
    MedEspoirTVBold textNoPrograms ;
    @BindView(R.id.swipe_container_programs)
    SwipeRefreshLayout swipeRefreshLayout ;
    private static Day day ;
    private static String day1 ;
    private static String month1;
    private static String year1 ;


    private static boolean fromSwiper = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_program);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fromSwiper = false ;
        listOfItemsProgram.clear();
        listener = null ;
        day = null;
        day1 = null ;
        month1 = null;
        year1 = null ;

    }

    void getDailyProgram(String day , String month , String year){
        Log.i(MEConstants.TAG ,"day from getDailyProgram  = "+ day + month +year );
        if(!DeviceConnectivity.isNetworkAvailable(getBaseContext())) {
            listOfItemsProgram.clear();
            textNoPrograms.setVisibility(View.GONE);
            loader.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.GONE);
            ConnexionDialogFragment connectivityFragment = ConnexionDialogFragment.newInstance();
            connectivityFragment.show(getSupportFragmentManager(), "ConnexionDialogFragment");
        } else {
            listOfItemsProgram.clear();
            if( !fromSwiper){
                loader.setVisibility(View.VISIBLE);
            }

            textNoPrograms.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.GONE);
            final JsonObject postParams = new JsonObject();

            postParams.addProperty("year", year + "");
            postParams.addProperty("day", day + "");
            postParams.addProperty("month", month + "");
            callPgrogram =  WebServiceFactory.getInstance().getApi().getDailyProgram("Bearer "+ MedespoirTokenSession.getUsergestToken(getBaseContext()),postParams);
            callPgrogram.enqueue(new Callback<MedespoirResponse>() {
                @Override
                public void onResponse(Call<MedespoirResponse> call, Response<MedespoirResponse> response) {

                    if (response.code() == MEConstants.CODE_TOKEN_EXPIRED || response.code() == MEConstants.CODE_NO_TOKEN) {
                        MedespoirSession.Reconnecter(getBaseContext());
                        getDailyProgram( day ,  month , year);
                    } else {
                        loader.setVisibility(View.GONE);

                        if (response != null) {
                            if (response.body() != null) {
                                Log.i(MEConstants.TAG ,"status = "+ response.body().getStatus() );

                                if (response.body().getStatus() == 1) {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                                    /*recyclerView.setHasFixedSize(true);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    ProgramAdapter adapter = new ProgramAdapter(getBaseContext() , listener ,listOfItemsProgram);
                                    recyclerView.setAdapter(adapter);*/
                                    textNoPrograms.setVisibility(View.GONE);

                                    Object data = null;
                                    data = response.body().getData();

                                    Gson gson = Utility.getGsonInstance();
                                    try {
                                        Log.i(MEConstants.TAG ,     response.body().getData().toString());

                                        Programs programs = gson.fromJson(gson.toJson(data), Programs.class);
                                        listOfItemsProgram = programs.getPrograms();
                                        recyclerView.setHasFixedSize(true);
                                        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
                                        recyclerView.setLayoutManager(linearLayoutManager1);
                                        ProgramAdapter adapter1 = null;
                                            adapter1 = new ProgramAdapter(getBaseContext() , listener ,listOfItemsProgram);


                                        recyclerView.setAdapter(adapter1);
                                        swipeRefreshLayout.setRefreshing(false);

                                    }catch (Exception e){

                                        e.printStackTrace();

                                        textNoPrograms.setVisibility(View.GONE);
                                        loader.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.GONE);
                                        swipeRefreshLayout.setVisibility(View.GONE);
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


                                } else if ( response.body().getStatus() == 2){

                                    textNoPrograms.setVisibility(View.VISIBLE);
                                    loader.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.GONE);
                                    swipeRefreshLayout.setVisibility(View.GONE);
                                }
                                else {

                                    loader.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.GONE);
                                    textNoPrograms.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.GONE);
                                    swipeRefreshLayout.setVisibility(View.GONE);
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

                                recyclerView.setVisibility(View.GONE);
                                textNoPrograms.setVisibility(View.GONE);
                                loader.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);
                                swipeRefreshLayout.setVisibility(View.GONE);
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


                        else{

                            recyclerView.setVisibility(View.GONE);
                            loader.setVisibility(View.GONE);
                            textNoPrograms.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            swipeRefreshLayout.setVisibility(View.GONE);
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
                    recyclerView.setVisibility(View.GONE);
                    loader.setVisibility(View.GONE);
                    textNoPrograms.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.GONE);

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

    private void init(){
        ButterKnife.bind(this);
        listener = new ProgramListener() {
            @Override
            public void onClick(View view, int position, String id) {
                Intent intent = new Intent(getBaseContext() , ProgramItemActivity.class);
                overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);
                intent.putExtra(MEConstants.PROGRAM_ID , id);
                startActivity(intent);
            }
        };

        day1 = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
        month1 = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
        year1 = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        Log.i(MEConstants.TAG , day1 + month1 + year1);

        getDailyProgram(day1 , month1 , year1);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorDarkGold) , getResources().getColor(R.color.colorBlack) ,
                getResources().getColor(R.color.grey_font) , getResources().getColor(R.color.colorGold));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //day.getDay()+"" , (day.getMonth()+1 )+"" , day.getYear()+""
                fromSwiper = true ;
                if ( day != null){

                    Log.i(MEConstants.TAG , "from setOnRefreshListener day != null "+ day.getDay());
                    getDailyProgram(day.getDay()+"" , (day.getMonth()+1) +"" , day.getYear()+"");
                }else {
                    Log.i(MEConstants.TAG , "from setOnRefreshListener day == null "+ day1);
                    getDailyProgram(day1 , month1 , year1);
                }

            }
        });

        fabProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext() , MessageActivity.class);
                startActivity(intent);
            }
        });



        programButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        programCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDaySelect() {

                  day = programCalendar.getSelectedItem();

                fromSwiper = false ;
                getDailyProgram(day.getDay()+"" , (day.getMonth()+1 )+"" , day.getYear()+"");

            }

            @Override
            public void onItemClick(@NotNull View view) {




            }

            @Override
            public void onDataUpdate() {


            }

            @Override
            public void onMonthChange() {

            }

            @Override
            public void onWeekChange(int i) {

            }

            @Override
            public void onClickListener() {


            }

            @Override
            public void onDayChanged() {



            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}