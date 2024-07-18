package vlu.android.numberseven.models;

import java.io.Serializable;

public class Song  implements Serializable {
    private static final long serialVersionUID = 1L;
    int songID;
    String songName;
    String singer;
    int playlistID;
    String songPath;
    String dateAdded;
    String songAlbumArt;

    public Song() {
    }

    public Song(int songID, String songName, String singer, int playlistID, String songPath, String dateAdded, String songAlbumArt) {
        this.songID = songID;
        this.songName = songName;
        this.singer = singer;
        this.playlistID = playlistID;
        this.songPath = songPath;
        this.dateAdded = dateAdded;
        this.songAlbumArt = songAlbumArt;
    }

    public int getSongID() {
        return songID;
    }

    public void setSongID(int songID) {
        this.songID = songID;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public int getPlaylistID() {
        return playlistID;
    }

    public void setPlaylistID(int playlistID) {
        this.playlistID = playlistID;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getSongAlbumArt() {
        return songAlbumArt;
    }

    public void setSongAlbumArt(String songAlbumArt) {
        this.songAlbumArt = songAlbumArt;
    }
}
