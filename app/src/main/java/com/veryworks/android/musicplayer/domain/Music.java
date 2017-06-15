package com.veryworks.android.musicplayer.domain;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by pc on 6/14/2017.
 */

public class Music {
    private static Music instance = null;
    private Set<Item> items = null;

    private Music() {
        items = new HashSet<>();
    }

    public Music getInstance(){
        if(instance == null)
            instance = new Music();

        return instance;
    }

    public Set<Item> getItems(){
        return items;
    }

    // 음악 데이터를 폰에서 꺼낸다음 List 저장소에 담아둔다.
    public void loader(Context context) {
        // items.clear(); Set을 사용함으로 중복을 방지할 수 있다
        ContentResolver resolver = context.getContentResolver();

        // 1. 테이블 명 정의 ?
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        // 2. 가져올 컬럼명 정의
        String proj[] = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST
        };
        // 3. 쿼링~
        Cursor cursor = resolver.query(uri, proj, null, null, null);
        if(cursor != null){
            while(cursor.moveToNext()){
                Item item = new Item();
                item.id = getValue(cursor, proj[0]);
                item.albumId = getValue(cursor, proj[1]);
                item.title = getValue(cursor, proj[2]);
                item.artist = getValue(cursor, proj[3]);

                item.musicUri = makeMusicUri(item.id);
                item.albumArt = makeAlbumUri(item.albumId);
                // 저장소에 담는다...
                items.add(item);
            }
        }
    }

    private String getValue(Cursor cursor, String name){
        int index = cursor.getColumnIndex(name);
        return cursor.getString(index);
    }

    // Set 이 중복값을 허용하지 않도록 equals 와 hashCode를 활용한다
    public class Item {
        String id;
        String albumId;
        String artist;
        String title;

        Uri musicUri;
        Uri albumArt;

        @Override
        public boolean equals(Object item) {
            // null 체크
            if(item == null) return false;
            // 객체 타입 체크
            if (!(item instanceof Item)) return false;
            // 키값의 hashcode 비교
            return id.hashCode() == item.hashCode();
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }

    private Uri makeMusicUri(String musicId){
        Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        return Uri.withAppendedPath(contentUri, musicId);
    }

    private Uri makeAlbumUri(String albumId){
        String albumUri = "content://media/external/audio/albumart/";
        return Uri.parse(albumUri + albumId);
    }

}
