package com.touchlink.medespoir.MVP.views.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.touchlink.medespoir.MVP.views.ui.activities.MainActivity;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVBold;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVLight;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVRegular;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.WebServices.retrofit.Urls;

public class ArticlePerCategorieDetailsFragment extends Fragment {

    private  String label ;
    private  String description ;
    private  String date ;
    private  String image ;
    private ImageView fab ;

    private MedEspoirTVLight descriptionview ;
    private MedEspoirTVBold labelview ;
    private ImageView imageview ;
    private MedEspoirTVRegular dateview ;
    private View view ;

    public ArticlePerCategorieDetailsFragment() {
        // Required empty public constructor
    }

    public static ArticlePerCategorieDetailsFragment newInstance() {
        ArticlePerCategorieDetailsFragment fragment = new ArticlePerCategorieDetailsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_article_per_categorie_details, container, false);
        init(view);

        return view;
    }

    private void init(View view){
        Bundle bundle = getArguments();
        label = bundle.getString("label");
        description = bundle.getString("description");
        date = bundle.getString("date");
        image = bundle.getString("image");
        fab = view.findViewById(R.id.fab);
        imageview = view.findViewById(R.id.image_article);
        labelview = view.findViewById(R.id.label_article);
        descriptionview = view.findViewById(R.id.desc_article);
        dateview = view.findViewById(R.id.date_article);
        labelview.setText(label);
        descriptionview.setText(description);
        dateview.setText(date);
        Glide
                .with(getContext())
                .load(Urls.MAIN_URL +image)
                .placeholder(R.drawable.image_not_found)
                .error(R.drawable.image_not_found)
                .centerCrop()
                .into(imageview);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageFragment messageFragment = new MessageFragment();
                MainActivity.goToFragment(messageFragment , true);
            }
        });



    }
}