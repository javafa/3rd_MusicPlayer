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
    public static final int STOP_THREAD = 98;
    static final String ARG1 = "position";
    private int position = -1;

    // 음악 플레이에 따라 seekbar를 변경해주는 thread
    SeekBarThread seekBarThread = null;

    private ViewHolder viewHolder = null;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case CHANGE_SEEKBAR:
                    viewHolder.setSeekBarPosition(msg.arg1);
                    break;
                case STOP_THREAD:
                    seekBarThread.setRunFlag(false);
                    break;
            }
        }
    };

    public DetailFragment() {
        // Required empty public constructor
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public static DetailFragment newInstance(int position) {
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
            viewHolder = new ViewHolder(view, this);
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

    public List<Music.Item> getDatas(){
        Music music = Music.getInstance();
        music.loader(getContext());

        return music.getItems();
    }

    // 최초에 호출될 경우는 페이지의 이동이 없으므로 호출되지 않는다.
    ViewPager.OnPageChangeListener viewPagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        // 페이지의 변경사항을 체크해서 현재 페이지 값을 알려준다
        @Override
        public void onPageSelected(int position) {
            // 현재 페이지가 변경된 후 호출된다.
            // 플레이어에 음악을 세팅해준다.
            musicInit(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };



    // 음악을 초기화해준다
    public void musicInit(int position){
        Uri musicUri = getDatas().get(position).musicUri;
        Player.init(musicUri, getContext(), handler);

        Log.d("DetailFragment","musicInit viewHolder====================================="+viewHolder);
        int musicDuration = Player.getDuration();
        viewHolder.setDuration(musicDuration);
        viewHolder.seekBar.setMax(Player.getDuration());

        // seekBar를 변경해주는 thread
        seekBarThread = new SeekBarThread(handler);
    }

    // ViewPager 의 View
    public class ViewHolder implements View.OnClickListener{
        View view;
        ViewPager viewPager;
        RelativeLayout layoutController;
        ImageButton btnPlay,btnNext,btnPrev;
        SeekBar seekBar;
        TextView current,duration;

        // 프레젠터 역할 - 인터페이스 설계 필요
        DetailFragment presenter;

        public View getView(){
            return view;
        }

        public ViewHolder(View view, DetailFragment presenter){
            this.view = view;
            this.presenter = presenter;
            viewPager = (ViewPager) view.findViewById(R.id.viewPager);
            layoutController = (RelativeLayout) view.findViewById(R.id.layoutController);
            btnPlay = (ImageButton) view.findViewById(R.id.play);
            btnNext = (ImageButton) view.findViewById(R.id.next);
            btnPrev = (ImageButton) view.findViewById(R.id.prev);
            seekBar = (SeekBar) view.findViewById(R.id.seekBar);
            current = (TextView) view.findViewById(R.id.current);
            duration = (TextView) view.findViewById(R.id.duration);
        }

        public void init(int position){
            setOnClickListener();
            setViewPager(position);
            setSeekBar();
        }

        private void setOnClickListener(){
            btnPlay.setOnClickListener(this);
            btnNext.setOnClickListener(this);
            btnPrev.setOnClickListener(this);
        }

        private void setViewPager(int position){
            DetailAdapter adapter = new DetailAdapter(getDatas());
            // 아답터를 생성
            viewPager.setAdapter(adapter);
            // 리스너를 달았다...
            viewPager.addOnPageChangeListener(viewPagerListener);
            // 페이지를 이동하고
            viewPager.setCurrentItem(position);
            // 처음 한번 Presenter 에 해당되는 Fragment 의 musicInit 을 호출해서 음악을 초기화 해준다.
            presenter.musicInit(position);
        }

        private void setSeekBar(){
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    // 사용자가 seekbar 를 터치했을 때만 동작하도록 설정
                    if(fromUser)
                        Player.setCurrent(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

        public void setDuration(int time){
            String formatted = miliToString(time);
            duration.setText( formatted );
        }

        // 시간 포맷 변경 Integer -> 00:00
        private String miliToString(int mSecond){
            long min = mSecond / 1000 / 60;
            long sec = mSecond / 1000 % 60;

            return String.format("%02d", min) + ":" + String.format("%02d", sec);
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.play:
                    play();
                    break;
                case R.id.next:
                    next();
                    break;
                case R.id.prev:
                    prev();
                    break;
            }
        }

        public void play(){
            Player.play();
            seekBarThread.start();
        }

        public void next(){
            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
        }

        public void prev(){
            viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
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

            Log.d("SeekBarThread", "current="+current+", duration="+Player.getDuration());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setRunFlag(boolean flag){
        runFlag = flag;
    }
}
