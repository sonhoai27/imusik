package com.sonhoai.sonho.imusik.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sonhoai.sonho.imusik.API.Delete;
import com.sonhoai.sonho.imusik.Fragments.DetailPlayListFragment;
import com.sonhoai.sonho.imusik.Interface.CallBack;
import com.sonhoai.sonho.imusik.Models.Playlist;
import com.sonhoai.sonho.imusik.R;
import com.sonhoai.sonho.imusik.Util.CircleTransformHelper;
import com.sonhoai.sonho.imusik.Util.SharedPreferencesHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myDialog));
                    builder.setTitle("Do you want to remove it?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doDelete(getAdapterPosition());
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.show();
                    return false;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSongView(getAdapterPosition());
                }
            });
        }
    }

    private void showSongView(int po){
        DetailPlayListFragment fragmentDialog = DetailPlayListFragment.newInstance(String.valueOf(playlists.get(po).getId()), playlists.get(po).getName());
        fragmentDialog.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentDialog.show(fm, "AAA");
    }

    private void doDelete(final int id){
        new Delete(new CallBack<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    if(!result.isEmpty()){
                        JSONObject object = new JSONObject(result);
                        if(object.getInt("status")==200){
                            Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
                            playlists.remove(id);
                            notifyDataSetChanged();
                        }
                    }else {
                        Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String result) {

            }
        }).execute("/PlaylistsApi/"+playlists.get(id).getId()+"/?token="+ SharedPreferencesHelper.getInstance(context).getToken()
                +"&user="+ SharedPreferencesHelper.getInstance(context).getIdUser());
    }
}
