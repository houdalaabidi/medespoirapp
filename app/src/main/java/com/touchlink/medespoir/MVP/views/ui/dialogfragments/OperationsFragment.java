package com.touchlink.medespoir.MVP.views.ui.dialogfragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.airbnb.lottie.LottieAnimationView;
import com.fxn.cue.Cue;
import com.fxn.cue.enums.Duration;
import com.fxn.cue.enums.Type;
import com.touchlink.medespoir.MVP.models.network.Operations ;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.touchlink.medespoir.MVP.models.network.MedespoirResponse;
import com.touchlink.medespoir.MVP.models.network.Sousintervention;
import com.touchlink.medespoir.MVP.views.adapters.OperationsAdapter;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVBold;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.WebServices.retrofit.WebServiceFactory;
import com.touchlink.medespoir.session.MedespoirSession;
import com.touchlink.medespoir.session.MedespoirTokenSession;
import com.touchlink.medespoir.utils.DeviceConnectivity;
import com.touchlink.medespoir.utils.MEConstants;
import com.touchlink.medespoir.utils.Utility;

import java.util.ArrayList;


public class OperationsFragment extends DialogFragment {

    private View rootView;
    private Unbinder unbinder;
    public static   ArrayList<Integer> listOfSousInterventionsIds = new ArrayList<Integer>();
    public static   ArrayList<String> listOfSousInterventionsLabels = new ArrayList<String>();
    @SuppressLint("id")
     @BindView(R.id.rv_fragment_operations)
    RecyclerView rvOperationsFragment ;
    @SuppressLint("id")
    @OnClick(R.id.negative_btn)
    void cancelDialog() {
        listOfSousInterventionsLabels.clear();
        listOfSousInterventionsIds.clear();
        dismiss();
    }
    @SuppressLint("id")
    @BindView(R.id.no_operations)
    MedEspoirTVBold noOperations ;
    @SuppressLint("id")
    @BindView(R.id.ll_operations)
    LinearLayout llOperations ;
    @SuppressLint("id")
    @BindView(R.id.loader)
    LottieAnimationView loader ;
    @SuppressLint("id")
    @BindView(R.id.ll_connexectivity_dialog)
    LinearLayout llConnectivityDialog ;


    @SuppressLint("id")
    @OnClick(R.id.positive_btn)
    void onClick(View v) {

        Intent intent = new Intent();
        intent.putIntegerArrayListExtra(MEConstants.OPERATIONS_LIST_IDS, listOfSousInterventionsIds);
        intent.putStringArrayListExtra(MEConstants.OPERATIONS_LIST_LAEBLS, listOfSousInterventionsLabels);

        getTargetFragment().onActivityResult(getTargetRequestCode() , 101 , intent);

        for (int i = 0 ; i < listOfSousInterventionsIds.size() ; i++){

        }

        dismiss();

    }

    public OperationsFragment() {

    }

    public static OperationsFragment newInstance() {
        OperationsFragment fragment = new OperationsFragment();

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        initViews();
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(rootView).setCancelable(false).create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setCancelable(false);
        //alertDialog.setOnShowListener(dialog -> onDialogShow(alertDialog));
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                listOfSousInterventionsLabels.clear();
                listOfSousInterventionsIds.clear();
                alertDialog.show();
            }
        });
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    listOfSousInterventionsLabels.clear();
                    listOfSousInterventionsIds.clear();
                    alertDialog.cancel();
                }
                return false;
            }
        });
        return alertDialog;
    }

    private void initViews() {
        rootView = View.inflate(getContext(), R.layout.fragment_operations, null);

        //rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_operations, null, false);
        unbinder = ButterKnife.bind(this, rootView);
        if(!DeviceConnectivity.isNetworkAvailable(getContext())) {
            loader.setVisibility(View.GONE);
            noOperations.setVisibility(View.GONE);
            llConnectivityDialog.setVisibility(View.VISIBLE);
            llOperations.setVisibility(View.GONE);
        } else {
            getOperationsWS();
        }
    }

    private void getOperationsWS(){

        loader.setVisibility(View.VISIBLE);
        llConnectivityDialog.setVisibility(View.GONE);
        llOperations.setVisibility(View.GONE);
        noOperations.setVisibility(View.GONE);

        Call<MedespoirResponse> call = WebServiceFactory.getInstance().getApi().getOperations("Bearer "+ MedespoirTokenSession.getUsergestToken(getContext()));
        call.enqueue(new Callback<MedespoirResponse>() {
            @Override
            public void onResponse(Call<MedespoirResponse> call, Response<MedespoirResponse> response) {
                if (response.code() == MEConstants.CODE_TOKEN_EXPIRED || response.code() == MEConstants.CODE_NO_TOKEN) {


                    MedespoirSession.Reconnecter(getContext());

                    getOperationsWS();
                } else {
                    loader.setVisibility(View.GONE);

                    if (response != null) {
                        if (response.body() != null) {
                            if (response.body().getStatus() == 1){



                                llConnectivityDialog.setVisibility(View.GONE);
                                llOperations.setVisibility(View.VISIBLE);
                                rvOperationsFragment.setVisibility(View.VISIBLE);
                                Object data = null;
                                data = response.body().getData();
                                Gson gson = Utility.getGsonInstance();
                                try {

                                    Operations operations = gson.fromJson(gson.toJson(data), Operations.class);

                                    rvOperationsFragment.setHasFixedSize(true);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                    rvOperationsFragment.setLayoutManager(layoutManager);
                                    OperationsAdapter operationsAdapter = new OperationsAdapter(getContext() , operations.getOperations());
                                    rvOperationsFragment.setAdapter(operationsAdapter);


                                }catch (Exception e){
                                    e.printStackTrace();

                                }
                            } else if (response.body().getStatus() == 2){
                                llConnectivityDialog.setVisibility(View.GONE);
                                llOperations.setVisibility(View.GONE);
                                noOperations.setVisibility(View.VISIBLE);

                            } else {
                                dismiss();
                                llConnectivityDialog.setVisibility(View.GONE);
                                llOperations.setVisibility(View.GONE);
                                noOperations.setVisibility(View.GONE);
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
                dismiss();
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