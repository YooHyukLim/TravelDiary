package com.example.y.travel_diary.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.y.travel_diary.MainActivity;
import com.example.y.travel_diary.R;
import com.example.y.travel_diary.Utils.DataBaseHelper;
import com.example.y.travel_diary.Utils.LocationTracker;
import com.example.y.travel_diary.Utils.MainItem;
import com.example.y.travel_diary.Utils.MapAPI;
import com.example.y.travel_diary.Utils.MapItem;
import com.example.y.travel_diary.Utils.PlanItem;
import com.example.y.travel_diary.Utils.TravelItem;
import com.example.y.travel_diary.Views.HomeListLayout;
import com.example.y.travel_diary.Views.MainListLayout;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainListAdapter extends BaseAdapter{
    final private static long MIN = 60*1000;
    final private static long HOUR = 60*MIN;
    final private static long DAY = 24*HOUR;

    private SharedPreferences pref = null;
    private DataBaseHelper dbhelper = null;
    private SQLiteDatabase db = null;
    private LocationTracker tracker = null;
    private int id;

    private Activity activity;
    private Context mContext;
    private List <MainItem> mainList = new ArrayList<MainItem>();

    public MainListAdapter(Activity activity) {
        this.activity = activity;
        this.mContext = activity.getApplicationContext();

        pref = activity.getSharedPreferences(MainActivity.TRAVEL_PREF, Context.MODE_PRIVATE);
        id = pref.getInt("id", -1);

        dbhelper = new DataBaseHelper(activity);
        db = dbhelper.getWritableDatabase();

        mainList.add(new MainItem(MainItem.MAP));

        addBucketToList();
        addPlansToList();
    }

    private void addPlansToList() {
        Cursor cursor;
        long date = new Date().getTime();
        int cnt = 0;

        cursor = db.query(DataBaseHelper.PLAN_TABLE,
                new String[]{dbhelper.PLAN_NAME, dbhelper.PLAN_SDATE},
                DataBaseHelper._ID + "=?",
                new String[]{String.valueOf(id)}, null, null, dbhelper.PLAN_SDATE + " ASC");

        int length = cursor.getCount();
        int nameCol = cursor.getColumnIndex(DataBaseHelper.PLAN_NAME);
        int sdateCol = cursor.getColumnIndex(DataBaseHelper.PLAN_SDATE);

        for (int i=0; i<length && cnt<=5; i++)  {
            cursor.moveToNext();
            long timeStamp = cursor.getLong(sdateCol) - date;

            if (timeStamp > MIN) {
                long day = timeStamp / DAY;
                long hour = (timeStamp % DAY) / HOUR;
                long min = (timeStamp % DAY % HOUR) / MIN;

                StringBuffer text = new StringBuffer();
                if(day != 0)
                    text.append(day+"일 ");
                if(hour != 0)
                    text.append(hour+"시간 ");
                text.append(min+"분 뒤에");

                MainItem mi = new MainItem(MainItem.PLAN,
                                        text.toString(),
                                        cursor.getString(nameCol));
                mainList.add(mi);
                cnt++;
            }
        }

        cursor.close();
    }

    public void addBucketToList() {
        Cursor cursor;

        cursor = db.query(DataBaseHelper.BUCKET_TABLE,
                new String[]{dbhelper.BUCKET_ID},
                DataBaseHelper._ID + "=? AND " + DataBaseHelper.BUCKET_DONE + "=?",
                new String[]{String.valueOf(id), "1"}, null, null, null);
        int cnt = cursor.getCount();
        cursor.close();

        cursor = db.query(DataBaseHelper.BUCKET_TABLE,
                new String[]{dbhelper.BUCKET_ID},
                DataBaseHelper._ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        int length = cursor.getCount();
        cursor.close();

        mainList.add(new MainItem(MainItem.BUCKET, "My bucket " + cnt + "/" + length));
    }

    public void clear() {
        mainList.clear();
    }

    public void remove(int position) {
        mainList.remove(position);
    }

    @Override
    public int getCount() {
        return mainList.size();
    }

    @Override
    public MainItem getItem(int position) {
        return mainList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MainListLayout ml = new MainListLayout(mContext, mainList.get(position));

        if(mainList.get(position).getType() == MainItem.MAP) {
            showPickers(ml);

            ml.mapView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            break;

                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }

                    v.onTouchEvent(event);
                    return true;
                }
            });
        }

        return ml;
    }

    private void showPickers(MainListLayout ml) {
        ml.mapView = new MapView(activity);
        ml.mapView.setDaumMapApiKey(MapAPI.MAP_API);

        ml.mapContainer = (ViewGroup) ml.findViewById(R.id.main_map_view);
        ml.mapContainer.addView(ml.mapView);

        Cursor cursor = db.query(dbhelper.MAP_TABLE,
                dbhelper.MAP_COL,
                dbhelper._ID + "=?", new String[]{String.valueOf(id)}, null, null,
                dbhelper.MAP_ID + " ASC");

        MapPointBounds mapPointBounds = new MapPointBounds();
        int length = cursor.getCount();
        int midCol = cursor.getColumnIndex(DataBaseHelper.MAP_ID);
        int nameCol = cursor.getColumnIndex(DataBaseHelper.MAP_NAME);
        int longCol = cursor.getColumnIndex(DataBaseHelper.MAP_LONG);
        int latCol = cursor.getColumnIndex(DataBaseHelper.MAP_LAT);

        tracker = new LocationTracker(mContext);
        Location location = tracker.getLocation();
        tracker.destroy();
        MapPoint mapPoint;

        if(location == null) {
            Toast.makeText(activity, "위치 정보를 받아 올 수 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            mapPoint = MapPoint.mapPointWithGeoCoord(location.getLatitude(), location.getLongitude());
            ml.mapView.addPOIItem(makeMapPOIItem("내 위치", 0, mapPoint));
        }

        for (int i = 0; i < length; i++) {
            cursor.moveToNext();

            mapPoint = MapPoint.mapPointWithGeoCoord(cursor.getDouble(latCol), cursor.getDouble(longCol));
            mapPointBounds.add(mapPoint);
            ml.mapView.addPOIItem(makeMapPOIItem(cursor.getString(nameCol),
                                                cursor.getInt(midCol),
                                                mapPoint));
        }

        ml.mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds));

        MapPOIItem[] poiItems = ml.mapView.getPOIItems();
        if (poiItems.length > 0) {
            ml.mapView.selectPOIItem(poiItems[0], false);
        }

        cursor.close();
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

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
