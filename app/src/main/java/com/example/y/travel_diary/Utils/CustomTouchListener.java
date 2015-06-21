package com.example.y.travel_diary.Utils;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.y.travel_diary.R;

public class CustomTouchListener implements View.OnTouchListener {
    private TextView textView;

    public CustomTouchListener(TextView textView) {
        this.textView = textView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                v.setBackgroundResource(R.drawable.custombutton_touched);
                textView.setTextColor(0xFFFFFFFF);
                break;
            case MotionEvent.ACTION_UP:
                v.setBackgroundResource(R.drawable.custombutton);
                textView.setTextColor(0xFF000000);
                break;
        }
        return false;
    }
}
