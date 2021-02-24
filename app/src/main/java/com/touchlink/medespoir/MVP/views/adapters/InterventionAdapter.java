package com.touchlink.medespoir.MVP.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.touchlink.medespoir.MVP.models.network.Article;
import com.touchlink.medespoir.MVP.models.network.Interventions;
import com.touchlink.medespoir.MVP.views.ui.callbacks.InterventionListener;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVBold;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVLight;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.utils.MEConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InterventionAdapter extends RecyclerView.Adapter<InterventionAdapter.MyViewHolder> {



    private Context context ;
    private List<Interventions> listOfInterventions;
    private InterventionListener listener ;


    public InterventionAdapter(Context context , InterventionListener listener,List<Interventions> listOfInterventions){
        this.listOfInterventions = listOfInterventions ;
        this.context = context ;
        this.listener = listener ;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_intervention_details, parent, false);
        return new InterventionAdapter.MyViewHolder(view , listener);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Interventions chat = listOfInterventions.get(position);
        //holder.showMessage.setText(chat.getMessage());
        Interventions intervention = listOfInterventions.get(position);
        Glide
                .with(this.context)
                .load(intervention.getIcon())
                .centerCrop()
                .into(holder.imageIntervention);
        holder.descriptionIntervention.setText(intervention.getDescription());



    }

    @Override
    public int getItemViewType(int position) {



        return Integer.valueOf(MEConstants.ARTICLE_TYPE_HALF_WIDTH) ;


    }




    @Override
    public int getItemCount() {
        //return listOfInterventions.size();
        return listOfInterventions.size() ;
    }

    public void clear() {
        this.clear();
    }

    public void refresh(List<Article> listchats) {
        listOfInterventions.clear();
        for (int i = 0 ; i < listchats.size() ; i++){
            this.listOfInterventions.add(listchats.size() , listOfInterventions.get(i) );
        }

        this.notifyDataSetChanged();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        //public TouchlinkTVRegular showMessage ;
        //public TouchlinkTVBold date ;
        @SuppressLint("id")
        @BindView(R.id.desc_intervention)
        MedEspoirTVBold descriptionIntervention;
        @SuppressLint("id")
        @BindView(R.id.image_intervention)
        ImageView imageIntervention;
        private  InterventionListener listener ;


        public MyViewHolder(View itemView, InterventionListener listener) {
            super(itemView);
            ButterKnife.bind(this , itemView);
            this.listener = listener ;
            itemView.setOnClickListener(this);
            //showMessage = itemView.findViewById(R.id.show_message);
            // date = itemView.findViewById(R.id.created_date);


        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());

        }
    }
}

