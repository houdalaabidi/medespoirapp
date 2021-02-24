package com.touchlink.medespoir.MVP.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.touchlink.medespoir.MVP.models.network.Article;
import com.touchlink.medespoir.MVP.models.network.ProgramItem;
import com.touchlink.medespoir.MVP.models.network.Reclamation;
import com.touchlink.medespoir.MVP.views.ui.callbacks.ProgramListener;
import com.touchlink.medespoir.MVP.views.ui.callbacks.ReclamationListener;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVBold;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVRegular;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.utils.MEConstants;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;




public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.MyViewHolder> {



    private Context context ;
    private List<ProgramItem> listOfProgramItems;
    private ProgramListener listener ;


    public ProgramAdapter(Context context , ProgramListener listener,List<ProgramItem> listOfProgramItems){
        this.listOfProgramItems = listOfProgramItems ;
        this.context = context ;
        this.listener = listener ;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_program_layout, parent, false);
        return new ProgramAdapter.MyViewHolder(view , listener);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ProgramItem program = listOfProgramItems.get(position);
        if(program.getLabel().equalsIgnoreCase("")){
            holder.labelProgram.setText(program.getCategorie());
        } else {
            holder.labelProgram.setText(program.getLabel()+"");
        }

        holder.timeProgram.setText(program.getHeuredebut() + " _ "+ program.getHeurefin());
        holder.descProgram.setText(program.getDescription());







    }

    @Override
    public int getItemViewType(int position) {



        return Integer.valueOf(MEConstants.ARTICLE_TYPE_HALF_WIDTH) ;


    }




    @Override
    public int getItemCount() {
        //return listOfInterventions.size();
        return listOfProgramItems.size() ;
    }

    public void clear() {
        listOfProgramItems.clear();
        notifyDataSetChanged();
    }

    public void refresh(List<ProgramItem> listOfProgramItems) {
        listOfProgramItems.clear();
        listOfProgramItems.addAll(listOfProgramItems);

        this.notifyDataSetChanged();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {


        ProgramListener listener ;
        @SuppressLint("id")
        @BindView(R.id.time_program)
        MedEspoirTVBold timeProgram ;
        @SuppressLint("id")
        @BindView(R.id.label_program)
        MedEspoirTVRegular labelProgram ;
        @SuppressLint("id")
        @BindView(R.id.desc_program)
        MedEspoirTVRegular descProgram ;
        ImageView image ;


        public MyViewHolder(View itemView, ProgramListener listener) {
            super(itemView);
            ButterKnife.bind(this , itemView);
           image = itemView.findViewById(R.id.image_program);
            this.listener = listener ;
            itemView.setOnClickListener(this);



        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition(),listOfProgramItems.get(getAdapterPosition()).getId());

        }
    }
}

