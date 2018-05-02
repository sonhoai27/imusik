package com.sonhoai.sonho.imusik.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sonhoai.sonho.imusik.Models.Playlist;
import com.sonhoai.sonho.imusik.R;
import com.sonhoai.sonho.imusik.Util.CircleTransformHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.sonhoai.sonho.imusik.Constants.Connect.URLASSET;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {
    private List<Playlist> playlists;
    private Context context;

    public PlayListAdapter(List<Playlist> playlists, Context context) {
        this.playlists = playlists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(playlists.get(position).getName());
        holder.txtDate.setText(playlists.get(position).getCreated_date());
        Picasso.with(context)
                .load(URLASSET+playlists.get(position).getImagePlaylist())
                .resize(150,150)
                .centerCrop()
                .transform(new CircleTransformHelper())
                .into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtDate;
        private ImageView cover;
        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtPlaylistName);
            txtDate = itemView.findViewById(R.id.txtPlaylistDate);
            cover = itemView.findViewById(R.id.imgPlaylistCover);
        }
    }
}
