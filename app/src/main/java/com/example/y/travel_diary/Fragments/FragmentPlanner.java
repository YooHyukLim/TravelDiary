package com.example.y.travel_diary.Fragments;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.y.travel_diary.R;
import com.example.y.travel_diary.Utils.DataBaseHelper;

public class FragmentPlanner extends Fragment {
    private DataBaseHelper dbhelper = null;
    private SQLiteDatabase db = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.planner_fragment, container, false);

        dbhelper = new DataBaseHelper(getActivity());
        db = dbhelper.getWritableDatabase();

        return view;
    }
}