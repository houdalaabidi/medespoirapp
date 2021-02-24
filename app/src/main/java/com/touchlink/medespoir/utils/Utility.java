package com.touchlink.medespoir.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.format.DateUtils;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.touchlink.medespoir.MVP.models.network.DateReclamation;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.annotation.Nullable;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Utility {

    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 130;
    public static final String[] PERMISSIONS = {
            Manifest.permission.CALL_PHONE
    };

    public static DateReclamation  getCalendarDate(long timeStamp) {

        Date date = new Date(timeStamp);
        SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
        SimpleDateFormat sdfYear= new SimpleDateFormat("yyyy");
        sdfDay.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        sdfMonth.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        sdfYear.setTimeZone(TimeZone.getTimeZone("GMT+1"));

       // sdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        //return sdf.format(date) + "";
        return  new DateReclamation((sdfDay.format(date) + ""),(sdfMonth.format(date) + ""),(sdfYear.format(date) + ""));
    }

    public static String  getCalendarObjectDate(long timeStamp) {

        Date date = new Date(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd , MMMM , yyyy");

        sdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));

        return sdf.format(date) + "";

    }

    public static String  getCalendarObjectDay(long timeStamp) {

        Date date = new Date(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd");

        sdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));

        return sdf.format(date) + "";

    }

    public static String  getCalendarObjectMonth(long timeStamp) {

        Date date = new Date(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("MM");

        sdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));

        return sdf.format(date) + "";

    }

    public static String  getCalendarObjectYear(long timeStamp) {

        Date date = new Date(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

        sdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));

        return sdf.format(date) + "";

    }



    public static String  getHeure(long timeStamp) {

        Date date = new Date(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        return sdf.format(date) + "";

        /*Date date = new Date (timeStamp);
        if (DateUtils.isToday(timeStamp)){
            return DateUtils.getRelativeTimeSpanString(timeStamp)+"";
        }

        if (isYesterday(timeStamp)){
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT-1"));
            return "Hier"+ " à "+ sdf.format(date) +"";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM 'à' hh:mm");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT-1"));
            return sdf.format(date) + "";
        }*/
    }


    public static String  getDate(long timeStamp) {

        Date date = new Date(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM 'à' hh:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        return sdf.format(date) + "";

        /*Date date = new Date (timeStamp);
        if (DateUtils.isToday(timeStamp)){
            return DateUtils.getRelativeTimeSpanString(timeStamp)+"";
        }

        if (isYesterday(timeStamp)){
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT-1"));
            return "Hier"+ " à "+ sdf.format(date) +"";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM 'à' hh:mm");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT-1"));
            return sdf.format(date) + "";
        }*/
    }

    public static boolean isYesterday(long date) {
        Calendar now = Calendar.getInstance();
        Calendar cdate = Calendar.getInstance();
        cdate.setTimeInMillis(date);

        now.add(Calendar.DATE,-1);

        return now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == cdate.get(Calendar.DATE);
    }

    public static Gson getGsonInstance(){
        Gson instance  = null ;

        if (instance == null){
            instance = new Gson() ;
        }
        return  instance ;
    }

    public static RequestBody createPartFormString(String value) {
        return RequestBody.create(MultipartBody.FORM, value);
    }


    public static RequestBody createPartFormData(Object value) {
        //return RequestBody.create(MultipartBody.FORM, value);
        return RequestBody.create(MultipartBody.FORM,(byte[])value);

    }

    public static RequestBody createArrayList(@Nullable MediaType contentType, ArrayList<JsonObject> content) {
        Charset charset = UTF_8;
        if (contentType != null) {
            charset = contentType.charset();
            if (charset == null) {
                charset = UTF_8;
                contentType = MediaType.parse(contentType + "; charset=utf-8");
            }
        }
        byte[] bytes = content.toString().getBytes();
        return RequestBody.create(contentType, bytes);
        }




    public static RequestBody createJsonObject(@Nullable MediaType contentType, JsonObject content) {
        Charset charset = UTF_8;
        if (contentType != null) {
            charset = contentType.charset();
            if (charset == null) {
                charset = UTF_8;
                contentType = MediaType.parse(contentType + "; charset=utf-8");
            }
        }
        byte[] bytes = content.toString().getBytes();

        return RequestBody.create(contentType, bytes);
    }




    public static boolean hasPermissions(Context context, int permission_request, String... permissions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, permissions, permission_request);
                    return false;
                }
            }
            return true;
        } else {
            for (String permission : permissions) {
                if (PermissionChecker.checkSelfPermission(context, permission) != PermissionChecker.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, permissions, permission_request);
                    return false;
                }
            }
            return true;

        }
    }

    public static boolean isAtLeastVersion(int version) {
        return Build.VERSION.SDK_INT >= version;
    }
}
