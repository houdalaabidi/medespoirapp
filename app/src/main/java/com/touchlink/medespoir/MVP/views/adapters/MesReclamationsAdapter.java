package com.touchlink.medespoir.MVP.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.touchlink.medespoir.MVP.models.network.Article;
import com.touchlink.medespoir.MVP.models.network.Interventions;
import com.touchlink.medespoir.MVP.models.network.Reclamation;
import com.touchlink.medespoir.MVP.views.ui.callbacks.InterventionListener;
import com.touchlink.medespoir.MVP.views.ui.callbacks.ReclamationListener;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVBold;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVRegular;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.utils.MEConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MesReclamationsAdapter extends RecyclerView.Adapter<MesReclamationsAdapter.MyViewHolder> {



    private Context context ;
    private List<Reclamation> listOfReclamations;
    private ReclamationListener listener ;


    public MesReclamationsAdapter(Context context , ReclamationListener listener,List<Reclamation> listOfReclamations){
        this.listOfReclamations = listOfReclamations ;
        this.context = context ;
        this.listener = listener ;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reclamation_layout, parent, false);
        return new MesReclamationsAdapter.MyViewHolder(view , listener);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Interventions chat = listOfInterventions.get(position);
        //holder.showMessage.setText(chat.getMessage());
        Reclamation reclamation = listOfReclamations.get(position);
        holder.day.setText(reclamation.getDate().getDay());
        holder.date.setText(reclamation.getDate().getMonth() + " / "+ reclamation.getDate().getYear());
        holder.status.setText(reclamation.getStatut());
        holder.text.setText(reclamation.getDescription());



    }

    @Override
    public int getItemViewType(int position) {



        return Integer.valueOf(MEConstants.ARTICLE_TYPE_HALF_WIDTH) ;


    }




    @Override
    public int getItemCount() {
        //return listOfInterventions.size();
        return listOfReclamations.size() ;
    }

    public void clear() {
        this.clear();
    }

    public void refresh(List<Article> listchats) {
        listOfReclamations.clear();
        for (int i = 0 ; i < listchats.size() ; i++){
            this.listOfReclamations.add(listchats.size() , listOfReclamations.get(i) );
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
        private  ReclamationListener listener ;
        @SuppressLint("id")
        @BindView(R.id.text_reclamation)
        MedEspoirTVRegular text;
        @SuppressLint("id")
        @BindView(R.id.status_reclamation)
        MedEspoirTVRegular status;
        @SuppressLint("id")
        @BindView(R.id.button_details_reclamation)
        LinearLayout buttonDetails;


        public MyViewHolder(View itemView, ReclamationListener listener) {
            super(itemView);
            ButterKnife.bind(this , itemView);
            this.listener = listener ;
            itemView.setOnClickListener(this);



        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition(), listOfReclamations.get(getAdapterPosition()).getId()+"");

        }
    }
}

