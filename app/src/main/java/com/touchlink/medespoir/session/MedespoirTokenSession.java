package com.touchlink.medespoir.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.touchlink.medespoir.MedespoirApplication;
import com.touchlink.medespoir.utils.Utility;

import static android.content.Context.MODE_PRIVATE;

public class MedespoirTokenSession {

    private final static String SHARED_PREFERENCES_TOKEN = "local_token";
    private final static String SHARED_PREFERENCES_EMAIL = "email";
    private final static String SHARED_PREFERENCES_PASSWORD = "password";
    private final static String SHARED_PREFERENCES_FCM_TOKEN = "local_fcm_token";

    public static void startSession(String token, Boolean statut_gest, Context context) {

        MedespoirTokenGuest rsLocalStorage = new MedespoirTokenGuest(token, statut_gest);
        saveIntoSharedPreferences(rsLocalStorage , context);
    }
    public static void startSessionFCM(String token, Boolean statut_gest, Context context) {

        MedespoirTokenGuest rsLocalStorage = new MedespoirTokenGuest(token, statut_gest);
        saveIntoSharedPreferencesFCM(rsLocalStorage , context);
    }
    private static void saveIntoSharedPreferencesFCM(MedespoirTokenGuest rsLocalStorage , Context context ) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FCM_TOKEN, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREFERENCES_FCM_TOKEN, new Gson().toJson(rsLocalStorage));
        editor.apply();
    }

    public static void refreshLocalStorage(String token, Boolean statut_gest, Context context) {
        MedespoirTokenGuest rsLocalStorage = new MedespoirTokenGuest(token, statut_gest);
        saveIntoSharedPreferences(rsLocalStorage, context);
    }

    public static void refreshEmailLocalStorage(String email, Boolean statut_gest, Context context) {
        MedespoirEmailGuest rsLocalStorage = new MedespoirEmailGuest(email, statut_gest);
        saveEmailIntoSharedPreferences(rsLocalStorage, context);
    }

    public static void refreshPasswordLocalStorage(String password, Boolean statut_gest, Context context) {
        MedespoirPasswordGuest rsLocalStorage = new MedespoirPasswordGuest(password, statut_gest);
        savePasswordIntoSharedPreferences(rsLocalStorage, context);
    }

    private static void saveIntoSharedPreferences(MedespoirTokenGuest rsLocalStorage , Context context ) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_TOKEN, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREFERENCES_TOKEN, new Gson().toJson(rsLocalStorage));
        editor.apply();
    }

    private static void saveEmailIntoSharedPreferences(MedespoirEmailGuest rsLocalStorage , Context context ) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_EMAIL, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREFERENCES_TOKEN, new Gson().toJson(rsLocalStorage));
        editor.apply();
    }
    private static void savePasswordIntoSharedPreferences(MedespoirPasswordGuest rsLocalStorage , Context context ) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_PASSWORD, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREFERENCES_TOKEN, new Gson().toJson(rsLocalStorage));
        editor.apply();
    }

    public static String getUsergestToken(Context context) {
         if (getLocalStorage(context) != null){
        return  getLocalStorage(context).getToken_geust();
        }
         return "";
    }

    public static String getUsergestEmail(Context context) {
        if (getLocalStorage(context) != null){
        return  getLocalStorageEmail(context).getEmail_geust();
        }
        return "";
    }

    public static String getUsergestPassword(Context context) {
        if (getLocalStorage(context) != null){
        return  getLocalStoragePassword(context).getPassword_geust();
        }
         return "";
    }

    public static boolean getStatutGestConnected(Context context) {
        return getLocalStorage(context).isStatut_geust_conected();
    }


    public static boolean getStatutGestConnectedFCM(Context context) {
        return getLocalStorageFCM(context).isStatut_geust_conected();
    }

    public static MedespoirTokenGuest getLocalStorage(Context context) {


        SharedPreferences sharedPreferences =  context.getSharedPreferences(SHARED_PREFERENCES_TOKEN, MODE_PRIVATE);
        String localStorage = sharedPreferences.getString(SHARED_PREFERENCES_TOKEN, null);
        return Utility.getGsonInstance().fromJson(localStorage, MedespoirTokenGuest.class);
    }

    public static MedespoirEmailGuest getLocalStorageEmail(Context context) {


        SharedPreferences sharedPreferences =  context.getSharedPreferences(SHARED_PREFERENCES_EMAIL, MODE_PRIVATE);
        String localStorage = sharedPreferences.getString(SHARED_PREFERENCES_EMAIL, null);
        return Utility.getGsonInstance().fromJson(localStorage, MedespoirEmailGuest.class);
    }

    public static MedespoirPasswordGuest getLocalStoragePassword(Context context) {


        SharedPreferences sharedPreferences =  context.getSharedPreferences(SHARED_PREFERENCES_PASSWORD, MODE_PRIVATE);
        String localStorage = sharedPreferences.getString(SHARED_PREFERENCES_PASSWORD, null);
        return Utility.getGsonInstance().fromJson(localStorage, MedespoirPasswordGuest.class);
    }
    public static MedespoirTokenGuest getLocalStorageFCM(Context context) {


        SharedPreferences sharedPreferences =  context.getSharedPreferences(SHARED_PREFERENCES_FCM_TOKEN, MODE_PRIVATE);
        String localStorage = sharedPreferences.getString(SHARED_PREFERENCES_FCM_TOKEN, null);
        return Utility.getGsonInstance().fromJson(localStorage, MedespoirTokenGuest.class);
    }

    public static void cancelSession(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_TOKEN, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SHARED_PREFERENCES_TOKEN);
        editor.apply();
    }
}
