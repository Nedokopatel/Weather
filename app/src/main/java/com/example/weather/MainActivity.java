package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.Api.ApiService;
import com.example.weather.Api.Condition;
import com.example.weather.Api.Current;
import com.example.weather.Api.Forecast;
import com.example.weather.Api.ForecastDay;
import com.example.weather.Api.ForecastWeather;
import com.example.weather.Api.Hour;
import com.example.weather.Api.Location;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    //Переменные запроса
    private String key = "893016022c894bd7bbf195521220211";
    private String q = "Moscow";
    private Integer days = 1;
    private String lang = "ru";

    private String weatherType;
    private String currentTemp;
    String item;

    TextView title;
    TextView temp;
    static ImageView weatherIcon;
    Spinner spinner;
    ProgressBar progressBar;

    //Переменные сохранения сессии
    private static final String PREFS_FILE = "Account";
    private static final String PREF_WEATHERTYPE = "Name";
    private static final String PREF_CURRENTCITY = "Name";
    private static final String PREF_CITY = "Name";

    SharedPreferences settings;
    SharedPreferences.Editor prefEditor;


    String [] cities = {"Moscow","London","Istanbul","Saratov","Vologda"};
    public static List<Hour> hours = new ArrayList<>();

    static Location location;
    Forecast fw;
    List<ForecastDay> forecastList;
    Current current;
    Condition condition;
    ForecastWeather city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);

        title = (TextView) findViewById(R.id.textView);
        temp = (TextView) findViewById(R.id.tempView);
        weatherIcon = (ImageView) findViewById(R.id.weatherIcon);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        getCurrent();
        progressBar.setVisibility(ProgressBar.INVISIBLE);

        spinner = findViewById(R.id.city);

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, cities);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = (String)parent.getItemAtPosition(position);
                title.setText(item);
                q = item;
                getCurrent();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);

        CardView cardView = findViewById(R.id.cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HourActivity.class);
                startActivity(intent);
            }
        });

    }
    //Сохранить сессию
    @Override
    protected void onPause(){
        super.onPause();

        prefEditor = settings.edit();
        prefEditor.putString(PREF_WEATHERTYPE, weatherType);
        prefEditor.putString(PREF_CURRENTCITY, currentTemp);
        prefEditor.putString(PREF_CITY, String.valueOf(title.getText()));

        Gson gson = new Gson();
        String json = gson.toJson(city);
        prefEditor.putString(PREF_CITY, json);

        prefEditor.commit();
        prefEditor.apply();
    }

    //Вызываем запрос
    public void getCurrent(){
        ApiService.getInstance()
                .getJSONApi()
                .getForecast(key,q,days,lang)
                .enqueue(new Callback<ForecastWeather>() {
                    @Override
                    public void onResponse(@NonNull Call<ForecastWeather> call, @NonNull Response<ForecastWeather> response) {
                        city = response.body();
                        location = city.getLocation();
                        fw = city.getForecast();

                        forecastList = fw.getForecastday();
                        hours = forecastList.get(0).getHour();

                        current = city.getCurrent();
                        condition = current.getCondition();

                        weatherType = condition.getText();
                        setWeatherIcon(weatherType);
                        currentTemp = String.valueOf(current.getTempC()+"\u2103");
                        temp.setText(currentTemp);

                    }

                    @Override
                    public void onFailure(@NonNull Call<ForecastWeather> call, @NonNull Throwable t) {
                        weatherType = settings.getString(PREF_WEATHERTYPE,weatherType);
                        setWeatherIcon(weatherType);

                        Gson gson = new Gson();
                        String json = settings.getString(PREF_CITY, "");
                        ForecastWeather obj = gson.fromJson(json, ForecastWeather.class);

                        city = obj;
                        fw = city.getForecast();
                        forecastList = fw.getForecastday();
                        hours = forecastList.get(0).getHour();
                        current = city.getCurrent();
                        location = city.getLocation();

                        currentTemp = String.valueOf(current.getTempC()+"\u2103");
                        temp.setText(currentTemp);
                        title.setText(location.getName());

                        spinner.setVisibility(View.GONE);

                        Toast toast = Toast.makeText(MainActivity.this, "Ошибка подключения \n запущена предыдущая сессия ",
                                Toast.LENGTH_LONG);
                        toast.show();

                        t.printStackTrace();
                    }
                });
    }

    public static void setWeatherIcon(String weatherType){
        String result = WeatherIcon.setWeatherIcon(weatherType);
        switch (result) {

            case "clean":weatherIcon.setImageResource(R.drawable.ic_clear_web);
                break;

            case "atmosphere":weatherIcon.setImageResource(R.drawable.ic_atmosphere_web);
                break;

            case "drizzle":weatherIcon.setImageResource(R.drawable.ic_drizzle_web);
                break;

            case "rain":weatherIcon.setImageResource(R.drawable.ic_rain_web);
                break;

            case "snow":weatherIcon.setImageResource(R.drawable.ic_snow_web);
                break;

            case "thunderstorm":weatherIcon.setImageResource(R.drawable.ic_thunderstorm_web);
                break;

            case "extreme":
                weatherIcon.setImageResource(R.drawable.ic_extreme_web);
                break;
            default: weatherIcon.setImageResource(R.drawable.ic_cloudy_web);
                break;
        }
    }

}