package com.example.y.travel_diary.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.y.travel_diary.Adapters.MapListAdapter;
import com.example.y.travel_diary.MainActivity;
import com.example.y.travel_diary.R;
import com.example.y.travel_diary.Utils.DataBaseHelper;
import com.example.y.travel_diary.Utils.MapAPI;

import net.daum.mf.map.api.MapView;

public class FragmentMap extends Fragment {
    private SharedPreferences pref = null;
    private DataBaseHelper dbhelper = null;
    private SQLiteDatabase db = null;
    private int id = -1;
    private ListView map_listView = null;
    private MapListAdapter madapter = null;
    private View view;
    private MapView mapView;
    private ViewGroup mapViewContainer;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.map_fragment, container, false);

        pref = getActivity().getSharedPreferences(MainActivity.TRAVEL_PREF, Context.MODE_PRIVATE);
        id = pref.getInt("id", -1);

        dbhelper = new DataBaseHelper(getActivity());
        db = dbhelper.getWritableDatabase();

        map_listView = (ListView) view.findViewById(R.id.list_map);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mapView = new MapView(getActivity());
        mapView.setDaumMapApiKey(MapAPI.MAP_API);

        mapViewContainer = (ViewGroup) view.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        if(pref != null && id != -1) {
            Cursor cursor = db.query(dbhelper.MAP_TABLE,
                    dbhelper.MAP_COL, null, new String[]{}, null, null,
                    dbhelper.MAP_ID + " ASC");

            madapter = new MapListAdapter(getActivity(), cursor);
            map_listView.setAdapter(madapter);

            cursor.close();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapViewContainer.removeView(mapView);
    }
}
