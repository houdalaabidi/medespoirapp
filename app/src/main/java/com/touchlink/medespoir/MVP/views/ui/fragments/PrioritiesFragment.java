package com.touchlink.medespoir.MVP.views.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.fxn.cue.Cue;
import com.fxn.cue.enums.Duration;
import com.fxn.cue.enums.Type;
import com.google.gson.Gson;
import com.touchlink.medespoir.MVP.models.network.MedespoirResponse;
import com.touchlink.medespoir.MVP.models.network.Priorite;
import com.touchlink.medespoir.MVP.models.network.Prioritee;
import com.touchlink.medespoir.MVP.models.network.Title;
import com.touchlink.medespoir.MVP.models.network.Titre;
import com.touchlink.medespoir.MVP.views.adapters.PrioritiesAdapter;
import com.touchlink.medespoir.MVP.views.adapters.TitlesAdapter;
import com.touchlink.medespoir.MVP.views.ui.callbacks.PrioritiesListener;
import com.touchlink.medespoir.MVP.views.ui.callbacks.TitleListener;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVBold;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.WebServices.retrofit.WebServiceFactory;
import com.touchlink.medespoir.session.MedespoirSession;
import com.touchlink.medespoir.session.MedespoirTokenSession;
import com.touchlink.medespoir.utils.DeviceConnectivity;
import com.touchlink.medespoir.utils.MEConstants;
import com.touchlink.medespoir.utils.Utility;

import java.util.ArrayList;


public class PrioritiesFragment extends DialogFragment {

    private View rootView;
    private Unbinder unbinder;

    private String prioriteLabel = null ;
    @SuppressLint("id")
    @BindView(R.id.rv_fragment_priorities)
    RecyclerView rvPrioritiesFragment ;
    @SuppressLint("id")
    @BindView(R.id.no_priorities)
    MedEspoirTVBold noPriorities ;
    PrioritiesListener listener ;
    @SuppressLint("id")
    @BindView(R.id.ll_priorities)
    LinearLayout llPriorities ;
    @SuppressLint("id")
    @BindView(R.id.loader)
    LottieAnimationView loader ;
    @SuppressLint("id")
    @BindView(R.id.ll_connexectivity_dialog)
    LinearLayout llConnectivityDialog ;

    public PrioritiesFragment() {
        // Required empty public constructor
    }


    public static PrioritiesFragment newInstance() {
        PrioritiesFragment fragment = new PrioritiesFragment();
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        initViews();
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(rootView).setCancelable(false).create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setCancelable(true);
        //alertDialog.setOnShowListener(dialog -> onDialogShow(alertDialog));
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                prioriteLabel = null ;
                alertDialog.show();
            }
        });
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    prioriteLabel = null ;
                    alertDialog.cancel();
                    dismiss();
                }
                return false;
            }
        });
        return alertDialog;
    }


    private void initViews(){

        //rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_priorities, null, false);
        rootView = View.inflate(getContext(), R.layout.fragment_priorities, null);
        unbinder = ButterKnife.bind(this, rootView);
        listener = new PrioritiesListener() {
            @Override
            public void onClick(View view, int position, String label) {
                Intent intent = new Intent();
                intent.putExtra(MEConstants.PRIORITE_LABEL , label);

                getTargetFragment().onActivityResult(getTargetRequestCode() , 119 , intent);

                dismiss();
            }
        };
        if(!DeviceConnectivity.isNetworkAvailable(getContext())) {
            loader.setVisibility(View.GONE);
            noPriorities.setVisibility(View.GONE);
            llConnectivityDialog.setVisibility(View.VISIBLE);
            llPriorities.setVisibility(View.GONE);
        } else {
            getPrioritiesWS();
        }
    }

    private void  getPrioritiesWS(){
        loader.setVisibility(View.VISIBLE);
        llConnectivityDialog.setVisibility(View.GONE);
        llPriorities.setVisibility(View.GONE);
        noPriorities.setVisibility(View.GONE);
        Call<MedespoirResponse> call = WebServiceFactory.getInstance().getApi().getReclamationPriorities("Bearer "+ MedespoirTokenSession.getUsergestToken(getContext()));
        call.enqueue(new Callback<MedespoirResponse>() {
            @Override
            public void onResponse(Call<MedespoirResponse> call, Response<MedespoirResponse> response) {
                if (response.code() == MEConstants.CODE_TOKEN_EXPIRED || response.code() == MEConstants.CODE_NO_TOKEN) {


                    MedespoirSession.Reconnecter(getContext());

                    getPrioritiesWS();
                } else {
                    loader.setVisibility(View.GONE);
                    if (response != null) {
                        if (response.body() != null) {
                            if (response.body().getStatus() == 1) {
                                llConnectivityDialog.setVisibility(View.GONE);
                                llPriorities.setVisibility(View.VISIBLE);
                                rvPrioritiesFragment.setVisibility(View.VISIBLE);
                                Object data = null;
                                data = response.body().getData();
                                Gson gson = Utility.getGsonInstance();
                                try {

                                    Priorite priorite = gson.fromJson(gson.toJson(data) , Priorite.class);
                                    ArrayList<Prioritee> listOfPriorities = priorite.getPriorite();
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                    rvPrioritiesFragment.setLayoutManager(layoutManager);
                                    PrioritiesAdapter prioritiesAdapter = new PrioritiesAdapter(getContext() , listOfPriorities, listener);
                                    rvPrioritiesFragment.setAdapter(prioritiesAdapter);



                                }catch (Exception e){
                                    e.printStackTrace();

                                    dismiss();
                                    llConnectivityDialog.setVisibility(View.GONE);
                                    llPriorities.setVisibility(View.GONE);
                                    noPriorities.setVisibility(View.GONE);
                                    Cue.init()
                                            .with(getContext())
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

                            } else if(response.body().getStatus() == 2 ){
                                llConnectivityDialog.setVisibility(View.GONE);
                                llPriorities.setVisibility(View.GONE);
                                noPriorities.setVisibility(View.VISIBLE);
                            }else {
                                dismiss();
                                llConnectivityDialog.setVisibility(View.GONE);
                                llPriorities.setVisibility(View.GONE);
                                noPriorities.setVisibility(View.GONE);
                                Cue.init()
                                        .with(getContext())
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
                            dismiss();
                            llConnectivityDialog.setVisibility(View.GONE);
                            llPriorities.setVisibility(View.GONE);
                            noPriorities.setVisibility(View.GONE);
                            Cue.init()
                                    .with(getContext())
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

                        dismiss();
                        llConnectivityDialog.setVisibility(View.GONE);
                        llPriorities.setVisibility(View.GONE);
                        noPriorities.setVisibility(View.GONE);
                        Cue.init()
                                .with(getContext())
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

                dismiss();
                llConnectivityDialog.setVisibility(View.GONE);
                llPriorities.setVisibility(View.GONE);
                noPriorities.setVisibility(View.GONE);
                Cue.init()
                        .with(getContext())
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