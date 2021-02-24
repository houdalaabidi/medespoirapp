package com.touchlink.medespoir.session;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;

import com.fxn.cue.Cue;
import com.fxn.cue.enums.Type;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.touchlink.medespoir.MVP.models.network.MedespoirResponse;
import com.touchlink.medespoir.MVP.models.network.Token;
import com.touchlink.medespoir.MVP.models.network.User;
import com.touchlink.medespoir.MVP.views.ui.activities.MainActivity;
import com.touchlink.medespoir.WebServices.retrofit.WebServiceFactory;
import com.touchlink.medespoir.utils.MEConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedespoirSession {

    public static void Reconnecter(Context context) {

        final JsonObject postParams = new JsonObject();

        postParams.addProperty("password",  SharedPreferencesUserFactory.retrieveUPW());
        postParams.addProperty("email", SharedPreferencesUserFactory.retrieveUseEmail());

        Call<MedespoirResponse> call = WebServiceFactory.getInstance().getApi().ConnexionUser(postParams);
        call.enqueue(new Callback<MedespoirResponse>() {
            @Override
            public void onResponse(Call<MedespoirResponse> call, Response<MedespoirResponse> response) {
                Object  data = null;


                if(response.body().getStatus() == 1){
                    data = response.body().getData();
                    User user = new Gson().fromJson(new Gson().toJson(data), User.class);
                    String token = user.getToken();
                    Intent intent = new Intent(context, MainActivity.class);
                    MedespoirTokenSession.refreshLocalStorage(token, true, context);
                    SharedPreferencesUserFactory.storeUserSession(user);
                    SharedPreferencesUserFactory.storeToken(token);
                    SharedPreferencesUserFactory.storeEmail(user.getEmail());
                    // MedespoirTokenSession.refreshEmailLocalStorage(user.getEmail(),true , context);
                   // MedespoirTokenSession.refreshPasswordLocalStorage(MedespoirTokenSession.getUsergestPassword(context),true , context);


                }


            }

            @Override
            public void onFailure(Call<MedespoirResponse> call, Throwable t) {



            }
        });
    }
}
