package com.example.y.travel_diary.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.example.y.travel_diary.Fragments.FragmentAlbum;
import com.example.y.travel_diary.MainActivity;
import com.example.y.travel_diary.R;

public class ImagePop extends Activity implements OnClickListener {
    private Context mContext = null;
    private final int imgWidth = 320;
    private final int imgHeight = 372;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_popup);
        mContext = this;


        Intent i = getIntent();
        Bundle extras = i.getExtras();
        String imgPath = extras.getString("filename");


        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inSampleSize = 2;
        ImageView iv = (ImageView)findViewById(R.id.imageView);
        Bitmap bm = BitmapFactory.decodeFile(imgPath, bfo);
        Bitmap resized = Bitmap.createScaledBitmap(bm, imgWidth, imgHeight, true);
        iv.setImageBitmap(resized);


        Button btn = (Button)findViewById(R.id.btn_back);
        btn.setOnClickListener(this);
    }
    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_back:
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

}
