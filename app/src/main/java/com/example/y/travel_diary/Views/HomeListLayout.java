package com.example.y.travel_diary.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.y.travel_diary.R;
import com.example.y.travel_diary.Utils.TravelItem;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeListLayout extends LinearLayout{
    TextView nameText = null;
    TextView dateText = null;

    public HomeListLayout(Context context) {
        super(context);
        init(context);
    }

    public HomeListLayout(Context context, TravelItem ti) {
        super(context);
        init(context);

        setItem(ti);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.homelist_layout, this, true);
        nameText = (TextView) findViewById(R.id.hl_name);
        dateText = (TextView) findViewById(R.id.hl_date);
    }

    public void setItem(TravelItem ti) {
        nameText.setText(ti.getName());

        Date sdate = new Date(ti.getSdate()*1000);
        Date edate = new Date(ti.getEdate()*1000);
        SimpleDateFormat sd = new SimpleDateFormat("YYYY-M/d");
        String date = sd.format(sdate).toString() + " ~ " + sd.format(edate).toString();
        dateText.setText(date);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
