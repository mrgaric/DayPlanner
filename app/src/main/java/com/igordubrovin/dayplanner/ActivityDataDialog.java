package com.igordubrovin.dayplanner;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ActivityDataDialog extends AppCompatActivity {
    TextView tvData;
    TextView tvTheme;
    Toolbar toolbarData;
    private int day;
    private int month;
    private int year;
    static final int REQUEST_CODE_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_dialog);

        String data;
        String theme;
        Intent intent = getIntent();
        toolbarData = (Toolbar)findViewById(R.id.toolbar_view_data);
        setSupportActionBar(toolbarData);
        toolbarData.setTitle("Задачи");
        toolbarData.setNavigationIcon(R.drawable.ic_checkmark_holo_light);
        toolbarData.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        day = intent.getIntExtra("day", 0);
        month = intent.getIntExtra("month", 0);
        year = intent.getIntExtra("year", 0);

        data = intent.getStringExtra("data");
        tvData = (TextView)findViewById(R.id.tv_data);
        tvData.setText(data);
        theme = intent.getStringExtra("theme");
        tvTheme = (TextView)findViewById(R.id.tv_theme);
        tvTheme.setText(theme);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_data_dialog, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_remove:
                DelTask delTask = new DelTask();
                delTask.execute(day, month, year);
                break;
            case R.id.action_edit:
                Intent intent = new Intent(this, ActivityAddDataToDB.class);
                intent.putExtra("data", tvData.getText().toString());
                intent.putExtra("theme", tvTheme.getText().toString());
                intent.putExtra("day", day);
                intent.putExtra("month", month-1);
                intent.putExtra("year", year);
                intent.setAction("updateData");
                startActivityForResult(intent, REQUEST_CODE_ACTIVITY);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ACTIVITY){
            if (resultCode == RESULT_OK){
                tvData = (TextView)findViewById(R.id.tv_data);
                tvData.setText(data.getStringExtra("data"));
                tvTheme.setText(data.getStringExtra("theme"));
            }
            else if (resultCode == RESULT_CANCELED){
                finish();
            }
        }
    }

    private class DelTask extends AsyncTask<Integer, Void, Void>{

        @Override
        protected Void doInBackground(Integer... integers) {
            int day = integers[0];
            int month = integers[1];
            int year = integers[2];
            try {
                DataBase.syncDBWork(getApplicationContext(), null, DataBase.ACT_DEL, day, month, year);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setResult(RESULT_OK);
            finish();
        }
    }
}
