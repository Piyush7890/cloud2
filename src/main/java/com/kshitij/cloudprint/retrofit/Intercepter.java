package com.kshitij.cloudprint.retrofit;

import com.kshitij.cloudprint.model.Token;
import com.kshitij.cloudprint.service.AuthenticatorService;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

import java.io.IOException;

public class Intercepter implements Interceptor {
    private String token;
AuthenticatorService service;
    public Intercepter(String token) {

        service = new AuthenticatorService();
        this.token = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {


        Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + token).build();
         Response response =chain.proceed(request);
        System.out.print("RESPONDD"+String.valueOf(response.code()));
         if(response.code()==403)
         {
             Call<Token> token= service.getToken();

             retrofit2.Response<Token> response1 =token.execute();
             if(response1.isSuccessful() && response1.body()!=null)
             {
                 Request request1 = chain.request().newBuilder().addHeader("Authorization","Bearer "+response1.body().access_token).build();
                 response = chain.proceed(request1);
             }
             else System.out.print("ERROR"+response1.errorBody().string());
         }
         return response;
    }
}
