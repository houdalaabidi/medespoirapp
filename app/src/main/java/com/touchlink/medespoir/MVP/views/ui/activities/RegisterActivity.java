package com.touchlink.medespoir.MVP.views.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.exception.ApolloException;
import com.fxn.cue.Cue;
import com.fxn.cue.enums.Duration;
import com.fxn.cue.enums.Type;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.medespoir.ClientUserMutation;
import com.medespoir.type.UserInput;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirETBold;
import com.touchlink.medespoir.MVP.views.ui.dialogfragments.ConnexionDialogFragment;
import com.touchlink.medespoir.R;

import com.touchlink.medespoir.WebServices.retrofit.WebServiceFactory;
import com.touchlink.medespoir.utils.DeviceConnectivity;
import com.touchlink.medespoir.utils.MEConstants;
import com.touchlink.medespoir.utils.Utility;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    @SuppressLint("id")
    @BindView(R.id.et_email)
    MedEspoirETBold etEmail;
    @SuppressLint("id")
    @BindView(R.id.et_username)
    MedEspoirETBold etUsername;
    @SuppressLint("id")
    @BindView(R.id.et_pw)
    MedEspoirETBold etPawword;
    @SuppressLint("id")
    @BindView(R.id.loader2)
    LottieAnimationView loader ;
    @SuppressLint("id")
    @OnClick(R.id.connexion)
    public void SignIn() {
        Intent intent = new Intent(getBaseContext(), ConnexionActivity.class);
        startActivity(intent);
        finish();


    }
    @SuppressLint("id")
    @OnClick(R.id.reset_pwd)
    public void resetPassword() {
        Intent intent = new Intent(getBaseContext(), ResetPasswordActivity.class);
        startActivity(intent);
    }
    @SuppressLint("id")
    @BindView(R.id.register_button)
    LinearLayout registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialiseInterface();
    }

    private void initialiseInterface() {
        ButterKnife.bind(this);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*ApolloClient apolloClient = WebServicesFactory.getInstance().getApolloClient();
                // Then enqueue your query


                UserInput userInput = UserInput.builder()
                        .username("fff")
                        .email("fff")
                        .password("fff")
                        .build();





                apolloClient
                        .mutate(new ClientUserMutation(userInput))
                        .enqueue(
                                new ApolloCall.Callback<ClientUserMutation.Data>() {
                                    @Override
                                    public void onResponse(@NotNull Response<ClientUserMutation.Data> response) {

                                    }

                                    @Override
                                    public void onFailure(@NotNull ApolloException e) {

                                    }
                                }
                        );

            }*/
                if (DeviceConnectivity.isNetworkAvailable(getBaseContext())) {
                    if ((etEmail.getText() + "").equalsIgnoreCase("") || ((etUsername.getText() + "")).equalsIgnoreCase("") || (etPawword.getText() + "").equalsIgnoreCase("")) {


                        Cue.init()
                                .with(getBaseContext())
                                .setMessage(MEConstants.EMPTY_CORD)
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
                        return;

                    } else if (!((etEmail.getText() + "").matches("^[A-Za-z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z0-9]{2,6}$"))) {

                        Cue.init()
                                .with(getBaseContext())
                                .setMessage(MEConstants.INVALID_MAIL)
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
                        return;


                    } else if (etPawword.getText().toString().length() <= 6){

                        Cue.init()
                                .with(getBaseContext())
                                .setMessage(MEConstants.ERROR_PASSWORD)
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

                }else{

                        loader.setVisibility(View.VISIBLE);

                        final JsonObject postParams = new JsonObject();
                        postParams.addProperty("username", etUsername.getText() + "");
                        postParams.addProperty("password", etPawword.getText() + "");
                        postParams.addProperty("email", etEmail.getText() + "");

                        Call<ResponseBody> call = WebServiceFactory.getInstance().getApi().inscriptionUser(postParams);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                loader.setVisibility(View.GONE);
                                String remoteResponse = null;

                                try {
                                    remoteResponse = response.body().string();
                                    if (response.code() == 200) {
                                        Gson gson = Utility.getGsonInstance();
                                        JSONObject object = new JSONObject(remoteResponse);
                                        String status = object.getString("status");
                                        if (status.equalsIgnoreCase("1")){

                                            Cue.init()
                                                    .with(getBaseContext())
                                                    .setMessage(MEConstants.SUCCESS_ACCESS)
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
                                            Intent intent = new Intent(getBaseContext(), ConnexionActivity.class);
                                            //intent.putExtra("id_new_client" , idClient);
                                            startActivity(intent);
                                            finish();
                                        } else if (status.equalsIgnoreCase("2")){
                                            etEmail.setText("");
                                            etPawword.setText("");
                                            etUsername.setText("");


                                            Cue.init()
                                                    .with(getBaseContext())
                                                    .setMessage(MEConstants.COMPTE_EXISTANT)
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

                                } catch (IOException e) {
                                    e.printStackTrace();
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

                                } catch (JSONException e) {
                                    e.printStackTrace();

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

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                loader.setVisibility(View.GONE);

                            }
                        });
                    }


                }  else {
                    ConnexionDialogFragment connectivityFragment = ConnexionDialogFragment.newInstance();
                    connectivityFragment.show(getSupportFragmentManager(), "ConnexionDialogFragment");


                }

            }


        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}