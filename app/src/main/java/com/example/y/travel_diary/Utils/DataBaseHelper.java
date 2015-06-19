package com.example.y.travel_diary.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper{
    final private static String NAME = "travel_db";
    final private static int VERSION = 1;
    private Context mContext = null;

    /* Tables' Name for DataBase. */
    final public static String TRAVEL_TABLE = "travel";
    final public static String MAP_TABLE = "map";
    final public static String BUCKET_TABLE = "bucket";
    final public static String PLAN_TABLE = "plan";

    /* Columns' Name for Tables. */
    final public static String _ID = "_id";
    final public static String TRAVEL_NAME = "name";
    final public static String TRAVEL_SDATE = "sdate";
    final public static String TRAVEL_EDATE = "edate";
    final public static String [] TRAVEL_COL = {_ID, TRAVEL_NAME, TRAVEL_SDATE, TRAVEL_EDATE};

    final public static String MAP_ID = "mid";
    final public static String MAP_NAME = "name";
    final public static String MAP_ADDRESS = "address";
    final public static String MAP_LONG = "long";
    final public static String MAP_LAT = "lat";
    final public static String [] MAP_COL = {MAP_ID, MAP_NAME, MAP_ADDRESS, MAP_LONG, MAP_LAT};

    final public static String BUCKET_ID = "bid";
    final public static String BUCKET_NAME = "name";
    final public static String BUCKET_DONE = "done";
    final public static String [] BUCKET_COL = {BUCKET_ID, BUCKET_NAME, BUCKET_DONE};

    final public static String PLAN_ID = "pid";
    final public static String PLAN_NAME = "name";
    final public static String PLAN_CONTENT = "content";
    final public static String PLAN_SDATE = "sdate"; // start time.
    final public static String PLAN_EDATE = "edate"; // end time.
    final public static String [] PLAN_COL = {_ID, PLAN_ID, PLAN_NAME, PLAN_CONTENT,
                                              PLAN_SDATE, PLAN_EDATE};

    /* Queries for creating tables. */
    final private static String CREATE_TRAVEL =
            "create table if not exists " + TRAVEL_TABLE
            + " (" + _ID + " integer primary key"
            + ", " + TRAVEL_NAME + " text not null"
            + ", " + TRAVEL_SDATE + " date not null"
            + ", " + TRAVEL_EDATE + " date not null);";
    final private static String CREATE_MAP =
            "create table if not exists " + MAP_TABLE
            + " (" + _ID + " integer not null"
            + ", " + MAP_ID + " integer not null"
            + ", " + MAP_NAME + " text not null"
            + ", " + MAP_ADDRESS + " text not null"
            + ", " + MAP_LONG + " real not null"
            + ", " + MAP_LAT + " real not null"
            + ", primary key (" + _ID +", "+ MAP_ID + ")"
            + ", foreign key (" + _ID + ") references " + TRAVEL_TABLE + "(" + _ID + ")"
            + " on delete cascade on update cascade);";
    final private static String CREATE_BUCKET =
            "create table if not exists " + BUCKET_TABLE
            + " (" + _ID + " integer not null"
            + ", " + BUCKET_ID + " integer not null"
            + ", " + BUCKET_NAME + " text not null"
            + ", " + BUCKET_DONE + " integer default 0"
            + ", primary key (" + _ID +", "+ BUCKET_ID + ")"
            + ", foreign key (" + _ID + ") references " + TRAVEL_TABLE + "(" + _ID + ")"
            + " on delete cascade on update cascade);";
    final private static String CREATE_PLAN =
            "create table if not exists " + PLAN_TABLE
            + " (" + _ID + " integer not null"
            + ", " + PLAN_ID + " integer not null"
            + ", " + PLAN_NAME + " text not null"
            + ", " + PLAN_CONTENT + " text"
            + ", " + PLAN_SDATE + " date not null"
            + ", " + PLAN_EDATE + " date not null"
            + ", primary key (" + _ID +", "+ PLAN_ID + ")"
            + ", foreign key (" + _ID + ") references " + TRAVEL_TABLE + "(" + _ID + ")"
            + " on delete cascade on update cascade);";

    public DataBaseHelper(Context context) {
        super (context, NAME, null, VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TRAVEL);
        db.execSQL(CREATE_MAP);
        db.execSQL(CREATE_BUCKET);
        db.execSQL(CREATE_PLAN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // N/A
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if(!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    public void dropTable () {
        mContext.deleteDatabase(NAME);
    }
}
