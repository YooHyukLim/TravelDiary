package com.example.y.travel_diary.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.y.travel_diary.R;
import com.example.y.travel_diary.Utils.PlanItem;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PlanListLayout extends LinearLayout{
    private TextView nameText = null;
    private TextView dateText = null;
    private ImageView imageView = null;

    public PlanListLayout(Context context) {
        super(context);
        init(context);
    }

    public PlanListLayout(Context context, PlanItem pi) {
        super(context);
        init(context);

        setItem(pi);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.planlist_layout, this, true);
        nameText = (TextView) findViewById(R.id.pl_name);
        dateText = (TextView) findViewById(R.id.pl_date);
        imageView = (ImageView) findViewById(R.id.alarmimg);
    }

    public void setItem(PlanItem pi) {
        nameText.setText(pi.getName());

        Date sdate = new Date(pi.getSdate());
        Date edate = new Date(pi.getEdate());
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-M/d H:mm");
        String date = sd.format(sdate).toString() + " ~ " + sd.format(edate).toString();
        dateText.setText(date);

        if(pi.getAlarm())
            imageView.setImageResource(R.drawable.alarm);
        else
            imageView.setImageResource(R.drawable.no_alarm);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}