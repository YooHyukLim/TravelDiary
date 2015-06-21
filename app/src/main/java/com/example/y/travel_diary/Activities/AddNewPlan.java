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
import android.database.Cursor;
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
import com.example.y.travel_diary.Utils.CustomTouchListener;
import com.example.y.travel_diary.Utils.DataBaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNewPlan extends Activity {

    private SharedPreferences pref = null;
    private DataBaseHelper dbhelper = null;
    private SQLiteDatabase db = null;
    private EditText nametext = null;
    private EditText contenttext = null;
    private TextView sdatetext = null;
    private TextView edatetext = null;
    private Date sdate = null;
    private Date edate = null;
    private Switch alarmswitch = null;
    private boolean isalarmed = false;
    private int pyear;
    private int pmonth;
    private int pday;
    private int id;
    private int max_pid;
    private int dbcheck = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewplan);

        dbhelper = new DataBaseHelper(this);
        db = dbhelper.getWritableDatabase();

        pref = getSharedPreferences(MainActivity.TRAVEL_PREF, Context.MODE_PRIVATE);
        id = pref.getInt("id", -1);
        Cursor cursor = db.query(DataBaseHelper.PLAN_TABLE,
                DataBaseHelper.PLAN_COL,
                DataBaseHelper._ID + "=?",
                new String[]{String.valueOf(id)}, null, null, dbhelper.PLAN_ID+" DESC");

        if (cursor.getCount() == 0)
            max_pid = 0;
        else {
            cursor.moveToNext();
            max_pid = cursor.getInt(cursor.getColumnIndex(dbhelper.PLAN_ID));
        }

        nametext = (EditText) findViewById(R.id.plan_name);
        contenttext = (EditText) findViewById(R.id.ContentText);
        sdatetext = (TextView) findViewById(R.id.plan_SdateText);
        edatetext = (TextView) findViewById(R.id.plan_EdateText);
        alarmswitch = (Switch) findViewById(R.id.switchAlarm);

        alarmswitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton cb, boolean isChecking){
                if(isChecking)
                    isalarmed = true;
                else
                    isalarmed = false;
            }
        });

        TextView create = (TextView) findViewById(R.id.createButton2);
        create.setOnTouchListener(new CustomTouchListener(create, 2));
        TextView cancel = (TextView) findViewById(R.id.cancelButton2);
        cancel.setOnTouchListener(new CustomTouchListener(cancel, 2));
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
            values.put(dbhelper._ID, id);
            values.put(dbhelper.PLAN_ID, max_pid +1);
            values.put(dbhelper.PLAN_NAME, nametext.getText().toString());
            values.put(dbhelper.PLAN_CONTENT, contenttext.getText().toString());
            values.put(dbhelper.PLAN_SDATE, sdate.getTime());
            values.put(dbhelper.PLAN_EDATE, edate.getTime());
            values.put(dbhelper.PLAN_ALARM, isalarmed);
            db.insert(dbhelper.PLAN_TABLE, null, values);

            if(isalarmed && System.currentTimeMillis() <= sdate.getTime()) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                Intent Intent = new Intent(this, AlertReceiver.class);

                Intent.putExtra("planName",nametext.getText().toString());

                alarmManager.set(AlarmManager.RTC_WAKEUP, sdate.getTime(),
                        PendingIntent.getBroadcast(this, max_pid+1, Intent,
                                PendingIntent.FLAG_UPDATE_CURRENT));
            }

            finish();

        } else {
            Toast.makeText(this,"빈칸을 채워주세요.",Toast.LENGTH_SHORT).show();
        }
    }

    public void plan_cancel(View v){
        finish();
    }

    public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

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
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-M/dd");
                Calendar c = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
                c2.clear();
                c.clear();

                pyear = year;
                pmonth = month;
                pday = day;

                if (dbcheck == 1) {
                    c.set(year, month, day);
                    c2.set(year, month, day+1);
                    if ((edate == null || c.getTimeInMillis() <= edate.getTime()) && c2.getTimeInMillis() >= System.currentTimeMillis()) {
                        DialogFragment myFragment = new TimeDialog();
                        myFragment.show(getFragmentManager(), "theDialog");
                    } else if(c.getTimeInMillis() < System.currentTimeMillis()){
                        Toast.makeText(getActivity(),"지난 시간입니다.",Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(getActivity(), "시작 날짜가 끝나는 날짜보다 늦습니다.", Toast.LENGTH_SHORT).show();
                } else if (dbcheck == 2) {
                    c.set(year, month, day+1);
                    if ((sdate == null || sdate.getTime() <= c.getTimeInMillis()) && c.getTimeInMillis() >= System.currentTimeMillis()) {
                        DialogFragment myFragment = new TimeDialog();
                        myFragment.show(getFragmentManager(), "theDialog");
                    } else if(c.getTimeInMillis() < System.currentTimeMillis()){
                        Toast.makeText(getActivity(),"지난 시간입니다.",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), "끝나는 날짜가 시작 날짜보다 빠릅니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }else{
                isvalid = 0;
            }
        }
    }

    public class TimeDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

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
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-M/dd H:mm");
            Calendar c = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c.clear();
            c2.clear();

            c.set(pyear,pmonth,pday,hourOfDay, minute);
            c2.set(pyear,pmonth,pday,hourOfDay, minute+1);

            if(isvalid == 0) {
                isvalid = 1;
                if (dbcheck == 1) {
                    if ((edate == null || c.getTimeInMillis() <= edate.getTime()) && c2.getTimeInMillis() >= System.currentTimeMillis()) {
                        sdate = new Date(c.getTimeInMillis());
                        sdatetext.setText(sd.format(sdate).toString());
                    } else if (c.getTimeInMillis() < System.currentTimeMillis()) {
                        Toast.makeText(getActivity(), "지난 시간입니다.", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getActivity(), "시작 날짜가 끝나는 날짜보다 늦습니다.", Toast.LENGTH_SHORT).show();
                } else if (dbcheck == 2) {
                    if ((sdate == null || sdate.getTime() <= c.getTimeInMillis()) && c2.getTimeInMillis() >= System.currentTimeMillis()) {
                        edate = new Date(c.getTimeInMillis());
                        edatetext.setText(sd.format(edate).toString());
                    } else if (c.getTimeInMillis() < System.currentTimeMillis()) {
                        Toast.makeText(getActivity(), "지난 시간입니다.", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getActivity(), "끝나는 날짜가 시작 날짜보다 빠릅니다.", Toast.LENGTH_SHORT).show();
                }
            }else
                isvalid = 0;
        }
    }
}
