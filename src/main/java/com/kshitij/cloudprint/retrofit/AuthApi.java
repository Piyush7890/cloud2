package com.kshitij.cloudprint.retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AuthApi {

    /*@POST("submit")
    Call<SubmitOutput> submitJob(@Query("printerid") String printerid,
                                 @Query("title") String title,
                                 @Query("ticket") String ticket,
                                 @Query("content") InputStreamBody content,
                                 @Query("contentType") String type);
    @POST("submit")
    Call<SubmitOutput> submitJob(@Query("printerid") String printerid,
                                 @Query("title") String title,
                                 @Query("ticket") String ticket,
                                 @Query("content") MultipartFile content);*/

    @Multipart
    @POST("submit")
    Call<SubmitOutput> submitJob(@Part("printerid") RequestBody printerid,
                                 @Part("title") RequestBody title,
                                 @Part("ticket") RequestBody ticket,
                                 @Part MultipartBody.Part content,
                                 @Part("contentType") RequestBody type);
}
