package com.example.y.travel_diary.Activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.y.travel_diary.MainActivity;
import com.example.y.travel_diary.R;
import com.example.y.travel_diary.Utils.AlertReceiver;
import com.example.y.travel_diary.Utils.DataBaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditOldPlan extends Activity {
    private DataBaseHelper dbhelper = null;
    private SQLiteDatabase db = null;
    private EditText nametext = null;
    private EditText contenttext = null;
    private TextView sdatetext = null;
    private TextView edatetext = null;
    private Date sdate = null;
    private Date edate = null;
    private boolean isalarmed = false;
    private boolean isa = false;
    private int pyear;
    private int pmonth;
    private int pday;
    private int id;
    private int plan_id;
    private int dbcheck = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Switch alarmswitch;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewplan);

        dbhelper = new DataBaseHelper(this);
        db = dbhelper.getWritableDatabase();

        Intent intent = getIntent();
        id = intent.getIntExtra("_id",-1);
        plan_id = intent.getIntExtra("pid",-1);
        String name = intent.getStringExtra("pn");
        String content = intent.getStringExtra("pc");
        long sd = intent.getLongExtra("ps", 0);
        long ed = intent.getLongExtra("pe", 0);
        isa = intent.getBooleanExtra("pa",false);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M/dd H:mm", getResources().getConfiguration().locale);
        sdate = new Date(sd);
        edate = new Date(ed);

        final String sds = sdf.format(sdate).toString();
        final String eds = sdf.format(edate).toString();

        nametext = (EditText) findViewById(R.id.plan_name);
        contenttext = (EditText) findViewById(R.id.ContentText);
        sdatetext = (TextView) findViewById(R.id.plan_SdateText);
        edatetext = (TextView) findViewById(R.id.plan_EdateText);
        alarmswitch = (Switch) findViewById(R.id.switchAlarm);

        nametext.setText(name);
        contenttext.setText(content);
        sdatetext.setText(sds);
        edatetext.setText(eds);
        alarmswitch.setChecked(isa);

        alarmswitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton cb, boolean isChecking){
                if(isChecking)
                    isalarmed = true;
                else
                    isalarmed = false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void plan_SdateEdit(View v){
        dbcheck = 1;
        DialogFragment myFragment = new DateDialog();
        myFragment.show(getFragmentManager(), "theDialog");
    }

    public void plan_EdateEdit(View v){
        dbcheck = 2;
        DialogFragment myFragment = new DateDialog();
        myFragment.show(getFragmentManager(), "theDialog");
    }

    public void plan_insertDB(View v) {
        String text = nametext.getText().toString();
        if(!text.trim().equals("") && sdate != null && edate != null) {
            ContentValues values = new ContentValues();
            values.put(dbhelper.PLAN_NAME, nametext.getText().toString());
            values.put(dbhelper.PLAN_CONTENT, contenttext.getText().toString());
            values.put(dbhelper.PLAN_SDATE, sdate.getTime());
            values.put(dbhelper.PLAN_EDATE, edate.getTime());
            values.put(dbhelper.PLAN_ALARM, isalarmed);
            db.update(dbhelper.PLAN_TABLE, values,
                    dbhelper._ID + "=? AND " + dbhelper.PLAN_ID + "=?",
                    new String[]{String.valueOf(id), String.valueOf(plan_id)});

            if(isa != isalarmed){
                if(isalarmed == true && System.currentTimeMillis() <= sdate.getTime()){
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    Intent Intent = new Intent(this, AlertReceiver.class);

                    alarmManager.set(AlarmManager.RTC_WAKEUP, sdate.getTime(),
                            PendingIntent.getBroadcast(this, plan_id, Intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT));
                }else if(isalarmed == false){
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    Intent Intent = new Intent(this, AlertReceiver.class);

                    alarmManager.cancel(PendingIntent.getBroadcast(this, plan_id, Intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT));
                }
            }

            finish();
        }else{
            Toast.makeText(this,"빈칸을 채워주세요.",Toast.LENGTH_SHORT).show();
        }
    }

    public void plan_cancel(View v){
        finish();
    }

    public void getResultFromDateDialog (int year, int month, int day) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-M/dd", getResources().getConfiguration().locale);
        Calendar c = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c2.clear();
        c.clear();

        pyear = year;
        pmonth = month;
        pday = day;

        if (dbcheck == 1) {
            c.set(year, month, day);
            c2.set(year, month, day + 1);
            if ((edate == null || c.getTimeInMillis() <= edate.getTime()) && c2.getTimeInMillis() >= System.currentTimeMillis()) {
                DialogFragment myFragment = new TimeDialog();
                myFragment.show(getFragmentManager(), "theDialog");
            } else if(c.getTimeInMillis() < System.currentTimeMillis()){
                Toast.makeText(this,"지난 시간입니다.",Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(this, "시작 날짜가 끝나는 날짜보다 늦습니다.", Toast.LENGTH_SHORT).show();
        } else if (dbcheck == 2) {
            c.set(year, month, day+1);
            if ((sdate == null || sdate.getTime() <= c.getTimeInMillis()) && c.getTimeInMillis() >= System.currentTimeMillis()) {
                DialogFragment myFragment = new TimeDialog();
                myFragment.show(getFragmentManager(), "theDialog");
            } else if(c.getTimeInMillis() < System.currentTimeMillis()){
                Toast.makeText(this,"지난 시간입니다.",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "끝나는 날짜가 시작 날짜보다 빠릅니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getResultFromTimeDialog (int hourOfDay, int minute) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-M/dd H:mm", getResources().getConfiguration().locale);
        Calendar c = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c.clear();
        c2.clear();

        c.set(pyear,pmonth,pday,hourOfDay, minute);
        c2.set(pyear, pmonth, pday, hourOfDay, minute + 1);

        if (dbcheck == 1) {
            if ((edate == null || c.getTimeInMillis() <= edate.getTime()) && c2.getTimeInMillis() >= System.currentTimeMillis()) {
                sdate = new Date(c.getTimeInMillis());
                sdatetext.setText(sd.format(sdate));
            } else if (c.getTimeInMillis() < System.currentTimeMillis()) {
                Toast.makeText(this, "지난 시간입니다.", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "시작 날짜가 끝나는 날짜보다 늦습니다.", Toast.LENGTH_SHORT).show();
        } else if (dbcheck == 2) {
            if ((sdate == null || sdate.getTime() <= c.getTimeInMillis()) && c2.getTimeInMillis() >= System.currentTimeMillis()) {
                edate = new Date(c.getTimeInMillis());
                edatetext.setText(sd.format(edate));
            } else if (c.getTimeInMillis() < System.currentTimeMillis()) {
                Toast.makeText(this, "지난 시간입니다.", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "끝나는 날짜가 시작 날짜보다 빠릅니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public static class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        int isvalid = 0;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            if(isvalid == 0) {
                isvalid = 1;
                EditOldPlan activity = (EditOldPlan) getActivity();
                activity.getResultFromDateDialog(year, month, day);
            }else{
                isvalid = 0;
            }
        }
    }

    public static class TimeDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        int isvalid = 0;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    android.text.format.DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if(isvalid == 0) {
                isvalid = 1;
                EditOldPlan activity = (EditOldPlan) getActivity();
                activity.getResultFromTimeDialog(hourOfDay, minute);
            }else
                isvalid = 0;
        }
    }
}
