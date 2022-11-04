package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weather.Api.Condition;
import com.example.weather.Api.Hour;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HourActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Adapter adapter;
    List<Hour> values = new ArrayList<>();
    static ImageView weatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hour);
        setTitle(MainActivity.location.getName());
        values = MainActivity.hours;
        recyclerView = (RecyclerView) findViewById(R.id.listForecast);

        adapter = new Adapter(this, values);
        recyclerView.setAdapter(adapter);

    }

    private static class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private final List<Hour> names;
        private final LayoutInflater inflater;

        Adapter(Context context, List<Hour> values) {
            this.names = values;
            this.inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_hour, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.bind(names.get(i));
        }

        @Override
        public int getItemCount() {
            return names.size();
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        Object object;

        TextView timeText;
        TextView tempText;
        Condition condition;
        String weatherType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeText = itemView.findViewById(R.id.timeText);
            tempText = itemView.findViewById(R.id.tempText);
            weatherIcon = itemView.findViewById(R.id.weatherIconItem);
        }

        public void bind(Hour object) {
            object = object;
            condition = object.getCondition();
            String time = object.getTime();
            time = time.substring(11,16);
            timeText.setText(time);
            tempText.setText(String.valueOf(object.getTempC()+"\u2103"));
            weatherType = condition.getText();
            setWeatherIcon(weatherType);
        }
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