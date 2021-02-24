package com.touchlink.medespoir.MVP.views.ui.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.google.gson.Gson;
import com.touchlink.medespoir.MVP.models.network.Actualite;
import com.touchlink.medespoir.MVP.models.network.Article;
import com.touchlink.medespoir.MVP.models.network.MedespoirResponse;
import com.touchlink.medespoir.MVP.views.adapters.ArticlesAdapter;
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

import java.util.ArrayList;

public class ActualitesFragment extends Fragment {
    @SuppressLint("id")
    @BindView(R.id.rv_list_actualities)
    RecyclerView rvListActualities ;
    @SuppressLint("id")
    @BindView(R.id.loader)
    LottieAnimationView loader ;
    ArrayList<Article> listOfArticles = new ArrayList<Article>();
    @SuppressLint("id")
    @BindView(R.id.no_articles)
    MedEspoirTVBold noArticles;
    @SuppressLint("id")
    @BindView(R.id.ll_no_connexion)
    LinearLayout noConnexion;
    ArticleListener listener ;




    public ActualitesFragment() {
        // Required empty public constructor
    }

    public static ActualitesFragment newInstance() {
        ActualitesFragment fragment = new ActualitesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actualites, container, false);
        init(view);
        return view;
    }
    private void init(View view) {
        ButterKnife.bind(this , view);



        if(DeviceConnectivity.isNetworkAvailable(getContext())) {
            listener = new ArticleListener() {
                @Override
                public void onClick(View view, int position, Article article) {

                    ArticleDetailsFragment articleDetailsFragment = new ArticleDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("label", article.getLabel());
                    bundle.putString("description", article.getDescription());
                    bundle.putString("date", article.getDate().getDay()+" / "+ article.getDate().getMonth()+ " / "+ article.getDate().getYear());
                    bundle.putString("image", article.getImage());
                    articleDetailsFragment.setArguments(bundle);
                    MainActivity.goToFragment(articleDetailsFragment, true);
                }
            };

            loadData();
        } else {

           noConnexion.setVisibility(View.VISIBLE);
           loader.setVisibility(View.GONE);
           rvListActualities.setVisibility(View.GONE);
           noArticles.setVisibility(View.GONE);
        }


    }

    private void loadData(){

        loader.setVisibility(View.VISIBLE);
        noArticles.setVisibility(View.GONE);
        noConnexion.setVisibility(View.GONE);
        rvListActualities.setVisibility(View.GONE);
        listOfArticles.clear();
        Call<MedespoirResponse> call = WebServiceFactory.getInstance().getApi().getListArticles("Bearer "+ MedespoirTokenSession.getUsergestToken(getContext()));
        call.enqueue(new Callback<MedespoirResponse>() {
            @Override
            public void onResponse(Call<MedespoirResponse> call, Response<MedespoirResponse> response) {




                if (response.code() == MEConstants.CODE_TOKEN_EXPIRED || response.code() == MEConstants.CODE_NO_TOKEN) {


                    MedespoirSession.Reconnecter(getContext());

                    loadData();
                } else {
                    loader.setVisibility(View.GONE);
                    if (response != null) {
                        if (response.body() != null) {
                            rvListActualities.setVisibility(View.VISIBLE);


                            if (response.body().getStatus() == 1) {
                                Object data = null;
                                data = response.body().getData();

                                Gson gson = Utility.getGsonInstance();
                                try {


                                    Actualite actualites = gson.fromJson(gson.toJson(data), Actualite.class);
                                    listOfArticles = actualites.getActualite();

                                    rvListActualities.setHasFixedSize(true);
                                    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), MEConstants.ITEM_PER_ROW);
                                    rvListActualities.setLayoutManager(layoutManager);
                                    ArticlesAdapter articlesAdapter = new ArticlesAdapter(getContext(), listOfArticles, listener);
                                    rvListActualities.setAdapter(articlesAdapter);

                                } catch (Exception e) {
                                    e.printStackTrace();

                                }


                            } else if (response.body().getStatus() == 2){
                                noConnexion.setVisibility(View.GONE);
                                noArticles.setVisibility(View.VISIBLE);
                                rvListActualities.setVisibility(View.GONE);
                                loader.setVisibility(View.GONE);

                        }else {
                                noConnexion.setVisibility(View.GONE);
                                noArticles.setVisibility(View.GONE);
                                rvListActualities.setVisibility(View.GONE);
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
                        } else {
                            noConnexion.setVisibility(View.GONE);
                            noArticles.setVisibility(View.GONE);
                            rvListActualities.setVisibility(View.GONE);
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


                    else{
                        noConnexion.setVisibility(View.GONE);
                        noArticles.setVisibility(View.GONE);
                        rvListActualities.setVisibility(View.GONE);
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
            }


            @Override
            public void onFailure(Call<MedespoirResponse> call, Throwable t) {

                noConnexion.setVisibility(View.GONE);
                noArticles.setVisibility(View.GONE);
                rvListActualities.setVisibility(View.GONE);
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
        });
    }
}