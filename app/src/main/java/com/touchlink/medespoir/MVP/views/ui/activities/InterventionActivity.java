package com.touchlink.medespoir.MVP.views.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.touchlink.medespoir.MVP.models.network.Interventions;
import com.touchlink.medespoir.MVP.views.adapters.InterventionAdapter;
import com.touchlink.medespoir.MVP.views.adapters.InterventionsAdapter;
import com.touchlink.medespoir.MVP.views.ui.callbacks.InterventionListener;
import com.touchlink.medespoir.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InterventionActivity extends AppCompatActivity {
    @SuppressLint("id")
    @BindView(R.id.rv_intervention)
    RecyclerView rvIntervention ;
    @SuppressLint("id")
    @BindView(R.id.arrow_back_intervention)
    LinearLayout arrowBackIntervention ;
    @SuppressLint("id")
    @BindView(R.id.arrow_forward_intervention)
    LinearLayout arrowForwardIntervention ;
    private InterventionAdapter interventionAdapter ;
    InterventionListener listener ;
    @SuppressLint("id")
    @BindView(R.id.fab_intervention)
    ImageView fabIntervention ;

    private ArrayList<Interventions> listOfIntervention = new ArrayList<Interventions>() ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intervention);
        initIntervention();
    }

    private void initIntervention() {

        ButterKnife.bind(this);

        // listener
        listener = new InterventionListener() {
            @Override
            public void onClick(View view, int position) {

                Intent intent = new Intent(getBaseContext() , InterventionDetailsActivity.class);
                startActivity(intent);
            }
        };
        // floating action button
        fabIntervention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext() , MessageActivity.class);
                startActivity(intent);
            }
        });
        // intervention
        rvIntervention.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
        rvIntervention.setLayoutManager(linearLayoutManager);
        listOfIntervention.add(new Interventions("Genioplastie" , R.drawable.genioplastie , "genioplastie"));
        listOfIntervention.add(new Interventions("Paupière" , R.drawable.paupiere , "paupière"));
        listOfIntervention.add(new Interventions("Rhinoplastie" , R.drawable.rhinoplastie , "rhinoplatie"));
        listOfIntervention.add(new Interventions("Lifting visage" , R.drawable.lifting_visage , "lifting visage"));
        interventionAdapter = new InterventionAdapter(getBaseContext() ,listener , listOfIntervention);
        rvIntervention.setAdapter(interventionAdapter);
        arrowForwardIntervention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvIntervention.smoothScrollToPosition(linearLayoutManager.findLastVisibleItemPosition() + 1);
            }
        });
        arrowBackIntervention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvIntervention.smoothScrollToPosition(linearLayoutManager.findFirstVisibleItemPosition() );
            }
        });
    }
}