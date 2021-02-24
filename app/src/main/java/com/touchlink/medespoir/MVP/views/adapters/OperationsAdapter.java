package com.touchlink.medespoir.MVP.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchlink.medespoir.MVP.models.network.Operation;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVBold;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.utils.MEConstants;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;




public class OperationsAdapter extends RecyclerView.Adapter<OperationsAdapter.MyViewHolder> {



    private Context context ;
    private List<Operation> listOfOperations;


    public OperationsAdapter(Context context  , List<Operation> listOfOperations){
        this.listOfOperations = listOfOperations ;
        this.context = context ;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_operation, parent, false);
        return new OperationsAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Operation operation = listOfOperations.get(position);
        holder.rvItemOperation.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(context, MEConstants.ITEM_PER_ROW);
        holder.rvItemOperation.setLayoutManager(layoutManager);
        holder.labelOperation.setText(operation.getOperation());

        sousInterventionAdapter sousInterventionAdapter = new sousInterventionAdapter(context ,  operation.getSousintervention() );
        holder.rvItemOperation.setAdapter(sousInterventionAdapter);




    }

    @Override
    public int getItemViewType(int position) {



        return Integer.valueOf(MEConstants.ARTICLE_TYPE_HALF_WIDTH) ;


    }




    @Override
    public int getItemCount() {
        return listOfOperations.size();
    }

    public void clear() {
        this.clear();
    }

    public void refresh(List<Operation> listOfOperations) {
        listOfOperations.clear();
        for (int i = 0 ; i < listOfOperations.size() ; i++){
            this.listOfOperations.add(listOfOperations.size() , listOfOperations.get(i) );
        }

        this.notifyDataSetChanged();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{


        RecyclerView rvItemOperation ;
        MedEspoirTVBold labelOperation ;



        public MyViewHolder(View itemView) {
            super(itemView);

            rvItemOperation = itemView.findViewById(R.id.rv_item_operation);
            labelOperation = itemView.findViewById(R.id.label_operation);




        }
    }
}

