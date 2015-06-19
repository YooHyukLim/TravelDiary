package com.example.y.travel_diary.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.y.travel_diary.Utils.BucketItem;
import com.example.y.travel_diary.Utils.DataBaseHelper;
import com.example.y.travel_diary.Views.BucketListLayout;

import java.util.ArrayList;
import java.util.List;

public class BucketListAdapter extends BaseAdapter{
    private Context mContext;
    private List <BucketItem> bucketlist = new ArrayList<BucketItem>();

    public BucketListAdapter(Context context) {
        this.mContext = context;
    }

    public BucketListAdapter(Context context, Cursor cursor) {
        this.mContext = context;

        int length = cursor.getCount();
        int bidCol = cursor.getColumnIndex(DataBaseHelper.BUCKET_ID);
        int nameCol = cursor.getColumnIndex(DataBaseHelper.BUCKET_NAME);
        int doneCol = cursor.getColumnIndex(DataBaseHelper.BUCKET_DONE);

        for (int i=0; i<length; i++) {
            cursor.moveToNext();
            BucketItem bi = new BucketItem(cursor.getInt(bidCol),
                                           cursor.getString(nameCol),
                                           cursor.getInt(doneCol) == 1);
            bucketlist.add(bi);
        }
    }

    public void addItem(BucketItem bi) {
        bucketlist.add(bi);
    }

    public void clear() {
        bucketlist.clear();
    }

    public void remove(int position) {
        bucketlist.remove(position);
    }

    @Override
    public int getCount() {
        return bucketlist.size();
    }

    @Override
    public BucketItem getItem(int position) {
        return bucketlist.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BucketListLayout bl;

        if(convertView == null)
            bl = new BucketListLayout(mContext);
        else
            bl = (BucketListLayout) convertView;

        bl.setItem(bucketlist.get(position));

        return bl;
    }

    @Override
    public long getItemId(int position) {
        return bucketlist.get(position).getBid();
    }
}
