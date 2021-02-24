package com.touchlink.medespoir.MVP.views.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fxn.cue.Cue;
import com.fxn.cue.enums.Duration;
import com.fxn.cue.enums.Type;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.touchlink.medespoir.MVP.models.network.MedespoirResponse;
import com.touchlink.medespoir.MVP.views.adapters.MenuAdapter;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;


import com.touchlink.medespoir.MVP.views.ui.fragments.CompteRenduFragment;
import com.touchlink.medespoir.MVP.views.ui.fragments.DevisFragment;
import com.touchlink.medespoir.MVP.views.ui.fragments.EditProfilFragment;
import com.touchlink.medespoir.MVP.views.ui.fragments.MainFragment;
import com.touchlink.medespoir.MVP.views.ui.fragments.MedespoirShopFragment;
import com.touchlink.medespoir.MVP.views.ui.fragments.MedespoirTVFragment;
import com.touchlink.medespoir.MVP.views.ui.fragments.MessageFragment;
import com.touchlink.medespoir.MVP.views.ui.fragments.ReclamationFragment;
import com.touchlink.medespoir.MVP.views.ui.fragments.SettingsFragment;
import com.touchlink.medespoir.MedespoirApplication;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.WebServices.retrofit.WebServiceFactory;
import com.touchlink.medespoir.session.MedespoirEmailGuest;
import com.touchlink.medespoir.session.MedespoirTokenSession;
import com.touchlink.medespoir.session.SharedPreferencesUserFactory;
import com.touchlink.medespoir.utils.MEConstants;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements DuoMenuView.OnMenuClickListener {

    FusedLocationProviderClient client ;
    public static  MenuAdapter mMenuAdapter;
    private ViewHolder mViewHolder;
    @SuppressLint("id")
    @BindView(R.id.icon_program)
    LinearLayout iconProgram ;
    @SuppressLint("id")
    @BindView(R.id.devis_icon)
    LinearLayout iconDevis ;
    @SuppressLint("id")
    @BindView(R.id.duo_view_header_text_title)
    TextView emailUser ;
    public static TextView username ;
    static boolean stateActivity = false ;

    private ArrayList<String> mTitles = new ArrayList<>();
    private static MainFragment mainFragment = new MainFragment();
    public static FragmentManager fm ;
    public static MainActivity instance = null ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        instance = this ;
        username = findViewById(R.id.duo_view_header_text_sub_title_2);
        checkLocation();
        Logger.addLogAdapter(new AndroidLogAdapter());
        fm = getSupportFragmentManager();
        stateActivity = true ;
        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));


        iconProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ProgramActivity.class);
                startActivity(intent);
            }
        });


         iconDevis.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 mMenuAdapter.setViewSelected(2, true);

                 DevisFragment devisFragment = new DevisFragment();
                 goToFragment(devisFragment , true);

             }
         });
        // Initialize the views
        mViewHolder = new ViewHolder();

        // Handle toolbar actions
        handleToolbar();

        // Handle menu actions
        handleMenu();

        // Handle drawer actions
        handleDrawer();

        // Show main fragment in container

        goToFragment(mainFragment, false);
        mMenuAdapter.setViewSelected(0, true);
        setTitle(mTitles.get(0));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPreferencesUserFactory.retrieveUseEmail() != null){
                    emailUser.setText(SharedPreferencesUserFactory.retrieveUseEmail());
                } else {
                    emailUser.setText("");
                }
                if (SharedPreferencesUserFactory.retrieveUserData() != null){
                    username.setText(SharedPreferencesUserFactory.retrieveUserData().getUsername());
                } else {
                    username.setText("");
                }
            }
        }, 1000);



    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(MEConstants.STATE_ACTIVITY, stateActivity);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        emailUser.setText(SharedPreferencesUserFactory.retrieveUseEmail());
        username.setText(SharedPreferencesUserFactory.retrieveUserData().getUsername());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stateActivity) {
            // no need to try-catch this, because we are not in a callback
            fm.popBackStackImmediate();
            stateActivity = false;
        }
        emailUser.setText(SharedPreferencesUserFactory.retrieveUseEmail());
        username.setText(SharedPreferencesUserFactory.retrieveUserData().getUsername());
    }

    private void checkLocation() {
        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            if (ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
            }
        }
    }

        private void getCurrentLocation(){



        try{
            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        Log.i(MEConstants.TAG , "Lat = "+location.getLatitude() + " long = " + location.getLongitude());
                        final JsonObject postParams = new JsonObject();

                        postParams.addProperty("longitude", location.getLongitude());
                        postParams.addProperty("latitude", location.getLatitude());


                        Call<MedespoirResponse> call = WebServiceFactory.getInstance().getApi().sendDeviceLocation("Bearer "+ MedespoirTokenSession.getUsergestToken(getBaseContext()) ,postParams);
                        call.enqueue(new Callback<MedespoirResponse>() {
                            @Override
                            public void onResponse(Call<MedespoirResponse> call, Response<MedespoirResponse> response) {
                                if(response != null){
                                    if (response.body() != null){
                                        if (response.body().getStatus() == 1){
                                            Log.i(MEConstants.TAG , "success send location");
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<MedespoirResponse> call, Throwable t) {

                            }
                        });


                    }
                }
            });
        }catch (SecurityException e){

        }

        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }

    private void handleToolbar() {
        setSupportActionBar(mViewHolder.mToolbar);
    }

    private void handleDrawer() {
        DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this,
                mViewHolder.mDuoDrawerLayout,
                mViewHolder.mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mViewHolder.mDuoDrawerLayout.setDrawerListener(duoDrawerToggle);
        duoDrawerToggle.syncState();

    }

    private void handleMenu() {
        mMenuAdapter = new MenuAdapter(mTitles, getApplicationContext());

        mViewHolder.mDuoMenuView.setOnMenuClickListener(this);
        mViewHolder.mDuoMenuView.setAdapter(mMenuAdapter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
    }



    @Override
    public void onFooterClicked() {
        //Toast.makeText(this, "onFooterClicked", Toast.LENGTH_SHORT).show();
        MedespoirTokenSession.cancelSession(getBaseContext());
        SharedPreferencesUserFactory.cancelEmail(getBaseContext());
        SharedPreferencesUserFactory.cancelPassword(getBaseContext());
        SharedPreferencesUserFactory.cancelToken(getBaseContext());
        SharedPreferencesUserFactory.storeUserSessionSettings(false);
        SharedPreferencesUserFactory.cancelUserData(getBaseContext());

        Cue.init()
                .with(getBaseContext())
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

        Intent intent = new Intent(getBaseContext(), ConnexionActivity.class);
        startActivity(intent);
        finish();
    }


    public static void pop() {
        fm.popBackStack();

    }

    @Override
    public void finish() {
        super.finish();
        instance = null ;
    }

    public static void popImediate() {
        Log.i(MEConstants.TAG , " count fm "+ fm.getBackStackEntryCount());

        instance.finish();

    }

    @Override
    public void onBackPressed() {

        Fragment fragment = fm.findFragmentById(R.id.container);
        if (fragment instanceof DevisFragment || fragment instanceof ReclamationFragment || fragment instanceof  MedespoirTVFragment
        || fragment instanceof  MedespoirShopFragment || fragment instanceof SettingsFragment){
            mMenuAdapter.setViewSelected(0 , true);

            fm.popBackStack(0 , FragmentManager.POP_BACK_STACK_INCLUSIVE);

            //navigation.setSelectedItemId(R.id.navigation_home);

           //goToFragment(mainFragment , true);


        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onHeaderClicked() {
        Toast.makeText(this, "onHeaderClicked", Toast.LENGTH_SHORT).show();
    }

    public static void goToFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction ;


            transaction = fm.beginTransaction();



        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.replace(R.id.container, fragment).commit();
    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        // Set the toolbar title
        setTitle("");


        // Set the right options selected
        mMenuAdapter.setViewSelected(position, true);

        Log.i(MEConstants.TAG , " Position menu fragment = "+ position);

        // Navigate to the right fragment
        switch (position) {
           /* // Home
            case 0:
                goToFragment(mainFragment, true);
                break;*/
            // Medespoir
            case 0:

                Log.i(MEConstants.TAG , " Position menu fragment case clicked = "+ position);
                goToFragment(mainFragment, true);
                break;
            // Messages
            case 1:
                Log.i(MEConstants.TAG , " Position menu fragment case clicked = "+ position);
                //goToFragment(mainFragment, false);
                MessageFragment messageFragment = new MessageFragment();
                goToFragment(messageFragment , true);
                break;
            // Devis
            case 2:
                Log.i(MEConstants.TAG , " Position menu fragment case clicked = "+ position);
                //goToFragment(mainFragment, false);
                DevisFragment devisFragment = new DevisFragment();
                goToFragment(devisFragment , true);
                break;
            // Post opératoire et réclamation
            case 3:
                CompteRenduFragment compteRenduFragment = new CompteRenduFragment();
                goToFragment(compteRenduFragment , true);
                break;
            // Medespoir TV
            case 4:

                MedespoirTVFragment medespoirTVFragment = new MedespoirTVFragment();
                goToFragment(medespoirTVFragment, true);
                break;
            // Medespoir shop
            case 5:
                MedespoirShopFragment medespoirShopFragment = new MedespoirShopFragment();
                goToFragment(medespoirShopFragment, true);
                break;
            // Paramètres
            case 6:
                SettingsFragment settingsFragment = new SettingsFragment();
                goToFragment(settingsFragment , true );

                //  goToFragment(mainFragment, false);

                break;
         /*   // Mentions légales
            case 7:
                Log.i(MEConstants.TAG , " Position menu fragment case clicked = "+ position);
              //  goToFragment(mainFragment, false);
                break;*/
            default:
                Log.i(MEConstants.TAG , " Position menu fragment case clicked = "+ position);
                //goToFragment(mainFragment, false);
                break;
        }

        // Close the drawer
        mViewHolder.mDuoDrawerLayout.closeDrawer();
    }

    private class ViewHolder {
        private DuoDrawerLayout mDuoDrawerLayout;
        private DuoMenuView mDuoMenuView;
        private Toolbar mToolbar;

        ViewHolder() {
            mDuoDrawerLayout = (DuoDrawerLayout) findViewById(R.id.drawer);
            //mDuoDrawerLayout.setMenuView(mDuoMenuView);
            mDuoMenuView = (DuoMenuView) mDuoDrawerLayout.getMenuView();
           // mDuoMenuView = (DuoMenuView)  findViewById(R.id.menu);
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
        }
    }
}
