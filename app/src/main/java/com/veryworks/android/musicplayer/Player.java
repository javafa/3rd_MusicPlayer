package com.veryworks.android.musicplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

/**
 * Created by pc on 6/16/2017.
 */

public class Player {
    public static final int STOP = 0;
    public static final int PLAY = 1;
    public static final int PAUSE = 2;
    private static MediaPlayer player = null;
    public static int playerStatus = STOP;

    public static void play(Uri musicUri, Context context){
        // 1. 미디어 플레이어 사용하기
        if(player != null) {
            player.release();
        }
        player = MediaPlayer.create(context, musicUri);
        player.setLooping(false); // 반복여부
        player.start();
        playerStatus = PLAY;
    }

    public static void pause(){
        player.pause();
        playerStatus = PAUSE;
    }

    public static void replay(){
        player.start();
        playerStatus = PLAY;
    }

    // 음원의 길이
    public static int getDuration(){
        if(player != null){
            return player.getDuration();
        }else{
            return 0;
        }
    }

    // 현재 실행 구간
    public static int getCurrent(){
        if(player != null){
            return player.getCurrentPosition();
        }else{
            return 0;
        }
    }
}
