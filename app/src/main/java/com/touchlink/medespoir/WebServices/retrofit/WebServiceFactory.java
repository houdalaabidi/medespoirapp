package com.touchlink.medespoir.WebServices.retrofit;

import com.touchlink.medespoir.utils.MEConstants;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebServiceFactory {

    private static WebServiceFactory instance;
    static OnConnectionTimeoutListener onConnectionTimeOutListener;
    private API api;

    public static Response onOnIntercept(Interceptor.Chain chain) throws IOException {
        try {
            Response response = chain.proceed(chain.request());
            String content = response.body().toString();
            return response.newBuilder().body(ResponseBody.create(response.body().contentType(), content)).build();

        } catch (SocketTimeoutException exception) {
            exception.printStackTrace();
            if (onConnectionTimeOutListener != null)
                onConnectionTimeOutListener.onConnectionTimeout();

        }
        return chain.proceed(chain.request());
    }

    public interface OnConnectionTimeoutListener {
        void onConnectionTimeout();
    }



    public WebServiceFactory(){
        // code without intercept
       /* OkHttpClient client = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder().client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Urls.MAIN_URL)
                .build();*/
        OkHttpClient client = new OkHttpClient();

        client.newBuilder().connectTimeout(MEConstants.TIMEOUT, TimeUnit.SECONDS);
        client.newBuilder().readTimeout(MEConstants.TIMEOUT, TimeUnit.SECONDS);
        client.newBuilder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                return onOnIntercept(chain);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.MAIN_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(API.class);
    }
    public static WebServiceFactory getInstance(){
        if (instance == null)
            instance = new WebServiceFactory();
        return instance ;

    }

    public API getApi(){
        return api ;
    }
}
