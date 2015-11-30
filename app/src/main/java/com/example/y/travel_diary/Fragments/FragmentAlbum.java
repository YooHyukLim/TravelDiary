package com.example.y.travel_diary.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.y.travel_diary.MainActivity;
import com.example.y.travel_diary.R;
import com.example.y.travel_diary.Activities.ImagePop;
import com.example.y.travel_diary.Utils.BitmapWorkerTask;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class FragmentAlbum extends Fragment {
    public Bitmap mPlaceHolder;
    private int width;
    private SharedPreferences pref = null;
    private Context mContext;
    private ImageAdapter ia;
    GridView gv;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mContext = getActivity();

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x / 3 - 5;

        Log.e("size", width+"");

        View view = inflater.inflate(R.layout.album_fragment, container, false);

        pref = getActivity().getSharedPreferences(MainActivity.TRAVEL_PREF, Context.MODE_PRIVATE);
        gv = (GridView)view.findViewById(R.id.ImgGridView);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder, options);
        mPlaceHolder =  Bitmap.createScaledBitmap(bmp, width, width, true);

        return view;
    }

    @Override
    public void onResume() {
        Log.e("FragmentAlbum", "onResume");
        ia = new ImageAdapter(mContext);
        gv.setAdapter(ia);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ia.callImageViewer(i);
            }
        });
        super.onResume();
    }

    public class ImageAdapter extends BaseAdapter {
        private ArrayList<String> thumbsDataList;

        ImageAdapter(Context c) {
            mContext = c;
            thumbsDataList = new ArrayList<String>();
            getThumbInfo(thumbsDataList);
        }

        public final void callImageViewer(int selectedIndex) {
            Intent i = new Intent(mContext, ImagePop.class);
            String imgPath = thumbsDataList.get(selectedIndex);
            i.putExtra("filename", imgPath);
            startActivityForResult(i, 1);
        }

        public int getCount() {
            return thumbsDataList.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(width, width));
                imageView.setAdjustViewBounds(false);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(5, 5, 5, 5);
            } else {
                imageView = (ImageView) convertView;
            }

            loadBitmap(mContext, thumbsDataList.get(position), imageView, width, width);

            return imageView;
        }

        private void getThumbInfo(ArrayList<String> thumbsDatas) {
            if ( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                                +"/"+pref.getString("name", "travelDiary");

                File file = new File(path);
                String str;

                if (file.exists() && file.listFiles().length > 0 )
                    for ( File f : file.listFiles() ) {
                        str = f.getName();
                        String filenameArray[] = str.split("\\.");
                        String extension = filenameArray[filenameArray.length-1];

                        if (extension.toLowerCase().equals("jpg")
                                ||extension.toLowerCase().equals("png")) {
                            thumbsDatas.add(path + "/" + str);
                            Log.e("imageID", path + "/" + str);
                        }
                    }
            }
        }

        public void loadBitmap(Context context, String fileName, ImageView imageView,
                               int width, int height) {
            if (cancelPotentialWork(fileName, imageView)) {
                final BitmapWorkerTask task = new BitmapWorkerTask(imageView, fileName);
                final AsyncDrawable asyncDrawable =
                        new AsyncDrawable(context.getResources(), mPlaceHolder, task);
                imageView.setImageDrawable(asyncDrawable); // imageView에 AsyncDrawable을 묶어줌
                task.execute(width, height);
            }
        }

        public boolean cancelPotentialWork(String fileName, ImageView imageView) {
            final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

            if (bitmapWorkerTask != null) {
                final String bmpfileName = bitmapWorkerTask.fileName;
                // If bitmapData is not yet set or it differs from the new data
                if (bmpfileName == null || !bmpfileName.equals(fileName)) {
                    // Cancel previous task
                    bitmapWorkerTask.cancel(true);
                } else {
                    // The same work is already in progress
                    return false;
                }
            }
            // No task associated with the ImageView, or an existing task was cancelled
            return true;
        }

        private BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
            if (imageView != null) {
                final Drawable drawable = imageView.getDrawable();
                if (drawable instanceof AsyncDrawable) { //drawable 이 null일 경우 false 반환
                    final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                    return asyncDrawable.getBitmapWorkerTask();
                }
            }
            return null;
        }

        class AsyncDrawable extends BitmapDrawable {
            private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

            public AsyncDrawable(Resources res, Bitmap bitmap,
                                 BitmapWorkerTask bitmapWorkerTask) {
                super(res, bitmap);
                bitmapWorkerTaskReference =
                        new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
            }

            public BitmapWorkerTask getBitmapWorkerTask() {
                return bitmapWorkerTaskReference.get();
            }
        }
    }
}
