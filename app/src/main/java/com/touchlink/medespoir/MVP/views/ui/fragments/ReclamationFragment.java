package com.touchlink.medespoir.MVP.views.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.fxn.cue.Cue;
import com.fxn.cue.enums.Duration;
import com.fxn.cue.enums.Type;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.touchlink.medespoir.MVP.models.network.DateReclamation;
import com.touchlink.medespoir.MVP.models.network.Interventions;
import com.touchlink.medespoir.MVP.models.network.MedespoirResponse;
import com.touchlink.medespoir.MVP.models.network.Reclamation;
import com.touchlink.medespoir.MVP.models.network.Reclamations;
import com.touchlink.medespoir.MVP.views.adapters.MesReclamationsAdapter;
import com.touchlink.medespoir.MVP.views.ui.activities.InterventionActivity;
import com.touchlink.medespoir.MVP.views.ui.activities.MainActivity;
import com.touchlink.medespoir.MVP.views.ui.activities.MessageActivity;
import com.touchlink.medespoir.MVP.views.ui.callbacks.InterventionListener;
import com.touchlink.medespoir.MVP.views.ui.callbacks.ReclamationListener;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVBold;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.WebServices.retrofit.WebServiceFactory;
import com.touchlink.medespoir.session.MedespoirSession;
import com.touchlink.medespoir.session.MedespoirTokenSession;
import com.touchlink.medespoir.utils.DeviceConnectivity;
import com.touchlink.medespoir.utils.MEConstants;
import com.touchlink.medespoir.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReclamationFragment extends Fragment {

    ReclamationListener listener ;
    private  boolean fromNewReclamation = false ;
    @SuppressLint("id")
    @BindView(R.id.no_connexion)
    LinearLayout noConnexionDialog ;
    private LinearLayoutManager linearLayoutManager ;
    @SuppressLint("id")
    @BindView(R.id.loader)
    LottieAnimationView loader ;
    private Call<MedespoirResponse> callReclamation;
    @SuppressLint("id")
    @BindView(R.id.recycler_view_reclamations)
    RecyclerView recyclerView ;
    @SuppressLint("id")
    @BindView(R.id.fab_reclamation)
    ImageView fab ;
    @SuppressLint("id")
    @BindView(R.id.text_no_reclamations)
    MedEspoirTVBold textNoReclamations ;
    private ArrayList<Reclamation> listOfReclamations = new ArrayList<Reclamation>() ;
    @SuppressLint("id")
    @OnClick(R.id.button_new_reclamation)
     void GoToNewReclamation(){
        NewReclamationFragment newReclamationFragment = new NewReclamationFragment();
        MainActivity.goToFragment(newReclamationFragment, true);

    }


    public ReclamationFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reclamation, container, false);
        ButterKnife.bind(this, view);
        init(view);
        return view;
    }

    private void init(View view) {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            fromNewReclamation = bundle.getBoolean("reclamation", false);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , MessageActivity.class);
                startActivity(intent);
            }
        });



        listener = new ReclamationListener() {
            @Override
            public void onClick(View view, int position, String id) {
                DetailsReclamationFragment detailsReclamationFragment = new DetailsReclamationFragment();
                Bundle bundle = new Bundle();
                bundle.putString(MEConstants.RECLAMATION_ID,id);
                detailsReclamationFragment.setArguments(bundle);
                MainActivity.goToFragment(detailsReclamationFragment, true);

            }
        };
        consumeWebService();


    }

    private void consumeWebService(){
        if(DeviceConnectivity.isNetworkAvailable(getContext())) {
            loader.setVisibility(View.VISIBLE);
            textNoReclamations.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            noConnexionDialog.setVisibility(View.GONE);
            callReclamation = WebServiceFactory.getInstance().getApi().getAllReclamation("Bearer "+ MedespoirTokenSession.getUsergestToken(getContext()));
            callReclamation.enqueue(new Callback<MedespoirResponse>() {
                @Override
                public void onResponse(Call<MedespoirResponse> call, Response<MedespoirResponse> response) {
                    if (response.code() == MEConstants.CODE_TOKEN_EXPIRED || response.code() == MEConstants.CODE_NO_TOKEN) {
                        MedespoirSession.Reconnecter(getContext());
                        consumeWebService();
                    } else {
                        textNoReclamations.setVisibility(View.GONE);
                        loader.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        noConnexionDialog.setVisibility(View.GONE);
                        if (response != null) {
                            if (response.body() != null) {

                                if (response.body().getStatus() == 1) {
                                    Object data = null;
                                    data = response.body().getData();


                                    Gson gson = Utility.getGsonInstance();
                                    try {

                                        Reclamations reclamations = gson.fromJson(gson.toJson(data), Reclamations.class);
                                        listOfReclamations = reclamations.getReclamations();

                                        recyclerView.setHasFixedSize(true);
                                        if(fromNewReclamation) {
                                            linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
                                        } else {
                                            linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                        }
                                        recyclerView.setLayoutManager(linearLayoutManager);
                                        MesReclamationsAdapter adapter = new MesReclamationsAdapter(getContext() , listener ,listOfReclamations);
                                        recyclerView.setAdapter(adapter);

                                    }catch (Exception e){
                                        e.printStackTrace();

                                    }


                                }  else if (response.body().getStatus() == 2){
                                    loader.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.GONE);
                                    noConnexionDialog.setVisibility(View.GONE);
                                    textNoReclamations.setVisibility(View.VISIBLE);

                                }
                                else {
                                    textNoReclamations.setVisibility(View.GONE);
                                    loader.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.GONE);
                                    noConnexionDialog.setVisibility(View.GONE);
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
                                textNoReclamations.setVisibility(View.GONE);
                                loader.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);
                                noConnexionDialog.setVisibility(View.GONE);
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


                        else{
                            textNoReclamations.setVisibility(View.GONE);
                            loader.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            noConnexionDialog.setVisibility(View.GONE);
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
                    loader.setVisibility(View.GONE);
                    textNoReclamations.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    noConnexionDialog.setVisibility(View.GONE);
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

        } else {
            loader.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            noConnexionDialog.setVisibility(View.VISIBLE);
        }
    }
}