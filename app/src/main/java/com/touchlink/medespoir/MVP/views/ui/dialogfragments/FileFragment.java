package com.touchlink.medespoir.MVP.views.ui.dialogfragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.fxn.cue.Cue;
import com.fxn.cue.enums.Type;
import com.google.gson.Gson;
import com.touchlink.medespoir.MVP.models.network.MedespoirResponse;
import com.touchlink.medespoir.MVP.models.network.Title;
import com.touchlink.medespoir.MVP.models.network.Titre;
import com.touchlink.medespoir.MVP.views.adapters.TitlesAdapter;
import com.touchlink.medespoir.MVP.views.ui.callbacks.TitleListener;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVBold;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.WebServices.retrofit.Urls;
import com.touchlink.medespoir.WebServices.retrofit.WebServiceFactory;
import com.touchlink.medespoir.session.MedespoirSession;
import com.touchlink.medespoir.session.MedespoirTokenSession;
import com.touchlink.medespoir.utils.DeviceConnectivity;
import com.touchlink.medespoir.utils.MEConstants;
import com.touchlink.medespoir.utils.Utility;

import java.util.ArrayList;




public class FileFragment extends DialogFragment {

    private View rootView ;
    private static String filePath = null;
    @SuppressLint("id")
    @BindView(R.id.file_details)
    ImageView fileDetails ;



    public FileFragment() {
        // Required empty public constructor
    }

    public static FileFragment newInstance(Bundle bundle) {
        FileFragment fragment = new FileFragment();
        filePath = bundle.getString(MEConstants.FILE_PATH);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        initViews();
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(rootView).setCancelable(true).create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setCancelable(true);
        //alertDialog.setOnShowListener(dialog -> onDialogShow(alertDialog));
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                alertDialog.show();
            }
        });
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    alertDialog.cancel();
                    dismiss();
                }
                return false;
            }
        });
        return alertDialog;
    }

    private void initViews(){

        rootView = View.inflate(getContext(), R.layout.fragment_file, null);

       // rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_file, null, false);
         ButterKnife.bind(this, rootView);
        Glide.with(getContext())
                .load(Urls.MAIN_URL +filePath)
                .centerCrop()
                .placeholder(R.drawable.image_not_found)
                .error(R.drawable.image_not_found)
                .into(fileDetails);



    }


}