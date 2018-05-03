package com.sonhoai.sonho.imusik.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sonhoai.sonho.imusik.API.Post;
import com.sonhoai.sonho.imusik.Interface.CallBack;
import com.sonhoai.sonho.imusik.Models.Playlist;
import com.sonhoai.sonho.imusik.Models.Song;
import com.sonhoai.sonho.imusik.R;
import com.sonhoai.sonho.imusik.Util.CircleTransformHelper;
import com.sonhoai.sonho.imusik.Util.SharedPreferencesHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.sonhoai.sonho.imusik.Constants.Connect.URLASSET;

public class addSongToListAdapter extends RecyclerView.Adapter<addSongToListAdapter.ViewHolder> {
    private Context context;
    private List<Playlist> playlists;
    private int idSong;

    public addSongToListAdapter(Context context, List<Playlist> playlists, int idSong) {
        this.context = context;
        this.playlists = playlists;
        this.idSong = idSong;
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
                .into(holder.imgCover);
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtDate;
        private ImageView imgCover;
        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtPlaylistName);
            txtDate = itemView.findViewById(R.id.txtPlaylistDate);
            imgCover = itemView.findViewById(R.id.imgPlaylistCover);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject object = new JSONObject();
                    try {
                        object.put("idSong", idSong);
                        object.put("idList", playlists.get(getAdapterPosition()).getId());
                        addSongToList(object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void addSongToList(final JSONObject object){
        new Post(new CallBack<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object1 = new JSONObject(result);
                    if(object1.getInt("status")==200){
                        Toast.makeText(context,"OK" ,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String result) {

            }
        },object ).execute("/PlaylistsApi/AddSongToPlaylist/?idUser="+SharedPreferencesHelper.getInstance(context).getIdUser()+"&token="+SharedPreferencesHelper.getInstance(context).getToken());
    }
}
