package com.example.y.travel_diary.Fragments;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.y.travel_diary.Utils.AlertReceiver;
import com.example.y.travel_diary.Activities.EditOldPlan;
import com.example.y.travel_diary.Adapters.PlanListAdapter;
import com.example.y.travel_diary.MainActivity;
import com.example.y.travel_diary.R;
import com.example.y.travel_diary.Utils.CustomTouchListener;
import com.example.y.travel_diary.Utils.DataBaseHelper;

import java.util.zip.Inflater;

public class FragmentPlanner extends Fragment {
    private SharedPreferences pref = null;
    private DataBaseHelper dbhelper = null;
    private SQLiteDatabase db = null;
    private ListView list_plan = null;
    private PlanListAdapter padapter = null;
    private View view = null;
    private TextView textViewNewPlan = null;
    private int position = -1;
    private int id;
    private int plan_id;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.planner_fragment, container, false);

        pref = getActivity().getSharedPreferences(MainActivity.TRAVEL_PREF, Context.MODE_PRIVATE);

        dbhelper = new DataBaseHelper(getActivity());
        db = dbhelper.getWritableDatabase();

        list_plan = (ListView) view.findViewById(R.id.list_plan);

        textViewNewPlan = (TextView) view.findViewById(R.id.button_new_plan);
        textViewNewPlan.setOnTouchListener(new CustomTouchListener(textViewNewPlan));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(pref != null && (id = pref.getInt("id", -1)) != -1) {
            Cursor cursor = db.query(DataBaseHelper.PLAN_TABLE,
                    DataBaseHelper.PLAN_COL,
                    DataBaseHelper._ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null);

            // If there isn't any travel information, show the button for adding new travel.
            if(cursor.getCount() == 0) {
                view.findViewById(R.id.list_plan).setVisibility(View.GONE);
                view.findViewById(R.id.image_new_plan).setVisibility(View.GONE);
                view.findViewById(R.id.button_new_plan).setVisibility(View.VISIBLE);
            } else {
                create_plan_view (view, cursor);
            }

            cursor.close();
        }
    }

    private void create_plan_view (View view, Cursor cursor) {
        view.findViewById(R.id.button_new_plan).setVisibility(View.GONE);
        view.findViewById(R.id.image_new_plan).setVisibility(View.VISIBLE);
        view.findViewById(R.id.list_plan).setVisibility(View.VISIBLE);
        padapter = new PlanListAdapter(getActivity().getApplicationContext(), cursor);
        list_plan.setAdapter(padapter);

        list_plan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                plan_id = padapter.getItem(pos).getpid();
                position = pos;
                AlertDialog dialog = createDialog(parent);
                dialog.show();
            }
        });

        list_plan.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long iD) {
                int pid = padapter.getItem(pos).getpid();
                boolean alarm = !padapter.getItem(pos).getAlarm();

                ContentValues values = new ContentValues();
                values.put(dbhelper.PLAN_ALARM, alarm);
                db.update(dbhelper.PLAN_TABLE, values,
                        dbhelper._ID + "=? AND " + dbhelper.PLAN_ID + "=?",
                        new String[]{String.valueOf(id), String.valueOf(pid)});

                padapter.getItem(pos).setAlarm(alarm);
                padapter.notifyDataSetChanged();

                if(alarm == true && System.currentTimeMillis() < padapter.getItem(pos).getSdate()){
                    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                    Intent Intent = new Intent(getActivity(), AlertReceiver.class);

                    alarmManager.set(AlarmManager.RTC_WAKEUP, padapter.getItem(pos).getSdate(),
                            PendingIntent.getBroadcast(getActivity(), padapter.getItem(pos).getpid(), Intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT));
                }else if(alarm == false){
                    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                    Intent Intent = new Intent(getActivity(), AlertReceiver.class);

                    alarmManager.cancel(PendingIntent.getBroadcast(getActivity(), padapter.getItem(pos).getpid(), Intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT));
                }

                return true;
            }
        });
    }



    private AlertDialog createDialog(AdapterView adapterView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(padapter.getItem(position).getName());
        builder.setMessage(padapter.getItem(position).getContent());
        builder.setNegativeButton("Edit!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(),EditOldPlan.class);
                intent.putExtra("_id",id);
                intent.putExtra("pid",plan_id);
                intent.putExtra("pn",padapter.getItem(position).getName());
                intent.putExtra("pc",padapter.getItem(position).getContent());
                intent.putExtra("ps",padapter.getItem(position).getSdate());
                intent.putExtra("pe",padapter.getItem(position).getEdate());
                intent.putExtra("pa",padapter.getItem(position).getAlarm());
                startActivity(intent);
            }
        });

        builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.delete(DataBaseHelper.PLAN_TABLE,
                        DataBaseHelper._ID + "=? AND " + DataBaseHelper.PLAN_ID + "=?",
                        new String[]{String.valueOf(id),
                                String.valueOf(padapter.getItem(position).getpid())});
                padapter.remove(position);
                padapter.notifyDataSetChanged();

                if (padapter.getCount() == 0) {
                    view.findViewById(R.id.list_plan).setVisibility(View.GONE);
                    view.findViewById(R.id.image_new_plan).setVisibility(View.GONE);
                    view.findViewById(R.id.button_new_plan).setVisibility(View.VISIBLE);
                }
            }
        });

        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //N/A
            }
        });

        return builder.create();
    }
}