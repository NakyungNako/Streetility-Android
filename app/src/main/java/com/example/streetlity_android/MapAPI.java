package com.example.streetlity_android;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface MapAPI {
    @GET("tour/list")
    Call<ResponseBody> getList(@Header("Authorization") String token, @Query("rowPerPage") int row, @Query("pageNum") int pageNum,
                               @Query("orderBy") String order, @Query("isDesc") boolean isDesc);
}
