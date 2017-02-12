package com.igordubrovin.dayplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class DataBase {
    private static final String DB_TABLE_NAME = "sqlDayPlanerTable";
    private static final int DB_VERSION = 1;

    private static final String COLUMN_ID_DB_TABLE = "id";
    static final String COLUMN_DAY_MONTH_DB_TABLE = "dayMonth";
    static final String COLUMN_MONTH_TABLE = "month";
    static final String COLUMN_YEAR_TABLE = "year";
    static final String COLUMN_DAY_WEEK_TABLE = "dayWeek";
    static final String COLUMN_DATA_TABLE = "data";
    static final String COLUMN_THEME_TABLE = "theme";
    private static final String COLUMN_WEATHER_TABLE = "weather";

    static final String ACT_SET = "set";
    static final String ACT_GET = "get";
    static final String ACT_UPD = "upd";
    static final String ACT_DEL = "del";
    static final String ACT_GET_DATA = "get_data";

    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;

    private DataBase(Context context){
        dbHelper = new DBHelper(context, DB_TABLE_NAME, null, DB_VERSION);
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    static synchronized ArrayList<HashMap<String, String>> syncDBWork(Context context, HashMap<String, String> map, @NonNull String act, int day, int month, int year) throws Exception {
        DataBase dataBase = new DataBase(context);
        switch (act){
            case ACT_GET:
                if (month == -1) return dataBase.getData();
                else return dataBase.getData(month);
            case ACT_GET_DATA:
                return dataBase.getData(day, month, year);
            case ACT_SET:
                dataBase.setData(map);
                break;
            case ACT_UPD:
                dataBase.updateItem(map);
                break;
            case ACT_DEL:
                dataBase.delItem(day, month, year);
                break;
        }
        return null;
    }

    private ArrayList<HashMap<String, String>> getData(){
        sqLiteDatabase = dbHelper.getWritableDatabase();
        HashMap<String, String> map;
        ArrayList<HashMap<String, String>> mapArrayList = new ArrayList<>();
        BuilderDateData builderDateData = new BuilderDateData();
        cursor = sqLiteDatabase.query(DB_TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            int dayMonthColumnIndex = cursor.getColumnIndex(COLUMN_DAY_MONTH_DB_TABLE);
            int monthColumnIndex = cursor.getColumnIndex(COLUMN_MONTH_TABLE);
            int yearColumnIndex = cursor.getColumnIndex(COLUMN_YEAR_TABLE);
            int dayWeekColumnIndex = cursor.getColumnIndex(COLUMN_DAY_WEEK_TABLE);
            int themeColumnIndex = cursor.getColumnIndex(COLUMN_THEME_TABLE);
            int weatherColumnIndex = cursor.getColumnIndex(COLUMN_WEATHER_TABLE);
            do {
                map = builderDateData.setDayMonth(cursor.getString(dayMonthColumnIndex))
                        .setMonth(cursor.getString(monthColumnIndex))
                        .setYear(cursor.getString(yearColumnIndex))
                        .setDayWeek(cursor.getString(dayWeekColumnIndex))
                        .setTheme(cursor.getString(themeColumnIndex))
                        .setWeather(cursor.getString(weatherColumnIndex))
                        .create();
                mapArrayList.add(map);
            }while (cursor.moveToNext());
        }
        dbHelper.close();
        return mapArrayList;
    }

    private ArrayList<HashMap<String, String>> getData(int day, int month, int year){
        String[] args = new String[]{String.valueOf(day), String.valueOf(month - 1), String.valueOf(year)};
        sqLiteDatabase = dbHelper.getWritableDatabase();
        HashMap<String, String> map;
        ArrayList<HashMap<String, String>> mapArrayList = new ArrayList<>();
        BuilderDateData builderDateData = new BuilderDateData();
        cursor = sqLiteDatabase.query(DB_TABLE_NAME, null, COLUMN_DAY_MONTH_DB_TABLE + "=? and "
                + COLUMN_MONTH_TABLE + " =? and "
                + COLUMN_YEAR_TABLE + " =?", args, null, null, null);
        if (cursor.moveToFirst()){
            int dataColumnIndex = cursor.getColumnIndex(COLUMN_DATA_TABLE);
            int themeColumnIndex = cursor.getColumnIndex(COLUMN_THEME_TABLE);
            do {
                map = builderDateData.setData(cursor.getString(dataColumnIndex))
                        .setTheme(cursor.getString(themeColumnIndex))
                        .create();
                mapArrayList.add(map);
            }while (cursor.moveToNext());
        }
        dbHelper.close();
        return mapArrayList;
    }

    private ArrayList<HashMap<String, String>> getData(int month){
        sqLiteDatabase = dbHelper.getWritableDatabase();
        HashMap<String, String> map;
        ArrayList<HashMap<String, String>> mapArrayList = new ArrayList<>();
        BuilderDateData builderDateData = new BuilderDateData();
        cursor = sqLiteDatabase.query(DB_TABLE_NAME, null, COLUMN_MONTH_TABLE + " = ?", new String[]{String.valueOf(month)}, null, null, null);
        if (cursor.moveToFirst()){
            int dayMonthColumnIndex = cursor.getColumnIndex(COLUMN_DAY_MONTH_DB_TABLE);
            int monthColumnIndex = cursor.getColumnIndex(COLUMN_MONTH_TABLE);
            int yearColumnIndex = cursor.getColumnIndex(COLUMN_YEAR_TABLE);
            int dayWeekColumnIndex = cursor.getColumnIndex(COLUMN_DAY_WEEK_TABLE);
            int themeColumnIndex = cursor.getColumnIndex(COLUMN_THEME_TABLE);
            int weatherColumnIndex = cursor.getColumnIndex(COLUMN_WEATHER_TABLE);
            do {
                map = builderDateData.setDayMonth(cursor.getString(dayMonthColumnIndex))
                        .setMonth(cursor.getString(monthColumnIndex))
                        .setYear(cursor.getString(yearColumnIndex))
                        .setDayWeek(cursor.getString(dayWeekColumnIndex))
                        .setTheme(cursor.getString(themeColumnIndex))
                        .setWeather(cursor.getString(weatherColumnIndex))
                        .create();
                mapArrayList.add(map);
            }while (cursor.moveToNext());
        }
        dbHelper.close();
        return mapArrayList;
    }

    private void setData(HashMap<String, String> map) throws Exception {
        ArrayList<HashMap<String, String>> result = getData(Integer.parseInt(map.get(COLUMN_DAY_MONTH_DB_TABLE)),
                Integer.parseInt(map.get(COLUMN_MONTH_TABLE))+1, Integer.parseInt(map.get(COLUMN_YEAR_TABLE)));
        if (result.size()==0) {
            sqLiteDatabase = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            sqLiteDatabase.beginTransaction();

            for (Map.Entry<String, String> dataMap : map.entrySet()) {
                if (checkMapKey(dataMap.getKey()))
                    contentValues.put(dataMap.getKey(), dataMap.getValue());
            }
            sqLiteDatabase.insert(DB_TABLE_NAME, null, contentValues);
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            dbHelper.close();
        }
        else throw new Exception();
    }

    private void updateItem(HashMap<String, String> map){
        String[] args = new String[]{map.get(COLUMN_DAY_MONTH_DB_TABLE), map.get(COLUMN_MONTH_TABLE), map.get(COLUMN_YEAR_TABLE)};
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        sqLiteDatabase.beginTransaction();
        for (Map.Entry<String, String> dataMap : map.entrySet()) {
            if (checkMapKey(dataMap.getKey()))
                contentValues.put(dataMap.getKey(), dataMap.getValue());
        }
        sqLiteDatabase.update(DB_TABLE_NAME, contentValues, COLUMN_DAY_MONTH_DB_TABLE + "=? and "
                + COLUMN_MONTH_TABLE + " =? and "
                + COLUMN_YEAR_TABLE + " =?", args);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        dbHelper.close();
    }

    private void delItem(int day, int month, int year){
        sqLiteDatabase = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(day), String.valueOf(month - 1), String.valueOf(year)};
        sqLiteDatabase.delete(DB_TABLE_NAME, COLUMN_DAY_MONTH_DB_TABLE + "=? and "
                + COLUMN_MONTH_TABLE + " =? and "
                + COLUMN_YEAR_TABLE + " =?", args);
        dbHelper.close();
    }

    private boolean checkMapKey(String key){
        return (key.equals(COLUMN_DATA_TABLE) || key.equals(COLUMN_DAY_MONTH_DB_TABLE) || key.equals(COLUMN_DAY_WEEK_TABLE)
        || key.equals(COLUMN_MONTH_TABLE) || key.equals(COLUMN_THEME_TABLE) || key.equals(COLUMN_WEATHER_TABLE)
        || key.equals(COLUMN_YEAR_TABLE));
    }

    private static class DBHelper extends SQLiteOpenHelper {

        DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("create table " + DB_TABLE_NAME + " ("
                                    + COLUMN_ID_DB_TABLE + " integer primary key autoincrement, "
                                    + COLUMN_DAY_MONTH_DB_TABLE + " text, "
                                    + COLUMN_MONTH_TABLE + " text, "
                                    + COLUMN_YEAR_TABLE + " text, "
                                    + COLUMN_DAY_WEEK_TABLE + " text, "
                                    + COLUMN_DATA_TABLE + " text, "
                                    + COLUMN_THEME_TABLE + " text, "
                                    + COLUMN_WEATHER_TABLE + " text" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }

}
