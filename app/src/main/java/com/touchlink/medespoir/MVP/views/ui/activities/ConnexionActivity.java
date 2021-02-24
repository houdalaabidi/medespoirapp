package com.touchlink.medespoir.MVP.views.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.fxn.cue.Cue;
import com.fxn.cue.enums.Duration;
import com.fxn.cue.enums.Type;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.touchlink.medespoir.MVP.models.network.MedespoirResponse;
import com.touchlink.medespoir.MVP.models.network.Token;
import com.touchlink.medespoir.MVP.models.network.User;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirETBold;
import com.touchlink.medespoir.MVP.views.ui.dialogfragments.ConnexionDialogFragment;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.WebServices.retrofit.WebServiceFactory;
import com.touchlink.medespoir.session.MedespoirSession;
import com.touchlink.medespoir.session.MedespoirTokenSession;
import com.touchlink.medespoir.session.SharedPreferencesUserFactory;
import com.touchlink.medespoir.utils.DeviceConnectivity;
import com.touchlink.medespoir.utils.MEConstants;
import com.touchlink.medespoir.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import androidx.appcompat.widget.AppCompatCheckBox;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ConnexionActivity extends AppCompatActivity {

    @SuppressLint("id")
    @BindView(R.id.et_email)
    MedEspoirETBold etEmail ;
    @SuppressLint("id")
    @BindView(R.id.et_pw)
    MedEspoirETBold etPW ;
    FirebaseAuth auth ;
    DatabaseReference reference ;
    @SuppressLint("id")
    @BindView(R.id.loader2)
    LottieAnimationView loader ;
    @SuppressLint("id")
    @BindView(R.id.ceckbox_connexion)
    AppCompatCheckBox checkBox ;
    @SuppressLint("id")
    @OnClick(R.id.button_inscription)
    public void register() {
        Intent intent = new Intent(getBaseContext() , RegisterActivity.class);
        startActivity(intent);
        finish();

    }
    @SuppressLint("id")
    @OnClick(R.id.connexion_button)
    public void SignIn() {
        if(DeviceConnectivity.isNetworkAvailable(getBaseContext())) {
            if ((etEmail.getText() + "").equalsIgnoreCase("")
                    || (etPW.getText() + "").equalsIgnoreCase("") ) {


                Cue.init()
                        .with(getBaseContext())
                        .setMessage(MEConstants.EMPTY_CORD2)
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

            }  else {


                final JsonObject postParams = new JsonObject();



                postParams.addProperty("password", etPW.getText().toString() + "");
                postParams.addProperty("email", etEmail.getText().toString() + "");
                loader.setVisibility(View.VISIBLE);

                Call<MedespoirResponse> call = WebServiceFactory.getInstance().getApi().ConnexionUser(postParams);
                call.enqueue(new Callback<MedespoirResponse>() {
                    @Override
                    public void onResponse(Call<MedespoirResponse> call, Response<MedespoirResponse> response) {


                        loader.setVisibility(View.GONE);
                        Object  data = null;
                        if (response != null){
                            if (response.body() != null){

                                if(response.body().getStatus() == 1){
                                    data = response.body().getData();
                                    User user = new Gson().fromJson(new Gson().toJson(data), User.class);
                                    SharedPreferencesUserFactory.storeUserSession(user);
                                    String token = user.getToken();
                                    connectToFirebase(user);
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

                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                    MedespoirTokenSession.refreshLocalStorage(token, true, getBaseContext());
                                    MedespoirTokenSession.refreshEmailLocalStorage(user.getEmail(),true , getBaseContext());
                                    SharedPreferencesUserFactory.storeUPW(etPW.getText().toString());
                                    SharedPreferencesUserFactory.storeEmail(user.getEmail());
                                    SharedPreferencesUserFactory.storeToken(token);
                                    //intent.putExtra("id_new_client" , idClient);
                                    startActivity(intent);
                                    finish();

                                 } else if (response.body().getStatus() == 0){
                                    etPW.setText("");
                                    etEmail.setText("");
                                    Cue.init()
                                            .with(getBaseContext())
                                            .setMessage(MEConstants.USER_NOT_FOUND)
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
                                else {
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







                    }

                    @Override
                    public void onFailure(Call<MedespoirResponse> call, Throwable t) {

                        loader.setVisibility(View.GONE);
                        etPW.setText("");
                        etEmail.setText("");
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
                });


            }

        } else  {
            ConnexionDialogFragment connectivityFragment = ConnexionDialogFragment.newInstance();
            connectivityFragment.show(getSupportFragmentManager(), "ConnexionDialogFragment");


        }






    }
    @SuppressLint("id")
    @OnClick(R.id.reset_pwd)
    public void resetPassword(){
        Intent intent = new Intent(getBaseContext() , ResetPasswordActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        initialiseInterface();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initialiseInterface() {
        ButterKnife.bind(this);
        SharedPreferencesUserFactory.initializedPreferences(ConnexionActivity.this );
        auth = FirebaseAuth.getInstance();
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if(ischecked){

                    if( etEmail.getText().toString() !=  "" && etEmail.getText().toString() !=  null && etPW.getText().toString() != "" && etPW.getText().toString() != null){
                        SharedPreferencesUserFactory.storeUserSessionSettings(true);
                    } else {
                        SharedPreferencesUserFactory.storeUserSessionSettings(false);
                    }

                } else {
                    SharedPreferencesUserFactory.storeUserSessionSettings(false);

                }

            }
        });
    }

    private void connectToFirebase(User user) {

        auth.signInWithEmailAndPassword(etEmail.getText()+"", etPW.getText()+"")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            /*Intent intent = new Intent(ConnexionActivity.this , HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();*/
                            //Log.e("successful connexion to" , "firebase");
                        } else {
                            saveInFirebase(user);



                        }
                    }
                });
    }

    private void saveInFirebase(User user) {
        auth = FirebaseAuth.getInstance() ;
        auth.createUserWithEmailAndPassword(etEmail.getText()+"" , etPW.getText()+"")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            if (isNew) {
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                String userId = firebaseUser.getUid();
                                reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("id", userId);
                                hashMap.put("username", user.getUsername());
                                hashMap.put("role", MEConstants.USER_ROLE);
                                hashMap.put("id_plateform" , user.getId()+"");

                                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // get the token for the first connexion , here i will use a webservice to send token to DB

                                            /*Intent intent = new Intent(ConnexionActivity.this, HomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();*/
                                            //Log.e("successful inscription to" , "firebase");

                                        }
                                    }
                                });
                            }
                        }  else {
                            /*Intent intent = new Intent(ConnexionActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();*/
                            Log.e("failed inscription to" , "firebase"+ task.getResult().toString());
                        }

                    }
                });

    }
}