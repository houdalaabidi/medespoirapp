package com.touchlink.medespoir.MVP.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchlink.medespoir.MVP.models.network.Sousoperation;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVLight;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.utils.MEConstants;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class NewDevisDetailsAdapter extends RecyclerView.Adapter<NewDevisDetailsAdapter.MyViewHolder> {



    private Context context ;
   // private List<Operation> listOfOperations;
    private ArrayList<Sousoperation>   listOfSousOperations ;

/*
    public OperationsAdapter(Context context  , List<Operation> listOfOperations){
        this.listOfOperations = listOfOperations ;
        this.context = context ;

    }*/

    public NewDevisDetailsAdapter(Context context ,ArrayList<Sousoperation>   listOfSousOperations ){
        this.context = context ;
        this.listOfSousOperations = listOfSousOperations ;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_new_devis_details, parent, false);
        return new NewDevisDetailsAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Sousoperation operation = listOfSousOperations.get(position);
        holder.operation.setText(operation.getName());
        holder.prix.setText(operation.getPrix());





    }

    @Override
    public int getItemViewType(int position) {



        return Integer.valueOf(MEConstants.ARTICLE_TYPE_HALF_WIDTH) ;


    }




    @Override
    public int getItemCount() {

       return listOfSousOperations.size();
    }

    public void clear() {
        this.clear();
    }



    public class MyViewHolder extends  RecyclerView.ViewHolder{
        @SuppressLint("id")
        @BindView(R.id.operation)
        MedEspoirTVLight operation ;
        @SuppressLint("id")
        @BindView(R.id.prix)
        MedEspoirTVLight prix;



        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this , itemView);

        }
    }
}

