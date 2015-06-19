package com.example.y.travel_diary.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.y.travel_diary.Adapters.TravelListAdapter;
import com.example.y.travel_diary.MainActivity;
import com.example.y.travel_diary.R;
import com.example.y.travel_diary.Utils.DataBaseHelper;

public class FragmentHome extends Fragment {
    private SharedPreferences pref = null;
    private DataBaseHelper dbhelper = null;
    private SQLiteDatabase db = null;
    private TravelListAdapter tadapter = null;
    private ListView list_travel = null;
    private View view = null;
    private int id = -1;
    private int position = -1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.home_fragment, container, false);

        pref = getActivity().getSharedPreferences(MainActivity.TRAVEL_PREF, Context.MODE_PRIVATE);
        id = pref.getInt("id", -1);

        dbhelper = new DataBaseHelper(getActivity());
        db = dbhelper.getWritableDatabase();

        list_travel = (ListView) view.findViewById(R.id.list_travel);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Cursor cursor = db.query(DataBaseHelper.TRAVEL_TABLE,
                DataBaseHelper.TRAVEL_COL, null, new String[] {}, null, null,
                DataBaseHelper.TRAVEL_EDATE + " ASC");

        // If there isn't any travel information, show the button for adding new travel.
        if(cursor.getCount() == 0) {
            view.findViewById(R.id.list_travel).setVisibility(View.GONE);
            view.findViewById(R.id.image_new_start).setVisibility(View.GONE);
            view.findViewById(R.id.button_new_start).setVisibility(View.VISIBLE);
        } else {
            create_travel_view (view, cursor);
        }

        cursor.close();
    }

    private void create_travel_view (View view, Cursor cursor) {
        view.findViewById(R.id.button_new_start).setVisibility(View.GONE);
        view.findViewById(R.id.image_new_start).setVisibility(View.VISIBLE);
        view.findViewById(R.id.list_travel).setVisibility(View.VISIBLE);

        tadapter = new TravelListAdapter(getActivity().getApplicationContext(), cursor);
        list_travel.setAdapter(tadapter);

        list_travel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences pref = getActivity().getSharedPreferences(MainActivity.TRAVEL_PREF, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("id", tadapter.getItem(position).get_id());
                editor.commit();

                Fragment fr = new FragmentMain();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_place, fr);
                fragmentTransaction.commit();
            }
        });

        list_travel.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
                position = pos;
                AlertDialog dialog = createDialog(parent);
                dialog.show();
                return true;
            }
        });
    }

    private AlertDialog createDialog(AdapterView adapterView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Delete?");

        builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int cid = tadapter.getItem(position).get_id();

                db.delete(DataBaseHelper.TRAVEL_TABLE,
                        DataBaseHelper._ID + "=?",
                        new String[]{String.valueOf(cid)});
                tadapter.remove(position);
                tadapter.notifyDataSetChanged();

                if (tadapter.getCount() == 0) {
                    view.findViewById(R.id.list_travel).setVisibility(View.GONE);
                    view.findViewById(R.id.image_new_start).setVisibility(View.GONE);
                    view.findViewById(R.id.button_new_start).setVisibility(View.VISIBLE);
                }

                if(cid == id) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("id", -1);
                    editor.commit();
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //N/A
            }
        });

        return builder.create();
    }
}