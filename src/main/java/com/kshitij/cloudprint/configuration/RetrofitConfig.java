package com.kshitij.cloudprint.configuration;

import com.kshitij.cloudprint.model.Constants;
import com.kshitij.cloudprint.retrofit.AuthApi;
import com.kshitij.cloudprint.retrofit.Authenticator;
import com.kshitij.cloudprint.retrofit.Intercepter;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
public class RetrofitConfig {
    @Autowired
    private CloudPrintProperties properties;

    private Retrofit retrofit() {
        System.out.print("RETROFIT");
        return new Retrofit
                .Builder()
                .baseUrl(properties.getBaseUrl())
                .client(new OkHttpClient
                        .Builder()
                        //.authenticator(new Authenticator())
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .addInterceptor(new Intercepter(Constants.token)).build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Bean
    public AuthApi authApi() {
        return retrofit().create(AuthApi.class);
    }
}
