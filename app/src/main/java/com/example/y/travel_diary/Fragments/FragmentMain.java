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
import android.widget.ImageView;
import android.widget.ListView;

import com.example.y.travel_diary.Adapters.MainListAdapter;
import com.example.y.travel_diary.MainActivity;
import com.example.y.travel_diary.R;
import com.example.y.travel_diary.Utils.DataBaseHelper;
import com.example.y.travel_diary.Utils.MainItem;

public class FragmentMain extends Fragment {
    private MainListAdapter madapter = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ListView mainListView;
        View view;

        view = inflater.inflate(R.layout.main_fragment, container, false);

        mainListView = (ListView) view.findViewById(R.id.listview_main);
        madapter = new MainListAdapter(getActivity());
        mainListView.setAdapter(madapter);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fr = null;
                int fid = 0;
                int drawable = 0;

                switch (madapter.getItem(position).getType()) {
                    case MainItem.MAP:
                        fid = R.id.button_map;
                        drawable = R.drawable.mapimg;
                        fr = new FragmentMap();
                        break;
                    case MainItem.BUCKET:
                        fid = R.id.button_list;
                        drawable = R.drawable.listimg;
                        fr = new FragmentList();
                        break;
                    case MainItem.PLAN:
                        fid = R.id.button_planner;
                        drawable = R.drawable.planimg;
                        fr = new FragmentPlanner();
                        break;
                }

                if (fr == null)
                    return;

                MainActivity activity = (MainActivity) getActivity();
                activity.setCur_id(fid);

                ImageView imageView = (ImageView) activity.findViewById(R.id.button_main);
                imageView.setImageResource(R.drawable.mainimg2);
                imageView = (ImageView) activity.findViewById(fid);
                imageView.setImageResource(drawable);

                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}