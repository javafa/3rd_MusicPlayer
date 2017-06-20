package com.veryworks.android.musicplayer.util;

/**
 * Created by pc on 6/20/2017.
 */

public class TimeUtil {

    // 시간 포맷 변경 Integer -> 00:00
    public static String miliToMinSec(int mSecond){
        long min = mSecond / 1000 / 60;
        long sec = mSecond / 1000 % 60;

        return String.format("%02d", min) + ":" + String.format("%02d", sec);
    }
}
