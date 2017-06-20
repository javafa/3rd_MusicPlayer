package com.veryworks.android.musicplayer;

import android.content.Context;
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

    public PlayerInterface playerInterface;

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
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof PlayerInterface){
            playerInterface = (PlayerInterface) context;
        }else{
            // PlayerInterface 를 Activity 가 구현하지 않았으면 강제종료 시켜버린다
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        Log.d("DetailFragment","viewHolder====================================="+viewHolder);
        if(viewHolder == null) {
            view = inflater.inflate(R.layout.fragment_pager, container, false);
            viewHolder = new DetailView(view, this, playerInterface);
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

    public void setDestroy() {
        viewHolder.setDestroy();
    }

    public interface PlayerInterface {
        void playPlayer();
        void stopPlayer();
        void pausePlayer();
        void initPlayer();
    }
}
