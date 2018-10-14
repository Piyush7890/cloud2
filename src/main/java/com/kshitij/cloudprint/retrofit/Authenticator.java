package com.kshitij.cloudprint.retrofit;

import com.kshitij.cloudprint.model.Constants;
import com.kshitij.cloudprint.model.Token;
import com.kshitij.cloudprint.service.AuthenticatorService;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import retrofit2.Call;


import static org.apache.http.HttpHeaders.AUTHORIZATION;

public class Authenticator implements okhttp3.Authenticator {
    AuthenticatorService service;
    public Authenticator() {
service = new AuthenticatorService();
    }

    @java.lang.Override
    public Request authenticate(Route route, Response response)  {
        try {
            Call<Token> token= service.getToken();

            retrofit2.Response<Token> response1 =token.execute();
            System.out.print("AUTHENTICATOR"+response1.body().access_token);

            if(response1!=null && response1.code()==200)
            {
                Constants.token=response1.body().access_token;
            return response.request().newBuilder()
                    .header(AUTHORIZATION, "Bearer "+response1.body().access_token)
                    .build();
        }
        }
        catch (java.io.IOException e)
        {
            return null;
        }

        return null;
    }
}
