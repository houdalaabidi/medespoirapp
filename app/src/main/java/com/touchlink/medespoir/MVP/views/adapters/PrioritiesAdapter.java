package com.touchlink.medespoir.MVP.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.touchlink.medespoir.MVP.models.network.Prioritee;
import com.touchlink.medespoir.MVP.models.network.Titre;
import com.touchlink.medespoir.MVP.views.ui.callbacks.PrioritiesListener;
import com.touchlink.medespoir.MVP.views.ui.callbacks.TitleListener;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVRegular;
import com.touchlink.medespoir.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;




public class PrioritiesAdapter extends RecyclerView.Adapter<PrioritiesAdapter.MyViewHolder> {



    private Context context ;
    private List<Prioritee> listOfPriorities;
    private PrioritiesListener listener ;


    public PrioritiesAdapter(Context context  , List<Prioritee> listOfPriorities, PrioritiesListener listener){
        this.listOfPriorities = listOfPriorities ;
        this.context = context ;
        this.listener = listener ;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_title, parent, false);
        return new PrioritiesAdapter.MyViewHolder(view, listener);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Prioritee prioritee = listOfPriorities.get(position);
        holder.labelTitle.setText(prioritee.getLable());





    }





    @Override
    public int getItemCount() {
        return listOfPriorities.size();
    }

    public void clear() {
        this.clear();
    }



    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{


        MedEspoirTVRegular labelTitle ;
        PrioritiesListener listener ;



        public MyViewHolder(View itemView, PrioritiesListener listener) {
            super(itemView);

            labelTitle = itemView.findViewById(R.id.title_item);
            this.listener = listener ;
            itemView.setOnClickListener(this);




        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition(),  listOfPriorities.get(getAdapterPosition()).getLable());
        }
    }
}

