package com.example.y.travel_diary.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.y.travel_diary.Utils.DataBaseHelper;
import com.example.y.travel_diary.Utils.TravelItem;
import com.example.y.travel_diary.Views.HomeListLayout;

import java.util.ArrayList;
import java.util.List;

public class TravelListAdapter extends BaseAdapter{
    private Context mContext;
    private List <TravelItem> travelList = new ArrayList<TravelItem>();

    public TravelListAdapter (Context context) {
        this.mContext = context;
    }

    public TravelListAdapter (Context context, Cursor cursor) {
        this.mContext = context;

        int length = cursor.getCount();
        int idCol = cursor.getColumnIndex(DataBaseHelper._ID);
        int nameCol = cursor.getColumnIndex(DataBaseHelper.TRAVEL_NAME);
        int sdateCol = cursor.getColumnIndex(DataBaseHelper.TRAVEL_SDATE);
        int edateCol = cursor.getColumnIndex(DataBaseHelper.TRAVEL_EDATE);

        for (int i=0; i<length; i++) {
            cursor.moveToNext();
            TravelItem ti = new TravelItem(cursor.getInt(idCol),
                                           cursor.getString(nameCol),
                                           cursor.getLong(sdateCol),
                                           cursor.getLong(edateCol));
            travelList.add(ti);
        }
    }

    public void addItem(TravelItem ti) {
        travelList.add(ti);
    }

    public void clear() {
        travelList.clear();
    }

    public void remove(int position) {
        travelList.remove(position);
    }

    @Override
    public int getCount() {
        return travelList.size();
    }

    @Override
    public TravelItem getItem(int position) {
        return travelList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HomeListLayout hl;

        if(convertView == null)
            hl = new HomeListLayout(mContext);
        else
            hl = (HomeListLayout) convertView;

        hl.setItem(travelList.get(position));

        return hl;
    }

    @Override
    public long getItemId(int position) {
        return travelList.get(position).get_id();
    }
}
