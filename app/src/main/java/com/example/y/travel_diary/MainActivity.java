package com.example.y.travel_diary;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.y.travel_diary.Activities.AddNewBucket;
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
    private int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the preference which indicates the information of
        // the current travel.
        pref = getSharedPreferences(TRAVEL_PREF, MODE_PRIVATE);

        Fragment fr;
        if ((pref != null) && (pref.contains("id"))) {
            id = pref.getInt("id", -1);
            fr = new FragmentMain();
        } else
            fr = new FragmentHome();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragment_place, fr);
        fragmentTransaction.commit();
    }

    // Change the fragment properly according to the Buttons clicked.
    public void selectFrag(View view) {
        Fragment fr = null;

        switch (view.getId()) {
            case R.id.button_home:
                fr = new FragmentHome();
                break;
            case R.id.button_main:
                fr = new FragmentMain();
                break;
            case R.id.button_map:
                fr = new FragmentMap();
                break;
            case R.id.button_list:
                fr = new FragmentList();
                break;
            case R.id.button_planner:
                fr = new FragmentPlanner();
                break;
            case R.id.button_album:
                fr = new FragmentAlbum();
                break;
        }

        if (fr == null)
            return;

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, fr);
        fragmentTransaction.commit();
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

    public void gotoAddActivity(View view) {
        Intent intent = new Intent(this, AddNewTravel.class);
        startActivity(intent);
    }

    public void gotoAddBucket(View view) {
        if(id == -1)
            return;
        Intent intent = new Intent(this, AddNewBucket.class);
        startActivity(intent);
    }
}
