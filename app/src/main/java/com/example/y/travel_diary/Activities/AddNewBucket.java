package com.example.y.travel_diary.Activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.y.travel_diary.MainActivity;
import com.example.y.travel_diary.R;
import com.example.y.travel_diary.Utils.DataBaseHelper;

public class AddNewBucket extends Activity {

    private SharedPreferences pref = null;
    private DataBaseHelper dbhelper = null;
    private SQLiteDatabase db = null;
    private EditText nametext = null;
    private int id;
    private int max_bid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewbucket);

        dbhelper = new DataBaseHelper(this);
        db = dbhelper.getWritableDatabase();

        pref = getSharedPreferences(MainActivity.TRAVEL_PREF, Context.MODE_PRIVATE);
        id = pref.getInt("id", -1);
        Cursor cursor = db.query(DataBaseHelper.BUCKET_TABLE,
                DataBaseHelper.BUCKET_COL,
                DataBaseHelper._ID + "=?",
                new String[]{String.valueOf(id)}, null, null, dbhelper.BUCKET_ID+" DESC");

        if (cursor.getCount() == 0)
            max_bid = 0;
        else {
            cursor.moveToNext();
            max_bid = cursor.getInt(cursor.getColumnIndex(dbhelper.BUCKET_ID));
        }
        cursor.close();

        nametext = (EditText) findViewById(R.id.bucket_name);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void bucket_insertDB(View v) {
        String text = nametext.getText().toString();
        if(!text.trim().equals("")) {
            ContentValues values = new ContentValues();
            values.put(dbhelper._ID, id);
            values.put(dbhelper.BUCKET_ID, max_bid+1);
            values.put(dbhelper.BUCKET_NAME, nametext.getText().toString());
            db.insert(dbhelper.BUCKET_TABLE, null, values);

            finish();
        } else {
            Toast.makeText(this,"빈칸을 채워주세요.",Toast.LENGTH_SHORT).show();
        }
    }

    public void bucket_cancel(View v){
        finish();
    }
}
