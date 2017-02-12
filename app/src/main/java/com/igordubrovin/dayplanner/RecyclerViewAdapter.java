package com.igordubrovin.dayplanner;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private Context context;
    private ArrayList<ArrayList<HashMap<String, String>>> mapArrayList;

    RecyclerViewAdapter(Context context){
        this.context = context;
    }

    void registerNewData(ArrayList<ArrayList<HashMap<String, String>>> mapArrayList){
        this.mapArrayList = mapArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String date;
        String theme;
        rstScrlView(holder);
        date = getDateStr(mapArrayList.get(position).get(0));
        holder.tvDay1.setText(mapArrayList.get(position).get(0).get(BuilderDateData.KEY_DAY_WEEK));
        holder.tvDate1.setText(date);
        theme = "Тема: " + mapArrayList.get(position).get(0).get(BuilderDateData.KEY_THEME);
        holder.tvTheme1.setText(theme);

        date = getDateStr(mapArrayList.get(position).get(1));
        holder.tvDay2.setText(mapArrayList.get(position).get(1).get(BuilderDateData.KEY_DAY_WEEK));
        holder.tvDate2.setText(date);
        theme = "Тема: " + mapArrayList.get(position).get(1).get(BuilderDateData.KEY_THEME);
        holder.tvTheme2.setText(theme);

        date = getDateStr(mapArrayList.get(position).get(2));
        holder.tvDay3.setText(mapArrayList.get(position).get(2).get(BuilderDateData.KEY_DAY_WEEK));
        holder.tvDate3.setText(date);
        theme = "Тема: " + mapArrayList.get(position).get(2).get(BuilderDateData.KEY_THEME);
        holder.tvTheme3.setText(theme);

        date = getDateStr(mapArrayList.get(position).get(3));
        holder.tvDay4.setText(mapArrayList.get(position).get(3).get(BuilderDateData.KEY_DAY_WEEK));
        holder.tvDate4.setText(date);
        theme = "Тема: " + mapArrayList.get(position).get(3).get(BuilderDateData.KEY_THEME);
        holder.tvTheme4.setText(theme);

        date = getDateStr(mapArrayList.get(position).get(4));
        holder.tvDay5.setText(mapArrayList.get(position).get(4).get(BuilderDateData.KEY_DAY_WEEK));
        holder.tvDate5.setText(date);
        theme = "Тема: " + mapArrayList.get(position).get(4).get(BuilderDateData.KEY_THEME);
        holder.tvTheme5.setText(theme);

        date = getDateStr(mapArrayList.get(position).get(5));
        holder.tvDay6.setText(mapArrayList.get(position).get(5).get(BuilderDateData.KEY_DAY_WEEK));
        holder.tvDate6.setText(date);
        theme = "Тема: " + mapArrayList.get(position).get(5).get(BuilderDateData.KEY_THEME);
        holder.tvTheme6.setText(theme);

        date = getDateStr(mapArrayList.get(position).get(6));
        holder.tvDay7.setText(mapArrayList.get(position).get(6).get(BuilderDateData.KEY_DAY_WEEK));
        holder.tvDate7.setText(date);
        theme = "Тема: " + mapArrayList.get(position).get(6).get(BuilderDateData.KEY_THEME);
        holder.tvTheme7.setText(theme);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private String getDateStr(HashMap<String, String> map){
        String date;
        if (Integer.parseInt(map.get(BuilderDateData.KEY_DAY_MONTH))<10){
            date = "0" + map.get(BuilderDateData.KEY_DAY_MONTH) + ".";
        }
        else date = map.get(BuilderDateData.KEY_DAY_MONTH) + ".";
        if (Integer.parseInt(map.get(BuilderDateData.KEY_MONTH))<10){
            date = date.concat("0").concat(map.get(BuilderDateData.KEY_MONTH)).concat(".");
        }
        else date = date.concat(map.get(BuilderDateData.KEY_MONTH)).concat(".");
        date = date.concat(map.get(BuilderDateData.KEY_YEAR));
        return date;
    }

    @Override
    public int getItemCount() {
        return mapArrayList.size();
    }

    interface CallbackDelDataBD{
        void callBackDelData();
    }

    interface CallbackClickCV{
        void clickCV(int day, int mont, int year);
    }

    private CallbackDelDataBD callbackDelDataBD;
    private CallbackClickCV callbackClickCV;

    void registerCallback(CallbackDelDataBD callbackDelDataBD, CallbackClickCV callbackClickCV){
        this.callbackDelDataBD = callbackDelDataBD;
        this.callbackClickCV = callbackClickCV;
    }

    void rstScrlView(ViewHolder viewHolder){
        viewHolder.scrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private final int ITEM_DEL_1 = 11;

        private final int ITEM_DEL_2 = 21;

        private final int ITEM_DEL_3 = 31;

        private final int ITEM_DEL_4 = 41;

        private final int ITEM_DEL_5 = 51;

        private final int ITEM_DEL_6 = 61;

        private final int ITEM_DEL_7 = 71;

        TextView tvDay1;
        TextView tvDate1;
        TextView tvTheme1;
        TextView tvDay2;
        TextView tvDate2;
        TextView tvTheme2;
        TextView tvDay3;
        TextView tvDate3;
        TextView tvTheme3;
        TextView tvDay4;
        TextView tvDate4;
        TextView tvTheme4;
        TextView tvDay5;
        TextView tvDate5;
        TextView tvTheme5;
        TextView tvDay6;
        TextView tvDate6;
        TextView tvTheme6;
        TextView tvDay7;
        TextView tvDate7;
        TextView tvTheme7;

        CardView cvPath1;
        CardView cvPath2;
        CardView cvPath3;
        CardView cvPath4;
        CardView cvPath5;
        CardView cvPath6;
        CardView cvPath7;

        ScrollView scrollView;

        ViewHolder(View itemView) {
            super(itemView);

            scrollView = (ScrollView)itemView.findViewById(R.id.scrl);

            tvDay1 = (TextView)itemView.findViewById(R.id.tvDay1);
            tvDate1 = (TextView)itemView.findViewById(R.id.tvDate1);
            tvTheme1 = (TextView)itemView.findViewById(R.id.tvTheme1);

            tvDay2 = (TextView)itemView.findViewById(R.id.tvDay2);
            tvDate2 = (TextView)itemView.findViewById(R.id.tvDate2);
            tvTheme2 = (TextView)itemView.findViewById(R.id.tvTheme2);

            tvDay3 = (TextView)itemView.findViewById(R.id.tvDay3);
            tvDate3 = (TextView)itemView.findViewById(R.id.tvDate3);
            tvTheme3 = (TextView)itemView.findViewById(R.id.tvTheme3);

            tvDay4 = (TextView)itemView.findViewById(R.id.tvDay4);
            tvDate4 = (TextView)itemView.findViewById(R.id.tvDate4);
            tvTheme4 = (TextView)itemView.findViewById(R.id.tvTheme4);

            tvDay5 = (TextView)itemView.findViewById(R.id.tvDay5);
            tvDate5 = (TextView)itemView.findViewById(R.id.tvDate5);
            tvTheme5 = (TextView)itemView.findViewById(R.id.tvTheme5);

            tvDay6 = (TextView)itemView.findViewById(R.id.tvDay6);
            tvDate6 = (TextView)itemView.findViewById(R.id.tvDate6);
            tvTheme6 = (TextView)itemView.findViewById(R.id.tvTheme6);

            tvDay7 = (TextView)itemView.findViewById(R.id.tvDay7);
            tvDate7 = (TextView)itemView.findViewById(R.id.tvDate7);
            tvTheme7 = (TextView)itemView.findViewById(R.id.tvTheme7);

            cvPath1 = (CardView)itemView.findViewById(R.id.cvPath1);
            cvPath2 = (CardView)itemView.findViewById(R.id.cvPath2);
            cvPath3 = (CardView)itemView.findViewById(R.id.cvPath3);
            cvPath4 = (CardView)itemView.findViewById(R.id.cvPath4);
            cvPath5 = (CardView)itemView.findViewById(R.id.cvPath5);
            cvPath6 = (CardView)itemView.findViewById(R.id.cvPath6);
            cvPath7 = (CardView)itemView.findViewById(R.id.cvPath7);

            cvPath1.setOnCreateContextMenuListener(this);
            cvPath2.setOnCreateContextMenuListener(this);
            cvPath3.setOnCreateContextMenuListener(this);
            cvPath4.setOnCreateContextMenuListener(this);
            cvPath5.setOnCreateContextMenuListener(this);
            cvPath6.setOnCreateContextMenuListener(this);
            cvPath7.setOnCreateContextMenuListener(this);

            cvPath1.setOnClickListener(clickItem);
            cvPath2.setOnClickListener(clickItem);
            cvPath3.setOnClickListener(clickItem);
            cvPath4.setOnClickListener(clickItem);
            cvPath5.setOnClickListener(clickItem);
            cvPath6.setOnClickListener(clickItem);
            cvPath7.setOnClickListener(clickItem);
        }

        View.OnClickListener clickItem = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] date;
                switch (view.getId()){
                    case R.id.cvPath1:
                        date = getDate(tvDate1.getText().toString());
                        break;
                    case R.id.cvPath2:
                        date = getDate(tvDate2.getText().toString());
                        break;
                    case R.id.cvPath3:
                        date = getDate(tvDate3.getText().toString());
                        break;
                    case R.id.cvPath4:
                        date = getDate(tvDate4.getText().toString());
                        break;
                    case R.id.cvPath5:
                        date = getDate(tvDate5.getText().toString());
                        break;
                    case R.id.cvPath6:
                        date = getDate(tvDate6.getText().toString());
                        break;
                    case R.id.cvPath7:
                        date = getDate(tvDate7.getText().toString());
                        break;
                    default: date = new int[] {0,0,0};
                }
                callbackClickCV.clickCV(date[0], date[1], date[2]);
            }
        };

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            switch (view.getId()) {
                case R.id.cvPath1:
                    contextMenu.add(Menu.NONE, ITEM_DEL_1, Menu.NONE, "удалить").setOnMenuItemClickListener(clickMenuItem);
             //       contextMenu.add(Menu.NONE, ITEM_UPD_1, Menu.NONE, "изменить").setOnMenuItemClickListener(clickMenuItem);
                    break;
                case R.id.cvPath2:
                    contextMenu.add(Menu.NONE, ITEM_DEL_2, Menu.NONE, "удалить").setOnMenuItemClickListener(clickMenuItem);
              //      contextMenu.add(Menu.NONE, ITEM_UPD_2, Menu.NONE, "изменить").setOnMenuItemClickListener(clickMenuItem);
                    break;
                case R.id.cvPath3:
                    contextMenu.add(Menu.NONE, ITEM_DEL_3, Menu.NONE, "удалить").setOnMenuItemClickListener(clickMenuItem);
              //      contextMenu.add(Menu.NONE, ITEM_UPD_3, Menu.NONE, "изменить").setOnMenuItemClickListener(clickMenuItem);
                    break;
                case R.id.cvPath4:
                    contextMenu.add(Menu.NONE, ITEM_DEL_4, Menu.NONE, "удалить").setOnMenuItemClickListener(clickMenuItem);
              //      contextMenu.add(Menu.NONE, ITEM_UPD_4, Menu.NONE, "изменить").setOnMenuItemClickListener(clickMenuItem);
                    break;
                case R.id.cvPath5:
                    contextMenu.add(Menu.NONE, ITEM_DEL_5, Menu.NONE, "удалить").setOnMenuItemClickListener(clickMenuItem);
              //      contextMenu.add(Menu.NONE, ITEM_UPD_5, Menu.NONE, "изменить").setOnMenuItemClickListener(clickMenuItem);
                    break;
                case R.id.cvPath6:
                    contextMenu.add(Menu.NONE, ITEM_DEL_6, Menu.NONE, "удалить").setOnMenuItemClickListener(clickMenuItem);
              //      contextMenu.add(Menu.NONE, ITEM_UPD_6, Menu.NONE, "изменить").setOnMenuItemClickListener(clickMenuItem);
                    break;
                case R.id.cvPath7:
                    contextMenu.add(Menu.NONE, ITEM_DEL_7, Menu.NONE, "удалить").setOnMenuItemClickListener(clickMenuItem);
              //      contextMenu.add(Menu.NONE, ITEM_UPD_7, Menu.NONE, "изменить").setOnMenuItemClickListener(clickMenuItem);
                    break;
            }
        }

        final MenuItem.OnMenuItemClickListener clickMenuItem = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case ITEM_DEL_1:
                        delDB(tvDate1.getText().toString());
                        break;
                    case ITEM_DEL_2:
                        delDB(tvDate2.getText().toString());
                        break;
                    case ITEM_DEL_3:
                        delDB(tvDate3.getText().toString());
                        break;
                    case ITEM_DEL_4:
                        delDB(tvDate4.getText().toString());
                        break;
                    case ITEM_DEL_5:
                        delDB(tvDate5.getText().toString());
                        break;
                    case ITEM_DEL_6:
                        delDB(tvDate6.getText().toString());
                        break;
                    case ITEM_DEL_7:
                        delDB(tvDate7.getText().toString());
                        break;
                }
                callbackDelDataBD.callBackDelData();
                return true;
            }
        };

        private  void delDB(String str){
            int[] date = getDate(str);
            try {
                DataBase.syncDBWork(context, null, DataBase.ACT_DEL, date[0], date[1], date[2]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private int[] getDate(String date){
            int day = 0;
            int month = 0;
            int year;
            int point = 0;
            for (int i = 0; i < date.length(); i++){
                if (date.charAt(i) == '.') {
                    if (i <= 2) {
                        point = i + 1;
                        day = Integer.parseInt(date.substring(0, i));
                    }
                    else if (i > 2 && i <= 6){
                        month = Integer.parseInt(date.substring(point , i));
                        point = i + 1;
                        break;
                    }
                }
            }
            year = Integer.parseInt(date.substring(point));
            return new int[]{day, month, year};
        }
    }
}
