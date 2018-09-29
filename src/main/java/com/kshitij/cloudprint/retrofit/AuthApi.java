package com.kshitij.cloudprint.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AuthApi {
    String grant_type = "authorization_type";

    @GET("submit")
    Call<SubmitOutput> submitJob(@Query("printerid") String printerid,
                                 @Query("title") String title,
                                 @Query("ticket") String ticket,
                                 @Query("content") byte[] content,
                                 @Query("contentType") String type);

}
