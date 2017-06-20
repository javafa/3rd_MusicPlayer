package com.veryworks.android.musicplayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DetailFragment extends Fragment {
    public static final int CHANGE_SEEKBAR = 99;
    public static final int STOP_THREAD = 98;
    private int position = -1;

    private DetailView viewHolder = null;

    public DetailFragment() {
        // Required empty public constructor
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public static DetailFragment newInstance() {
        DetailFragment fragment = new DetailFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        Log.d("DetailFragment","viewHolder====================================="+viewHolder);
        if(viewHolder == null) {
            view = inflater.inflate(R.layout.fragment_pager, container, false);
            viewHolder = new DetailView(view, this);
        }else{
            view = viewHolder.getView();
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        viewHolder.init(position);
    }
}
