package vlu.android.numberseven.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import vlu.android.numberseven.models.Song;

public class SongHandler extends SQLiteOpenHelper {
    private static final String PATH = "/data/data/vlu.android.numberseven/databases/number7.db";
    private static final String DB_NAME = "number7.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_SONG = "Song";
    private static final String SONG_ID = "SongID";
    private static final String SONG_NAME = "SongName";
    private static final String SONG_PLAYLISTID = "PlaylistID";
    private static final String SONG_SINGER = "Singer";
    private static final String SONG_PATH = "SongPath";
    private static final String SONG_DATEADDED = "DateAdded";
    private static final String SONG_ALBUMART = "SongAlbumArt";

    public SongHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addSong(Song song){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SONG_NAME, song.getSongName());
        values.put(SONG_SINGER, song.getSinger());
        values.put(SONG_PLAYLISTID, song.getPlaylistID());
        values.put(SONG_PATH, song.getSongPath());
        values.put(SONG_DATEADDED, song.getDateAdded());
        values.put(SONG_ALBUMART, song.getSongAlbumArt());
        db.insert(TABLE_SONG, null, values);
        db.close();
    }
}
