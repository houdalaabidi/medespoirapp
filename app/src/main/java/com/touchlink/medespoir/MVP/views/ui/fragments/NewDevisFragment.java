package com.touchlink.medespoir.MVP.views.ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Header;
import retrofit2.http.Part;
import ru.slybeaver.slycalendarview.SlyCalendarDialog;

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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tiper.MaterialSpinner;
import com.touchlink.medespoir.MVP.models.network.MedespoirResponse;
import com.touchlink.medespoir.MVP.models.network.Sousintervention;
import com.touchlink.medespoir.MVP.views.ui.activities.MainActivity;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirETRegular;
import com.touchlink.medespoir.MVP.views.ui.customviews.MedEspoirTVRegular;
import com.touchlink.medespoir.MVP.views.ui.dialogfragments.ConnexionDialogFragment;
import com.touchlink.medespoir.MVP.views.ui.dialogfragments.OperationsFragment;
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
import org.json.JSONArray;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class NewDevisFragment extends Fragment  implements View.OnClickListener, DatePickerDialog.OnDateSetListener{


    public static final int DIALOG_FRAGMENT = 2;
    public static final int RESULT_POPUP_OK = 101;

    ArrayList<Uri> uris = new ArrayList<>();
    @SuppressLint("id")
    @BindView(R.id.add_files)
    LinearLayout addFilesButton;
    @SuppressLint("id")
    @BindView(R.id.et_files_names)
    MedEspoirETRegular etFilesNames;
    @SuppressLint("id")
    @BindView(R.id.calendar)
    LinearLayout calendar ;
    @SuppressLint("id")
    @BindView(R.id.et_date_devis)
    MedEspoirETRegular etDate ;
    @SuppressLint("id")
    @BindView(R.id.heure_devis)
   MaterialSpinner etHeure;
    private static int  i = 0 ;
    private static ArrayList<MultipartBody.Part> parts = new ArrayList<MultipartBody.Part>();
    ArrayAdapter<String> arrayHeuresAdapter;
    String arrayListHeures[] = {"08:00H", "09:00H", "10:00H", "11:00H", "12:00H", "13:00H", "14:00H", "15:00H", "16:00H", "17:00H", "18:00H", "19:00H"};
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int ALL_FILE_REQUEST = 102;
    int method = 0;
    Uri all_file_path =null;
    MultipartBody.Part part= null;
    @SuppressLint("id")
    @BindView(R.id.send_devis)
    LinearLayout sendDevis ;
    @SuppressLint("id")
    @BindView(R.id.material_spinner_operations)
    LinearLayout operationsSpinner;
    @SuppressLint("id")
    @BindView(R.id.et_text_devis)
    TextInputEditText etText ;
    @SuppressLint("id")
    @BindView(R.id.fab_new_devis)
    ImageView fab ;
    ArrayList<Integer> listOfSousInterventionsIds = new ArrayList<Integer>();
    ArrayList<String> listOfSousInterventionsLabels = new ArrayList<String>();
    private Call<MedespoirResponse> callSendDevis ;
    private String date =  null  ;
    @SuppressLint("id")
    @BindView(R.id.tv_operations)
    MedEspoirTVRegular tvOperations ;
    JsonObject datedebutObject = new JsonObject();
    JsonObject datefinObject = new JsonObject();



    public NewDevisFragment() {
        // Required empty public constructor
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ALL_FILE_REQUEST) {
                if (data == null) {
                    return;
                }

                Uri uri = data.getData();
                String paths = FilePath.getFilePath(getContext(), uri);
                // Log.d("File Path : ", "" + paths);
                if (paths != null) {
                    etFilesNames.setText(etFilesNames.getText().toString() + new File(paths).getName());
                }
                all_file_path =  data.getData();


                parts.add(prepareFilePart("image[]", all_file_path));




            }
        }else if (resultCode == RESULT_POPUP_OK) {
            listOfSousInterventionsLabels = data.getStringArrayListExtra(MEConstants.OPERATIONS_LIST_LAEBLS);
            listOfSousInterventionsIds = data.getIntegerArrayListExtra(MEConstants.OPERATIONS_LIST_IDS);



                if (listOfSousInterventionsLabels.size() == 0){
                    tvOperations.setText(" Pas d'opérations choisis");
                    listOfSousInterventionsLabels.clear();
                    listOfSousInterventionsIds.clear();
                } else if ( listOfSousInterventionsLabels.size() == 1){
                    tvOperations.setText(listOfSousInterventionsLabels.get(0));
                } else if (listOfSousInterventionsLabels.size() == 2){
                    tvOperations.setText(listOfSousInterventionsLabels.get(0)+ " "+ listOfSousInterventionsLabels.get(1));
                } else {
                    tvOperations.setText(listOfSousInterventionsLabels.get(0)+ " "+ listOfSousInterventionsLabels.get(1)+ " ...");

                }



        }
    }

    /*private void addImagesList(Business business, ArrayList<ImageGalerie> img) {
        if (img != null)
            if (img.size() != 0) {
                final MultipartBody.Part[] parts = new MultipartBody.Part[img.size()];
                for (int i = 0; i < img.size(); i++) {

                    File file = new File(Uri.parse(img.get(i).getImage()).getPath());
                    RequestBody reqFileBody = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part imageToupload =
                            MultipartBody.Part.createFormData("image", file.getName(), reqFileBody);
                    parts[i] = imageToupload;
                }*/

    private void filePicker(int i) {
        if (i == 0) {
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Choose File to Upload"), ALL_FILE_REQUEST);
        }

        /*if (i == 1) {
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST);
        }
        if (i == 2) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(intent, GALLERY_REQUEST);
        }*/

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_devis, container, false);
        init(view);
        return view ;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onResume() {
        super.onResume();

    }



    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth +" / "+(monthOfYear+1) + " / " + year;

        etDate.setText(date);

    }

    public MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        File file = FileUtils.getFile(getContext(), fileUri);
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContext().getContentResolver().getType(fileUri)), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }



    private void init(View view) {
        ButterKnife.bind(this, view);
        arrayHeuresAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arrayListHeures);
        etHeure.setAdapter(arrayHeuresAdapter);
        etHeure.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageFragment messageFragment = new MessageFragment();
                MainActivity.goToFragment(messageFragment , true);
            }
        });
        operationsSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OperationsFragment operationsFragment = OperationsFragment.newInstance();
                operationsFragment.setTargetFragment(NewDevisFragment.this , RESULT_POPUP_OK);
                operationsFragment.show(getFragmentManager() , "OperationsFragments");
            }
        });
        addFilesButton.setOnClickListener(this);
        // TODO change button sendDevis

        sendDevis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewDevisDetailsFragment newDevisDetailsFragment = new NewDevisDetailsFragment();
                onDestroy();
                MainActivity.goToFragment(newDevisDetailsFragment , true);

            }
        });



        SlyCalendarDialog.Callback callback = new SlyCalendarDialog.Callback() {
            @Override
            public void onCancelled() {
                etDate.setText("");

            }

            @Override
            public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {
                if (firstDate != null) {
                    if (secondDate == null){
                        etDate.setText(Utility.getCalendarObjectDate(firstDate.getTimeInMillis()));
                        datedebutObject.addProperty("year", Utility.getCalendarObjectYear(firstDate.getTimeInMillis()));
                        datedebutObject.addProperty("month", Utility.getCalendarObjectMonth(firstDate.getTimeInMillis()));
                        datedebutObject.addProperty("day", Utility.getCalendarObjectDay(firstDate.getTimeInMillis()));


                        datefinObject.addProperty("year","");
                        datefinObject.addProperty("month", "");
                        datefinObject.addProperty("day","");


                    }else {
                        etDate.setText("de " + Utility.getCalendarObjectDate(firstDate.getTimeInMillis()) + " à " +Utility.getCalendarObjectDate(secondDate.getTimeInMillis()));
                        datedebutObject.addProperty("year", Utility.getCalendarObjectYear(firstDate.getTimeInMillis()));
                        datedebutObject.addProperty("month", Utility.getCalendarObjectMonth(firstDate.getTimeInMillis()));
                        datedebutObject.addProperty("day", Utility.getCalendarObjectDay(firstDate.getTimeInMillis()));



                        datefinObject.addProperty("year", Utility.getCalendarObjectYear(secondDate.getTimeInMillis()));
                        datefinObject.addProperty("month",  Utility.getCalendarObjectMonth(secondDate.getTimeInMillis()));
                        datefinObject.addProperty("day", Utility.getCalendarObjectDay(secondDate.getTimeInMillis()));

                    }


                } else {
                    etDate.setText("");
                    etDate.setText(Utility.getCalendarObjectDate(firstDate.getTimeInMillis()));
                    datedebutObject.addProperty("year", "");
                    datedebutObject.addProperty("month", "");
                    datedebutObject.addProperty("day", "");


                    datefinObject.addProperty("year","");
                    datefinObject.addProperty("month", "");
                    datefinObject.addProperty("day","");
                }


            }
        };


        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               SlyCalendarDialog slyCalendarDialog =  new SlyCalendarDialog()
                        .setSingle(false)
                        .setCallback(callback);

               slyCalendarDialog.setBackgroundColor(ContextCompat.getColor(getContext() , R.color.design_default_color_background));
               slyCalendarDialog.setSelectedTextColor(ContextCompat.getColor(getContext() ,R.color.design_default_color_background));
               slyCalendarDialog.setSelectedColor(ContextCompat.getColor(getContext() ,R.color.colorGold));
               slyCalendarDialog.setHeaderColor(ContextCompat.getColor(getContext() ,R.color.colorGold));
               slyCalendarDialog.setHeaderTextColor(ContextCompat.getColor(getContext() , R.color.design_default_color_background));
               slyCalendarDialog.setTextColor(ContextCompat.getColor(getContext() ,R.color.colorBlack));
               slyCalendarDialog.show(getFragmentManager() , MEConstants.TAG_CALENDAR);
            }
        });




      sendDevis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DeviceConnectivity.isNetworkAvailable(getContext())) {
                    if ((tvOperations.getText().toString()+ "").equalsIgnoreCase("")){

                        Cue.init()
                                .with(getContext())
                                .setMessage(MEConstants.EMPTY_OPERATION)
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
                    }  else if ((etText.getText()+ "").equalsIgnoreCase("")) {
                        Cue.init()
                                .with(getContext())
                                .setMessage(MEConstants.EMPTY_TEXT_DEVIS)
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
                    } else if((etHeure.getEditText().getText()+"").equalsIgnoreCase("")){
                        Cue.init()
                                .with(getContext())
                                .setMessage(MEConstants.EMPTY_HEURE)
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



                    }else if(etDate.getText().toString().equalsIgnoreCase("")) {
                        Cue.init()
                                .with(getContext())
                                .setMessage(MEConstants.EMPTY_DATE)
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
                    else {




                        ArrayList<JsonObject>  listOfJsonObjects = new ArrayList<JsonObject>();
                        if (listOfSousInterventionsIds.size() != 0){
                           for(int i = 0 ; i < listOfSousInterventionsIds.size() ; i++) {
                                JsonObject postParams = new JsonObject();

                                postParams.addProperty("id_operation", (listOfSousInterventionsIds.get(i) + ""));

                               listOfJsonObjects.add(postParams);
                            }






                        } else {

                        }


                        callSendDevis = WebServiceFactory.getInstance().getApi().sendDevis(
                                "Bearer "+ MedespoirTokenSession.getUsergestToken(getContext()),
                                parts,
                                Utility.createArrayList(MultipartBody.FORM , listOfJsonObjects),
                                Utility.createJsonObject(MultipartBody.FORM ,datedebutObject),
                                Utility.createJsonObject(MultipartBody.FORM ,datefinObject),
                                Utility.createPartFormString(etText.getText().toString()+""),
                                Utility.createPartFormString(etHeure.getSelectedItem().toString()+"")



                        );


                        callSendDevis.enqueue(new Callback<MedespoirResponse>() {
                            @Override
                            public void onResponse(Call<MedespoirResponse> call, Response<MedespoirResponse> response) {

                                if (response != null){
                                    if (response.body() != null){
                                        Log.i(MEConstants.TAG , "  demande devis response "+ response.body().getStatus() +response.body().toString().toString());
                                        if(response.body().getStatus() == 1){

                                            OperationsFragment.listOfSousInterventionsIds.clear();
                                            OperationsFragment.listOfSousInterventionsLabels.clear();

                                            Log.i(MEConstants.TAG , " demande devis success");
                                            Cue.init()
                                                    .with(getContext())
                                                    .setMessage(MEConstants.SUCCESS_DEVIS)
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

                                            DevisFragment devisFragment = new DevisFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putBoolean("devis", true);
                                            devisFragment.setArguments(bundle);
                                            onDestroy();
                                            MainActivity.goToFragment(devisFragment , true);

                                        } else {
                                            Log.i(MEConstants.TAG , " demande devis failure status != 1 statut = "+ response.body().getStatus()+" body = " +response.body().toString());

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
                                        Log.i(MEConstants.TAG , " demande devis response body null (failure ) ");
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
                                    Log.i(MEConstants.TAG , " demande devis response  null (failure ) ");
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
                                t.printStackTrace();

                                Log.i(MEConstants.TAG , "  demande devis failure  "+"on failure"+ t.getMessage()+ call.toString() );

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
    public void onDestroyView() {
        super.onDestroyView();
        i = 0 ;
    }
}