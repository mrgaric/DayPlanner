package com.igordubrovin.dayplanner;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.CallbackDelDataBD, RecyclerViewAdapter.CallbackClickCV{

    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerViewMain;
    FloatingActionButton addBtn;
    Spinner spMonth;
    Spinner spYear;
    Toolbar toolbar;

    RecyclerViewAdapter recyclerViewAdapter;
    ArrayAdapter<String> adapterMonthSpinner;
    ArrayAdapter<Integer> adapterYearSpinner;
    List<String> listMonth = new ArrayList<>();
    List<Integer> listYear = new ArrayList<>();

    private boolean firstStart = true;

    final String LOG_TAG = "myLog";
    int dxChange = 0;
    int previewState = 0;
    int dxUpdate;
    int dyUpdate;

    int month;
    int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setLogo(R.mipmap.ic_launcher);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));

        month = calendar.get(Calendar.MONTH);
        year =  calendar.get(Calendar.YEAR);

        spMonth = (Spinner)findViewById(R.id.sp_month_main);
        spYear = (Spinner)findViewById(R.id.sp_year_main);

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

        adapterMonthSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listMonth);
        adapterMonthSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMonth.setAdapter(adapterMonthSpinner);
        adapterYearSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listYear);
        adapterYearSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYear.setAdapter(adapterYearSpinner);

        spMonth.setSelection(month);
        for (int i = 0; i < listYear.size(); i++){
            if (year == listYear.get(i)){
                spYear.setSelection(i);
                break;
            }
        }

        addBtn = (FloatingActionButton)findViewById(R.id.fab);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityAddDataToDB.class);
                intent.setAction("addData");
                startActivity(intent);
            }
        });

        recyclerViewMain = (RecyclerView)findViewById(R.id.recycleViewMain);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMain.setLayoutManager(linearLayoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext());
        recyclerViewAdapter.registerCallback(this, this);

        updateData();

        recyclerViewMain.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (dyUpdate == 0) {
                    if (newState == 0) {
                        if (dxChange > 0) {
                            if (dxChange > 500) {
                                recyclerView.smoothScrollToPosition(linearLayoutManager.findLastVisibleItemPosition());
                            }
                            else
                                recyclerView.smoothScrollToPosition(linearLayoutManager.findFirstVisibleItemPosition());
                        }
                        if (dxChange < 0) {
                            if (dxChange < -500)
                                recyclerView.smoothScrollToPosition(linearLayoutManager.findFirstVisibleItemPosition());
                            else
                                recyclerView.smoothScrollToPosition(linearLayoutManager.findLastVisibleItemPosition());
                        }
                    } else {
                        if (newState == 2 && previewState != 0) {
                            if (dxUpdate > 0) {
                                recyclerView.smoothScrollToPosition(linearLayoutManager.findLastVisibleItemPosition());
                                //recyclerViewAdapter.rstScrlView((RecyclerViewAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(linearLayoutManager.findFirstVisibleItemPosition()));
                            }
                            else
                                recyclerView.smoothScrollToPosition(linearLayoutManager.findFirstVisibleItemPosition());
                        }
                    }
                    previewState = newState;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                dyUpdate = dy;
                if (dy == 0) {
                    if (dxChange > 900){
                        recyclerViewAdapter.rstScrlView((RecyclerViewAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(linearLayoutManager.findFirstVisibleItemPosition()));
                    }
                    if (dxChange < -900){
                        recyclerViewAdapter.rstScrlView((RecyclerViewAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(linearLayoutManager.findLastVisibleItemPosition()));
                    }

                    dxUpdate = dx;

                    int firstCompleteVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    int lastCompleteVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                    if (firstCompleteVisibleItemPosition == -1 || lastCompleteVisibleItemPosition == -1) {
                        dxChange = dxChange + dx;
                    } else dxChange = 0;
                }
                Log.d("myLog", String.valueOf(dxChange));
            }
        });

        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(linearLayoutManager.findFirstVisibleItemPosition() != 0){
                    if (linearLayoutManager.findFirstVisibleItemPosition() == 5)linearLayoutManager.scrollToPosition(4);
                    recyclerViewMain.smoothScrollToPosition(0);}
                month = spMonth.getSelectedItemPosition();
                updateData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(linearLayoutManager.findFirstVisibleItemPosition() != 0){
                    if (linearLayoutManager.findFirstVisibleItemPosition() == 5)linearLayoutManager.scrollToPosition(4);
                    recyclerViewMain.smoothScrollToPosition(0);}
                year = (Integer)spYear.getSelectedItem();
                updateData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            updateData();
        }
    }

    @Override
    public void callBackDelData() {
        updateData();
    }

    @Override
    public void clickCV(int day, int mont, int year) {
        GetDataTask getDataTask = new GetDataTask();
        getDataTask.execute(day, mont, year);
    }

    private void updateData(){
        LoadDataTask loadDataTask = new LoadDataTask();
        loadDataTask.execute(month, year);
    }

    class LoadDataTask extends AsyncTask<Integer, Void, ArrayList<ArrayList<HashMap<String, String>>>>{

        @Override
        protected ArrayList<ArrayList<HashMap<String, String>>> doInBackground(Integer... ints) {
            int year;
            int month;

            month = ints[0];
            year = ints[1];

            ArrayList<HashMap<String, String>> dataInDB = null;
            ArrayList<ArrayList<HashMap<String,String>>> monthSplitWeeks;
            BuilderWeekData builderWeekData = new BuilderWeekData();
            try {
                dataInDB = DataBase.syncDBWork(getApplicationContext(), null, DataBase.ACT_GET, 0, -1, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            monthSplitWeeks = builderWeekData.setMonthTask(dataInDB, String.valueOf(month), String.valueOf(year))
                    .getSplitMonthWeeks();

            return monthSplitWeeks;
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<HashMap<String, String>>> mapArrayList) {
            super.onPostExecute(mapArrayList);
            if (firstStart) {
                recyclerViewAdapter.registerNewData(mapArrayList);
                recyclerViewMain.setAdapter(recyclerViewAdapter);
                firstStart = false;
            }
            else {
                recyclerViewAdapter.registerNewData(mapArrayList);
                recyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    private class GetDataTask extends AsyncTask<Integer, Void, String[]>{
        int day;
        int month;
        int year;
        @Override
        protected String[] doInBackground(Integer... integers) {
            day = integers[0];
            month = integers[1];
            year = integers[2];
            ArrayList<HashMap<String, String>> dataInDB = null;
            try {
                dataInDB = DataBase.syncDBWork(getApplicationContext(), null, DataBase.ACT_GET_DATA, day, month, year);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (dataInDB != null) {
                if (!dataInDB.isEmpty())
                return new String[]{dataInDB.get(0).get(BuilderDateData.KEY_DATA), dataInDB.get(0).get(BuilderDateData.KEY_THEME)};
                else return null;
            }
            else return null;
        }

        @Override
        protected void onPostExecute(String... data) {
            super.onPostExecute(data);
            Intent intent;
            if (data != null){
                intent = new Intent(MainActivity.this, ActivityDataDialog.class);
                intent.putExtra("data", data[0]);
                intent.putExtra("theme", data[1]);
                intent.putExtra("day", day);
                intent.putExtra("month", month);
                intent.putExtra("year", year);
                startActivityForResult(intent, 1);
            }
            else{
                intent = new Intent(MainActivity.this, ActivityAddDataToDB.class);
                intent.putExtra("day", day);
                intent.putExtra("month", month-1);
                intent.putExtra("year", year);
                intent.setAction("addData");
                startActivity(intent);
            }
        }
    }
}
