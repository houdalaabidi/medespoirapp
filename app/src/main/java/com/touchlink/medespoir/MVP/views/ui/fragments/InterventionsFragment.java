package com.touchlink.medespoir.MVP.views.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.touchlink.medespoir.MVP.models.network.Interventions;
import com.touchlink.medespoir.MVP.views.adapters.InterventionsAdapter;
import com.touchlink.medespoir.MVP.views.ui.activities.InterventionActivity;
import com.touchlink.medespoir.MVP.views.ui.callbacks.InterventionListener;
import com.touchlink.medespoir.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
@SuppressLint("id")
public class InterventionsFragment extends Fragment {
    @SuppressLint("id")
    @BindView(R.id.rv_intervention_visage)
    RecyclerView recyclerViewVisage ;
    @SuppressLint("id")
    @BindView(R.id.rv_chirurgie_intime)
    RecyclerView recyclerViewCIntime ;
    @SuppressLint("id")
    @BindView(R.id.rv_chirurgie_yeux)
    RecyclerView recyclerViewCYeux ;
    @SuppressLint("id")
    @BindView(R.id.rv_medecine_esthetique)
    RecyclerView recyclerViewMedecineEsthetique ;
    @SuppressLint("id")
    @BindView(R.id.rv_intervention_dentaire)
    RecyclerView recyclerViewDentaire ;
    @SuppressLint("id")
    @BindView(R.id.rv_intervention_peau)
    RecyclerView recyclerViewInterventionPeau ;
    @SuppressLint("id")
    @BindView(R.id.rv_intervention_seins)
    RecyclerView recyclerViewInterventionSeins ;
    @SuppressLint("id")
    @BindView(R.id.rv_intervention_silhouette)
    RecyclerView recyclerViewInterventionSilhouette ;
    @SuppressLint("id")
    @BindView(R.id.rv_in_vitro)
    RecyclerView recyclerViewInVitro ;
    @SuppressLint("id")
    @BindView(R.id.rv_intervention_obesite)
    RecyclerView recyclerViewObesite ;
    @SuppressLint("id")
    @BindView(R.id.arrow_back_intervention_visage)
    LinearLayout arrowBackVisage ;
    @SuppressLint("id")
    @BindView(R.id.arrow_forward_intervention_visage)
    LinearLayout arrowForwardVisage ;
    @SuppressLint("id")
    @BindView(R.id.arrow_forward_intervention_intime)
    LinearLayout arrowForwardCIntime ;
    @SuppressLint("id")
    @BindView(R.id.arrow_back_intervention_intime)
    LinearLayout arrowBackCIntime ;
    @SuppressLint("id")
    @BindView(R.id.arrow_forward_chirurgie_yeux)
    ImageView arrowForwardCYeux ;
    @SuppressLint("id")
    @BindView(R.id.arrow_forward_medecine_esthetique)
    LinearLayout arrowForwardMedecineEsthetique ;
    @SuppressLint("id")
    @BindView(R.id.arrow_back_medecine_esthetique)
    LinearLayout arrowBackMedecineEsthetique ;
    @SuppressLint("id")
    @BindView(R.id.arrow_back_intervention_dentaire)
    LinearLayout arrowBackIDentaire ;
    @SuppressLint("id")
    @BindView(R.id.arrow_forward_intervention_dentaire)
    LinearLayout arrowForwardIDentaire ;
    @SuppressLint("id")
    @BindView(R.id.arrow_forward_intervention_peau)
    ImageView arrowForwardIPeau ;
    @SuppressLint("id")
    @BindView(R.id.arrow_forward_intervention_seins)
    LinearLayout arrowForwardISeins ;
    @SuppressLint("id")
    @BindView(R.id.arrow_back_intervention_seins)
    LinearLayout arrowBackISeins ;
    @SuppressLint("id")
    @BindView(R.id.arrow_forward_intervention_silhouette)
    LinearLayout arrowForwardISilhouette ;
    @SuppressLint("id")
    @BindView(R.id.arrow_back_intervention_silhouette)
    LinearLayout arrowBackISilhouette ;

    private InterventionsAdapter interventionVisageAdapter ,interventionObesiteAdapter , interventionYeuxAdapter , interventionSeinsAdapter
            , interventionSilhouetteAdapter , interventionDentaireAdapter , interventionPeauApdapter , interventionInVitroAdapter
            , interventionExclusiviteMedespoirAdapter, interventionMedecineEsthetiqueAdapter , interventionChirurgieIntimeAdapter
    ;


