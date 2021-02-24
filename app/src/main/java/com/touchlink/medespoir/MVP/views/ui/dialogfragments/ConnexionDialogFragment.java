package com.touchlink.medespoir.MVP.views.ui.dialogfragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchlink.medespoir.R;

public class ConnexionDialogFragment extends DialogFragment {

    private View rootView;
    private Unbinder unbinder;

    public ConnexionDialogFragment() {
        // Required empty public constructor
    }


    public static ConnexionDialogFragment newInstance() {
        ConnexionDialogFragment fragment = new ConnexionDialogFragment();

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        initViews();
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(rootView).setCancelable(false).create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setCancelable(false);
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
                }
                return false;
            }
        });
        return alertDialog;
    }

    private void initViews() {
        rootView = View.inflate(getContext(), R.layout.fragment_connexion_dialog, null);
        //rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_connexion_dialog, null, false);
        unbinder = ButterKnife.bind(this, rootView);

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_connexion_dialog, container, false);
    }
}