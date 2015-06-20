package com.example.y.travel_diary.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.y.travel_diary.Adapters.MapListAdapter;
import com.example.y.travel_diary.MainActivity;
import com.example.y.travel_diary.R;
import com.example.y.travel_diary.Utils.DataBaseHelper;
import com.example.y.travel_diary.Utils.LocationTracker;
import com.example.y.travel_diary.Utils.MapAPI;
import com.example.y.travel_diary.Utils.MapItem;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapView;

import java.util.HashMap;

public class FragmentMap extends Fragment {
    private SharedPreferences pref = null;
    private DataBaseHelper dbhelper = null;
    private SQLiteDatabase db = null;
    private int id = -1;
    private int position = -1;

    private HashMap<Integer, MapPOIItem> mTagItemMap = new HashMap<Integer, MapPOIItem>();
    private ListView map_listView = null;
    private MapListAdapter madapter = null;
    private View view;
    private ImageView my_pos = null;
    private MapView mapView;
    private ViewGroup mapViewContainer;

    private LocationTracker tracker = null;

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

        tracker = new LocationTracker(getActivity().getApplicationContext());

        mapView = new MapView(getActivity());
        mapView.setDaumMapApiKey(MapAPI.MAP_API);

        mapViewContainer = (ViewGroup) view.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        if(pref != null && id != -1) {
            Cursor cursor = db.query(dbhelper.MAP_TABLE,
                    dbhelper.MAP_COL,
                    dbhelper._ID + "=?", new String[]{String.valueOf(id)}, null, null,
                    dbhelper.MAP_ID + " ASC");

            madapter = new MapListAdapter(getActivity(), cursor);
            map_listView.setAdapter(madapter);

            map_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MapItem mapItem = madapter.getItem(position);
                    MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(mapItem.getLatitude(), mapItem.getLongitude());
                    mapView.moveCamera(CameraUpdateFactory.newMapPoint(mapPoint));
                    mapView.selectPOIItem(mTagItemMap.get(mapItem.getMid()), false);
                }
            });

            map_listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
                    position = pos;
                    AlertDialog dialog = createDialog(parent);
                    dialog.show();
                    return true;
                }
            });

            cursor.close();

            showMakers ();
        }

        my_pos = (ImageView) view.findViewById(R.id.image_my_pos);
        my_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTagItemMap.get(0) != null) {
                    mapView.removePOIItem(mTagItemMap.get(0));
                    mTagItemMap.remove(0);
                }

                Location location = tracker.getLocation();

                if(location == null) {
                    Toast.makeText(getActivity(), "위치 정보를 받아 올 수 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(location.getLatitude(), location.getLongitude());
                MapPOIItem poiItem = makeMapPOIItem("내 위치", 0, mapPoint);
                mapView.addPOIItem(poiItem);
                mTagItemMap.put(poiItem.getTag(), poiItem);

                mapView.moveCamera(CameraUpdateFactory.newMapPoint(mapPoint));
                mapView.selectPOIItem(poiItem, false);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mapViewContainer.removeView(mapView);
        tracker.destroy();
    }

    public void showMakers () {
        MapPointBounds mapPointBounds = new MapPointBounds();
        int length = madapter.getCount();

        for (int i = 0; i < length; i++) {
            MapItem mapItem = madapter.getItem(i);

            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(mapItem.getLatitude(), mapItem.getLongitude());
            MapPOIItem poiItem = makeMapPOIItem(mapItem.getName(), mapItem.getMid(), mapPoint);

            mapView.addPOIItem(poiItem);
            mTagItemMap.put(poiItem.getTag(), poiItem);
        }

        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds));

        MapPOIItem[] poiItems = mapView.getPOIItems();
        if (poiItems.length > 0) {
            mapView.selectPOIItem(poiItems[0], false);
        }
    }

    public MapPOIItem makeMapPOIItem (String name, int tag, MapPoint mapPoint) {
        MapPOIItem poiItem = new MapPOIItem();
        poiItem.setItemName(name);
        poiItem.setTag(tag);
        poiItem.setMapPoint(mapPoint);
        poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        poiItem.setCustomImageResourceId(R.drawable.map_pin_blue);
        poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
        poiItem.setCustomSelectedImageResourceId(R.drawable.map_pin_red);
        poiItem.setCustomImageAutoscale(false);
        poiItem.setCustomImageAnchor(0.5f, 1.0f);

        return poiItem;
    }

    private AlertDialog createDialog(AdapterView adapterView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Delete?");

        builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int mid = madapter.getItem(position).getMid();
                db.delete(DataBaseHelper.MAP_TABLE,
                        DataBaseHelper._ID + "=? AND " + DataBaseHelper.MAP_ID + "=?",
                        new String[]{String.valueOf(id), String.valueOf(mid)});
                madapter.remove(position);
                madapter.notifyDataSetChanged();

                mapView.removePOIItem(mTagItemMap.get(mid));
                mTagItemMap.remove(mid);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //N/A
            }
        });

        return builder.create();
    }
}
