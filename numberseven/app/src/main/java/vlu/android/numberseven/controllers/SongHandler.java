package vlu.android.numberseven.controllers;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

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

    public void addSong(int selectedPlaylistID,Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SONG_NAME, song.getSongName());
        values.put(SONG_SINGER, song.getSinger());
        values.put(SONG_PLAYLISTID, selectedPlaylistID);
        values.put(SONG_PATH, song.getSongPath());
        values.put(SONG_DATEADDED, song.getDateAdded());
        values.put(SONG_ALBUMART, song.getSongAlbumArt());
        db.insert(TABLE_SONG, null, values);
        db.close();
    }

    public void updateSong(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SONG_NAME, song.getSongName());
        values.put(SONG_SINGER, song.getSinger());
        values.put(SONG_PLAYLISTID, song.getPlaylistID());
        values.put(SONG_PATH, song.getSongPath());
        values.put(SONG_DATEADDED, song.getDateAdded());
        values.put(SONG_ALBUMART, song.getSongAlbumArt());

        // Update the song record
        db.update(TABLE_SONG, values, SONG_ID + "=?", new String[]{String.valueOf(song.getSongID())});
        db.close();
    }

    @SuppressLint("Range")
    public ArrayList<Song> getSongForPlaylist(int playlistID) {
        ArrayList<Song> songList = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = getReadableDatabase();
        try {
            // Query songs from your database based on playlistID
            cursor = db.query(TABLE_SONG, null, "playlistID=?", new String[]{String.valueOf(playlistID)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Song song = new Song();
                    song.setSongID(cursor.getInt(cursor.getColumnIndex(SONG_ID)));
                    song.setSongName(cursor.getString(cursor.getColumnIndex(SONG_NAME)));
                    song.setSinger(cursor.getString(cursor.getColumnIndex(SONG_SINGER)));
                    song.setPlaylistID(cursor.getInt(cursor.getColumnIndex(SONG_PLAYLISTID)));
                    song.setSongPath(cursor.getString(cursor.getColumnIndex(SONG_PATH)));
                    song.setDateAdded(cursor.getString(cursor.getColumnIndex(SONG_DATEADDED)));
                    song.setSongAlbumArt(cursor.getString(cursor.getColumnIndex(SONG_ALBUMART)));

                    // Add the song to the list
                    songList.add(song);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("SongHandler", "Error fetching songs for playlist", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return songList;
    }

    public void deleteSong(int songID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SONG, SONG_ID + "=?", new String[]{String.valueOf(songID)});
        db.close();
    }

}
