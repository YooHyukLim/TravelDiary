package com.example.y.travel_diary.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.y.travel_diary.MainActivity;
import com.example.y.travel_diary.R;
import com.example.y.travel_diary.Utils.CustomTouchListener;
import com.example.y.travel_diary.Utils.DataBaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNewTravel extends Activity {

    private DataBaseHelper dbhelper = null;
    private SQLiteDatabase db = null;
    private EditText nametext = null;
    private TextView sdatetext = null;
    private TextView edatetext = null;
    private Date sdate = null;
    private Date edate = null;
    private int dbcheck = 0;
    private int max_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewtravel);

        dbhelper = new DataBaseHelper(this);
        db = dbhelper.getWritableDatabase();

        dbcheck = 0;
        nametext = (EditText) findViewById(R.id.NameText);
        sdatetext = (TextView) findViewById(R.id.SdateText);
        edatetext = (TextView) findViewById(R.id.EdateText);

        TextView create = (TextView) findViewById(R.id.createButton);
        create.setOnTouchListener(new CustomTouchListener(create, 2));
        TextView cancel = (TextView) findViewById(R.id.cancelButton);
        cancel.setOnTouchListener(new CustomTouchListener(cancel, 2));

        Cursor cursor = db.query(DataBaseHelper.TRAVEL_TABLE,
                new String[] {DataBaseHelper._ID}, null, new String[] {}, null, null,
                DataBaseHelper._ID + " DESC");

        if (cursor.getCount() == 0)
            max_id = 1;
        else {
            cursor.moveToNext();
            max_id = cursor.getInt(cursor.getColumnIndex(dbhelper._ID)) + 1;
        }
        cursor.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void SdateEdit(View v){
        dbcheck = 1;
        DialogFragment myFragment = new MyDialog();
        myFragment.show(getFragmentManager(), "theDialog");
    }

    public void EdateEdit(View v){
        dbcheck = 2;
        DialogFragment myFragment = new MyDialog();
        myFragment.show(getFragmentManager(), "theDialog");
    }

    public void insertDB(View v){
        ContentValues values = new ContentValues();
        String text = nametext.getText().toString();
        if(!text.trim().equals("") && sdate != null && edate != null) {
            values.put(dbhelper._ID, max_id);
            values.put(dbhelper.TRAVEL_NAME, nametext.getText().toString());
            values.put(dbhelper.TRAVEL_SDATE, sdate.getTime());
            values.put(dbhelper.TRAVEL_EDATE, edate.getTime());

            db.insert(dbhelper.TRAVEL_TABLE, null, values);

            SharedPreferences pref = getSharedPreferences(MainActivity.TRAVEL_PREF, MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("id", max_id);
            editor.putString("name", nametext.getText().toString());
            editor.apply();

            Intent intent = new Intent();
            this.setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this,"빈칸을 채워주세요.",Toast.LENGTH_SHORT).show();
        }
    }

    public void Cancel(View v){
        finish();
    }

    public void getResultFromDialog (int year, int month, int day) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-M/dd", getResources().getConfiguration().locale);
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(year, month, day);

        if(dbcheck == 1){
            sdate = new Date(c.getTimeInMillis());

            if(edate == null || sdate.getTime() <= edate.getTime())
                sdatetext.setText(sd.format(sdate));
            else
                Toast.makeText(this,"시작 날짜가 끝나는 날짜보다 늦습니다.",Toast.LENGTH_SHORT).show();
        }else if (dbcheck == 2){
            edate = new Date(c.getTimeInMillis());

            if(sdate == null || sdate.getTime() <= edate.getTime())
                edatetext.setText(sd.format(edate));
            else
                Toast.makeText(this,"끝나는 날짜가 시작 날짜보다 빠릅니다.",Toast.LENGTH_SHORT).show();
        }
    }

    public static class MyDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
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
            AddNewTravel activity = (AddNewTravel) getActivity();
            activity.getResultFromDialog(year, month, day);
        }
    }
}
