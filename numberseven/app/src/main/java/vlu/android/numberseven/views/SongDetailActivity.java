package vlu.android.numberseven.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

import vlu.android.numberseven.R;
import vlu.android.numberseven.controllers.SongHandler;
import vlu.android.numberseven.models.Song;

public class SongDetailActivity extends AppCompatActivity {
    ImageView ivSongDetailAlbumArt;
    EditText edtSongName, edtSongSinger, edtSongPath, edtSongDateAdded;
    Button btnUpdateSong, btnDeleteSong;
    Song songDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_song_detail);
        controls();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        events();
        receiveSongDetail();
    }

    void controls() {
        ivSongDetailAlbumArt = findViewById(R.id.ivSongDetailAlbumArt);
        edtSongName = findViewById(R.id.edtSongDetailName);
        edtSongSinger = findViewById(R.id.edtSongDetailSinger);
        edtSongPath = findViewById(R.id.edtSongDetailPath);
        edtSongDateAdded = findViewById(R.id.edtSongDetailDateAdded);
        btnUpdateSong = findViewById(R.id.btnUpdateSong);
        btnDeleteSong = findViewById(R.id.btnDeleteSong);
    }

    void events(){
        btnUpdateSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedSongName = edtSongName.getText().toString().trim();
                String updatedSinger = edtSongSinger.getText().toString().trim();
                String updatedSongPath = edtSongPath.getText().toString().trim();
                String updatedDateAdded = edtSongDateAdded.getText().toString().trim();
                if (songDetail != null) {
                    songDetail.setSongName(updatedSongName);
                    songDetail.setSinger(updatedSinger);
                    songDetail.setSongPath(updatedSongPath);
                    songDetail.setDateAdded(updatedDateAdded);
                    SongHandler songHandler = new SongHandler(SongDetailActivity.this, "number7", null, 1);
                    songHandler.updateSong(songDetail);
                    Toast.makeText(SongDetailActivity.this, "Song updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("updatedSong", songDetail);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        btnDeleteSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SongDetailActivity.this)
                        .setTitle("Confirm Delete")
                        .setMessage("Are you sure you want to delete this song?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int songId = songDetail.getSongID();
                                SongHandler songHandler = new SongHandler(SongDetailActivity.this, "number7", null, 1);
                                songHandler.deleteSong(songId);
                                Toast.makeText(SongDetailActivity.this, "Song deleted successfully", Toast.LENGTH_SHORT).show();
                                finish(); // Close the activity after deletion
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User cancelled the operation, do nothing
                            }
                        })
                        .show();
            }
        });
    }

    void receiveSongDetail() {
        songDetail = (Song) getIntent().getSerializableExtra("songDetail");
        String albumArtPath = songDetail.getSongAlbumArt();
        if (albumArtPath != null && !albumArtPath.isEmpty()) {
            File imgFile = new File(albumArtPath);
            Bitmap bitmap = null;
            if (imgFile.exists()) {
                bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            }
            if (bitmap != null) {
                ivSongDetailAlbumArt.setImageBitmap(bitmap);
            } else {
                ivSongDetailAlbumArt.setImageResource(R.drawable.music_icon); // Placeholder image
            }
        } else {
            ivSongDetailAlbumArt.setImageResource(R.drawable.music_icon); // Placeholder image when path is empty or null
        }
            edtSongName.setText(songDetail.getSongName());
            edtSongSinger.setText(songDetail.getSinger());
            edtSongPath.setText(songDetail.getSongPath());
            edtSongDateAdded.setText(songDetail.getDateAdded());
        }
    }