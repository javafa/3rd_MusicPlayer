package com.veryworks.android.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PlayerService extends Service {
    public PlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if( intent != null ){
            String action = intent.getAction(); // 인텐트를 통해 전달된 명령어를 꺼낸다.
            switch(action){
                case Const.Action.PLAY:
                    Player.play();
                    // 음악실행
                    break;
                case Const.Action.PAUSE:
                    Player.pause();
                    // 일시정지
                    break;
                case Const.Action.STOP:
                    Player.pause();
                    // 완전정지
                    break;
                case Const.Action.INIT:
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
