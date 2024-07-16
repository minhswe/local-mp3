package vlu.android.numberseven.models;

public class Playlist {
    int playlistID;
    String playlistName;
    String username;
    String createOn;

    public Playlist() {
    }

    public Playlist(int playlistID, String playlistName, String username, String createOn) {
        this.playlistID = playlistID;
        this.playlistName = playlistName;
        this.username = username;
        this.createOn = createOn;
    }

    public int getPlaylistID() {
        return playlistID;
    }

    public void setPlaylistID(int playlistID) {
        this.playlistID = playlistID;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreateOn() {
        return createOn;
    }

    public void setCreateOn(String createOn) {
        this.createOn = createOn;
    }
}
