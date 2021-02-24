package com.touchlink.medespoir.MVP.views.ui.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonObject;
import com.touchlink.medespoir.MVP.models.network.MedespoirResponse;
import com.touchlink.medespoir.MVP.models.network.User;
import com.touchlink.medespoir.MVP.views.ui.activities.MainActivity;
import com.touchlink.medespoir.MVP.views.ui.dialogfragments.ConnexionDialogFragment;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.WebServices.retrofit.WebServiceFactory;
import com.touchlink.medespoir.session.MedespoirSession;
import com.touchlink.medespoir.session.MedespoirTokenSession;
import com.touchlink.medespoir.session.SharedPreferencesUserFactory;
import com.touchlink.medespoir.utils.DeviceConnectivity;
import com.touchlink.medespoir.utils.MEConstants;


public class EditProfilFragment extends Fragment {
    @SuppressLint("id")
    @BindView(R.id.et_username)
    TextInputEditText etUsername ;
    @SuppressLint("id")
    @BindView(R.id.et_password)
    TextInputEditText etPassword ;
    @SuppressLint("id")
    @BindView(R.id.et_confirm_password)
    TextInputEditText etConfirmPassword ;
    @SuppressLint("id")
    @BindView(R.id.button_confirm_edit_profil)
    LinearLayout sendEditProfil ;
    @SuppressLint("id")
    @BindView(R.id.loader)
    LottieAnimationView loader ;


    public EditProfilFragment() {
        // Required empty public constructor
    }

    public static EditProfilFragment newInstance(String param1, String param2) {
        EditProfilFragment fragment = new EditProfilFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profil, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        ButterKnife.bind(this , view);
        if(!DeviceConnectivity.isNetworkAvailable(getContext())) {
            ConnexionDialogFragment connectivityFragment = ConnexionDialogFragment.newInstance();
            connectivityFragment.show(getFragmentManager(), "ConnexionDialogFragment");
        }else {
            etUsername.setText(SharedPreferencesUserFactory.retrieveUserData().getUsername());
            etPassword.setText(SharedPreferencesUserFactory.retrieveUPW());
            etConfirmPassword.setText(SharedPreferencesUserFactory.retrieveUPW());
        }

        sendEditProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendWebService(view);
            }
        });


    }


    private void sendWebService(View view){
        if(!DeviceConnectivity.isNetworkAvailable(getContext())) {
            ConnexionDialogFragment connectivityFragment = ConnexionDialogFragment.newInstance();
            connectivityFragment.show(getFragmentManager(), "ConnexionDialogFragment");
        }else if(etUsername.getText().toString().isEmpty()){

            Cue.init()
                    .with(getContext())
                    .setMessage(MEConstants.EMPTY_USERNAME)
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

        } else if(etPassword.getText().toString().isEmpty()){

            Cue.init()
                    .with(getContext())
                    .setMessage(MEConstants.EMPTY_PASSWORD)
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
        } else if(etConfirmPassword.getText().toString().isEmpty()){

            Cue.init()
                    .with(getContext())
                    .setMessage(MEConstants.EMPTY_SECOND_PASSWORD)
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
        } else if(!etConfirmPassword.getText().toString().trim().equalsIgnoreCase(etPassword.getText().toString().trim())){

            Cue.init()
                    .with(getContext())
                    .setMessage(MEConstants.NOT_CONFORM_PASSWORD)
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

            JsonObject postParams = new JsonObject();
            postParams.addProperty("username", etUsername.getText().toString().trim());
            postParams.addProperty("password", etPassword.getText().toString().trim());

            loader.setVisibility(View.VISIBLE);

            Call<MedespoirResponse> call = WebServiceFactory.getInstance().getApi().editProfil("Bearer "+ MedespoirTokenSession.getUsergestToken(getContext()),postParams);
            call.enqueue(new Callback<MedespoirResponse>() {
                @Override
                public void onResponse(Call<MedespoirResponse> call, Response<MedespoirResponse> response) {
                    if (response.code() == MEConstants.CODE_TOKEN_EXPIRED || response.code() == MEConstants.CODE_NO_TOKEN) {



                        MedespoirSession.Reconnecter(getContext());
                        sendWebService(view);
                    } else {
                        loader.setVisibility(View.GONE);

                        if (response != null) {
                            if (response.body() != null) {


                                if (response.body().getStatus() == 1) {
                                    User user = SharedPreferencesUserFactory.retrieveUserData();
                                    MainActivity.username.setText(etUsername.getText().toString());
                                    user.setUsername(etUsername.getText().toString());
                                    SharedPreferencesUserFactory.storeUserSession(user);
                                    SharedPreferencesUserFactory.storeUPW(etPassword.getText().toString());
                                    MedespoirSession.Reconnecter(getContext());
                                    updateFirebasePassword(etPassword.getText().toString());
                                    MainActivity.pop();


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
            );


        }
    }

    private void updateFirebasePassword(String password){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider.getCredential(SharedPreferencesUserFactory.retrieveUseEmail() ,
                SharedPreferencesUserFactory.retrieveUPW());


// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(MEConstants.TAG, "Password updated");
                            } else {
                                Log.d(MEConstants.TAG, "Error password not updated");
                            }
                        }
                    });
                } else {
                    Log.d(MEConstants.TAG, "Error auth failed");
                }

            }
        });


    }
}