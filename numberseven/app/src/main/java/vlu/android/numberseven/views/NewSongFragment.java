package vlu.android.numberseven.views;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import vlu.android.numberseven.R;
import vlu.android.numberseven.controllers.PlaylistHandler;
import vlu.android.numberseven.controllers.SongHandler;
import vlu.android.numberseven.models.Playlist;
import vlu.android.numberseven.models.Song;
import vlu.android.numberseven.models.User;

public class NewSongFragment extends Fragment {
    private static final int PICK_FILE_REQUEST = 1;

    SearchView svSong;
    EditText edtSongName, edtSinger;
    Button btnAddSong, btnBrowseSong;
    ImageView ivAlbumArt;
    RecyclerView rcvSongList;
    Spinner spnPlaylist;
    PlaylistHandler playlistHandler;
    SongHandler songHandler;

    PlaylistSpinnerAdapter playlistSpinnerAdapter;

    SongRecyclerViewAdapter songRecyclerViewAdapter;

    Song song;
    int selectedPlaylistID;

    public NewSongFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_song, container, false);
        controls(view);
        events();
        User user = new User("quangminh", "123", "Quang Minh", "abc@gmaill.com");
        return view;
    }

    private void controls(View view) {
        svSong = view.findViewById(R.id.svSong);
        edtSongName = view.findViewById(R.id.edtSongName);
        edtSinger = view.findViewById(R.id.edtSinger);
        btnBrowseSong = view.findViewById(R.id.btnBrowseSong);
        btnAddSong = view.findViewById(R.id.btnAddSong);
        rcvSongList = view.findViewById(R.id.rcvSongList);
        rcvSongList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        ivAlbumArt = view.findViewById(R.id.ivAlbumArt);
        spnPlaylist = view.findViewById(R.id.spnPlaylist);
        playlistHandler = new PlaylistHandler(getActivity(), "number7", null, 1);
        loadPlaylistDataOnSpinner();
//        loadSongForRecyclerView(selectedPlaylistID);
    }

    private void events() {
        btnBrowseSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });

        btnAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songHandler = new SongHandler(getActivity(), "number7", null, 1);
                songHandler.addSong(song);
                loadSongForRecyclerView(selectedPlaylistID);
                Toast.makeText(getActivity(), "Song added successfully", Toast.LENGTH_SHORT).show();
            }
        });

        spnPlaylist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Playlist selectedPlaylist = (Playlist) parent.getItemAtPosition(position);
                selectedPlaylistID = selectedPlaylist.getPlaylistID();
                loadSongForRecyclerView(selectedPlaylistID);
                Toast.makeText(getActivity(), "Selected Playlist ID: " + selectedPlaylistID, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(getActivity(), fileUri);
                String title = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                String artist = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                byte[] art = mediaMetadataRetriever.getEmbeddedPicture();
                String albumArtPath = null;

                if (art != null) {
                    Bitmap albumArt = BitmapFactory.decodeByteArray(art, 0, art.length);
                    ivAlbumArt.setImageBitmap(albumArt);
                    albumArtPath = saveAlbumArtToFile(albumArt, title); // Save the album art and get the file path
                } else {
                    ivAlbumArt.setImageResource(R.drawable.baseline_add_box_24); // Set default image if no album art found
                }

                String fileName = getFileName(fileUri);
                edtSongName.setText(title != null ? title : fileName);
                edtSinger.setText(artist != null ? artist : "Unknown Artist");

                // Create a new song and add it to the database
                song = new Song();
                song.setSongName(title != null ? title : fileName);
                song.setSinger(artist != null ? artist : "Unknown Artist");
                song.setPlaylistID(1);
                song.setSongPath(fileUri.toString());
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                String dateAdded = dateFormat.format(new Date());
                song.setDateAdded(dateAdded);
                song.setSongAlbumArt(albumArtPath);
            }
        }
    }

    private String saveAlbumArtToFile(Bitmap albumArt, String title) {
        File directory = getActivity().getFilesDir(); // Use internal storage directory
        String fileName = "album_art_" + title.replaceAll("[^a-zA-Z0-9]", "_") + ".png"; // Sanitize the file name
        File file = new File(directory, fileName);

        try (OutputStream os = new FileOutputStream(file)) {
            albumArt.compress(Bitmap.CompressFormat.PNG, 100, os);
            return file.getAbsolutePath();
        } catch (IOException e) {
            Log.e("SaveAlbumArt", "Error saving album art", e);
            return null;
        }
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    void loadPlaylistDataOnSpinner() {
        ArrayList<Playlist> playlists = new ArrayList<>();
        playlists = playlistHandler.getAllPlaylist();
        if (playlists == null || playlists.isEmpty()) {
            ArrayList<String> stringArrayList = new ArrayList<>();
            stringArrayList.add("No playlists to show");
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                    getActivity(),
                    android.R.layout.simple_spinner_dropdown_item,
                    stringArrayList
            );
            spnPlaylist.setAdapter(spinnerAdapter);
        } else {
            playlistSpinnerAdapter = new PlaylistSpinnerAdapter(
                    getActivity(),
                    playlists
            );
            spnPlaylist.setAdapter(playlistSpinnerAdapter);
        }
    }

    void loadSongForRecyclerView(int selectedPlaylistID){
        songHandler = new SongHandler(getActivity(), "number7", null, 1);
        ArrayList<Song> songArrayList = songHandler.getSongForPlaylist(selectedPlaylistID);
        if (songArrayList != null) {
            songRecyclerViewAdapter = new SongRecyclerViewAdapter(getActivity(), songArrayList);
            rcvSongList.setAdapter(songRecyclerViewAdapter);
        } else {
            rcvSongList.setAdapter(null);
        }
    }
}
