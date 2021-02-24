package com.touchlink.medespoir.MVP.views.ui.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.fxn.cue.Cue;
import com.fxn.cue.enums.Duration;
import com.fxn.cue.enums.Type;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.touchlink.medespoir.MVP.models.network.Chat;
import com.touchlink.medespoir.MVP.views.adapters.MessageAdapter;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirETRegular;
import com.touchlink.medespoir.MVP.views.ui.dialogfragments.ConnexionDialogFragment;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.session.SharedPreferencesUserFactory;
import com.touchlink.medespoir.utils.DeviceConnectivity;
import com.touchlink.medespoir.utils.MEConstants;
import com.touchlink.medespoir.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;

import static com.touchlink.medespoir.utils.MEConstants.ADMIN_ID;


public class MessageFragment extends Fragment {

    static FirebaseUser fUser ;
    DatabaseReference reference ;
    @SuppressLint("id")
    @BindView(R.id.et_message)
    MedEspoirETRegular etTextSend ;
    @SuppressLint("id")
    @BindView(R.id.icon_send)
    ImageView btnSend ;
    ArrayList<Chat> chats;
    MessageAdapter messageAdapter ;
    @SuppressLint("id")
    @BindView(R.id.rv_chat)
    RecyclerView rvChat ;
    LottieAnimationView loader ;




    public MessageFragment() {
        // Required empty public constructor
    }


    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        loader = view.findViewById(R.id.loader3);
        init(view);
        return view ;
    }

    private void init(View view){
        ButterKnife.bind(this , view);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        rvChat.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        rvChat.setLayoutManager(linearLayoutManager);
        reference = FirebaseDatabase.getInstance().getReference("Users").child(ADMIN_ID);
        if( ! DeviceConnectivity.isNetworkAvailable(getContext())) {
            ConnexionDialogFragment connectivityFragment = ConnexionDialogFragment.newInstance();
            connectivityFragment.show(getFragmentManager(), "ConnexionDialogFragment");
        }else {
            loader.setVisibility(View.VISIBLE);
        }

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readMessage(fUser.getUid() , ADMIN_ID);
                Log.e(" firebase user id " , fUser.getUid()+" !");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Cue.init()
                        .with(getContext())
                        .setMessage(MEConstants.TECH_ERROR)
                        .setType(Type.CUSTOM)
                        .setDuration(Duration.LONG)
                        .setBorderWidth(5)
                        .setCornerRadius(10)
                        .setCustomFontColor(Color.parseColor("#ca994c"),
                                Color.parseColor("#ffffff"),
                                Color.parseColor("#b6843d"))
                        .setFontFace("fonts/nunitosansregular.ttf")
                        .setBorderWidth(1)
                        .setCornerRadius(20)
                        .setPadding(30)
                        .setTextSize(16)
                        .setGravity(Gravity.CENTER)
                        .show();

            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( ! DeviceConnectivity.isNetworkAvailable(getContext())) {
                    ConnexionDialogFragment connectivityFragment = ConnexionDialogFragment.newInstance();
                    connectivityFragment.show(getFragmentManager(), "ConnexionDialogFragment");
                } else {
                    String message = etTextSend.getText().toString();
                    if ( !message.equalsIgnoreCase("")){
                        sendMessage(fUser.getUid() , ADMIN_ID , message);
                    } else {

                        Cue.init()
                                .with(getContext())
                                .setMessage(MEConstants.EMPTY_MESSAGE)
                                .setType(Type.CUSTOM)
                                .setDuration(Duration.LONG)
                                .setBorderWidth(5)
                                .setCornerRadius(10)
                                .setCustomFontColor(Color.parseColor("#ca994c"),
                                        Color.parseColor("#ffffff"),
                                        Color.parseColor("#b6843d"))
                                .setFontFace("fonts/nunitosansregular.ttf")
                                .setBorderWidth(1)
                                .setCornerRadius(20)
                                .setPadding(30)
                                .setTextSize(16)
                                .setGravity(Gravity.CENTER)
                                .show();

                    }
                }
                //notify = true ;

                etTextSend.setText("");

            }
        });
    }


    public static  void sendMessage(final String sender, final String receiver, String message){
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("sender" , sender);
        hashMap.put("receiver", receiver);
        hashMap.put("created_date" , Utility.getDate(System.currentTimeMillis())+"");
        hashMap.put("message", message);
        hashMap.put("id_plateform", SharedPreferencesUserFactory.retrieveUserData().getId() +"");
        hashMap.put("seen" , "0");
        reference.child("Chats").push().setValue(hashMap);


    }
    private void readMessage(final String myId , final String userId ){
        loader.setVisibility(View.VISIBLE);
        rvChat.setVisibility(View.GONE);
        chats = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();
                loader.setVisibility(View.GONE);
                rvChat.setVisibility(View.VISIBLE);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.e(" snapshot value !!!" , snapshot.child("created_date")+ " !");
                    Log.e("snapshot !!!", snapshot.toString() + " !");
                    Chat chat = snapshot.getValue(Chat.class);

                    if(chat.getReceiver().equals(userId) && chat.getSender().equals(myId) ||
                            chat.getReceiver().equals(myId) && chat.getSender().equals(userId)){
                        chats.add(chat);
                        Log.e("toString() chat object ", chat.toString()+ " !");
                        Log.e("test created_date value", chat.getCreated_date()+ " !");
                    }

                }
                if( chats.size() == 0){

                    Cue.init()
                            .with(getContext())
                            .setMessage(MEConstants.EMPTY_MESSAGES)
                            .setType(Type.CUSTOM)
                            .setDuration(Duration.LONG)
                            .setBorderWidth(5)
                            .setCornerRadius(10)
                            .setCustomFontColor(Color.parseColor("#ca994c"),
                                    Color.parseColor("#ffffff"),
                                    Color.parseColor("#b6843d"))
                            .setFontFace("fonts/nunitosansregular.ttf")
                            .setBorderWidth(1)
                            .setCornerRadius(20)
                            .setPadding(30)
                            .setTextSize(16)
                            .setGravity(Gravity.CENTER)
                            .show();
                } else {
                    messageAdapter = new MessageAdapter(getContext() , chats);
                    rvChat.setAdapter(messageAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Cue.init()
                        .with(getContext())
                        .setMessage(MEConstants.TECH_ERROR)
                        .setType(Type.CUSTOM)
                        .setDuration(Duration.LONG)
                        .setBorderWidth(5)
                        .setCornerRadius(10)
                        .setCustomFontColor(Color.parseColor("#ca994c"),
                                Color.parseColor("#ffffff"),
                                Color.parseColor("#b6843d"))
                        .setFontFace("fonts/nunitosansregular.ttf")
                        .setBorderWidth(1)
                        .setCornerRadius(20)
                        .setPadding(30)
                        .setTextSize(16)
                        .setGravity(Gravity.CENTER)
                        .show();

            }
        });
    }

}