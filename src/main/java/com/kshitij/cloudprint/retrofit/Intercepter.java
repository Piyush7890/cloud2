package com.kshitij.cloudprint.retrofit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class Intercepter implements Interceptor {
    private String token;

    public Intercepter(String token) {
        this.token = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + token).build();
        return chain.proceed(request);
    }
}
