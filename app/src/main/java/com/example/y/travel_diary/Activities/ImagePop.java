package com.example.y.travel_diary.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.example.y.travel_diary.R;

import java.io.IOException;

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

        try {
            ExifInterface exif;
            exif = new ExifInterface(imgPath);

            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int exifDegree = exifOrientationToDegrees(exifOrientation);
            bm = rotate(bm, exifDegree);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public int exifOrientationToDegrees(int exifOrientation) {
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public Bitmap rotate(Bitmap bitmap, int degrees) {
        if(degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);

            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if(bitmap != converted) {
                    bitmap.recycle();
                    bitmap = converted;
                }
            } catch(OutOfMemoryError ex) {
                //Out of Memory
            }
        }
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bitmap.recycle();
        bitmap = null;
    }
}
