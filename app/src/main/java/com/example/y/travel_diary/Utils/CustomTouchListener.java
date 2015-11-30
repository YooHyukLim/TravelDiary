package com.example.y.travel_diary.Utils;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.y.travel_diary.R;

public class CustomTouchListener implements View.OnTouchListener {
    private TextView textView;
    private int type = 0;

    public CustomTouchListener(TextView textView) {
        this.textView = textView;
        type = 1;
    }

    public CustomTouchListener(TextView textView, int type) {
        this.textView = textView;
        this.type = type;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int padding = 0;

        if (type == 2)
            padding = dp_to_px(v.getContext(), 10);
        else
            padding = dp_to_px(v.getContext(), 8);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (type == 1) {
                    v.setBackgroundResource(R.drawable.custombutton_touched);
                    textView.setTextColor(0xFFFFFFFF);
                } else if (type == 2 || type == 3){
                    v.setBackgroundResource(R.drawable.customtextview_clicked);
                    textView.setTextColor(0xFF666699);
                    textView.setPadding(padding, padding, padding, padding);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (type == 1) {
                    v.setBackgroundResource(R.drawable.custombutton);
                    textView.setTextColor(0xFF000000);
                } else if (type == 2 || type == 3) {
                    v.setBackgroundResource(R.drawable.customtextview);
                    textView.setTextColor(0xFFFFFFFF);
                    textView.setPadding(padding, padding, padding, padding);
                }
                break;
        }
        return false;
    }

    public int dp_to_px (Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
