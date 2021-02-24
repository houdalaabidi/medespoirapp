package com.touchlink.medespoir.MVP.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchlink.medespoir.MVP.models.network.Sousintervention;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVRegular;
import com.touchlink.medespoir.MVP.views.ui.dialogfragments.OperationsFragment;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.utils.MEConstants;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;




public class sousInterventionAdapter extends RecyclerView.Adapter<sousInterventionAdapter.MyViewHolder> {



    private Context context ;
    private List<Sousintervention> listOfSousIntervention;



    public sousInterventionAdapter(Context context ,List<Sousintervention> listOfSousIntervention){
        this.listOfSousIntervention = listOfSousIntervention ;
        this.context = context ;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sous_intervention, parent, false);
        return new sousInterventionAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Sousintervention sousintervention = listOfSousIntervention.get(position);
        holder.label.setText(sousintervention.getSousoperation());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( holder.checkBox.isChecked()){


                    if(!OperationsFragment.listOfSousInterventionsIds.contains(sousintervention.getId())
                    &&(!OperationsFragment.listOfSousInterventionsLabels.contains(sousintervention.getSousoperation() ))){
                        OperationsFragment.listOfSousInterventionsIds.add(sousintervention.getId());
                        OperationsFragment.listOfSousInterventionsLabels.add(sousintervention.getSousoperation());

                    }

                } else {

                    if( OperationsFragment.listOfSousInterventionsIds.contains(sousintervention.getId())
                    && OperationsFragment.listOfSousInterventionsLabels.contains(sousintervention.getSousoperation() ) ){
                        OperationsFragment.listOfSousInterventionsIds.remove(sousintervention.getId());
                        OperationsFragment.listOfSousInterventionsLabels.remove(sousintervention.getSousoperation());

                    }
                }



            }
        });




    }

    @Override
    public int getItemViewType(int position) {



        return Integer.valueOf(MEConstants.ARTICLE_TYPE_HALF_WIDTH) ;


    }




    @Override
    public int getItemCount() {
        return listOfSousIntervention.size();
    }

    public void clear() {
        this.clear();
    }


    public class MyViewHolder extends  RecyclerView.ViewHolder {



        AppCompatCheckBox checkBox ;
        MedEspoirTVRegular label ;




        public MyViewHolder(View itemView ) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.check_box_sous_intervention);
            label =  itemView.findViewById(R.id.label_sous_intervention);

        }

    }
}

