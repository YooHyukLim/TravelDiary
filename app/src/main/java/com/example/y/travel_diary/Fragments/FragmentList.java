package com.example.y.travel_diary.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
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

import com.example.y.travel_diary.Adapters.BucketListAdapter;
import com.example.y.travel_diary.MainActivity;
import com.example.y.travel_diary.R;
import com.example.y.travel_diary.Utils.DataBaseHelper;

public class FragmentList extends Fragment {
    private SharedPreferences pref = null;
    private DataBaseHelper dbhelper = null;
    private SQLiteDatabase db = null;
    private ListView list_bucket = null;
    private BucketListAdapter badapter = null;
    private View view = null;
    private int position = -1;
    private int id;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.list_fragment, container, false);

        pref = getActivity().getSharedPreferences(MainActivity.TRAVEL_PREF, Context.MODE_PRIVATE);

        dbhelper = new DataBaseHelper(getActivity());
        db = dbhelper.getWritableDatabase();

        list_bucket = (ListView) view.findViewById(R.id.list_bucket);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(pref != null && (id = pref.getInt("id", -1)) != -1) {
            Cursor cursor = db.query(DataBaseHelper.BUCKET_TABLE,
                    DataBaseHelper.BUCKET_COL,
                    DataBaseHelper._ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null);

            // If there isn't any travel information, show the button for adding new travel.
            if(cursor.getCount() == 0) {
                view.findViewById(R.id.list_bucket).setVisibility(View.GONE);
                view.findViewById(R.id.image_new_bucket).setVisibility(View.GONE);
                view.findViewById(R.id.button_new_bucket).setVisibility(View.VISIBLE);
            } else {
                create_bucket_view (view, cursor);
            }
        }
    }

    private void create_bucket_view (View view, Cursor cursor) {
        view.findViewById(R.id.button_new_bucket).setVisibility(View.GONE);
        view.findViewById(R.id.image_new_bucket).setVisibility(View.VISIBLE);
        view.findViewById(R.id.list_bucket).setVisibility(View.VISIBLE);
        badapter = new BucketListAdapter(getActivity().getApplicationContext(), cursor);
        list_bucket.setAdapter(badapter);

        list_bucket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                position = pos;
                AlertDialog dialog = createDialog(parent);
                dialog.show();
            }
        });

        list_bucket.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
                int bid = badapter.getItem(pos).getBid();
                boolean done = !badapter.getItem(pos).isDone();

                ContentValues values = new ContentValues();
                values.put(dbhelper.BUCKET_DONE, done);
                db.update(dbhelper.BUCKET_TABLE, values,
                        dbhelper._ID + "=? AND " + dbhelper.BUCKET_ID + "=?",
                        new String[] {String.valueOf(id), String.valueOf(bid)});

                badapter.getItem(pos).setDone(done);
                badapter.notifyDataSetChanged();

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
                db.delete(DataBaseHelper.BUCKET_TABLE,
                        DataBaseHelper._ID + "=? AND " + DataBaseHelper.BUCKET_ID + "=?",
                        new String[]{String.valueOf(id),
                                String.valueOf(badapter.getItem(position).getBid())});
                badapter.remove(position);
                badapter.notifyDataSetChanged();

                if (badapter.getCount() == 0) {
                    view.findViewById(R.id.list_bucket).setVisibility(View.GONE);
                    view.findViewById(R.id.image_new_bucket).setVisibility(View.GONE);
                    view.findViewById(R.id.button_new_bucket).setVisibility(View.VISIBLE);
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