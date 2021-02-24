package com.touchlink.medespoir.MVP.views.ui.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.fxn.cue.Cue;
import com.fxn.cue.enums.Duration;
import com.fxn.cue.enums.Type;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.touchlink.medespoir.MVP.models.network.Actualite;
import com.touchlink.medespoir.MVP.models.network.Article;
import com.touchlink.medespoir.MVP.models.network.DateReclamation;
import com.touchlink.medespoir.MVP.models.network.MedespoirResponse;
import com.touchlink.medespoir.MVP.models.network.Reclamations;
import com.touchlink.medespoir.MVP.views.adapters.ArticlesAdapter;
import com.touchlink.medespoir.MVP.views.adapters.ArticlesAdapter2;
import com.touchlink.medespoir.MVP.views.adapters.MesReclamationsAdapter;
import com.touchlink.medespoir.MVP.views.ui.activities.MainActivity;
import com.touchlink.medespoir.MVP.views.ui.callbacks.ArticleListener;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVBold;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.WebServices.retrofit.WebServiceFactory;
import com.touchlink.medespoir.session.MedespoirSession;
import com.touchlink.medespoir.session.MedespoirTokenSession;
import com.touchlink.medespoir.utils.DeviceConnectivity;
import com.touchlink.medespoir.utils.MEConstants;
import com.touchlink.medespoir.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticlesFragment extends Fragment {
    @SuppressLint("id")
    @BindView(R.id.articles_recycler_view)
    RecyclerView recyclerView ;
    @SuppressLint("id")
    @BindView(R.id.no_actualities1)
    MedEspoirTVBold noActualities1;
    @SuppressLint("id")
    @BindView(R.id.no_actualities2)
    MedEspoirTVBold noActualities2 ;
    @SuppressLint("id")
    @BindView(R.id.loader1)
    LottieAnimationView loader1;
    @SuppressLint("id")
    @BindView(R.id.loader2)
    LottieAnimationView loader2 ;
    @SuppressLint("id")
    @BindView(R.id.articles_recycler_view2)
    RecyclerView recyclerView2 ;
    @SuppressLint("id")
    @BindView(R.id.see_actualities)
    MedEspoirTVBold seeActualities;
    private LinearLayoutManager linearLayoutManager ;
    private ArticlesAdapter2 articlesAdapter2 ;
    private ArticlesAdapter articlesAdapter ;
    ArrayList<Article> listOfArticles = new ArrayList<Article>();
    ArrayList<Article> listOfArticles2 = new ArrayList<Article>();
    @SuppressLint("id")
    @BindView(R.id.see_actualities_per_categorie)
    MedEspoirTVBold seeArticlesPerCategorie ;
    @SuppressLint("id")
    @BindView(R.id.ll_no_connexion)
    LinearLayout llNoConnexion ;
    @SuppressLint("id")
    @BindView(R.id.ll_remote_data)
    LinearLayout llRemoteData ;
    ArticleListener listener ;
    public ArticlesFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_articles, container, false) ;

        init(view);
        if(DeviceConnectivity.isNetworkAvailable(getContext())) {
            llNoConnexion.setVisibility(View.GONE);
            llRemoteData.setVisibility(View.VISIBLE);
            loadData1();
            loadData2();
        } else {
            llNoConnexion.setVisibility(View.VISIBLE);
            llRemoteData.setVisibility(View.GONE);



        }


        return view ;
    }

    private void init(View view){
        ButterKnife.bind(this , view);
        listener = new ArticleListener() {
            @Override
            public void onClick(View view, int position, Article article) {

            }
        };

        seeActualities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActualitesFragment actualitesFragment = new ActualitesFragment();
                MainActivity.goToFragment(actualitesFragment, true);
            }
        });
        seeArticlesPerCategorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActualitiesPerCategoriFragment actualitiesPerCategoriFragment = new ActualitiesPerCategoriFragment();
                MainActivity.goToFragment(actualitiesPerCategoriFragment , true );
            }
        });
    }

    private void loadData1() {


        recyclerView.setVisibility(View.GONE);
        loader1.setVisibility(View.VISIBLE);
        listOfArticles.clear();
        noActualities1.setVisibility(View.GONE);
        Call<MedespoirResponse> call = WebServiceFactory.getInstance().getApi().getListPromotions("Bearer "+ MedespoirTokenSession.getUsergestToken(getContext()));
        call.enqueue(new Callback<MedespoirResponse>() {
            @Override
            public void onResponse(Call<MedespoirResponse> call, Response<MedespoirResponse> response) {

                if (response.code() == MEConstants.CODE_TOKEN_EXPIRED || response.code() == MEConstants.CODE_NO_TOKEN) {


                    MedespoirSession.Reconnecter(getContext());

                    loadData1();
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    loader1.setVisibility(View.GONE);
                    if (response != null) {
                        if (response.body() != null) {


                            if (response.body().getStatus() == 1) {
                                Object data = null;
                                data = response.body().getData();

                                Gson gson = Utility.getGsonInstance();
                                try {


                                    Actualite actualites= gson.fromJson(gson.toJson(data), Actualite.class);
                                    listOfArticles =    actualites.getActualite();

                                    recyclerView.setHasFixedSize(true);
                                    GridLayoutManager layoutManager = new GridLayoutManager(getContext()  , MEConstants.ITEM_PER_ROW );
                                    recyclerView.setLayoutManager(layoutManager);
                                    ArticlesAdapter articlesAdapter = new ArticlesAdapter(getContext() ,listOfArticles, listener);
                                    recyclerView.setAdapter(articlesAdapter);

                                }catch (Exception e){
                                    e.printStackTrace();

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


                             } else if ( response.body().getStatus()== 2) {
                                recyclerView.setVisibility(View.GONE);
                                noActualities1.setVisibility(View.VISIBLE);
                            }
                            else {
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


                    else{
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


                recyclerView.setVisibility(View.GONE);
                loader1.setVisibility(View.GONE);
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

    private void loadData2() {

        recyclerView2.setVisibility(View.GONE);
        loader2.setVisibility(View.VISIBLE);
        listOfArticles2.clear();
        noActualities2.setVisibility(View.GONE);

        Call<MedespoirResponse>  call2 = WebServiceFactory.getInstance().getApi().getListArticles("Bearer "+ MedespoirTokenSession.getUsergestToken(getContext()));
        call2.enqueue(new Callback<MedespoirResponse>() {
            @Override
            public void onResponse(Call<MedespoirResponse> call, Response<MedespoirResponse> response) {
                if (response.code() == MEConstants.CODE_TOKEN_EXPIRED || response.code() == MEConstants.CODE_NO_TOKEN) {

                    MedespoirSession.Reconnecter(getContext());

                    loadData2();
                } else {
                    recyclerView2.setVisibility(View.VISIBLE);
                    loader2.setVisibility(View.GONE);
                    if (response != null) {
                        if (response.body() != null) {
                            recyclerView2.setVisibility(View.VISIBLE);
                            loader2.setVisibility(View.GONE);

                            if (response.body().getStatus() == 1) {
                                Object data = null;
                                data = response.body().getData();

                                Gson gson = Utility.getGsonInstance();
                                try {


                                    Actualite actualites= gson.fromJson(gson.toJson(data), Actualite.class);
                                    listOfArticles2 =    actualites.getActualite();


                                    recyclerView2.setHasFixedSize(true);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                                    recyclerView2.setLayoutManager(linearLayoutManager);


                                    articlesAdapter2 = new ArticlesAdapter2(getContext() , listOfArticles2, listener);
                                    recyclerView2.setAdapter(articlesAdapter2);

                                }catch (Exception e){
                                    e.printStackTrace();
                                    loader2.setVisibility(View.GONE);
                                    recyclerView2.setVisibility(View.GONE);

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


                             } else if (response.body().getStatus() == 2){
                                noActualities2.setVisibility(View.VISIBLE);
                                recyclerView2.setVisibility(View.GONE);
                            }
                            else {
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


                    else{
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
                recyclerView2.setVisibility(View.GONE);
                loader2.setVisibility(View.GONE);
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

