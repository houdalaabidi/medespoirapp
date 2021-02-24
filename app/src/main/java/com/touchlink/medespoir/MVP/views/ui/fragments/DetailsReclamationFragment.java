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

import android.os.FileObserver;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.touchlink.medespoir.MVP.models.network.Fichier;
import com.touchlink.medespoir.MVP.models.network.Image;
import com.touchlink.medespoir.MVP.models.network.Interventions;
import com.touchlink.medespoir.MVP.models.network.MedespoirResponse;
import com.touchlink.medespoir.MVP.models.network.Reclamation;
import com.touchlink.medespoir.MVP.models.network.Reclamations;
import com.touchlink.medespoir.MVP.views.adapters.FileAdapter;
import com.touchlink.medespoir.MVP.views.adapters.InterventionsAdapter;
import com.touchlink.medespoir.MVP.views.adapters.ReclamationFileAdapter;
import com.touchlink.medespoir.MVP.views.ui.activities.MainActivity;
import com.touchlink.medespoir.MVP.views.ui.callbacks.FileListener;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVBold;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVLight;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVRegular;
import com.touchlink.medespoir.MVP.views.ui.dialogfragments.ConnexionDialogFragment;
import com.touchlink.medespoir.MVP.views.ui.dialogfragments.FileFragment;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.WebServices.retrofit.WebServiceFactory;
import com.touchlink.medespoir.session.MedespoirSession;
import com.touchlink.medespoir.session.MedespoirTokenSession;
import com.touchlink.medespoir.utils.DeviceConnectivity;
import com.touchlink.medespoir.utils.MEConstants;
import com.touchlink.medespoir.utils.Utility;

import java.util.ArrayList;


public class DetailsReclamationFragment extends Fragment {
    @SuppressLint("id")
    @BindView(R.id.rv_fichiers)
    RecyclerView recyclerViewFichiers ;
    private ArrayList<Image> listOfFiles = new ArrayList<Image>() ;
    private FileListener listener ;
    private static String id = null ;
    @SuppressLint("id")
    @BindView(R.id.ll_connectivity_dialog)
    LinearLayout llConnectivityDialog ;
    @SuppressLint("id")
    @BindView(R.id.loader)
    LottieAnimationView loader ;
    @SuppressLint("id")
    @BindView(R.id.ll_reclamation_details)
    LinearLayout llReclamationDetails ;
    @SuppressLint("id")
    @BindView(R.id.fab_reclamation_details)
    ImageView fab ;
    private Call<MedespoirResponse> callReclamation;

    @SuppressLint("id")
    @BindView(R.id.title_reclamation)
    MedEspoirTVBold titleReclamation ;
    @SuppressLint("id")
    @BindView(R.id.date_reclamation)
    MedEspoirTVRegular dateReclamation ;
    @SuppressLint("id")
    @BindView(R.id.priorite_reclamation)
    MedEspoirTVBold priorite ;
    @SuppressLint("id")
    @BindView(R.id.desc_reclamation)
    MedEspoirTVLight description ;
    @SuppressLint("id")
    @BindView(R.id.statut_reclamation)
    MedEspoirTVBold status ;
    @SuppressLint("id")
    @BindView(R.id.label_files)
    MedEspoirTVBold labelFiles ;



