package com.touchlink.medespoir.MVP.views.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.bumptech.glide.Glide;
import com.touchlink.medespoir.MVP.models.network.Article;
import com.touchlink.medespoir.MVP.views.ui.callbacks.ArticleListener;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVBold;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVLight;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVRegular;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.WebServices.retrofit.API;
import com.touchlink.medespoir.WebServices.retrofit.Urls;
import com.touchlink.medespoir.utils.MEConstants;

import java.util.List;

public class ArticlesAdapter2 extends RecyclerView.Adapter<ArticlesAdapter2.MyViewHolder> {



    private Context context ;
    private List<Article> listOfArticles;
    private ArticleListener listener ;


    public ArticlesAdapter2(Context context , List<Article> listOfArticles, ArticleListener listener){
        this.listOfArticles = listOfArticles ;
        this.context = context ;
        this.listener = listener ;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_article_full_width, parent, false);
        return new ArticlesAdapter2.MyViewHolder(view, listener);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Article article = listOfArticles.get(listOfArticles.size() -  position - 1);


        holder.date.setText(article.getDate().getDay() + "/"+article.getDate().getMonth()+"/"+article.getDate().getYear());
        holder.articleTitle.setText(article.getLabel());
        holder.description.setText(article.getDescription());
        Glide.with(context)
                .load(Urls.MAIN_URL +article.getImage())
                .centerCrop()
                .placeholder(R.drawable.image_not_found)
                .error(R.drawable.image_not_found)
                .into(holder.imageView);




    }

    @Override
    public int getItemViewType(int position) {



        return Integer.valueOf(MEConstants.ARTICLE_TYPE_HALF_WIDTH) ;


    }




    @Override
    public int getItemCount() {
        return listOfArticles.size();
    }

    public void clear() {
        this.clear();
    }

    public void refresh(List<Article> listchats) {
        listOfArticles.clear();
        for (int i = 0 ; i < listchats.size() ; i++){
            this.listOfArticles.add(listchats.size() , listchats.get(i) );
        }

        this.notifyDataSetChanged();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        //public TouchlinkTVRegular showMessage ;
        //public TouchlinkTVBold date ;
        @SuppressLint("id")
        @BindView(R.id.image_article_full)
        ImageView imageView ;
        @SuppressLint("id")
        @BindView(R.id.article_title_full)
        MedEspoirTVBold articleTitle ;
        @SuppressLint("id")
        @BindView(R.id.date_article_full)
        MedEspoirTVRegular date ;
        @SuppressLint("id")
        @BindView(R.id.article_desc_full)
        MedEspoirTVLight description ;
        ArticleListener listener ;


        public MyViewHolder(View itemView,ArticleListener listener) {
            super(itemView);
            ButterKnife.bind(this , itemView);
            this.listener = listener ;
            itemView.setOnClickListener(this);
            //showMessage = itemView.findViewById(R.id.show_message);
            // date = itemView.findViewById(R.id.created_date);

        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition(), listOfArticles.get(listOfArticles.size() - getAdapterPosition() - 1));
        }
    }
}
