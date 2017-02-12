package com.igordubrovin.dayplanner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

class BuilderWeekData {
    private ArrayList<HashMap<String, String>> monthTask;
    private int month;
    private int year;
    private int countDaysMonth = 1;
    private boolean stopCreate = false;

    BuilderWeekData setMonthTask(ArrayList<HashMap<String, String>> monthTask, String month, String year){
        this.monthTask = monthTask;
        this.month = Integer.parseInt(month);
        this.year = Integer.parseInt(year);
        return this;
    }

    private HashMap<String, String> createDay(String dayWeek, int day, int month, int year){
        BuilderDateData builderDateData = new BuilderDateData();
        HashMap<String, String> otherDayWeek;
        otherDayWeek = builderDateData.setDayWeek(dayWeek)
                .setDayMonth(String.valueOf(day))
                .setMonth(String.valueOf(month + 1))
                .setYear(String.valueOf(year))
                .create();
        return otherDayWeek;
    }

    private HashMap<String, String> getDay(int day, int month, int year){
        HashMap<String, String> dayWeek = new HashMap<>();
        String dayDB;
        int dayOfWeek;
        for (HashMap<String, String> mapDB : monthTask) {
            dayDB = mapDB.get(BuilderDateData.KEY_DAY_MONTH);
            if (day == Integer.parseInt(dayDB)) {
                dayWeek.putAll(mapDB);
                dayWeek.put(BuilderDateData.KEY_MONTH, String.valueOf(Integer.parseInt(mapDB.get(BuilderDateData.KEY_MONTH))+1));
                return dayWeek;
            }
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek){
            case Calendar.MONDAY:
                dayWeek = createDay("Понедельник", day, month, year);
                break;
            case Calendar.TUESDAY:
                dayWeek = createDay("Вторник", day, month, year);
                break;
            case Calendar.WEDNESDAY:
                dayWeek = createDay("Среда", day, month, year);
                break;
            case Calendar.THURSDAY:
                dayWeek = createDay("Четверг", day, month, year);
                break;
            case Calendar.FRIDAY:
                dayWeek = createDay("Пятница", day, month, year);
                break;
            case Calendar.SATURDAY:
                dayWeek = createDay("Суббота", day, month, year);
                break;
            case Calendar.SUNDAY:
                dayWeek = createDay("Воскресенье", day, month, year);
                break;
            }
        return dayWeek;
    }

    private ArrayList<HashMap<String, String>> getWeek(){
        int daysOfPreviousMonth;
        int daysOfNextMonth;

        int firstDayFirstWeek;
        int lastDayMonth;
        int countDaysWeek = 0;
        int FIRST_DAY_MONTH = 1;
        int otherMonth;
        int otherYear;

        ArrayList<HashMap<String, String>> week = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        lastDayMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        if (countDaysMonth == FIRST_DAY_MONTH) {
            calendar.set(year, month, FIRST_DAY_MONTH);
            firstDayFirstWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (month != 0) {
                otherMonth = month - 1;
                otherYear = year;
            }
            else {
                otherMonth = 11;
                otherYear = year - 1;}
            switch (firstDayFirstWeek) {
                case Calendar.MONDAY:

                    break;
                case Calendar.TUESDAY:
                    calendar.set(Calendar.MONTH, otherMonth);
                    daysOfPreviousMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    week.add(getDay(daysOfPreviousMonth, otherMonth, otherYear));
                    countDaysWeek = countDaysWeek + 1;
                    break;
                case Calendar.WEDNESDAY:
                    calendar.set(Calendar.MONTH, otherMonth);
                    daysOfPreviousMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    for (int i = 1; i >= 0; i--) {
                        week.add(getDay(daysOfPreviousMonth - i, otherMonth, otherYear));
                        countDaysWeek = countDaysWeek + 1;
                    }
                    break;
                case Calendar.THURSDAY:
                    calendar.set(Calendar.MONTH, otherMonth);
                    daysOfPreviousMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    for (int i = 2; i >= 0; i--) {
                        week.add(getDay(daysOfPreviousMonth - i, otherMonth, otherYear));
                        countDaysWeek = countDaysWeek + 1;
                    }
                    break;
                case Calendar.FRIDAY:
                    calendar.set(Calendar.MONTH, otherMonth);
                    daysOfPreviousMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    for (int i = 3; i >= 0; i--) {
                        week.add(getDay(daysOfPreviousMonth - i, otherMonth, otherYear));
                        countDaysWeek = countDaysWeek + 1;
                    }
                    break;
                case Calendar.SATURDAY:
                    calendar.set(Calendar.MONTH, otherMonth);
                    daysOfPreviousMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    for (int i = 4; i >= 0; i--) {
                        week.add(getDay(daysOfPreviousMonth - i, otherMonth, otherYear));
                        countDaysWeek = countDaysWeek + 1;
                    }
                    break;
                case Calendar.SUNDAY:
                    calendar.set(Calendar.MONTH, otherMonth);
                    daysOfPreviousMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    for (int i = 5; i >= 0; i--) {
                        week.add(getDay(daysOfPreviousMonth - i, otherMonth, otherYear));
                        countDaysWeek = countDaysWeek + 1;
                    }
                    break;
            }
        }
        if (countDaysMonth <= lastDayMonth) {
            for (int i = countDaysWeek; i < 7; i++) {
                if (countDaysMonth > lastDayMonth) break;
                else {
                    week.add(getDay(countDaysMonth, month, year));
                    countDaysMonth++;
                }
            }
        }
        if (countDaysMonth > lastDayMonth) {
            if (month != 11) {
                otherMonth = month + 1;
                otherYear = year;
            }
            else {
                otherMonth = 0;
                otherYear = year + 1;
            }
            calendar.set(otherYear, otherMonth, FIRST_DAY_MONTH);
            daysOfNextMonth = calendar.get(Calendar.DAY_OF_WEEK);
            switch (daysOfNextMonth) {
                case Calendar.MONDAY:

                    break;
                case Calendar.TUESDAY:
                    for (int i = 1; i <= 6; i++){
                        week.add(getDay(i, otherMonth, otherYear));
                    }
                    break;
                case Calendar.WEDNESDAY:
                    for (int i = 1; i <= 5; i++){
                        week.add(getDay(i, otherMonth, otherYear));
                    }
                    break;
                case Calendar.THURSDAY:
                    for (int i = 1; i <= 4; i++){
                        week.add(getDay(i, otherMonth, otherYear));
                    }
                    break;
                case Calendar.FRIDAY:
                    for (int i = 1; i <= 3; i++){
                        week.add(getDay(i, otherMonth, otherYear));
                    }
                    break;
                case Calendar.SATURDAY:
                    for (int i = 1; i <= 2; i++){
                        week.add(getDay(i, otherMonth, otherYear));
                    }
                    break;
                case Calendar.SUNDAY:
                        week.add(getDay(1, otherMonth, otherYear));
                    break;
            }
            stopCreate = true;
        }
        return week;
    }

    ArrayList<ArrayList<HashMap<String,String>>> getSplitMonthWeeks(){
        ArrayList<ArrayList<HashMap<String,String>>> monthSplitWeeks = new ArrayList<>();
        for (int i = 0; i < 6; i++){
            monthSplitWeeks.add(getWeek());
            if (stopCreate) break;
        }
        countDaysMonth = 1;
        return monthSplitWeeks;
    }

}
