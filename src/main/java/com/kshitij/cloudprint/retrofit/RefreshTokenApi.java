package com.kshitij.cloudprint.retrofit;

import com.kshitij.cloudprint.model.Token;
import retrofit2.Call;
import retrofit2.http.*;

public interface RefreshTokenApi {

    @POST("oauth2/v4/token")
    @FormUrlEncoded
    Call<Token> refreshToken(@Field("client_secret") String secret,
                             @Field("grant_type") String grantType,
                             @Field("refresh_token") String refreshToken,
                             @Field("client_id") String client_id);
}
