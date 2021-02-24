package com.touchlink.medespoir.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.touchlink.medespoir.MVP.models.network.User;
import com.touchlink.medespoir.utils.MEConstants;
import com.touchlink.medespoir.utils.Utility;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferencesUserFactory {

    public static SharedPreferences pref ;
    public static SharedPreferences.Editor editor;
    private static Gson gson = Utility.getGsonInstance() ;


    public static void initializedPreferences(Context context){
        pref = context.getSharedPreferences("MyPref" , 0) ;
        editor = pref.edit() ;
    }


    public static void storeUserSession(User user){

        String json = gson.toJson(user);
        editor.putString("user_session" , json);
        editor.apply();

    }

    public static User retrieveUserData(){
        String json = pref.getString("user_session" , null);
        if (json != null) {
            User user = gson.fromJson(json, User.class);
            return user;
        } else {
            return null ;
        }
    }

    public static void cancelUserData(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user_session");
        editor.apply();

    }

    public static void storeUserSessionSettings(boolean isSessionOpened){

        editor.putBoolean(MEConstants.USER_SESSION_PREFERENCES, isSessionOpened);
        editor.apply();

    }


    public static void cancelUserSessionSettings(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences(MEConstants.USER_SESSION_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(MEConstants.USER_SESSION_PREFERENCES);
        editor.apply();

    }
    public static void storeNotificationsSettings(boolean isEnabled){

        editor.putBoolean(MEConstants.NOTIFICATION_PREFERENCES, isEnabled);
        editor.apply();

    }

    public static boolean getNotificationsSettings(){

        return pref.getBoolean(MEConstants.NOTIFICATION_PREFERENCES , false);
    }

    public static void cancelNotificationsSettings(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences(MEConstants.NOTIFICATION_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(MEConstants.NOTIFICATION_PREFERENCES);
        editor.apply();

    }
    public static String retrieveUPW(){
        return pref.getString(MEConstants.USER_PW , null);

    }

    public static void storeUPW(String  password){

        editor.putString(MEConstants.USER_PW, password);
        editor.apply();

    }

    public static boolean retrieveUserSessionSettings(){
        return pref.getBoolean(MEConstants.USER_SESSION_PREFERENCES , false);

    }


    public static void storeToken(String  token){

        editor.putString(MEConstants.USER_TOKEN, token);
        editor.apply();

    }

    public static String retrieveUserToken(){
        return pref.getString(MEConstants.USER_TOKEN , null);

    }

    public static void storeEmail(String  email){

        editor.putString(MEConstants.USER_EMAIL, email);
        editor.apply();

    }

    public static String retrieveUseEmail(){
        return pref.getString(MEConstants.USER_EMAIL , null);

    }

    public static void cancelEmail(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MEConstants.USER_EMAIL, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(MEConstants.USER_EMAIL);
        editor.apply();
    }


    public static void cancelToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MEConstants.USER_TOKEN, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(MEConstants.USER_TOKEN);
        editor.apply();
    }

    public static void cancelPassword(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MEConstants.USER_PW, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(MEConstants.USER_PW);
        editor.apply();
    }

}
