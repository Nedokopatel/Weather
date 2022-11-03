package com.example.weather.Api;


import retrofit2.Call;
import retrofit2.http.GET;

import retrofit2.http.Query;


public interface AuthApi {

    @GET("forecast.json")
    Call<Forecast> getForecast(@Query("key") String key, @Query("q") String q, @Query("days") Integer days, @Query("lang") String lang);

}