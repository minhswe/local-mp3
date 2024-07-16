package vlu.android.numberseven.controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import vlu.android.numberseven.models.Playlist;
import vlu.android.numberseven.models.Song;

public class PlaylistHandler extends SQLiteOpenHelper {

    private static final String PATH = "/data/data/vlu.android.numberseven/databases/number7.db";
    private static final String DB_NAME = "number7";
    private static final int DB_VERSION = 1;
    private static final String TABLE_PLAYLIST = "Playlist";
    private static final String PLAYLIST_ID = "PlaylistID";
    private static final String PLAYLIST_NAME = "PlaylistName";
    private static final String PLAYLIST_USERNAME = "Username";
    private static final String PLAYLIST_CREATEON = "CreateOn";

    public PlaylistHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            ensureRecentlyColumnExists(db);

    }

    private void ensureRecentlyColumnExists(SQLiteDatabase db) {
        if (!isColumnExists(db, "Recently")) {
            // Alter the table to add the "Recently" column
            String addColumnQuery = "ALTER TABLE " + TABLE_PLAYLIST + " ADD COLUMN " + "Recently" + " TEXT DEFAULT 'No'";
            db.execSQL(addColumnQuery);
        }
    }

    private boolean isColumnExists(SQLiteDatabase db, String columnName) {
        boolean columnExists = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("PRAGMA table_info(" + TABLE_PLAYLIST + ")", null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("Recently"));
                    if (name.equals(columnName)) {
                        columnExists = true;
                        break;
                    }
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return columnExists;
    }

    public void addSong(Song song) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READWRITE);

    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public ArrayList<Playlist> getAllPlaylist(){
        ArrayList<Playlist> playlists = new ArrayList<>();
        SQLiteDatabase db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PLAYLIST, null);
        if (cursor.moveToFirst()){
            do{
                Playlist playlist = new Playlist();
                playlist.setPlaylistID(cursor.getInt(0));
                playlist.setPlaylistName(cursor.getString(1));
                playlist.setUsername(cursor.getString(2));
                playlist.setCreateOn(cursor.getString(3));
                playlists.add(playlist);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return playlists;
    }
}


