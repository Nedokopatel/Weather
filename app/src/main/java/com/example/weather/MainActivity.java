package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.weather.Api.ApiService;
import com.example.weather.Api.Forecast;
import com.example.weather.Api.Location;
import com.example.weather.Api.MainApplication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private String key = "893016022c894bd7bbf195521220211";
    private String q = "London";
    private Integer days = 1;
    private String lang = "ru";
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title = (TextView) findViewById(R.id.TextPing);
        ApiService.getInstance()
                .getJSONApi()
                .getForecast(key,q,days,lang)
                .enqueue(new Callback<Forecast>() {
                    @Override
                    public void onResponse(@NonNull Call<Forecast> call, @NonNull Response<Forecast> response) {
                        Forecast city = response.body();
                        Location location = city.getLocation();
                        title.setText(location.getCountry());

                    }

                    @Override
                    public void onFailure(@NonNull Call<Forecast> call, @NonNull Throwable t) {
                        title.setText("Запрос неуспешен");
                        t.printStackTrace();
                    }
                });
    }
}