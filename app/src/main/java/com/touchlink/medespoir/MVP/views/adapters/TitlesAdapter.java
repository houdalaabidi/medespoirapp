package com.touchlink.medespoir.MVP.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.touchlink.medespoir.MVP.models.network.Operation;
import com.touchlink.medespoir.MVP.models.network.Reclamation;
import com.touchlink.medespoir.MVP.models.network.Titre;
import com.touchlink.medespoir.MVP.views.ui.callbacks.ReclamationListener;
import com.touchlink.medespoir.MVP.views.ui.callbacks.TitleListener;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVBold;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVRegular;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.utils.MEConstants;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



public class TitlesAdapter extends RecyclerView.Adapter<TitlesAdapter.MyViewHolder> {



    private Context context ;
    private List<Titre> listOfTitles;
    private TitleListener listener ;


    public TitlesAdapter(Context context  , List<Titre> listOfTitles, TitleListener listener){
        this.listOfTitles = listOfTitles ;
        this.context = context ;
        this.listener = listener ;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_title, parent, false);
        return new TitlesAdapter.MyViewHolder(view, listener);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Titre titre = listOfTitles.get(position);
        holder.labelTitle.setText(titre.getLable());





    }





    @Override
    public int getItemCount() {
        return listOfTitles.size();
    }

    public void clear() {
        this.clear();
    }



    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{


        LinearLayout layoutItemTitle ;
        MedEspoirTVRegular labelTitle ;
        TitleListener listener ;



        public MyViewHolder(View itemView, TitleListener listener) {
            super(itemView);

            layoutItemTitle = itemView.findViewById(R.id.ll_item_title);
            labelTitle = itemView.findViewById(R.id.title_item);
            this.listener = listener ;
            itemView.setOnClickListener(this);




        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition(), listOfTitles.get(getAdapterPosition()).getId()+"", listOfTitles.get(getAdapterPosition()).getLable());
        }
    }
}

