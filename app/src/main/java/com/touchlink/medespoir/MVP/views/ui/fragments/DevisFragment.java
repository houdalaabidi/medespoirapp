package com.touchlink.medespoir.MVP.views.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.touchlink.medespoir.MVP.models.network.Actualite;
import com.touchlink.medespoir.MVP.models.network.DateReclamation;
import com.touchlink.medespoir.MVP.models.network.Devis;
import com.touchlink.medespoir.MVP.models.network.DevisItem;
import com.touchlink.medespoir.MVP.models.network.MedespoirResponse;
import com.touchlink.medespoir.MVP.models.network.Reclamation;
import com.touchlink.medespoir.MVP.views.adapters.DevisAdapter;
import com.touchlink.medespoir.MVP.views.adapters.MesReclamationsAdapter;
import com.touchlink.medespoir.MVP.views.adapters.NewDevisDetailsAdapter;
import com.touchlink.medespoir.MVP.views.ui.activities.MainActivity;
import com.touchlink.medespoir.MVP.views.ui.activities.MessageActivity;
import com.touchlink.medespoir.MVP.views.ui.callbacks.DevisListener;
import com.touchlink.medespoir.MVP.views.ui.callbacks.ReclamationListener;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVBold;
import com.touchlink.medespoir.MVP.views.ui.dialogfragments.OperationsFragment;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.WebServices.retrofit.WebServiceFactory;
import com.touchlink.medespoir.session.MedespoirSession;
import com.touchlink.medespoir.session.MedespoirTokenSession;
import com.touchlink.medespoir.utils.DeviceConnectivity;
import com.touchlink.medespoir.utils.MEConstants;
import com.touchlink.medespoir.utils.Utility;

import java.lang.reflect.Array;
import java.util.ArrayList;





public class DevisFragment extends Fragment {

    DevisListener listener ;
    @SuppressLint("id")
    @BindView(R.id.fab_intervention)
    ImageView fab ;
    @SuppressLint("id")
    @BindView(R.id.recycler_view_devis)
    RecyclerView recyclerView ;
    private ArrayList<DevisItem> listOfDevis = new ArrayList<DevisItem>() ;
    @SuppressLint("id")
    @BindView(R.id.loader)
    LottieAnimationView loader ;
    @SuppressLint("id")
    @BindView(R.id.ll_no_connexion)
    LinearLayout llNoConnexion ;
    @SuppressLint("id")
    @BindView(R.id.devis_label)
    LinearLayout devisLabel ;
    @SuppressLint("id")
    @BindView(R.id.rv_devis_container)
    LinearLayout rvDevisContainer ;
    @SuppressLint("id")
    @BindView(R.id.devis_add_button)
    LinearLayout devisAddButton ;
    @SuppressLint("id")
    @BindView(R.id.text_no_devis_items)
    LinearLayout textNoDevisItems ;
    private  boolean fromNewDevis = false ;
    @SuppressLint("id")
    @OnClick(R.id.button_new_devis)
    void GoToNewDevis(){
        NewDevisFragment newDevisFragment = new NewDevisFragment();

        MainActivity.goToFragment(newDevisFragment, true);

    }


    public DevisFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_devis, container, false);
        ButterKnife.bind(this, view);
        init(view);
        return view;
    }

    private void init(View view) {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            fromNewDevis = bundle.getBoolean("devis", false);
        }



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageFragment messageFragment = new MessageFragment();
                MainActivity.goToFragment(messageFragment , true);
            }
        });

        listener = new DevisListener() {
            @Override
            public void onClick(View view, int position, String id) {
                /*DetailsReclamationFragment detailsReclamationFragment = new DetailsReclamationFragment();
                MainActivity.goToFragment(detailsReclamationFragment, false);*/
                NewDevisDetailsFragment newDevisDetailsFragment = new NewDevisDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString(MEConstants.DEVIS_ID,id);
                newDevisDetailsFragment.setArguments(bundle);
                MainActivity.goToFragment(newDevisDetailsFragment , true);

            }
        };
        if(!DeviceConnectivity.isNetworkAvailable(getContext())) {

            llNoConnexion.setVisibility(View.VISIBLE);
            textNoDevisItems.setVisibility(View.GONE);
            loader.setVisibility(View.GONE);
            devisLabel.setVisibility(View.VISIBLE);
            rvDevisContainer.setVisibility(View.GONE);
            devisAddButton.setVisibility(View.VISIBLE);

        } else {
            loadData();
        }



    }

    private void loadData(){

        llNoConnexion.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
        devisLabel.setVisibility(View.VISIBLE);
        rvDevisContainer.setVisibility(View.VISIBLE);
        devisAddButton.setVisibility(View.VISIBLE);
        textNoDevisItems.setVisibility(View.GONE);

        Call<MedespoirResponse> call = WebServiceFactory.getInstance().getApi().getAllDevis("Bearer "+ MedespoirTokenSession.getUsergestToken(getContext()));
        call.enqueue(new Callback<MedespoirResponse>() {
            @Override
            public void onResponse(Call<MedespoirResponse> call, Response<MedespoirResponse> response) {
                if (response.code() == MEConstants.CODE_TOKEN_EXPIRED || response.code() == MEConstants.CODE_NO_TOKEN) {


                    MedespoirSession.Reconnecter(getContext());

                    loadData();
                } else {

                    if (response != null) {
                        if (response.body() != null) {

                            if (response.body().getStatus() == 1) {
                                loader.setVisibility(View.GONE);
                                rvDevisContainer.setVisibility(View.VISIBLE);
                                Object data = null;
                                data = response.body().getData();


                                Gson gson = Utility.getGsonInstance();
                                try {

                                    Devis devis= gson.fromJson(gson.toJson(data), Devis.class);
                                     listOfDevis =    devis.getDevis();
                                    recyclerView.setHasFixedSize(true);
                                    LinearLayoutManager linearLayoutManager ;
                                    if(fromNewDevis) {
                                        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
                                    } else {
                                        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                    }
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    DevisAdapter adapter = new DevisAdapter(getContext() , listener ,listOfDevis);
                                    recyclerView.setAdapter(adapter);




                                }catch (Exception e){
                                    e.printStackTrace();

                                }


                            } else if (response.body().getStatus() == 2) {
                                loader.setVisibility(View.GONE);
                                rvDevisContainer.setVisibility(View.GONE);
                                textNoDevisItems.setVisibility(View.VISIBLE);


                            } else {
                                loader.setVisibility(View.GONE);
                                rvDevisContainer.setVisibility(View.GONE);
                                textNoDevisItems.setVisibility(View.GONE);
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
                rvDevisContainer.setVisibility(View.GONE);
                textNoDevisItems.setVisibility(View.GONE);
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