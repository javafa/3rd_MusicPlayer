package com.veryworks.android.musicplayer;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by pc on 6/20/2017.
 */

public class SeekBarThread extends Thread {
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