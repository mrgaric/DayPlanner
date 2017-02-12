package com.igordubrovin.dayplanner;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityAddDataToDB extends AppCompatActivity implements UpdateDialog.CallbackActivityUpdateDialog {
    Spinner spDay;
    Spinner spMonth;
    Spinner spYear;
    Button btnAddData;
    EditText etTheme;
    EditText etData;
    TextView tvDayWeek;
    Toolbar toolbar;

    ArrayAdapter<String> adapterDaySpinner;
    ArrayAdapter<String> adapterMonthSpinner;
    ArrayAdapter<Integer> adapterYearSpinner;

    public List<String> listDay = new ArrayList<>();
    List<String> listMonth = new ArrayList<>();
    List<Integer> listYear = new ArrayList<>();

    Intent intent;

    int currentDay;
    int currentMonth;
    int currentYear;

    int dayMonth;
    int month;
    int year;

    HashMap<String, String> mapData = new HashMap<>();
    boolean firstStart;

    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        toolbar = (Toolbar)findViewById(R.id.toolbar_add_data);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Добавить задачу");
        toolbar.setNavigationIcon(R.drawable.ic_ab_back_holo_light_am);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        firstStart = true;

        calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));

        spDay = (Spinner)findViewById(R.id.spDay);
        spMonth = (Spinner)findViewById(R.id.spMonth);
        spYear = (Spinner)findViewById(R.id.spYear);

        btnAddData = (Button)findViewById(R.id.btn_add_data);

        etData = (EditText)findViewById(R.id.ed_comment_act_order);
        etTheme = (EditText)findViewById(R.id.edit_text_theme);

        tvDayWeek = (TextView)findViewById(R.id.tvDayWeek);

        listMonth.add("Январь");
        listMonth.add("Февраль");
        listMonth.add("Март");
        listMonth.add("Апрель");
        listMonth.add("Май");
        listMonth.add("Июнь");
        listMonth.add("Июль");
        listMonth.add("Август");
        listMonth.add("Сентябрь");
        listMonth.add("Октябрь");
        listMonth.add("Ноябрь");
        listMonth.add("Декабрь");

        listYear.add(2016);
        listYear.add(2017);
        listYear.add(2018);
        listYear.add(2019);
        listYear.add(2020);

        intent = getIntent();

        setAdapterSp();

        if (intent.getAction().equals("updateData")){
            etTheme.setText(intent.getStringExtra("theme"));
            etData.setText(intent.getStringExtra("data"));
            currentDay = intent.getIntExtra("day", calendar.get(Calendar.DAY_OF_MONTH));
            currentMonth = intent.getIntExtra("month", calendar.get(Calendar.MONTH));
            currentYear = intent.getIntExtra("year", calendar.get(Calendar.YEAR));
        }

        dayMonth = intent.getIntExtra("day", calendar.get(Calendar.DAY_OF_MONTH));
        month = intent.getIntExtra("month", calendar.get(Calendar.MONTH));
        year = intent.getIntExtra("year", calendar.get(Calendar.YEAR));

        spMonth.setSelection(month);

        for (int i = 0; i < listYear.size(); i++){
            if (year == listYear.get(i)){
                spYear.setSelection(i);
                break;
            }
        }

        replaysDayWeek();

        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                if (!etTheme.getText().toString().equals("") && !etData.getText().toString().equals("")) {
                    mapData.put(DataBase.COLUMN_DAY_MONTH_DB_TABLE, String.valueOf(spDay.getSelectedItem()));
                    mapData.put(DataBase.COLUMN_MONTH_TABLE, String.valueOf(spMonth.getSelectedItemPosition()));
                    mapData.put(DataBase.COLUMN_YEAR_TABLE, String.valueOf(spYear.getSelectedItem()));
                    mapData.put(DataBase.COLUMN_THEME_TABLE, etTheme.getText().toString());
                    mapData.put(DataBase.COLUMN_DATA_TABLE, etData.getText().toString());
                    mapData.put(DataBase.COLUMN_DAY_WEEK_TABLE, getDayWeek(dayMonth, month, year));
                    if (intent.getAction().equals("updateData") && currentDay == dayMonth && currentMonth == month && currentYear == year){
                        Intent intent = new Intent();
                        intent.putExtra("data", etData.getText().toString());
                        intent.putExtra("theme", etTheme.getText().toString());
                        setResult(RESULT_OK, intent);
                        onClickPositiveBtn();
                    }
                    else {
                        setResult(RESULT_CANCELED);
                        AddData addData = new AddData();
                        addData.execute(mapData);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int maxDayMonth;
                month = i;
                maxDayMonth = getMaxDayMonth(month, year);
                if (dayMonth > maxDayMonth){
                    spDay.setSelection(maxDayMonth - 1);
                }
                updateListDay(maxDayMonth);
                month = spMonth.getSelectedItemPosition();
                adapterDaySpinner.notifyDataSetChanged();
                replaysDayWeek();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int maxDayMonth;
                year = (Integer)spYear.getSelectedItem();
                maxDayMonth = getMaxDayMonth(month, year);
                if (dayMonth > maxDayMonth){
                    spDay.setSelection(maxDayMonth - 1);
                }
                updateListDay(maxDayMonth);
                adapterDaySpinner.notifyDataSetChanged();
                replaysDayWeek();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dayMonth = Integer.parseInt((String) spDay.getSelectedItem());
                replaysDayWeek();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void updateListDay(int maxDayMonth){
        listDay.clear();
        for (int day = 1; day <= maxDayMonth; day++){
            if (day<10) listDay.add("0" + day);
            else listDay.add(String.valueOf(day));
        }
        if (firstStart){
            spDay.setSelection(dayMonth - 1);
            firstStart = false;
        }
    }

    private int getMaxDayMonth(int month, int year){
        calendar.clear();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private String getDayWeek(int day, int month, int year){
        int dayOfWeek;
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek){
            case Calendar.MONDAY:
                return "Понедельник";
            case Calendar.TUESDAY:
                return "Вторник";
            case Calendar.WEDNESDAY:
                return "Среда";
            case Calendar.THURSDAY:
                return "Четверг";
            case Calendar.FRIDAY:
                return "Пятница";
            case Calendar.SATURDAY:
                return "Суббота";
            case Calendar.SUNDAY:
                return "Воскресенье";
        }
        return null;
    }

    private void setAdapterSp(){
        adapterDaySpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listDay);
        adapterDaySpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDay.setAdapter(adapterDaySpinner);
        adapterMonthSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listMonth);
        adapterMonthSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMonth.setAdapter(adapterMonthSpinner);
        adapterYearSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listYear);
        adapterYearSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYear.setAdapter(adapterYearSpinner);
    }

    private void replaysDayWeek(){
        String dayWeek;
        dayWeek = getDayWeek(dayMonth, month, year);
        tvDayWeek.setText("");
        if (dayWeek != null) {
            for (int i = 0; i < dayWeek.length(); i++){
                tvDayWeek.setText(tvDayWeek.getText().toString().concat(dayWeek.charAt(i) + "\n"));
            }
        }

    }

    @Override
    public void onClickPositiveBtn() {
        try {
            UpdData updData = new UpdData();
            updData.execute(mapData);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickNegativeBtn() {
        finish();
    }

    class AddData extends AsyncTask<HashMap<String, String>, Void, Integer>{
        @SafeVarargs
        @Override
        protected final Integer doInBackground(HashMap<String, String>... hashMaps) {
            try {
                DataBase.syncDBWork(getApplicationContext(), hashMaps[0], DataBase.ACT_SET, 0, -1, 0);
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == -1) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                UpdateDialog updateDialog = new UpdateDialog();
                updateDialog.registerCallback(ActivityAddDataToDB.this);
                updateDialog.show(fragmentManager, "dialogUpdate");
            }
            else finish();
        }
    }

    class UpdData extends AsyncTask<HashMap<String, String>, Void, Void>{
        @SafeVarargs
        @Override
        protected final Void doInBackground(HashMap<String, String>... hashMaps) {
            try {
                DataBase.syncDBWork(getApplicationContext(), hashMaps[0], DataBase.ACT_UPD, 0, -1, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
