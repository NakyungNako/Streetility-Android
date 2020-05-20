package com.example.streetlity_android;

import org.json.JSONArray;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
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

    @GET("service/maitain/all")
    Call<ResponseBody> getAllMaintenance();

    @GET("service/fuel/range")
    Call<ResponseBody> getFuelInRange(@Header("Version") String version, @Query("location") float lat, @Query("location") float lon,
                                      @Query("range") float range);

    @GET("service/atm/range")
    Call<ResponseBody> getATMInRange(@Header("Version") String version, @Query("location") float lat, @Query("location") float lon,
                                      @Query("range") float range);

    @GET("service/toilet/range")
    Call<ResponseBody> getWCInRange(@Header("Version") String version, @Query("location") float lat, @Query("location") float lon,
                                      @Query("range") float range);

    @GET("service/maintain/range")
    Call<ResponseBody> getMaintenanceInRange(@Header("Version") String version, @Query("location") float lat, @Query("location") float lon,
                                    @Query("range") float range);

    @GET("service/range")
    Call<ResponseBody> getServiceInRange(@Query("location") float lat, @Query("location") float lon,
                                         @Query("range") float range);

    @FormUrlEncoded
    @POST("service/fuel/add")
    Call<ResponseBody> addFuel(@Header("Version") String version, @Field("rtoken") String token, @Field("location") float lat, @Field("location" )float lon,
                               @Field("address") String address, @Field("note") String note);

    @FormUrlEncoded
    @POST("service/atm/add")
    Call<ResponseBody> addATM(@Header("Version") String version, @Field("rtoken") String token,@Field("location") float lat, @Field("location" )float lon,
                              @Field("type") String type, @Field("address") String address, @Field("note") String note);

    @FormUrlEncoded
    @POST("service/toilet/add")
    Call<ResponseBody> addWC(@Header("Version") String version, @Field("rtoken") String token,@Field("location") float lat, @Field("location" )float lon,
                             @Field("address") String address, @Field("note") String note);

    @FormUrlEncoded
    @POST("service/maintain/add")
    Call<ResponseBody> addMaintenance(@Header("Version") String version, @Field("rtoken") String token,@Field("location") float lat, @Field("location" )float lon,
                                      @Field("address") String address, @Field("name") String name, @Field("note") String note);

    @GET("json")
    Call<ResponseBody> geocode(@Query("address") String address, @Query("key") String key);

    @FormUrlEncoded
    @POST("user/login")
    Call<ResponseBody> login(@Field("username") String username, @Field("passwd") String password,
                             @Field("deviceToken") String deviceToken);

    @FormUrlEncoded
    @POST("user/common/register")
    Call<ResponseBody> signupCommon(@Field("username") String username, @Field("passwd") String password,
                                    @Field("email") String email, @Field("phone") String phone, @Field("address") String address);

    @FormUrlEncoded
    @POST("user/maintenance/register")
    Call<ResponseBody> signupMaintainer(@Field("username") String username, @Field("passwd") String password,
                                    @Field("email") String email, @Field("phone") String phone,
                                    @Field("address") String address, @Field("serviceId") int id);

    @FormUrlEncoded
    @POST("user/logout")
    Call<ResponseBody> logout(@Field("rtoken") String token, @Field("username") String username,
                              @Field("deviceToken") String deviceToken);

    @FormUrlEncoded
    @POST("service/maintain/order")
    Call<ResponseBody> broadcast(@Header("Version") String version, @Field("reason") String reason, @Field("name") String name,
                                 @Field("phone") String phone, @Field("note") String note,
                                 @Field("id") int[] id, @Field("address") String address, @Field("preferTime") String time);

    @FormUrlEncoded
    @POST("user/device")
    Call<ResponseBody> addDevice(@Header("Version") String version ,@Field("token") String token, @Field("user") String username);
}
