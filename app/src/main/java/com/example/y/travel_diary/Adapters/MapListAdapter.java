package com.example.y.travel_diary.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.y.travel_diary.Utils.DataBaseHelper;
import com.example.y.travel_diary.Utils.MapItem;
import com.example.y.travel_diary.Views.MapListLayout;

import java.util.ArrayList;
import java.util.List;

public class MapListAdapter extends BaseAdapter{
    private Context mContext;
    private List <MapItem> maplist = new ArrayList<MapItem>();

    public MapListAdapter(Context context, Cursor cursor) {
        this.mContext = context;

        int length = cursor.getCount();
        int midCol = cursor.getColumnIndex(DataBaseHelper.MAP_ID);
        int nameCol = cursor.getColumnIndex(DataBaseHelper.MAP_NAME);
        int addressCol = cursor.getColumnIndex(DataBaseHelper.MAP_ADDRESS);
        int longCol = cursor.getColumnIndex(DataBaseHelper.MAP_LONG);
        int latCol = cursor.getColumnIndex(DataBaseHelper.MAP_LAT);

        for (int i=0; i<length; i++) {
            cursor.moveToNext();
            MapItem mi = new MapItem(cursor.getInt(midCol),
                                     cursor.getString(nameCol),
                                     cursor.getString(addressCol),
                                     cursor.getDouble(longCol),
                                     cursor.getDouble(latCol));
            maplist.add(mi);
        }
    }

    public void remove(int position) {
        maplist.remove(position);
    }

    @Override
    public int getCount() {
        return maplist.size();
    }

    @Override
    public MapItem getItem(int position) {
        return maplist.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MapListLayout ml;

        if(convertView == null) {
            ml = new MapListLayout(mContext);
        } else {
            ml = (MapListLayout) convertView;
        }

        ml.setItem(maplist.get(position));

        return ml;
    }

    @Override
    public long getItemId(int position) {
        return maplist.get(position).getMid();
    }
}
