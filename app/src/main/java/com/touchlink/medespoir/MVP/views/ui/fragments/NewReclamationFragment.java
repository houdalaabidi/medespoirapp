package com.touchlink.medespoir.MVP.views.ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fxn.cue.Cue;
import com.fxn.cue.enums.Duration;
import com.fxn.cue.enums.Type;
import com.google.android.material.textfield.TextInputEditText;
import com.tiper.MaterialSpinner;
import com.touchlink.medespoir.MVP.models.network.MedespoirResponse;
import com.touchlink.medespoir.MVP.views.ui.activities.MainActivity;
import com.touchlink.medespoir.MVP.views.ui.activities.MessageActivity;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirETRegular;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVRegular;
import com.touchlink.medespoir.MVP.views.ui.dialogfragments.ConnexionDialogFragment;
import com.touchlink.medespoir.MVP.views.ui.dialogfragments.OperationsFragment;
import com.touchlink.medespoir.MVP.views.ui.dialogfragments.TitlesFragment;
import com.touchlink.medespoir.R;
import com.touchlink.medespoir.WebServices.retrofit.WebServiceFactory;
import com.touchlink.medespoir.session.MedespoirTokenSession;
import com.touchlink.medespoir.utils.DeviceConnectivity;
import com.touchlink.medespoir.utils.FilePath;
import com.touchlink.medespoir.utils.FileUtils;
import com.touchlink.medespoir.utils.MEConstants;
import com.touchlink.medespoir.utils.Utility;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewReclamationFragment extends Fragment implements View.OnClickListener {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int ALL_FILE_REQUEST = 102;
    int method = 0;

    private static int  i = 0 ;

    String all_file_path;
    @SuppressLint("id")
    @BindView(R.id.material_spinner_titles_reclamtion)
    LinearLayout titlesSpinner ;
    @SuppressLint("id")
    @BindView(R.id.material_spinner_priorities_reclamtion)
    LinearLayout prioritiesSpinner;
    @SuppressLint("id")
    @BindView(R.id.add_files)
    LinearLayout addFilesButton;
    @SuppressLint("id")
    @BindView(R.id.et_files_names)
    MedEspoirETRegular etFilesNames;
    @SuppressLint("id")
    @BindView(R.id.et_text_reclamation)
    TextInputEditText etText ;
    @SuppressLint("id")
    @BindView(R.id.send_reclamation)
    LinearLayout sendReclamation;

    public static final int RESULT_POPUP_OK = 109;
    public static final int RESULT_POPUP_OK_PRIORITIES = 119;
    static String prioriteLabel ;
    static int titleID = -1;
    static String titleLabel = null ;
    MultipartBody.Part part= null;
    @SuppressLint("id")
    @BindView(R.id.fab_new_reclamation)
    ImageView fab ;
    @SuppressLint("id")
    @BindView(R.id.tv_title_reclmation)
    MedEspoirTVRegular titleReclamation ;
    @SuppressLint("id")
    @BindView(R.id.tv_priorite_reclmation)
    MedEspoirTVRegular prioriteReclamation;
    private static ArrayList<MultipartBody.Part> parts = new ArrayList<MultipartBody.Part>();
    private Call<MedespoirResponse> callSendReclamation ;

    public NewReclamationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_reclamation, container, false);
        init(view);
        return view;
    }



    private void init(View view) {
        ButterKnife.bind(this, view);

        addFilesButton.setOnClickListener(this);
        titlesSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TitlesFragment titlesFragment = TitlesFragment.newInstance();
                titlesFragment.setTargetFragment(NewReclamationFragment.this , RESULT_POPUP_OK);
                titlesFragment.show(getFragmentManager() , "TitlesFragment");
            }
        });

        prioritiesSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrioritiesFragment prioritiesFragment = PrioritiesFragment.newInstance();
                prioritiesFragment.setTargetFragment(NewReclamationFragment.this , RESULT_POPUP_OK_PRIORITIES);

                prioritiesFragment.show(getFragmentManager() , "PrioritiesFragment");
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , MessageActivity.class);
                startActivity(intent);
            }
        });












        sendReclamation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DeviceConnectivity.isNetworkAvailable(getContext())) {
                if (titleLabel == null || titleLabel ==  ""){
                    Cue.init()
                            .with(getContext())
                            .setMessage(MEConstants.EMPTY_TITLE)
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

                } else  if (prioriteLabel == null || prioriteLabel == ""){
                    Cue.init()
                            .with(getContext())
                            .setMessage(MEConstants.EMPTY_PRIORITY)
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

                } else if ((etText.getText()+ "").equalsIgnoreCase("")) {
                    Cue.init()
                            .with(getContext())
                            .setMessage(MEConstants.EMPTY_TEXT)
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
                    // web service

                    callSendReclamation = WebServiceFactory.getInstance().getApi().sendReclamation(
                            "Bearer "+ MedespoirTokenSession.getUsergestToken(getContext()),
                            parts,
                            Utility.createPartFormString( ""),
                            Utility.createPartFormString(titleLabel),
                            Utility.createPartFormString(prioriteLabel),
                            Utility.createPartFormString(etText.getText() + "")

                            );



                   callSendReclamation.enqueue(new Callback<MedespoirResponse>() {
                        @Override
                        public void onResponse(Call<MedespoirResponse> call, Response<MedespoirResponse> response) {

                            if (response != null){
                                if (response.body() != null){
                                    if(response.body().getStatus() == 1){

                                        Cue.init()
                                                .with(getContext())
                                                .setMessage(MEConstants.SUCCESS_RECLAMATION)
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



                                        ReclamationFragment reclamationFragment = new ReclamationFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putBoolean("reclamation", true);
                                        reclamationFragment.setArguments(bundle);

                                        onDestroy();
                                        MainActivity.goToFragment(reclamationFragment , true);


                                    } else {

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

                                } else {
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

                            } else {
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



                        }

                        @Override
                        public void onFailure(Call<MedespoirResponse> call, Throwable t) {

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




            } else {
                    ConnexionDialogFragment connectivityFragment = ConnexionDialogFragment.newInstance();
                    connectivityFragment.show(getFragmentManager(), "ConnexionDialogFragment");
                }

        }

    });
}

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_files) {
            method = 0;
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    filePicker(0);
                } else {
                    requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            } else {
                filePicker(0);
            }

        }

    }


    private void filePicker(int i) {
        if (i == 0) {
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Choose File to Upload"), ALL_FILE_REQUEST);
        }


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ALL_FILE_REQUEST) {
                if (data == null) {
                    return;
                }

                Uri uri = data.getData();
                String paths = FilePath.getFilePath(getContext(), uri);
                Log.d("File Path : ", "" + paths);
                if (paths != null) {
                     etFilesNames.setText( etFilesNames.getText() + new File(paths).getName());
                }
                all_file_path = paths;
                part = prepareFilePart("image[]", data.getData());
                parts.add(part);

            }
        } else if(resultCode == RESULT_POPUP_OK){
            titleID = data.getIntExtra(MEConstants.TITLE_ID , -1);
            titleLabel =  data.getStringExtra(MEConstants.TITLE_LABEL);
            if (titleLabel != null && titleLabel != ""){
                titleReclamation.setText(titleLabel);
            }


        } else if(resultCode == RESULT_POPUP_OK_PRIORITIES){

            prioriteLabel =  data.getStringExtra(MEConstants.PRIORITE_LABEL);
            if (prioriteLabel != null && prioriteLabel != ""){
                prioriteReclamation.setText(prioriteLabel);
            }
        }
    }


        public MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
            File file = FileUtils.getFile(getContext(), fileUri);
            RequestBody requestFile = RequestBody.create(MediaType.parse(getContext().getContentResolver().getType(fileUri)), file);
            return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
        }





    private boolean checkPermission(String permission) {
        int result = ContextCompat.checkSelfPermission(getContext(), permission);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission(String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getContext(), permission)) {
            //Toast.makeText(AdvanceFileUpload.this, "Please Allow Permission", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{permission}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(AdvanceFileUpload.this, "Permission Successfull", Toast.LENGTH_SHORT).show();
                    filePicker(method);
                } else {
                    //Toast.makeText(AdvanceFileUpload.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }




}