    private boolean programaticallyScrolled = false ;
    private ArrayList<Interventions> listOfInterventionVisage = new ArrayList<Interventions>() ;
    private ArrayList<Interventions> listOfInterventionObesite= new ArrayList<Interventions>() ;
    private ArrayList<Interventions> listOfInterventionYeux = new ArrayList<Interventions>() ;
    private ArrayList<Interventions> listOfInterventionSeins = new ArrayList<Interventions>() ;
    private ArrayList<Interventions> listOfInterventionSilhouette = new ArrayList<Interventions>() ;
    private ArrayList<Interventions> listOfInterventionDentaire = new ArrayList<Interventions>() ;
    private ArrayList<Interventions> listOfInterventionPeau = new ArrayList<Interventions>() ;
    private ArrayList<Interventions> listOfInterventionInVitro = new ArrayList<Interventions>() ;
    private ArrayList<Interventions> listOfMedecineEsthetique = new ArrayList<Interventions>() ;
    private ArrayList<Interventions> listOfInterventionChirurgieIntime = new ArrayList<Interventions>() ;



    private InterventionListener listener ;
    public InterventionsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interventions, container, false);
        ButterKnife.bind(this, view);
        init(view);
        return view;
    }

    private void init(View view) {

        listener = new InterventionListener() {
            @Override
            public void onClick(View view, int position) {

                Intent intent = new Intent(getContext() , InterventionActivity.class);
                startActivity(intent);
            }
        };
        // intervention visage
        recyclerViewVisage.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewVisage.setLayoutManager(linearLayoutManager);
        listOfInterventionVisage.add(new Interventions("Genioplastie" , R.drawable.genioplastie , "genioplastie"));
        listOfInterventionVisage.add(new Interventions("Paupière" , R.drawable.paupiere , "paupière"));
        listOfInterventionVisage.add(new Interventions("Rhinoplastie" , R.drawable.rhinoplastie , "rhinoplatie"));
        listOfInterventionVisage.add(new Interventions("Lifting visage" , R.drawable.lifting_visage , "lifting visage"));
        interventionVisageAdapter = new InterventionsAdapter(getContext() ,listener , listOfInterventionVisage);
        recyclerViewVisage.setAdapter(interventionVisageAdapter);
        arrowForwardVisage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewVisage.smoothScrollToPosition(linearLayoutManager.findLastVisibleItemPosition() + 1);
            }
        });
        arrowBackVisage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewVisage.smoothScrollToPosition(linearLayoutManager.findFirstVisibleItemPosition() );
            }
        });



        // intervention obésité
        recyclerViewCYeux.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewObesite.setLayoutManager(linearLayoutManager2);
        listOfInterventionObesite.add(new Interventions("Bypass" , R.drawable.bypass , "bypass"));
        listOfInterventionObesite.add(new Interventions("Sleeve" , R.drawable.sleeve , "sleeve"));
        interventionObesiteAdapter = new InterventionsAdapter(getContext(),listener , listOfInterventionObesite);
        recyclerViewObesite.setAdapter(interventionObesiteAdapter);
        arrowForwardCYeux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewCYeux.smoothScrollToPosition(linearLayoutManager2.findLastVisibleItemPosition() + 1);
            }
        });

        // intervention seins
        recyclerViewInterventionSeins.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewInterventionSeins.setLayoutManager(linearLayoutManager3);
        listOfInterventionSeins.add(new Interventions("Gynécomastie",R.drawable.gynecomastie ,"gynécomastie"));
        listOfInterventionSeins.add(new Interventions("Reduction mammaire",R.drawable.reduction_mammaire ,"reduction mammaire"));
        listOfInterventionSeins.add(new Interventions("Augmentation sein",R.drawable.augmentation_sein ,"augmentation sein"));
        listOfInterventionSeins.add(new Interventions("Lifting sein",R.drawable.lifting_sein ,"lifting sein"));
        interventionSeinsAdapter = new InterventionsAdapter(getContext() ,listener, listOfInterventionSeins);
        recyclerViewInterventionSeins.setAdapter(interventionSeinsAdapter);
        arrowForwardISeins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewInterventionSeins.smoothScrollToPosition(linearLayoutManager3.findLastVisibleItemPosition() + 1);
            }
        });

        arrowBackISeins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewInterventionSeins.smoothScrollToPosition(linearLayoutManager3.findFirstVisibleItemPosition() );

            }
        });

        // intervention silhouette
        recyclerViewInterventionSilhouette.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewInterventionSilhouette.setLayoutManager(linearLayoutManager4);
        listOfInterventionSilhouette.add(new Interventions("Augmentation fesses",R.drawable.aug_fesses,"augmentation fesses"));
        listOfInterventionSilhouette.add(new Interventions("Liposuccion",R.drawable.liposuccion ,"liposuccion"));
        listOfInterventionSilhouette.add(new Interventions("Lifting cuisses",R.drawable.lifting_cuisses ,"lifting cuisses"));
        listOfInterventionSilhouette.add(new Interventions("Abdominoplastie",R.drawable.abdominoplastie ,"abdominoplastie"));
        interventionSilhouetteAdapter = new InterventionsAdapter(getContext(),listener , listOfInterventionSilhouette);
        recyclerViewInterventionSilhouette.setAdapter(interventionSilhouetteAdapter);
        arrowForwardISilhouette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewInterventionSilhouette.smoothScrollToPosition(linearLayoutManager4.findLastVisibleItemPosition() + 1);
            }
        });

        arrowBackISilhouette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewInterventionSilhouette.smoothScrollToPosition(linearLayoutManager4.findFirstVisibleItemPosition() );

            }
        });

        // intervention sentaire
        recyclerViewDentaire.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager5 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewDentaire.setLayoutManager(linearLayoutManager5);
        listOfInterventionDentaire.add(new Interventions("Blanchiment",R.drawable.blanchiment ,"blanchiment"));
        listOfInterventionDentaire.add(new Interventions("Implant dentaire",R.drawable.implant_dentaire ,"implant dentaire"));
        listOfInterventionDentaire.add(new Interventions("Facette dentaire",R.drawable.facette_dentaire ,"facette dentaire"));
        interventionDentaireAdapter = new InterventionsAdapter(getContext(),listener , listOfInterventionDentaire);
        recyclerViewDentaire.setAdapter(interventionDentaireAdapter);
        arrowForwardIDentaire.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View v) {
                                                         recyclerViewDentaire.smoothScrollToPosition(linearLayoutManager5.findLastVisibleItemPosition() + 1);
                                                     }
                                                 });

        arrowBackIDentaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewDentaire.smoothScrollToPosition(linearLayoutManager5.findFirstVisibleItemPosition() );

            }
        });

        // intervention yeux
        recyclerViewCYeux.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager6 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCYeux.setLayoutManager(linearLayoutManager6);
        listOfInterventionYeux.add(new Interventions("Lazik",R.drawable.lazik ,"lazik"));
        listOfInterventionYeux.add(new Interventions("Cataracte",R.drawable.cataracte ,"cataracte"));
        interventionYeuxAdapter = new InterventionsAdapter(getContext() ,listener, listOfInterventionYeux);
        recyclerViewCYeux.setAdapter(interventionYeuxAdapter);
        arrowForwardCYeux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewCYeux.smoothScrollToPosition(linearLayoutManager6.findLastVisibleItemPosition() + 1);
            }
        });

        // intervention In Vitro
        recyclerViewInVitro.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager7 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewInVitro.setLayoutManager(linearLayoutManager7);
        listOfInterventionInVitro.add(new Interventions("Insemination artificielle",R.drawable.insemination_artificielle ,"insemination artificielle"));
        listOfInterventionInVitro.add(new Interventions("Biopsie testiculaire",R.drawable.biopsie_testiculaire ,"biopsie testiculaire"));
        interventionInVitroAdapter= new InterventionsAdapter(getContext() ,listener, listOfInterventionInVitro);
        recyclerViewInVitro.setAdapter(interventionInVitroAdapter);

        // chirurgie intime
        recyclerViewCIntime.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager9 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCIntime.setLayoutManager(linearLayoutManager9);
        listOfInterventionChirurgieIntime.add(new Interventions("Hymenoplastie",R.drawable.hymenoplastie,"hymenoplastie"));
        listOfInterventionChirurgieIntime.add(new Interventions("Labiaplastie",R.drawable.labiaplastie,"labiaplastie"));
        listOfInterventionChirurgieIntime.add(new Interventions("Penoplastie",R.drawable.penoplastie,"penoplastie"));
        listOfInterventionChirurgieIntime.add(new Interventions("Vaginoplastie",R.drawable.vaginoplastie,"vaginoplastie"));
        interventionChirurgieIntimeAdapter = new InterventionsAdapter(getContext(),listener , listOfInterventionChirurgieIntime);
        recyclerViewCIntime.setAdapter(interventionChirurgieIntimeAdapter);
        arrowForwardCIntime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewCIntime.smoothScrollToPosition(linearLayoutManager9.findLastVisibleItemPosition() + 1);
            }
        });
        arrowBackCIntime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewCIntime.smoothScrollToPosition(linearLayoutManager9.findFirstVisibleItemPosition() );

            }
        });

        // intervention medecine esthetique
        recyclerViewMedecineEsthetique.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager8 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMedecineEsthetique.setLayoutManager(linearLayoutManager8);
        listOfMedecineEsthetique.add(new Interventions("Acide hyaluronique",R.drawable.acide_hyaluronique,"acide hyaluronique"));
        listOfMedecineEsthetique.add(new Interventions("Acide botulique",R.drawable.acide_botulique,"acide botulique"));
        listOfMedecineEsthetique.add(new Interventions("Plasma",R.drawable.plasma,"plasma"));
        interventionMedecineEsthetiqueAdapter = new InterventionsAdapter(getContext(),listener , listOfMedecineEsthetique);
        recyclerViewMedecineEsthetique.setAdapter(interventionMedecineEsthetiqueAdapter);
        arrowForwardMedecineEsthetique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewMedecineEsthetique.smoothScrollToPosition(linearLayoutManager8.findLastVisibleItemPosition() + 1);
            }
        });
        arrowBackMedecineEsthetique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewMedecineEsthetique.smoothScrollToPosition(linearLayoutManager8.findFirstVisibleItemPosition() );

            }
        });




    }


}

