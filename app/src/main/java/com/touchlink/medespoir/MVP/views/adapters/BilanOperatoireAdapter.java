package com.touchlink.medespoir.MVP.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.touchlink.medespoir.MVP.models.network.Article;
import com.touchlink.medespoir.MVP.models.network.Bilanoperatoires;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVBold;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVRegular;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.utils.MEConstants;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class BilanOperatoireAdapter extends RecyclerView.Adapter<BilanOperatoireAdapter.MyViewHolder> {



    private Context context ;
    private List<Bilanoperatoires> bilanoperatoires;


    public BilanOperatoireAdapter(Context context , List<Bilanoperatoires> bilanoperatoires){
        this.bilanoperatoires = bilanoperatoires ;
        this.context = context ;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bilan_operatoire, parent, false);
        return new BilanOperatoireAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Bilanoperatoires bilanoperatoire = bilanoperatoires.get(position);
        holder.labelBilanOperatoire.setText(bilanoperatoire.getOperastion()+"");
        holder.bilanOperatoireDesc.setText(bilanoperatoire.getDescription()+"");




    }

    @Override
    public int getItemViewType(int position) {



        return Integer.valueOf(MEConstants.ARTICLE_TYPE_HALF_WIDTH) ;


    }




    @Override
    public int getItemCount() {
        return bilanoperatoires.size();
    }

    public void clear() {
        this.clear();
    }

    public void refresh(List<Bilanoperatoires> bilanoperatoires) {
        bilanoperatoires.clear();
        for (int i = 0 ; i < bilanoperatoires.size() ; i++){
            this.bilanoperatoires.add(bilanoperatoires.size() , bilanoperatoires.get(i) );
        }

        this.notifyDataSetChanged();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{

        MedEspoirTVRegular bilanOperatoireDesc ;
        MedEspoirTVRegular labelBilanOperatoire ;



        public MyViewHolder(View itemView) {
            super(itemView);
            labelBilanOperatoire = itemView.findViewById(R.id.label_bilan_operatoire);
            bilanOperatoireDesc = itemView.findViewById(R.id.bilan_operatoire_desc);




        }
    }
}
