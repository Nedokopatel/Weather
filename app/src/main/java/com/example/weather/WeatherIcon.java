package com.example.weather;

public class WeatherIcon {
    public static String setWeatherIcon(String weatherType) {
        String result;
        switch (weatherType) {
            case "Солнечно":
            case "Ясно":
                result = "clean";
                break;

            case "Туман":
            case "Переохлажденный туман":
            case "Дымка":
                result = "atmosphere";
                break;

            case "Местами небольшой дождь":
            case "Слабая морось":
            case "Временами умеренный дождь":
            case "Местами дождь":
                result = "drizzle";
                break;

            case "Умеренный дождь":
            case "Небольшой дождь":
            case "Небольшой ливневый дождь":
            case "Небольшой ледяной дождь":
            case "Умеренный или сильный ливневый дождь":
            case "Умеренный или сильный ледяной дождь":
            case "Сильные ливни":
            case "Ледяной дождь":
                result = "rain";
                break;

            case "Небольшой снег":
            case "Поземок":
            case "Местами дождь со снегом":
            case "Местами умеренный снег":
            case "Умеренный снег":
            case "Небольшой ливневый дождь со снегом":
            case "Умеренные или сильные ливневые дожди со снегом":
            case "Местами сильный снег":
            case "Сильный снег":
            case "Умеренный или сильный снег":
            case "Местами снег":
                result = "snow";
                break;

            case "В отдельных районах мест небольшой дождь с грозой":
            case "В отдельных районах умеренный или сильный дождь с грозой":
            case "В отдельных районах местами небольшой снег с грозой":
            case "В отдельных районах умеренный или сильный снег с грозой":
            case "Местами грозы":
                result = "thunderstorm";
                break;

            case "Метель":
                result =  "extreme";
                break;

            default:
                result = "default";
                break;
        }
        return result;
    }
}
