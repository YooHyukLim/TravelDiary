package com.example.y.travel_diary.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.y.travel_diary.R;
import com.example.y.travel_diary.Utils.MainItem;
import com.example.y.travel_diary.Utils.MapAPI;
import com.example.y.travel_diary.Utils.TravelItem;

import net.daum.mf.map.api.MapView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainListLayout extends LinearLayout{
    int type;
    public ViewGroup mapContainer;
    public MapView mapView;

    public MainListLayout(Context context, MainItem mi) {
        super(context);
        this.type = mi.getType();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (this.type) {
            case MainItem.MAP:
                inflater.inflate(R.layout.mainmap_layout, this, true);
                break;
            case MainItem.PLAN:
                inflater.inflate(R.layout.mainplan_layout, this, true);
                TextView text1 = (TextView) findViewById(R.id.main_plan_text1);
                TextView text2 = (TextView) findViewById(R.id.main_plan_text2);
                text1.setText(mi.getText1());
                text2.setText(mi.getText2());
                break;
            case MainItem.BUCKET:
                inflater.inflate(R.layout.mainbucket_layout, this, true);
                TextView text = (TextView) findViewById(R.id.main_plan_text);
                text.setText(mi.getText1());
                break;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (type == MainItem.MAP)
            mapContainer.removeView(mapView);
        super.onDetachedFromWindow();
    }
}
