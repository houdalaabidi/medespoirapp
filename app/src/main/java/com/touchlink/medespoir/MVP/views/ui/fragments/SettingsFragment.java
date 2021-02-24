package com.touchlink.medespoir.MVP.views.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.fxn.cue.Cue;
import com.fxn.cue.enums.Duration;
import com.fxn.cue.enums.Type;
import com.touchlink.medespoir.MVP.views.ui.activities.ConnexionActivity;
import com.touchlink.medespoir.MVP.views.ui.activities.MainActivity;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVBold;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVLight;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVRegular;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.session.MedespoirTokenSession;
import com.touchlink.medespoir.session.SharedPreferencesUserFactory;
import com.touchlink.medespoir.utils.MEConstants;


public class SettingsFragment extends Fragment {

    NotificationManagerCompat notificationManager;
    @SuppressLint("id")
    @BindView(R.id.tv_email)
    MedEspoirTVRegular tvEmail ;
    @SuppressLint("id")
    @BindView(R.id.deconnexion)
    MedEspoirTVBold deconnexion ;
    @SuppressLint("id")
    @BindView(R.id.edit_profil)
    MedEspoirTVLight editProfil ;
    @SuppressLint("id")
    @BindView(R.id.checkbox_notification)
    SwitchCompat switcher ;



    public SettingsFragment() {
        // Required empty public constructor
    }


    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        ButterKnife.bind(this , view);
        notificationManager = NotificationManagerCompat.from(getContext());
        if(SharedPreferencesUserFactory.getNotificationsSettings()){
            switcher.setChecked(true);
        } else {
            switcher.setChecked(false);
        }
        SharedPreferencesUserFactory.initializedPreferences(getContext());
        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    SharedPreferencesUserFactory.storeNotificationsSettings(true);

                    Cue.init()
                            .with(getContext())
                            .setMessage(MEConstants.ENABLE_NOTIFICATION)
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
                    NotificationManagerCompat.from(getContext()).cancelAll();

                    Cue.init()
                            .with(getContext())
                            .setMessage(MEConstants.DISABLE_NOTIFICATION)
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
                    SharedPreferencesUserFactory.storeNotificationsSettings(false);
                }

            }
        });
        tvEmail.setText(SharedPreferencesUserFactory.retrieveUseEmail());
        editProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditProfilFragment editProfilFragment = new EditProfilFragment();
                MainActivity.goToFragment(editProfilFragment , true);
            }
        });
        deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Cue.init()
                        .with(getContext())
                        .setMessage(MEConstants.LOGOUT)
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



                Intent intent = new Intent(getContext(), ConnexionActivity.class);

                MedespoirTokenSession.cancelSession(getContext());
                SharedPreferencesUserFactory.cancelEmail(getContext());
                SharedPreferencesUserFactory.cancelPassword(getContext());

                SharedPreferencesUserFactory.storeUserSessionSettings(false);
                SharedPreferencesUserFactory.cancelUserData(getContext());

                SharedPreferencesUserFactory.cancelToken(getContext());
                MainActivity.popImediate();
                startActivity(intent);



            }
        });

    }
}