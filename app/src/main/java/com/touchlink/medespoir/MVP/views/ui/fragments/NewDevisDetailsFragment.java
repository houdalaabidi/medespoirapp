package com.touchlink.medespoir.MVP.views.ui.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.fxn.cue.Cue;
import com.fxn.cue.enums.Duration;
import com.fxn.cue.enums.Type;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.touchlink.medespoir.MVP.models.network.DeviItem;
import com.touchlink.medespoir.MVP.models.network.Operationimage;
import com.touchlink.medespoir.MVP.models.network.Sousoperation;
import com.touchlink.medespoir.MVP.models.network.Sousoperations;
import com.touchlink.medespoir.MVP.models.network.devisI;
import com.touchlink.medespoir.MVP.models.network.Fichier;
import com.touchlink.medespoir.MVP.models.network.MedespoirResponse;
import com.touchlink.medespoir.MVP.views.adapters.FileAdapter;
import com.touchlink.medespoir.MVP.views.adapters.NewDevisDetailsAdapter;
import com.touchlink.medespoir.MVP.views.ui.callbacks.FileListener;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVBold;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirETRegular;
import com.touchlink.medespoir.MVP.views.ui.dialogfragments.ConnexionDialogFragment;
import com.touchlink.medespoir.MVP.views.ui.dialogfragments.FileFragment;
import com.touchlink.medespoir.MVP.views.ui.dialogfragments.OperationsFragment;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.WebServices.retrofit.WebServiceFactory;
import com.touchlink.medespoir.session.MedespoirSession;
import com.touchlink.medespoir.session.MedespoirTokenSession;
import com.touchlink.medespoir.utils.DeviceConnectivity;
import com.touchlink.medespoir.utils.MEConstants;
import com.touchlink.medespoir.utils.Utility;

import java.util.ArrayList;


public class NewDevisDetailsFragment extends Fragment {
    @SuppressLint("id")
    @BindView(R.id.rv_new_devis_details)
    RecyclerView rvNewDevisDetails1;
    @SuppressLint("id")
    @BindView(R.id.rv_new_devis_details2)
    RecyclerView rvNewDevisDetails2;
    @SuppressLint("id")
    @BindView(R.id.rv_new_devis_details3)
    RecyclerView rvNewDevisDetails3;
    FileListener listener ;
    ArrayList<Fichier> listOfFiles = new ArrayList<>();
    @SuppressLint("id")
    @BindView(R.id.loader)
    LottieAnimationView loader ;
    @SuppressLint("id")
    @BindView(R.id.ll_no_connexion)
    LinearLayout llNoConnexion ;
    @SuppressLint("id")
    @BindView(R.id.ll_devis_response)
    RelativeLayout llDevisResponse ;
    @SuppressLint("id")
    @BindView(R.id.tv_prix)
    MedEspoirTVBold prix ;
    @SuppressLint("id")
    @BindView(R.id.et_date_devis_details)
    MedEspoirETRegular etdateDevis ;
    @SuppressLint("id")
    @BindView(R.id.label_extras)
    MedEspoirTVBold labelExtras ;
    @SuppressLint("id")
    @BindView(R.id.button_accepter)
    LinearLayout accepterButton ;
    @SuppressLint("id")
    @BindView(R.id.button_decliner)
    LinearLayout buttonDecliner ;
    private static int id2 = -1;
    @SuppressLint("id")
    @BindView(R.id.et_text_devis)
    TextInputEditText etTextDevis ;



    public NewDevisDetailsFragment() {
        // Required empty public constructor
    }

