package com.touchlink.medespoir.MVP.views.ui.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.fxn.cue.Cue;
import com.fxn.cue.enums.Duration;
import com.fxn.cue.enums.Type;
import com.google.gson.Gson;
import com.touchlink.medespoir.MVP.models.network.Actualite;
import com.touchlink.medespoir.MVP.models.network.Bilanoperatoires;
import com.touchlink.medespoir.MVP.models.network.CompteRBilan;
import com.touchlink.medespoir.MVP.models.network.Compterendu;
import com.touchlink.medespoir.MVP.models.network.MedespoirResponse;
import com.touchlink.medespoir.MVP.views.adapters.BilanOperatoireAdapter;
import com.touchlink.medespoir.MVP.views.ui.activities.MainActivity;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVBold;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVRegular;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.WebServices.retrofit.WebServiceFactory;
import com.touchlink.medespoir.session.MedespoirSession;
import com.touchlink.medespoir.session.MedespoirTokenSession;
import com.touchlink.medespoir.utils.DeviceConnectivity;
import com.touchlink.medespoir.utils.MEConstants;
import com.touchlink.medespoir.utils.Utility;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompteRenduFragment extends Fragment {
    @SuppressLint("id")
    @BindView(R.id.date)
    MedEspoirTVBold date ;
    @SuppressLint("id")
    @BindView(R.id.username)
    MedEspoirTVBold username ;
    @SuppressLint("id")
    @BindView(R.id.doctor_name)
    MedEspoirTVBold doctorName ;
    @SuppressLint("id")
    @BindView(R.id.type_anesthesie)
    MedEspoirTVBold typeAnesthesie ;
    @SuppressLint("id")
    @BindView(R.id.categorie_operation)
    MedEspoirTVBold categorieOperation;
    @SuppressLint("id")
    @BindView(R.id.bilan_preoperatoire_desc)
    MedEspoirTVRegular bilanPreoperatoireDesc ;
    @SuppressLint("id")
    @BindView(R.id.rv_bilan_operatoire)
    RecyclerView rvBilanOperatoire ;
    @SuppressLint("id")
    @BindView(R.id.bilan_operatoire_label)
    MedEspoirTVBold labelBilanOperatoire ;
    @SuppressLint("id")
    @BindView(R.id.bilan_preoperatoire_label)
    MedEspoirTVBold labelBilanPreoperatoire ;
    @SuppressLint("id")
    @BindView(R.id.loader)
    LottieAnimationView loader ;
    @SuppressLint("id")
    @BindView(R.id.ll_compte_rendu_response)
    LinearLayout llCompteRenduResponse ;
    @SuppressLint("id")
    @BindView(R.id.text_no_compte_rendu)
    MedEspoirTVBold textNoCompteRendu ;
    @SuppressLint("id")
    @BindView(R.id.ll_no_connexion)
    LinearLayout llNoConnexion ;

    @SuppressLint("id")
    @OnClick(R.id.button_assistance)
    public void goToAssistance(){
        ReclamationFragment reclamationFragment = new ReclamationFragment();
        MainActivity.goToFragment(reclamationFragment , true);

    }





    public CompteRenduFragment() {
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
        View view = inflater.inflate(R.layout.fragment_compte_rendu, container, false);
        init(view);

        return view ;
    }
    private void init(View view){
        ButterKnife.bind(this , view);
        if(!DeviceConnectivity.isNetworkAvailable(getContext())) {
            loader.setVisibility(View.GONE);
            llCompteRenduResponse.setVisibility(View.GONE);
            textNoCompteRendu.setVisibility(View.GONE);
            llNoConnexion.setVisibility(View.VISIBLE);
        } else {

            getCompteRendu();

        }


    }


    private void getCompteRendu(){
        loader.setVisibility(View.VISIBLE);
        llCompteRenduResponse.setVisibility(View.GONE);
        textNoCompteRendu.setVisibility(View.GONE);
        llNoConnexion.setVisibility(View.GONE);

        Call<MedespoirResponse> call = WebServiceFactory.getInstance().getApi().getCompteRendu("Bearer "+ MedespoirTokenSession.getUsergestToken(getContext()));
        call.enqueue(new Callback<MedespoirResponse>() {
            @Override
            public void onResponse(Call<MedespoirResponse> call, Response<MedespoirResponse> response) {
                if(response.code() == MEConstants.CODE_TOKEN_EXPIRED || response.code() == MEConstants.CODE_NO_TOKEN)
                {



                    MedespoirSession.Reconnecter(getContext());

                    getCompteRendu();
                } else{
                    loader.setVisibility(View.GONE);
                    llCompteRenduResponse.setVisibility(View.VISIBLE);
                    llNoConnexion.setVisibility(View.GONE);

                    if (response != null) {
                        if (response.body() != null) {
                            if (response.body().getStatus() == 1) {
                                textNoCompteRendu.setVisibility(View.GONE);
                                Object data = null;
                                data = response.body().getData();


                                Gson gson = Utility.getGsonInstance();
                                try {

                                    CompteRBilan compterendu = gson.fromJson(gson.toJson(data), CompteRBilan.class);


                                    if (!(compterendu.getCompterendu().getName() == "") && compterendu.getCompterendu().getName() != null){
                                        username.setVisibility(View.VISIBLE);
                                        if ( (!(compterendu.getCompterendu().getSecondeName()
                                                == "")) && compterendu.getCompterendu().getSecondeName() != null){
                                            username.setText(username.getText() + compterendu.getCompterendu().getName() + " " + compterendu.getCompterendu().getSecondeName());
                                        } else {
                                            username.setText(username.getText() + compterendu.getCompterendu().getName());
                                        }



                                    } else {
                                        username.setVisibility(View.GONE);
                                    }
                                    if (compterendu.getCompterendu().getCompterendus() != null){
                                        if (compterendu.getCompterendu().getCompterendus().get(0).getDocteur() != "" && compterendu.getCompterendu().getCompterendus().get(0).getDocteur() != null){
                                            doctorName.setVisibility(View.VISIBLE);
                                            doctorName.setText(doctorName.getText() + compterendu.getCompterendu().getCompterendus().get(0).getDocteur());
                                        } else {
                                            doctorName.setVisibility(View.GONE);
                                        }

                                        if (compterendu.getCompterendu().getCompterendus().get(0).getCategorie() !="" && compterendu.getCompterendu().getCompterendus().get(0).getCategorie() != null ){
                                            typeAnesthesie.setVisibility(View.VISIBLE);
                                            typeAnesthesie.setText(typeAnesthesie.getText() + compterendu.getCompterendu().getCompterendus().get(0).getCategorie());
                                        } else {
                                            typeAnesthesie.setVisibility(View.GONE);
                                        }
                                        if (compterendu.getCompterendu().getCompterendus().get(0).getBilanpreparatoire() != "" && compterendu.getCompterendu().getCompterendus().get(0).getBilanpreparatoire() != null){
                                            labelBilanPreoperatoire.setVisibility(View.VISIBLE);
                                            bilanPreoperatoireDesc.setVisibility(View.VISIBLE);
                                            bilanPreoperatoireDesc.setText(compterendu.getCompterendu().getCompterendus().get(0).getBilanpreparatoire());
                                        } else {
                                            labelBilanPreoperatoire.setVisibility(View.GONE);
                                            bilanPreoperatoireDesc.setVisibility(View.GONE);
                                        }
                                        int size = compterendu.getCompterendu().getCompterendus().get(0).getBilanoperatoires().size();

                                        if( size != 0){
                                            rvBilanOperatoire.setVisibility(View.VISIBLE);
                                            labelBilanOperatoire.setVisibility(View.VISIBLE);
                                            ArrayList<Bilanoperatoires> bilanoperatoires = new ArrayList<Bilanoperatoires>();
                                            for (int i = 0 ; i < size ; i++){
                                                bilanoperatoires.add(compterendu.getCompterendu().getCompterendus().get(0).getBilanoperatoires().get(i));
                                            }
                                            rvBilanOperatoire.setHasFixedSize(true);
                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                                            rvBilanOperatoire.setLayoutManager(linearLayoutManager);

                                            BilanOperatoireAdapter bilanOperatoireAdapter = new BilanOperatoireAdapter(getContext() , bilanoperatoires);
                                            rvBilanOperatoire.setAdapter(bilanOperatoireAdapter);


                                        } else {
                                            rvBilanOperatoire.setVisibility(View.GONE);
                                            labelBilanOperatoire.setVisibility(View.GONE);

                                        }

                                    }





                                } catch (Exception e){
                                    e.printStackTrace();

                                    loader.setVisibility(View.GONE);
                                    llCompteRenduResponse.setVisibility(View.GONE);
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
                            } else if (response.body().getStatus() == 2){
                                textNoCompteRendu.setVisibility(View.VISIBLE);
                                loader.setVisibility(View.GONE);
                                llCompteRenduResponse.setVisibility(View.GONE);
                            } else {
                                textNoCompteRendu.setVisibility(View.GONE);
                                loader.setVisibility(View.GONE);
                                llCompteRenduResponse.setVisibility(View.GONE);
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

                }
            }


            @Override
            public void onFailure(Call<MedespoirResponse> call, Throwable t) {

                loader.setVisibility(View.GONE);
                llNoConnexion.setVisibility(View.GONE);
                llCompteRenduResponse.setVisibility(View.GONE);
                textNoCompteRendu.setVisibility(View.GONE);
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