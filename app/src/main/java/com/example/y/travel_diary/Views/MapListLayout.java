package com.example.y.travel_diary.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.y.travel_diary.R;
import com.example.y.travel_diary.Utils.MapItem;

public class MapListLayout extends RelativeLayout{
    private TextView nameText = null;
    private Context mContext;

    public MapListLayout(Context context) {
        super(context);
        mContext = context;

        init();
    }

    public MapListLayout(Context context, MapItem mi) {
        super(context);
        mContext = context;

        init();
        setItem(mi);
    }

    private void init () {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.maplist_layout, this, true);
        nameText = (TextView) findViewById(R.id.ml_name);
    }

    public void setItem(MapItem mi) {
        nameText.setText(mi.getName());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
