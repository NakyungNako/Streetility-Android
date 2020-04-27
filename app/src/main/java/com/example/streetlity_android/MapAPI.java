package com.example.streetlity_android;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MapAPI {
    @GET("service/fuel/all")
    Call<ResponseBody> getAllFuel();

    @GET("service/atm/all")
    Call<ResponseBody> getAllATM();

    @GET("service/toilet/all")
    Call<ResponseBody> getAllWC();

    @GET("service/fuel/range")
    Call<ResponseBody> getFuelInRange(@Query("location") float lat, @Query("location") float lon,
                                      @Query("range") float range);

    @GET("service/atm/range")
    Call<ResponseBody> getATMInRange(@Query("location") float lat, @Query("location") float lon,
                                      @Query("range") float range);

    @GET("service/toilet/range")
    Call<ResponseBody> getWCInRange(@Query("location") float lat, @Query("location") float lon,
                                      @Query("range") float range);

    @GET("service/range")
    Call<ResponseBody> getServiceInRange(@Query("location") float lat, @Query("location") float lon,
                                         @Query("range") float range);

    @FormUrlEncoded
    @POST("service/fuel/add")
    Call<ResponseBody> addFuel(@Field("location") float lat, @Field("location" )float lon);

    @FormUrlEncoded
    @POST("service/atm/add")
    Call<ResponseBody> addATM(@Field("location") float lat, @Field("location" )float lon);

    @FormUrlEncoded
    @POST("service/toilet/add")
    Call<ResponseBody> addWC(@Field("location") float lat, @Field("location" )float lon);

    @GET("json")
    Call<ResponseBody> geocode(@Query("address") String address, @Query("key") String key);

    @FormUrlEncoded
    @POST("user/login")
    Call<ResponseBody> login(@Field("username") String username, @Field("passwd") String password);

    @FormUrlEncoded
    @POST("user/register")
    Call<ResponseBody> signup(@Field("username") String username, @Field("passwd") String password,
                              @Field("email") String email);
}
