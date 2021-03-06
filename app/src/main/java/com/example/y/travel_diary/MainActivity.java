package com.example.y.travel_diary;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.y.travel_diary.Activities.AddNewBucket;
import com.example.y.travel_diary.Activities.AddNewMap;
import com.example.y.travel_diary.Activities.AddNewPlan;
import com.example.y.travel_diary.Activities.AddNewTravel;
import com.example.y.travel_diary.Fragments.FragmentAlbum;
import com.example.y.travel_diary.Fragments.FragmentHome;
import com.example.y.travel_diary.Fragments.FragmentList;
import com.example.y.travel_diary.Fragments.FragmentMain;
import com.example.y.travel_diary.Fragments.FragmentMap;
import com.example.y.travel_diary.Fragments.FragmentPlanner;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends Activity {
    final private static int NEW_START_ACTIVITY = 101;
    final private static int TAKE_CAMERA = 201;
    final public static String TRAVEL_PREF = "cur_travel";
    private SharedPreferences pref = null;
    private int cur_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the preference which indicates the information of
        // the current travel.
        pref = getSharedPreferences(TRAVEL_PREF, MODE_PRIVATE);

        TextView homeTextView = (TextView) findViewById(R.id.textview_home);
        homeTextView.setTypeface(Typeface.createFromAsset(getAssets(), "apopcircle.otf"));
Log.e("Main", "onCreate");
        Fragment fr;
        if ((pref != null) && pref.getInt("id", -1) != -1) {
            cur_id = pref.getInt("cur_id", R.id.button_main);
        } else {
            cur_id = R.id.button_home;
        }

        changeFrag(cur_id);
    }

    // Change the fragment properly according to the Buttons clicked.
    public void selectFrag(View view) {
        int sid = view.getId();

        if(sid == cur_id || pref.getInt("id", -1) == -1)
            return;

        changeFrag(sid);
    }

    public void changeFrag(int sid) {
        Fragment fr = null;
        ImageView imageView = null;

        if (cur_id != R.id.button_home) {
            imageView = (ImageView) findViewById(cur_id);
            switch (cur_id) {
                case R.id.button_main:
                    imageView.setImageResource(R.drawable.mainimg2);
                    break;
                case R.id.button_map:
                    imageView.setImageResource(R.drawable.mapimg2);
                    break;
                case R.id.button_list:
                    imageView.setImageResource(R.drawable.listimg2);
                    break;
                case R.id.button_planner:
                    imageView.setImageResource(R.drawable.planimg2);
                    break;
                case R.id.button_album:
                    imageView.setImageResource(R.drawable.albumimg2);
                    break;
            }
        }

        if (sid != R.id.button_home)
            imageView = (ImageView) findViewById(sid);

        switch (sid) {
            case R.id.button_home:
                fr = new FragmentHome();
                break;
            case R.id.button_main:
                imageView.setImageResource(R.drawable.mainimg);
                fr = new FragmentMain();
                break;
            case R.id.button_map:
                imageView.setImageResource(R.drawable.mapimg);
                fr = new FragmentMap();
                break;
            case R.id.button_list:
                imageView.setImageResource(R.drawable.listimg);
                fr = new FragmentList();
                break;
            case R.id.button_planner:
                imageView.setImageResource(R.drawable.planimg);
                fr = new FragmentPlanner();
                break;
            case R.id.button_album:
                imageView.setImageResource(R.drawable.albumimg);
                fr = new FragmentAlbum();
                break;
        }

        cur_id = sid;

        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("cur_id", sid);
        editor.apply();

        if (fr == null)
            return;

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, fr);
        fragmentTransaction.commit();
    }

    public void gotoAddActivity(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.button_new_start:
            case R.id.image_new_start:
                intent = new Intent(this, AddNewTravel.class);
                startActivityForResult(intent, NEW_START_ACTIVITY);
                return;
            case R.id.image_new_map:
                intent = new Intent(this, AddNewMap.class);
                break;
            case R.id.button_new_bucket:
            case R.id.image_new_bucket:
                intent = new Intent(this, AddNewBucket.class);
                break;
            case R.id.button_new_plan:
            case R.id.image_new_plan:
                intent = new Intent(this, AddNewPlan.class);
                break;
            case R.id.image_camera:
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat CurYearFormat = new SimpleDateFormat("yyyy", getResources().getConfiguration().locale);
                SimpleDateFormat CurMonthFormat = new SimpleDateFormat("MM", getResources().getConfiguration().locale);
                SimpleDateFormat CurDayFormat = new SimpleDateFormat("dd", getResources().getConfiguration().locale);

                String strCurYear = CurYearFormat.format(date);
                String strCurMonth = CurMonthFormat.format(date);
                String strCurDay = CurDayFormat.format(date);

                String dir_path = Environment.getExternalStorageDirectory().getAbsolutePath()
                                    + "/"+pref.getString("name", "TravleDiary");
                File dir = new File (dir_path);
                if (!dir.exists()) {
                    Log.e("filename", "create the directory");
                    dir.mkdirs();
                }

                String fileName = dir_path + "/" + String.format("%s%s%s_%d.jpg",strCurYear,strCurMonth,strCurDay, System.currentTimeMillis());
                Uri uri = Uri.fromFile(new File(fileName));
                Log.e("filename", fileName);

                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, TAKE_CAMERA);
                return;
            default:
                return;
        }

        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == NEW_START_ACTIVITY) {
                ImageView imageView = (ImageView) findViewById((cur_id = R.id.button_main));
                imageView.setImageResource(R.drawable.mainimg);
                Fragment fr = new FragmentMain();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.commit();
            } else if (requestCode == TAKE_CAMERA) {
                Log.e("TAKE_CAMERA", "Return from camera");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setCur_id (int id) {
        this.cur_id = id;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        pref = getSharedPreferences(TRAVEL_PREF, MODE_PRIVATE);
        if ((pref != null) && pref.getInt("id", -1) != -1) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("cur_id", R.id.button_main);
            editor.apply();
        }
    }
}
