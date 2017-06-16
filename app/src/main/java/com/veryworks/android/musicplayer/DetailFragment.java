package com.veryworks.android.musicplayer;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.veryworks.android.musicplayer.domain.Music;

import java.util.List;

public class DetailFragment extends Fragment {
    public static final int CHANGE_SEEKBAR = 99;
    static final String ARG1 = "position";
    private int position = -1;

    ViewHolder viewHolder = null;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case CHANGE_SEEKBAR:
                    viewHolder.setSeekBarPosition(msg.arg1);
                    break;
            }
        }
    };

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(int position) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG1,position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager, container, false);
        Bundle bundle = getArguments();
        position = bundle.getInt(ARG1);
        viewHolder = new ViewHolder(view, position);
        return view;
    }

    public List<Music.Item> getDatas(){
        Music music = Music.getInstance();
        music.loader(getContext());

        return music.getItems();
    }

    // ViewPager 의 View
    public class ViewHolder implements View.OnClickListener{
        ViewPager viewPager;
        RelativeLayout layoutController;
        ImageButton btnPlay,btnNext,btnPrev;
        SeekBar seekBar;
        TextView current,duration;

        public ViewHolder(View view, int position){
            viewPager = (ViewPager) view.findViewById(R.id.viewPager);
            layoutController = (RelativeLayout) view.findViewById(R.id.layoutController);
            btnPlay = (ImageButton) view.findViewById(R.id.play);
            btnNext = (ImageButton) view.findViewById(R.id.next);
            btnPrev = (ImageButton) view.findViewById(R.id.prev);
            seekBar = (SeekBar) view.findViewById(R.id.seekBar);
            current = (TextView) view.findViewById(R.id.current);
            duration = (TextView) view.findViewById(R.id.duration);
            setOnClickListener();
            setViewPager(position);
        }

        private void setOnClickListener(){
            btnPlay.setOnClickListener(this);
            btnNext.setOnClickListener(this);
            btnPrev.setOnClickListener(this);
        }

        private void setViewPager(int position){
            DetailAdapter adapter = new DetailAdapter(getDatas());
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(position);
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.play:
                    Uri musicUri = getDatas().get(position).musicUri;
                    Player.play(musicUri , v.getContext());
                    // seekBar 의 최대길이를 지정
                    Log.d("DetailFragment","duration="+Player.getDuration());
                    seekBar.setMax(Player.getDuration());

                    // seekBar를 변경해주는 thread
                    new SeekBarThread(handler).start();

                    break;
                case R.id.next:
                    break;
                case R.id.prev:
                    break;
            }
        }

        public void setSeekBarPosition(int current){
            seekBar.setProgress(current);
        }
    }
}

class SeekBarThread extends Thread {
    Handler handler;
    boolean runFlag = true;

    public SeekBarThread(Handler handler){
        this.handler = handler;
    }

    @Override
    public void run(){

        while(runFlag) {
            // 매초마다 음원의 실행영역을 가져와서
            int current = Player.getCurrent();
            // seekbar 의 위치를 변경해준다.
            Message msg = new Message();
            msg.what = DetailFragment.CHANGE_SEEKBAR;
            msg.arg1 = current;
            handler.sendMessage(msg);

            // 플레이 시간이 끝나면 thread 를 종료한다.
            if(current >= Player.getDuration()){
                runFlag = false;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
