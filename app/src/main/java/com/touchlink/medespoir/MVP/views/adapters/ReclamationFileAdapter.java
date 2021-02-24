package com.touchlink.medespoir.MVP.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.touchlink.medespoir.MVP.models.network.Image;
import com.touchlink.medespoir.MVP.models.network.Operationimage;
import com.touchlink.medespoir.MVP.views.ui.callbacks.FileListener;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.WebServices.retrofit.Urls;
import com.touchlink.medespoir.utils.MEConstants;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;




public class ReclamationFileAdapter extends RecyclerView.Adapter<ReclamationFileAdapter.MyViewHolder> {



    private Context context ;
    private List<Image> listOfFiles;
    private FileListener listener ;


    public ReclamationFileAdapter(Context context , FileListener listener, List<Image> listOfFiles){
        this.listOfFiles = listOfFiles ;
        this.context = context ;
        this.listener = listener ;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_file, parent, false);
        return new ReclamationFileAdapter.MyViewHolder(view , listener);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Image file = listOfFiles.get(position);
        if (file.getImage() != null && file.getImage() !=  ""){
            Glide
                    .with(this.context)
                    .load(Urls.MAIN_URL + file.getImage())
                    .centerCrop()
                    .error(R.drawable.image_not_found)
                    .placeholder(R.drawable.image_not_found)
                    .into(holder.file);
        }




    }

    @Override
    public int getItemViewType(int position) {



        return Integer.valueOf(MEConstants.ARTICLE_TYPE_HALF_WIDTH) ;


    }




    @Override
    public int getItemCount() {
        //return listOfInterventions.size();
        return listOfFiles.size() ;
    }

    public void clear() {
        this.clear();
    }

    public void refresh(List<Image> listOfFiles) {
        listOfFiles.clear();
        for (int i = 0 ; i < listOfFiles.size() ; i++){
            this.listOfFiles.add(listOfFiles.size() , listOfFiles.get(i) );
        }

        this.notifyDataSetChanged();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        @SuppressLint("id")
        @BindView(R.id.file)
        ImageView file;
        private  FileListener listener ;


        public MyViewHolder(View itemView, FileListener listener) {
            super(itemView);
            ButterKnife.bind(this , itemView);
            this.listener = listener ;
            itemView.setOnClickListener(this);



        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition(), listOfFiles.get(getAdapterPosition()).getImage());

        }
    }
}

