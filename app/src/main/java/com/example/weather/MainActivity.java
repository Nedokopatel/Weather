package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weather.Api.ApiService;
import com.example.weather.Api.Forecast;
import com.example.weather.Api.ForecastDay;
import com.example.weather.Api.ForecastWeather;
import com.example.weather.Api.Hour;
import com.example.weather.Api.Location;
import com.example.weather.Api.MainApplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private String key = "893016022c894bd7bbf195521220211";
    private String q = "Moscow";
    private Integer days = 1;
    private String lang = "ru";
    TextView title;

    RecyclerView cityView;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title = (TextView) findViewById(R.id.TextPing);
        ApiService.getInstance()
                .getJSONApi()
                .getForecast(key,q,days,lang)
                .enqueue(new Callback<ForecastWeather>() {
                    @Override
                    public void onResponse(@NonNull Call<ForecastWeather> call, @NonNull Response<ForecastWeather> response) {
                        ForecastWeather city = response.body();
                        Location location = city.getLocation();
                        Forecast fw = city.getForecast();

                        List<ForecastDay> fwlist = fw.getForecastday();

                        List<Hour> hours = fwlist.get(0).getHour();

                        title.setText(hours.get(0).getTime());

                    }

                    @Override
                    public void onFailure(@NonNull Call<ForecastWeather> call, @NonNull Throwable t) {
                        title.setText("Запрос неуспешен");
                        t.printStackTrace();
                    }
                });
    }
    private static class Adapter extends RecyclerView.Adapter<ViewHolder> {
        //private final List<TableObj> names;
        private final LayoutInflater inflater;

        Adapter(Context context, List<TableObj> values) {
            this.names = values;
            this.inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_table, viewGroup, false));
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

        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OrderedActivity.start(view.getContext());
                    /*Intent intent = new Intent(view.getContext(), OrderedActivity.class);
                    startActivity(intent);*/
                }
            });
        }

        public void bind(TableObj object) {
            object = object;
            text.setText(object.getName());
        }
    }
}