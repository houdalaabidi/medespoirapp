package com.touchlink.medespoir.MVP.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.touchlink.medespoir.MVP.models.network.Article;
import com.touchlink.medespoir.MVP.models.network.Devis;
import com.touchlink.medespoir.MVP.models.network.DevisItem;
import com.touchlink.medespoir.MVP.models.network.Reclamation;
import com.touchlink.medespoir.MVP.views.ui.callbacks.DevisListener;
import com.touchlink.medespoir.MVP.views.ui.callbacks.ReclamationListener;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVBold;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVRegular;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.utils.MEConstants;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;



public class DevisAdapter extends RecyclerView.Adapter<DevisAdapter.MyViewHolder> {



    private Context context ;
    private List<DevisItem> listOfDevis;
    private DevisListener listener ;


    public DevisAdapter(Context context , DevisListener listener,List<DevisItem> listOfDevis){
        this.listOfDevis = listOfDevis ;
        this.context = context ;
        this.listener = listener ;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.devis_layout, parent, false);
        return new DevisAdapter.MyViewHolder(view , listener);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        DevisItem devis = listOfDevis.get(position);
        holder.day.setText(devis.getDatedebut());
        if (devis.getDatefin() != null && devis.getDatefin()!= ""&& (!devis.getDatefin().equalsIgnoreCase( "//"))){
            holder.date.setVisibility(View.VISIBLE);
            holder.date.setText(devis.getDatefin());
        }else {
            holder.date.setVisibility(View.GONE);
        }
        holder.status.setText(devis.getStatut());
        holder.cost.setText(devis.getTotal());



    }

    @Override
    public int getItemViewType(int position) {



        return Integer.valueOf(MEConstants.ARTICLE_TYPE_HALF_WIDTH) ;


    }




    @Override
    public int getItemCount() {
        //return listOfInterventions.size();
        return listOfDevis.size() ;
    }

    public void clear() {
        this.clear();
    }

    public void refresh(List<DevisItem> listOfDevis) {
        listOfDevis.clear();
        for (int i = 0 ; i < listOfDevis.size() ; i++){
            this.listOfDevis.add(listOfDevis.size() , listOfDevis.get(i) );
        }

        this.notifyDataSetChanged();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        @SuppressLint("id")
        @BindView(R.id.day_reclamation)
        MedEspoirTVBold day;
        @SuppressLint("id")
        @BindView(R.id.date_reclamation)
        MedEspoirTVBold date;
         DevisListener listener ;
        @SuppressLint("id")
        @BindView(R.id.status_reclamation)
        MedEspoirTVRegular status;
        @SuppressLint("id")
        @BindView(R.id.button_details_reclamation)
        LinearLayout buttonDetails;
        @SuppressLint("id")
        @BindView(R.id.cost_devis)
        MedEspoirTVRegular cost ;


        public MyViewHolder(View itemView, DevisListener listener) {
            super(itemView);
            ButterKnife.bind(this , itemView);
            this.listener = listener ;
            itemView.setOnClickListener(this);



        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition(), listOfDevis.get(getAdapterPosition()).getId()+"");

        }
    }
}
