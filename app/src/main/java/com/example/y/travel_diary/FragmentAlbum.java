package com.example.y.travel_diary;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by วับ๓ on 2015-06-16.
 */
public class FragmentAlbum extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        return inflater.inflate(R.layout.album_fragment, container, false);
    }
}