    public static NewDevisDetailsFragment newInstance() {
        NewDevisDetailsFragment fragment = new NewDevisDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_devis_details, container, false);
       init(view);
        return view ;
    }

    private void init(View view){
        ButterKnife.bind(this , view);
        listener = new FileListener() {
            @Override
            public void onClick(View view, int position, String filePath) {
                if(!DeviceConnectivity.isNetworkAvailable(getContext())) {
                    ConnexionDialogFragment connectivityFragment = ConnexionDialogFragment.newInstance();
                    connectivityFragment.show(getFragmentManager(), "ConnexionDialogFragment");
                } else {

                    Bundle bundle = new Bundle();
                    bundle.putString(MEConstants.FILE_PATH, filePath);
                    FileFragment fileFragment = FileFragment.newInstance(bundle);
                    fileFragment.show(getFragmentManager(), "FileFragment");
                }
            }
        };

        if(!DeviceConnectivity.isNetworkAvailable(getContext())) {
            llDevisResponse.setVisibility(View.GONE);
            llNoConnexion.setVisibility(View.VISIBLE);
            loader.setVisibility(View.GONE);
        } else {

            loadData();
        }
            buttonDecliner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setStatus(0 , 1);
                }
            });
            accepterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setStatus(1,0);
                }
            });
    }


    private void setStatus(int accepter , int decliner ){

        if(!DeviceConnectivity.isNetworkAvailable(getContext())) {
            llDevisResponse.setVisibility(View.GONE);
            llNoConnexion.setVisibility(View.VISIBLE);
            loader.setVisibility(View.GONE);
        } else {
            if (id2 != -1){

                JsonObject postParams = new JsonObject();
                postParams.addProperty("id", id2+"");
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("accepter",accepter);
                jsonObject.addProperty("decliner",decliner);
                postParams.add("statut", jsonObject);
                Call<MedespoirResponse> call2 = WebServiceFactory.getInstance().getApi().setStatus("Bearer " + MedespoirTokenSession.getUsergestToken(getContext()), postParams);
                call2.enqueue(new Callback<MedespoirResponse>() {
                    @Override
                    public void onResponse(Call<MedespoirResponse> call, Response<MedespoirResponse> response) {

                        if (response.code() == MEConstants.CODE_TOKEN_EXPIRED || response.code() == MEConstants.CODE_NO_TOKEN) {


                            MedespoirSession.Reconnecter(getContext());

                            setStatus(accepter, decliner);
                        } else {
                            if (response != null) {
                                if (response.body() != null) {

                                    if (response.body().getStatus() == 1) {


                                        loadData();

                                    } else if (response.body().getStatus() ==2) {
                                        llDevisResponse.setVisibility(View.GONE);
                                        llNoConnexion.setVisibility(View.GONE);
                                        loader.setVisibility(View.GONE);
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

                                    } else {
                                        llDevisResponse.setVisibility(View.GONE);
                                        llNoConnexion.setVisibility(View.GONE);
                                        loader.setVisibility(View.GONE);
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
                                    llDevisResponse.setVisibility(View.GONE);
                                    llNoConnexion.setVisibility(View.GONE);
                                    loader.setVisibility(View.GONE);
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
                                llDevisResponse.setVisibility(View.GONE);
                                llNoConnexion.setVisibility(View.GONE);
                                loader.setVisibility(View.GONE);
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
                        llDevisResponse.setVisibility(View.GONE);
                        llNoConnexion.setVisibility(View.GONE);
                        loader.setVisibility(View.GONE);
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

    }

    private void loadData(){


            llDevisResponse.setVisibility(View.GONE);
            llNoConnexion.setVisibility(View.GONE);
            loader.setVisibility(View.VISIBLE);



            llDevisResponse.setVisibility(View.GONE);
            llNoConnexion.setVisibility(View.GONE);
            loader.setVisibility(View.VISIBLE);
            Bundle bundle = getArguments();
            String id = bundle.getString(MEConstants.DEVIS_ID);
            if (!id.equalsIgnoreCase("") && id != null) {
                JsonObject postParams = new JsonObject();
                postParams.addProperty("id", id);
                Call<MedespoirResponse> call = WebServiceFactory.getInstance().getApi().getOneDevis("Bearer " + MedespoirTokenSession.getUsergestToken(getContext()), postParams);
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
                                        llDevisResponse.setVisibility(View.VISIBLE);
                                        llNoConnexion.setVisibility(View.GONE);
                                        loader.setVisibility(View.GONE);
                                        Object data = null;
                                        data = response.body().getData();


                                        Gson gson = Utility.getGsonInstance();
                                        try {

                                            DeviItem devisItem = gson.fromJson(gson.toJson(data), DeviItem.class);
                                            if (devisItem.getDevisI().get(0).getStatut().getAccepter() == 0) {
                                                accepterButton.setVisibility(View.VISIBLE);
                                            } else {
                                                accepterButton.setVisibility(View.GONE);
                                            }

                                            if (devisItem.getDevisI().get(0).getStatut().getDecliner() == 0) {
                                                buttonDecliner.setVisibility(View.VISIBLE);

                                            } else {
                                                buttonDecliner.setVisibility(View.GONE);
                                            }

                                            ArrayList<devisI> listOfDevisI = devisItem.getDevisI();
                                            devisI devisI = listOfDevisI.get(0);
                                            id2 = devisI.getId() ;


                                            if ((devisI.getDatefin() != "") && (devisI.getDatefin() != null)&&  (!devisI.getDatefin().equalsIgnoreCase("//"))
                                                    &&  (!devisI.getDatefin().equalsIgnoreCase( "/ /"))) {
                                                etdateDevis.setText("de " + devisI.getDatedebut() + " à " + devisI.getDatefin());
                                                etdateDevis.setActivated(false);
                                                etdateDevis.setFocusable(false);
                                            } else {
                                                if ((devisI.getDatedebut() != "") && (devisI.getDatedebut() != null)) {
                                                    etdateDevis.setText(devisI.getDatedebut());
                                                    etdateDevis.setActivated(false);
                                                    etdateDevis.setFocusable(false);
                                                } else {
                                                    etdateDevis.setText("");
                                                    etdateDevis.setVisibility(View.GONE);
                                                }

                                            }

                                            if (devisI.getDescription() != null && devisI.getDescription() != ""){
                                                etTextDevis.setText(devisI.getDescription());
                                            }




                                            ArrayList<Sousoperation> listOfSousOperations = devisI.getSousoperations();
                                            prix.setText(devisI.getTotal());

                                            ArrayList<Sousoperation> listSousOperations = new ArrayList<Sousoperation>();
                                            ArrayList<Sousoperation> listExtras = new ArrayList<Sousoperation>();
                                            for (int i = 0; i < listOfSousOperations.size(); i++) {
                                                if (listOfSousOperations.get(i).isExtras()) {
                                                    listExtras.add(listOfSousOperations.get(i));
                                                } else {
                                                    listSousOperations.add(listOfSousOperations.get(i));
                                                }
                                            }


                                            rvNewDevisDetails1.setHasFixedSize(true);
                                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                            rvNewDevisDetails1.setLayoutManager(layoutManager);
                                            NewDevisDetailsAdapter adapter = new NewDevisDetailsAdapter(getContext(), listSousOperations);
                                            rvNewDevisDetails1.setAdapter(adapter);

                                            if (listExtras.size() != 0) {
                                                labelExtras.setVisibility(View.VISIBLE);
                                                rvNewDevisDetails2.setHasFixedSize(true);
                                                LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
                                                rvNewDevisDetails2.setLayoutManager(layoutManager2);
                                                NewDevisDetailsAdapter adapter2 = new NewDevisDetailsAdapter(getContext(), listExtras);
                                                rvNewDevisDetails2.setAdapter(adapter2);
                                            } else {
                                                labelExtras.setVisibility(View.GONE);
                                                rvNewDevisDetails2.setVisibility(View.GONE);
                                            }

                                            ArrayList<Operationimage> listOfFiles = new ArrayList<Operationimage>();
                                            if (listOfDevisI.get(0).getOperationimages() != null ){
                                                listOfFiles = listOfDevisI.get(0).getOperationimages();
                                            }



                                            if (listOfFiles.size() != 0) {
                                                rvNewDevisDetails3.setVisibility(View.VISIBLE);
                                                FileAdapter fileAdapter = new FileAdapter(getContext(), listener, listOfFiles);

                                                rvNewDevisDetails3.setHasFixedSize(true);
                                                LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                                                rvNewDevisDetails3.setLayoutManager(layoutManager3);
                                                rvNewDevisDetails3.setAdapter(fileAdapter);
                                            } else {

                                                rvNewDevisDetails3.setVisibility(View.GONE);
                                            }


                                        } catch (Exception e) {
                                            llDevisResponse.setVisibility(View.GONE);
                                            llNoConnexion.setVisibility(View.GONE);
                                            loader.setVisibility(View.GONE);
                                            e.printStackTrace();

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

                                    } else if (response.body().getStatus() == 2) {

                                        llDevisResponse.setVisibility(View.GONE);
                                        llNoConnexion.setVisibility(View.GONE);
                                        loader.setVisibility(View.GONE);
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
                                    } else {
                                        llDevisResponse.setVisibility(View.GONE);
                                        llNoConnexion.setVisibility(View.GONE);
                                        loader.setVisibility(View.GONE);
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
                                    llDevisResponse.setVisibility(View.GONE);
                                    llNoConnexion.setVisibility(View.GONE);
                                    loader.setVisibility(View.GONE);
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

                    @Override
                    public void onFailure(Call<MedespoirResponse> call, Throwable t) {
                        llDevisResponse.setVisibility(View.GONE);
                        llNoConnexion.setVisibility(View.GONE);
                        loader.setVisibility(View.GONE);
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
                llDevisResponse.setVisibility(View.GONE);
                llNoConnexion.setVisibility(View.GONE);
                loader.setVisibility(View.GONE);
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