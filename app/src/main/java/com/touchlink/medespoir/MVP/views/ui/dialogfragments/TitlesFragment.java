package com.touchlink.medespoir.MVP.views.ui.dialogfragments;

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
import butterknife.OnClick;
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
import com.touchlink.medespoir.MVP.models.network.Operations;
import com.touchlink.medespoir.MVP.models.network.Title;
import com.touchlink.medespoir.MVP.models.network.Titre;
import com.touchlink.medespoir.MVP.views.adapters.OperationsAdapter;
import com.touchlink.medespoir.MVP.views.adapters.TitlesAdapter;
import com.touchlink.medespoir.MVP.views.ui.callbacks.ReclamationListener;
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


public class TitlesFragment extends DialogFragment {


    private View rootView;
    private Unbinder unbinder;
    private int titleID = -1 ;
    private String titleLabel = null ;
    @SuppressLint("id")
    @BindView(R.id.rv_fragment_titles)
    RecyclerView rvTitlesFragment ;
    @SuppressLint("id")
    @BindView(R.id.no_titles)
    MedEspoirTVBold noTitles ;
    TitleListener listener ;
    @SuppressLint("id")
    @BindView(R.id.ll_titles)
    LinearLayout llTitles ;
    @SuppressLint("id")
    @BindView(R.id.loader)
    LottieAnimationView loader ;
    @SuppressLint("id")
    @BindView(R.id.ll_connexectivity_dialog)
    LinearLayout llConnectivityDialog ;




    public TitlesFragment() {
        // Required empty public constructor
    }



    public static TitlesFragment newInstance() {
        TitlesFragment fragment = new TitlesFragment();
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
                titleID = -1;
                titleLabel = null ;
                alertDialog.show();
            }
        });
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    titleID = -1 ;
                    titleLabel = null ;
                    alertDialog.cancel();
                    dismiss();
                }
                return false;
            }
        });
        return alertDialog;
    }

    private void initViews(){

        //rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_titles, null, false);
        rootView = View.inflate(getContext(), R.layout.fragment_titles, null);
        unbinder = ButterKnife.bind(this, rootView);
        listener = new TitleListener() {
            @Override
            public void onClick(View view, int position, String id , String label) {
                Intent intent = new Intent();
                intent.putExtra(MEConstants.TITLE_ID, id);
                intent.putExtra(MEConstants.TITLE_LABEL , label);

                getTargetFragment().onActivityResult(getTargetRequestCode() , 109 , intent);

                dismiss();
            }
        };
        if(!DeviceConnectivity.isNetworkAvailable(getContext())) {
            loader.setVisibility(View.GONE);
            noTitles.setVisibility(View.GONE);
            llConnectivityDialog.setVisibility(View.VISIBLE);
            llTitles.setVisibility(View.GONE);
        } else {
            getTitlesWS();
        }
    }

    private void getTitlesWS(){
        loader.setVisibility(View.VISIBLE);
        llConnectivityDialog.setVisibility(View.GONE);
        llTitles.setVisibility(View.GONE);
        noTitles.setVisibility(View.GONE);
        Call<MedespoirResponse> call = WebServiceFactory.getInstance().getApi().getReclamationTitles("Bearer "+ MedespoirTokenSession.getUsergestToken(getContext()));
        call.enqueue(new Callback<MedespoirResponse>() {
            @Override
            public void onResponse(Call<MedespoirResponse> call, Response<MedespoirResponse> response) {
                if (response.code() == MEConstants.CODE_TOKEN_EXPIRED || response.code() == MEConstants.CODE_NO_TOKEN) {


                    MedespoirSession.Reconnecter(getContext());

                    getTitlesWS();
                } else {
                    loader.setVisibility(View.GONE);
                    if (response != null) {
                        if (response.body() != null) {
                            if (response.body().getStatus() == 1) {
                                llConnectivityDialog.setVisibility(View.GONE);
                                llTitles.setVisibility(View.VISIBLE);
                                rvTitlesFragment.setVisibility(View.VISIBLE);
                                Object data = null;
                                data = response.body().getData();
                                Gson gson = Utility.getGsonInstance();
                                try {

                                    Title title = gson.fromJson(gson.toJson(data) , Title.class);
                                    ArrayList<Titre>  listOfTitles = title.getTitle();
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                    rvTitlesFragment.setLayoutManager(layoutManager);
                                    TitlesAdapter titlesAdapter = new TitlesAdapter(getContext() , listOfTitles, listener);
                                    rvTitlesFragment.setAdapter(titlesAdapter);



                                }catch (Exception e){
                                    e.printStackTrace();

                                    dismiss();
                                    llConnectivityDialog.setVisibility(View.GONE);
                                    llTitles.setVisibility(View.GONE);
                                    noTitles.setVisibility(View.GONE);
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
                                llTitles.setVisibility(View.GONE);
                                noTitles.setVisibility(View.VISIBLE);
                            }else {
                                dismiss();
                                llConnectivityDialog.setVisibility(View.GONE);
                                llTitles.setVisibility(View.GONE);
                                noTitles.setVisibility(View.GONE);
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
                            llTitles.setVisibility(View.GONE);
                            noTitles.setVisibility(View.GONE);
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
                        llTitles.setVisibility(View.GONE);
                        noTitles.setVisibility(View.GONE);
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
                llTitles.setVisibility(View.GONE);
                noTitles.setVisibility(View.GONE);
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



