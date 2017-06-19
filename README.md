# 3rd_MusicPlayer

get AlbumArt Image
```java
    // 앨범아트 데이터만 따로 저장
    private HashMap<Integer, String> albumMap = new HashMap<>(); //앨범아이디와 썸네일 경로 저장
    private void setAlbumArt(Context context) {

        String[] Album_cursorColumns = new String[]{
                MediaStore.Audio.Albums.ALBUM_ART, //앨범아트
                MediaStore.Audio.Albums._ID
        };

        Cursor Album_cursor = context.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                Album_cursorColumns, null, null, null);
        if (Album_cursor != null) { //커서가 널값이 아니면
            if (Album_cursor.moveToFirst()) { //처음참조
                int albumArt = Album_cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
                int albumId = Album_cursor.getColumnIndex(MediaStore.Audio.Albums._ID);
                do {
                    if (!albumMap.containsKey(Integer.parseInt(Album_cursor.getString(albumId)))) { //맵에 앨범아이디가 없으면
                        albumMap.put(Integer.parseInt(Album_cursor.getString(albumId)), Album_cursor.getString(albumArt)); //집어넣는다
                    }
                } while (Album_cursor.moveToNext());
            }
        }
        Album_cursor.close();
    }
    
    //사용하기
    String albumArt = albumMap.get(ALBUM_ID);
```
