package com.domonz.swipe;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @GET("/api/public/get")
    Call<Object> getProducts();

    @POST("/api/public/add")
    Call<ResponseBody> uploadMultiFile(@Body RequestBody file);
}