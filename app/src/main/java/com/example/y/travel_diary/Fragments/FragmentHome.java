package com.example.y.travel_diary.Fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.y.travel_diary.R;
import com.example.y.travel_diary.Utils.DataBaseHelper;

public class FragmentHome extends Fragment {
    private DataBaseHelper dbhelper = null;
    private SQLiteDatabase db = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        dbhelper = new DataBaseHelper(getActivity());
        db = dbhelper.getWritableDatabase();

        Cursor cursor = db.query(DataBaseHelper.TRAVEL_TABLE,
                                 DataBaseHelper.TRAVEL_COL, null, new String[] {}, null, null, null);

        // If there isn't any travel information, show the button for adding new travel.
        if(cursor.getCount() == 0)
            view.findViewById(R.id.list_travel).setVisibility(View.GONE);
        else
            view. findViewById(R.id.button_new_start).setVisibility(View.GONE);

        return view;
    }
}