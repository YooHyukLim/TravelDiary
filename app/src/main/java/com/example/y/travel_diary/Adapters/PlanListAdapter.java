package com.example.y.travel_diary.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.y.travel_diary.R;
import com.example.y.travel_diary.Utils.CustomTouchListener;
import com.example.y.travel_diary.Utils.DataBaseHelper;
import com.example.y.travel_diary.Utils.PlanItem;
import com.example.y.travel_diary.Views.PlanListLayout;

import java.util.ArrayList;
import java.util.List;

public class PlanListAdapter extends BaseAdapter{
    private Context mContext;
    private List <PlanItem> planList = new ArrayList<PlanItem>();

    public PlanListAdapter(Context context) {
        this.mContext = context;
    }

    public PlanListAdapter(Context context, Cursor cursor) {
        this.mContext = context;

        int length = cursor.getCount();
        int idCol = cursor.getColumnIndex(DataBaseHelper.PLAN_ID);
        int nameCol = cursor.getColumnIndex(DataBaseHelper.PLAN_NAME);
        int contentCol = cursor.getColumnIndex(DataBaseHelper.PLAN_CONTENT);
        int sdateCol = cursor.getColumnIndex(DataBaseHelper.PLAN_SDATE);
        int edateCol = cursor.getColumnIndex(DataBaseHelper.PLAN_EDATE);
        int alarmCol = cursor.getColumnIndex(DataBaseHelper.PLAN_ALARM);

        for (int i=0; i<length; i++) {
            cursor.moveToNext();
            PlanItem pi = new PlanItem(cursor.getInt(idCol),
                                           cursor.getString(nameCol),
                                           cursor.getString(contentCol),
                                           cursor.getLong(sdateCol),
                                           cursor.getLong(edateCol),
                    cursor.getInt(alarmCol) == 1);
            planList.add(pi);
        }
    }

    public void addItem(PlanItem pi) {
        planList.add(pi);
    }

    public void clear() {
        planList.clear();
    }

    public void remove(int position) {
        planList.remove(position);
    }

    @Override
    public int getCount() {
        return planList.size();
    }

    @Override
    public PlanItem getItem(int position) {
        return planList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlanListLayout pl;

        if(convertView == null)
            pl = new PlanListLayout(mContext);
        else
            pl = (PlanListLayout) convertView;

        pl.setItem(planList.get(position));

        return pl;
    }

    @Override
    public long getItemId(int position) {
        return planList.get(position).getpid();
    }
}
