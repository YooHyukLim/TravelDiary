package com.example.y.travel_diary.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.y.travel_diary.MainActivity;
import com.example.y.travel_diary.R;

public class SplashActivity extends Activity {
    public final static int SPLASH_DISPLAY_LENGTH = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        TextView travelTextView = (TextView) findViewById(R.id.splash_travel);
        travelTextView.setTypeface(Typeface.createFromAsset(getAssets(), "apopcircle.otf"));

        TextView diaryTextView = (TextView) findViewById(R.id.splash_diary);
        diaryTextView.setTypeface(Typeface.createFromAsset(getAssets(), "apopcircle.otf"));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
