package com.igordubrovin.dayplanner;

import java.util.HashMap;

/**
 * Created by Игорь on 21.01.2017.
 */

class BuilderDateData {

    public static final String KEY_DATA = "data";
    public static final String KEY_DAY_WEEK = "day";
    public static final String KEY_DAY_MONTH = "dayMonth";
    public static final String KEY_MONTH = "month";
    public static final String KEY_YEAR = "year";
    public static final String KEY_THEME = "theme";
    public static final String KEY_WEATHER = "weather";

    private String data = null;
    private String dayWeek = null;
    private String dayMonth = null;
    private String month = null;
    private String year = null;
    private String theme = "";
    private String weather = "";

    BuilderDateData setData(String data) {
        this.data = data;
        return this;
    }

    BuilderDateData setDayWeek(String dayWeek) {
        this.dayWeek = dayWeek;
        return this;
    }

    BuilderDateData setDayMonth(String dayMonth) {
        this.dayMonth = dayMonth;
        return this;
    }

    BuilderDateData setMonth(String month) {
        this.month = month;
        return this;
    }

    BuilderDateData setYear(String year) {
        this.year = year;
        return this;
    }

    public BuilderDateData setTheme(String theme) {
        this.theme = theme;
        return this;
    }

    BuilderDateData setWeather(String weather) {
        this.weather = weather;
        return this;
    }

    HashMap<String, String> create(){
        HashMap<String, String> mMap = new HashMap<>();
        mMap.put(KEY_DATA, data);
        mMap.put(KEY_DAY_WEEK, dayWeek);
        mMap.put(KEY_DAY_MONTH, dayMonth);
        mMap.put(KEY_MONTH, month);
        mMap.put(KEY_YEAR, year);
        mMap.put(KEY_THEME, theme);
        mMap.put(KEY_WEATHER, weather);
        return mMap;
    }
}
