package com.example.y.travel_diary.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    public String fileName = null;

    public BitmapWorkerTask(ImageView imageView, String fileName) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);
        this.fileName = fileName;
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(Integer... params) {
        return decodeSampledBitmapFromResource(fileName, params[0], params[1]);
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();

            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(String fileName,
                                                         int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();

        // First decode with inJustDecodeBounds=true to check dimensions
//        options.inJustDecodeBounds = true;

        // Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inSampleSize = 16;

        Bitmap bmp = BitmapFactory.decodeFile(fileName, options);

        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;

        if (bmp == null)
            Log.e("BitmapWorkerTask", fileName + " null");
        return Bitmap.createScaledBitmap(bmp, reqWidth, reqHeight, true);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}