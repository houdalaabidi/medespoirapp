package com.touchlink.medespoir.MVP.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.touchlink.medespoir.MVP.models.network.Chat;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVRegular;
import com.touchlink.medespoir.R;

import java.util.ArrayList;

public class MessageAdapter extends  RecyclerView.Adapter<MessageAdapter.MyViewHolder>{

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context context ;
    private ArrayList<Chat> chats;

    FirebaseUser fUser ;

    public MessageAdapter(Context context , ArrayList<Chat> chats){
        this.chats = chats ;
        this.context = context ;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_right, parent, false);
            return new MessageAdapter.MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_left, parent, false);
            return new MessageAdapter.MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Chat chat = chats.get(position);
        holder.showMessage.setText(chat.getMessage());

        if( position == 0) {
            holder.createdDate.setText(chat.getCreated_date()+"");
        } else if (!chat.getSender().equalsIgnoreCase(chats.get(position-1).getSender()) && position != 0)
        {
            holder.createdDate.setText(chat.getCreated_date()+"");
        } else {
            holder.createdDate.setText("");

        }


    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chats.get(position).getSender().equals(fUser.getUid())){
            return  MSG_TYPE_RIGHT;
        } else {

            return MSG_TYPE_LEFT ;
        }

    }




    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{

        public MedEspoirTVRegular showMessage , createdDate;


        public MyViewHolder(View itemView) {
            super(itemView);
            showMessage = itemView.findViewById(R.id.show_message);
            createdDate = itemView.findViewById(R.id.created_date);

        }
    }
}
