package com.kshitij.cloudprint.service;

import com.kshitij.cloudprint.configuration.CloudPrintProperties;
import com.kshitij.cloudprint.model.Token;
import com.kshitij.cloudprint.retrofit.RefreshTokenApi;
import org.springframework.beans.factory.annotation.Autowired;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthenticatorService {


    private RefreshTokenApi api;

    public RefreshTokenApi refreshTokenApi()
    {
        if (api==null){ api = new Retrofit
                .Builder().baseUrl("https://www.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RefreshTokenApi.class);
        }
        return api;
    }

    public Call<Token> getToken () throws java.io.IOException
    {
       return refreshTokenApi().refreshToken("THcVujj-6DB-2UPJAWTG6uoT",
                "refresh_token","1/hZCYyVTBtJpECpR_Bc8zpoGTHHEAxc9YrmIOiIUFs-4",
                "76023066008-nlh5h6fa6jhd32339m9depuh8tocjhqo.apps.googleusercontent.com");
    }
}
