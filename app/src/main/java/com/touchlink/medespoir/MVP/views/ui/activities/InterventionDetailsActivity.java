package com.touchlink.medespoir.MVP.views.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.touchlink.medespoir.MVP.views.ui.fragments.DevisFragment;
import com.touchlink.medespoir.R;

public class InterventionDetailsActivity extends AppCompatActivity {
    @SuppressLint("id")
    @BindView(R.id.fab_intervention_details)
    ImageView fabInterventionDetails ;
    @SuppressLint("id")
    @BindView(R.id.button_devis)
    LinearLayout buttonDevis ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intervention_details);

        init();
    }
    private void init(){
        ButterKnife.bind(this);
        buttonDevis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DevisFragment devisFragment = new DevisFragment();
                MainActivity.goToFragment(devisFragment , true);
                MainActivity.mMenuAdapter.setViewSelected(2, true);
                Intent intent = new Intent(InterventionDetailsActivity.this , MainActivity.class);
                startActivity(intent);




            }
        });

        fabInterventionDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext() , MessageActivity.class);
                startActivity(intent);
            }
        });
    }
}