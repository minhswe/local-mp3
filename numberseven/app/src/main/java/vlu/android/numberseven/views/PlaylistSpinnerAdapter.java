package vlu.android.numberseven.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import vlu.android.numberseven.R;
import vlu.android.numberseven.models.Playlist;

public class PlaylistSpinnerAdapter extends ArrayAdapter<Playlist> {
    private Context context;
    private ArrayList<Playlist> playlists;



    public PlaylistSpinnerAdapter(@NonNull Context context, ArrayList<Playlist> playlists) {
        super(context, R.layout.playlist_spinner_item, playlists);
        this.context = context;
        this.playlists = playlists;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, R.layout.playlist_spinner_item);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, R.layout.playlist_spinner_item);
    }

    private View createViewFromResource(int position, @Nullable View convertView, @NonNull ViewGroup parent, int resource) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, parent, false);
        }

        Playlist playlist = playlists.get(position);
        TextView tvPlaylistName = view.findViewById(R.id.tvSpinnerPlaylisName);
        tvPlaylistName.setText(playlist.getPlaylistName());
        return view;
    }
}
