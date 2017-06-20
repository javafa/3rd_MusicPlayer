package com.veryworks.android.musicplayer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

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
                    playerStart();
                    // 음악실행
                    break;
                case Const.Action.PAUSE:
                    Player.pause();
                    // 일시정지
                    break;
                case Const.Action.STOP:
                    // 서비스를 종료
                    stopService();
                    break;
                case Const.Action.INIT:
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void playerStart(){
        NotificationCompat.Action pauseAction = makeAction(android.R.drawable.ic_media_pause, "Pause", Const.Action.PAUSE);
        buildNotification(pauseAction, Const.Action.PLAY);
        Player.play();
    }

    // 노티피케이션의 고유 ID
    private static final int NOTIFICATION_ID = 100;

    // 서비스를 종료하는 함수
    private void stopService(){
        // 1. 노티바를 제거
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFICATION_ID);
        // 2. 서비스를 종료
        Intent intent = new Intent(getApplicationContext(), PlayerService.class);
        stopService(intent);
    }

    // 1. 노티바를 생성하는 함수
    private void buildNotification(NotificationCompat.Action action, String action_flag){

        // 가. 노티바 생성
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.icon)
                .setContentTitle("음악제목")
                .setContentText("가수");

        // 가.1. 서비스를 종료하는 intent 생성
        Intent deleteIntent = new Intent(getApplicationContext(), PlayerService.class);
        deleteIntent.setAction(Const.Action.STOP);
        PendingIntent pendingDeleteIntent
                = PendingIntent.getService(getApplicationContext(), 1, deleteIntent, 0);

        // 가.2 서비스 종료 intent 등록
        builder.setContentIntent(pendingDeleteIntent);

        // 가.3 노티바에서 사용할 action(버튼) 등록
        NotificationCompat.Action prevAction = makeAction(android.R.drawable.ic_media_previous, "Prev", Const.Action.PREV);
        builder.addAction(prevAction);

        builder.addAction(action);
        NotificationCompat.Action nextAction = makeAction(android.R.drawable.ic_media_next, "Next", Const.Action.NEXT);
        builder.addAction(nextAction);

        // 나. 노티바 등록
        // 나.1 매니저 가져오기
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // 나.2 매니저를 통해 등록
        manager.notify(NOTIFICATION_ID, builder.build());
    }

    // 2. 노티바에서 동작하는 버튼을 생성하는 함수
    private NotificationCompat.Action makeAction(int btnIcon, String btnTitle, String action){
        Intent intent = new Intent(getApplicationContext(), PlayerService.class);
        intent.setAction(action);

        // PendingInten : 인텐트를 서비스 밖 혹은 액티비티 밖에서 실행할 수 있도록 담아두는 주머니
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);

        return new NotificationCompat.Action.Builder(btnIcon, btnTitle, pendingIntent).build();
    }
}
