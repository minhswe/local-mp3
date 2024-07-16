package vlu.android.numberseven.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vlu.android.numberseven.R;
import vlu.android.numberseven.models.Song;

public class SongRecyclerViewAdapter extends RecyclerView.Adapter<SongRecyclerViewAdapter.SongRecyclerViewHolder> {
    private Context mContext;
    private ArrayList<Song> mSongList;

    public SongRecyclerViewAdapter(Context context, ArrayList<Song> songList) {
        this.mContext = context;
        this.mSongList = songList;
    }
    @NonNull
    @Override
    public SongRecyclerViewAdapter.SongRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_recycler_view_item, parent, false);
        return new SongRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongRecyclerViewAdapter.SongRecyclerViewHolder holder, int position) {
        Song song = mSongList.get(position);
        if (song.getSongAlbumArt() != null && !song.getSongAlbumArt().isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(song.getSongAlbumArt());
            if (bitmap != null) {
                holder.ivSongAlbumArt.setImageBitmap(bitmap);
            } else {
                holder.ivSongAlbumArt.setImageResource(R.drawable.baseline_add_box_24);
            }
        } else {
            holder.ivSongAlbumArt.setImageResource(R.drawable.baseline_add_box_24);
        }
        holder.tvSongName.setText(song.getSongName());
        holder.tvSinger.setText(song.getSinger());
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    public class SongRecyclerViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivSongAlbumArt;
        public TextView tvSongName;
        public TextView tvSinger;

        public SongRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSongAlbumArt = itemView.findViewById(R.id.ivRecyclerSongAlbumArt);
            tvSongName = itemView.findViewById(R.id.tvRecyclerViewSongName);
            tvSinger = itemView.findViewById(R.id.tvRecyclerViewSinger);
        }
    }
}
