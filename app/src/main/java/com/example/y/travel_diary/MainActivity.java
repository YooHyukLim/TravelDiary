package com.example.y.travel_diary;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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


public class MainActivity extends Activity {
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

        Fragment fr;
        if ((pref != null) && pref.getInt("id", -1) != -1) {
            cur_id = R.id.button_main;
            ImageView mainImage = (ImageView) findViewById(R.id.button_main);
            mainImage.setImageResource(R.drawable.mainimg);
            fr = new FragmentMain();
        } else {
            cur_id = R.id.button_home;
            fr = new FragmentHome();
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragment_place, fr);
        fragmentTransaction.commit();
    }

    // Change the fragment properly according to the Buttons clicked.
    public void selectFrag(View view) {
        Fragment fr = null;
        int sid = view.getId();
        ImageView imageView = null;

        if(pref.getInt("id", -1) == -1)
            return;

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

        cur_id = sid;

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
                break;
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
            default:
                return;
        }

        startActivity(intent);
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
}
