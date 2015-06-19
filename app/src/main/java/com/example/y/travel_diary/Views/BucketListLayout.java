package com.example.y.travel_diary.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.y.travel_diary.R;
import com.example.y.travel_diary.Utils.BucketItem;
import com.example.y.travel_diary.Utils.TravelItem;

public class BucketListLayout extends RelativeLayout{
    TextView nameText = null;
    ImageView doneImage = null;

    public BucketListLayout(Context context) {
        super(context);
        init(context);
    }

    public BucketListLayout(Context context, BucketItem bi) {
        super(context);
        init(context);

        setItem(bi);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.bucketlist_layout, this, true);
        nameText = (TextView) findViewById(R.id.bl_name);
        doneImage = (ImageView) findViewById(R.id.bl_done);
    }

    public void setItem(BucketItem bi) {
        nameText.setText(bi.getName());

        if(bi.isDone())
            doneImage.setImageResource(R.drawable.o);
        else
            doneImage.setImageResource(R.drawable.x);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
