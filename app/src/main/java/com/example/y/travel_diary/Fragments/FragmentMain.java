package com.example.y.travel_diary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.y.travel_diary.Adapters.MainListAdapter;
import com.example.y.travel_diary.MainActivity;
import com.example.y.travel_diary.R;
import com.example.y.travel_diary.Utils.DataBaseHelper;
import com.example.y.travel_diary.Utils.MainItem;

public class FragmentMain extends Fragment {
    private SharedPreferences pref = null;
    private DataBaseHelper dbhelper = null;
    private SQLiteDatabase db = null;
    private View view = null;
    private ListView mainListView = null;
    private MainListAdapter madapter = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.main_fragment, container, false);

        pref = getActivity().getSharedPreferences(MainActivity.TRAVEL_PREF, Context.MODE_PRIVATE);

        dbhelper = new DataBaseHelper(getActivity());
        db = dbhelper.getWritableDatabase();

        mainListView = (ListView) view.findViewById(R.id.listview_main);
        madapter = new MainListAdapter(getActivity());
        mainListView.setAdapter(madapter);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fr = null;

                switch (madapter.getItem(position).getType()) {
                    case MainItem.MAP:
                        fr = new FragmentMap();
                        break;
                    case MainItem.BUCKET:
                        fr = new FragmentList();
                        break;
                    case MainItem.PLAN:
                        fr = new FragmentPlanner();
                        break;
                }

                if (fr == null)
                    return;

                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}