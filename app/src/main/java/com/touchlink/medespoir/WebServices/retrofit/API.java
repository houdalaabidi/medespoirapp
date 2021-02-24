package com.touchlink.medespoir.WebServices.retrofit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.touchlink.medespoir.MVP.models.network.MedespoirResponse;
import com.touchlink.medespoir.utils.MEConstants;

import org.json.JSONArray;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface API {

    @Headers({
           "Accept:application/json"
     })

    // send message
   // @POST("message/sendMessageFromUser")
    //Call<TouchlinkResponse> sendMessage(@Header(RSConstants.HEADER_TOKEN) String token, @Body MessageItem messageItem);


    // load conversation
    // @FormUrlEncoded
    // @POST("message/loadConversation")
   // Call<TouchlinkResponse>  loadConversation(@Header(RSConstants.HEADER_TOKEN) String token,@Field("idDevice") String idDevice);

    // inscription
    @POST("register")
    Call<ResponseBody>   inscriptionUser( @Body JsonObject requestObj);

    @POST("connection")
    Call<MedespoirResponse>   ConnexionUser( @Body JsonObject requestObj);


    @Multipart
    @POST("api/user/reclamation")
    Call<MedespoirResponse> sendReclamation(
            @Header("Authorization") String token,
            @Part  ArrayList<MultipartBody.Part> parts,
            @Part("date") RequestBody date,
            @Part("title") RequestBody title,
            @Part("priorite") RequestBody priorite,
            @Part("description") RequestBody description

    );

    @GET("api/getall/reclamation")
    Call<MedespoirResponse>   getAllReclamation(  @Header("Authorization") String token);

    @POST("api/localisation")
    Call<MedespoirResponse>   sendDeviceLocation(  @Header("Authorization") String token,  @Body JsonObject requestObj);
    //
    // @Part("devis") ArrayList<JsonObject> devis,
    @Multipart
    @POST("api/user/devis")
    Call<MedespoirResponse> sendDevis(
            @Header("Authorization") String token,
            @Part  ArrayList<MultipartBody.Part>  parts,
            @Part("devis") RequestBody devis,
            @Part("datedebut") RequestBody datedebut,
            @Part("datefin") RequestBody datefin,
            @Part("description") RequestBody description,
            @Part("heure") RequestBody heure);
    @GET("api/get/actualites")
    Call<MedespoirResponse>   getListArticles(  @Header("Authorization") String token);


    @GET("api/affichage/promotion")
    Call<MedespoirResponse>   getListPromotions(  @Header("Authorization") String token);

    @POST("api/get/programme")
    Call<MedespoirResponse>   getDailyProgram(  @Header("Authorization") String token,  @Body JsonObject requestObj);

    @GET("api/affichage/actualites")
    Call<MedespoirResponse>   getListArticlesPerCategories(  @Header("Authorization") String token);


    @POST("api/get/sousprogramme")
    Call<MedespoirResponse>   getDailyProgramDetails(  @Header("Authorization") String token,  @Body JsonObject requestObj);


    @POST("api/save/token")
    Call<MedespoirResponse>   sendFirebaseToken(  @Header("Authorization") String token,  @Body JsonObject requestObj);

    @GET("api/get/compterendu")
    Call<MedespoirResponse>   getCompteRendu( @Header("Authorization") String token);


    @GET("api/get/operations")
    Call<MedespoirResponse>   getOperations( @Header("Authorization") String token);


    @GET("api/get/alldevis")
    Call<MedespoirResponse>   getAllDevis( @Header("Authorization") String token);


    @POST("api/get/onedevis")
    Call<MedespoirResponse>   getOneDevis( @Header("Authorization") String token, @Body JsonObject  requestObj);


    @POST("api/set/statut")
    Call<MedespoirResponse>   setStatus( @Header("Authorization") String token, @Body JsonObject  requestObj);


    @POST("api/edit/user")
    Call<MedespoirResponse>   editProfil( @Header("Authorization") String token, @Body JsonObject  requestObj);


    @POST("api/find/reclamationid")
    Call<MedespoirResponse>   getReclamationById( @Header("Authorization") String token, @Body JsonObject  requestObj);


    @POST("api/admin/get/title")
    Call<MedespoirResponse>   getReclamationTitles( @Header("Authorization") String token);


    @POST("api/admin/get/priorite")
    Call<MedespoirResponse>   getReclamationPriorities( @Header("Authorization") String token);

}