    public DetailsReclamationFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details_reclamation, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        ButterKnife.bind(this , view);
        Bundle bundle = getArguments();
        id = bundle.getString(MEConstants.RECLAMATION_ID);
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageFragment messageFragment = new MessageFragment();
                MainActivity.goToFragment(messageFragment , true);
            }
        });



        if(DeviceConnectivity.isNetworkAvailable(getContext())) {

            if (id != null){

                loadData(id);

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
            llConnectivityDialog.setVisibility(View.VISIBLE);
            llReclamationDetails.setVisibility(View.GONE);
            loader.setVisibility(View.GONE);

        }



    }

    private void loadData(String id){
        llReclamationDetails.setVisibility(View.GONE);
        llConnectivityDialog.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
        JsonObject postParams = new JsonObject();
        postParams.addProperty("id", id);
        callReclamation = WebServiceFactory.getInstance().getApi().getReclamationById("Bearer "+ MedespoirTokenSession.getUsergestToken(getContext()),postParams);
        callReclamation.enqueue(new Callback<MedespoirResponse>() {
            @Override
            public void onResponse(Call<MedespoirResponse> call, Response<MedespoirResponse> response) {
                if (response.code() == MEConstants.CODE_TOKEN_EXPIRED || response.code() == MEConstants.CODE_NO_TOKEN) {
                    MedespoirSession.Reconnecter(getContext());
                    loadData(id);
                } else {
                    loader.setVisibility(View.GONE);
                    if (response != null) {
                        if (response.body() != null) {
                            if (response.body().getStatus() == 1) {
                                Object data = null;
                                data = response.body().getData();
                                Gson gson = Utility.getGsonInstance();
                                try {

                                    Reclamation reclamation = gson.fromJson(gson.toJson(data), Reclamation.class);
                                    if (reclamation != null){
                                        llConnectivityDialog.setVisibility(View.GONE);
                                        llReclamationDetails.setVisibility(View.VISIBLE);
                                        if (reclamation.getTitle() != null && reclamation.getTitle() != ""){
                                            titleReclamation.setText(reclamation.getTitle());
                                        } else {
                                            titleReclamation.setVisibility(View.GONE);
                                        }
                                        if (reclamation.getDate() != null){
                                            dateReclamation.setText(reclamation.getDate().getDay()+ " / "+
                                                    reclamation.getDate().getMonth() + " / "+ reclamation.getDate().getYear());
                                        } else {
                                            dateReclamation.setVisibility(View.GONE);
                                        }
                                        if (reclamation.getPriorite() != null && reclamation.getPriorite() != ""){
                                            priorite.setText(priorite.getText().toString() + reclamation.getPriorite());
                                        } else {
                                            priorite.setVisibility(View.GONE);
                                        }
                                        if (reclamation.getDescription() != null && reclamation.getDescription() != ""){
                                            description.setText(reclamation.getDescription());
                                        } else {
                                            description.setVisibility(View.GONE);
                                        }
                                        if (reclamation.getStatut() != "" && reclamation.getStatut() != ""){
                                            status.setText(status.getText().toString() + reclamation.getStatut());
                                        } else {
                                            status.setVisibility(View.GONE);
                                        }
                                        if (reclamation.getImages() != null){
                                            listOfFiles = reclamation.getImages();
                                            if ( listOfFiles.size() != 0){
                                                recyclerViewFichiers.setHasFixedSize(true);
                                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                                                recyclerViewFichiers.setLayoutManager(linearLayoutManager);
                                                ReclamationFileAdapter fileAdapter = new ReclamationFileAdapter(getContext() ,listener , listOfFiles);
                                                recyclerViewFichiers.setAdapter(fileAdapter);
                                            } else {
                                                recyclerViewFichiers.setVisibility(View.GONE);
                                                labelFiles.setVisibility(View.GONE);
                                            }

                                        } else {
                                            recyclerViewFichiers.setVisibility(View.GONE);
                                            labelFiles.setVisibility(View.GONE);
                                        }
                                    } else {

                                        llConnectivityDialog.setVisibility(View.GONE);
                                        llReclamationDetails.setVisibility(View.GONE);
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


                                }catch (Exception e){
                                    e.printStackTrace();

                                    llConnectivityDialog.setVisibility(View.GONE);
                                    llReclamationDetails.setVisibility(View.GONE);
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
                                llConnectivityDialog.setVisibility(View.GONE);
                                llReclamationDetails.setVisibility(View.GONE);
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

                            llConnectivityDialog.setVisibility(View.GONE);
                            llReclamationDetails.setVisibility(View.GONE);
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

                        llConnectivityDialog.setVisibility(View.GONE);
                        llReclamationDetails.setVisibility(View.GONE);
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
                llConnectivityDialog.setVisibility(View.GONE);
                llReclamationDetails.setVisibility(View.GONE);
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