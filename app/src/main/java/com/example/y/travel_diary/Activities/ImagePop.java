package com.example.y.travel_diary.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.example.y.travel_diary.Fragments.FragmentAlbum;
import com.example.y.travel_diary.MainActivity;
import com.example.y.travel_diary.R;

public class ImagePop extends Activity {
    private Bitmap bitmap;
    private int device_width;
    private int device_height;
    private int width;
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_popup);

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        device_width = displayMetrics.widthPixels;
        device_height = displayMetrics.heightPixels;

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        String imgPath = extras.getString("filename");


        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inSampleSize = 2;
        ImageView iv = (ImageView)findViewById(R.id.imageView);
        Bitmap bm = BitmapFactory.decodeFile(imgPath, bfo);

        int imageWidth = bm.getWidth();
        int imageHeight = bm.getHeight();

        if (imageWidth >= imageHeight) {
            width = device_width;
            height = (int) (((double) imageHeight / (double) imageWidth) * (double) width);
        } else {
            height = device_height;
            width = (int) (((double) imageWidth / (double) imageHeight) * (double) height);
        }

        bitmap = Bitmap.createScaledBitmap(bm, width, height, true);
        iv.setImageBitmap(bitmap);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bitmap.recycle();
        bitmap = null;
    }
}
