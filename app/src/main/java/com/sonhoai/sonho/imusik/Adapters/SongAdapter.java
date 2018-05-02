package com.sonhoai.sonho.imusik.Adapters;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sonhoai.sonho.imusik.Activities.PlayerActivity;
import com.sonhoai.sonho.imusik.Constants.Connect;
import com.sonhoai.sonho.imusik.Constants.State;
import com.sonhoai.sonho.imusik.MainActivity;
import com.sonhoai.sonho.imusik.Models.Song;
import com.sonhoai.sonho.imusik.R;
import com.sonhoai.sonho.imusik.Util.CircleTransformHelper;
import com.sonhoai.sonho.imusik.Util.PlayerHelper;
import com.squareup.picasso.Picasso;


import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private Context context;
    private List<Song> songList;

    public SongAdapter(Context context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.with(context)
                .load(Connect.URLASSET+songList.get(position).getImageSong())
                .resize(130, 130)
                .centerCrop()
                .transform(new CircleTransformHelper())
                .into(holder.imgCover);
        holder.txtNameSinger.setText(songList.get(position).getNameSinger());
        holder.txtName.setText(songList.get(position).getNameSong());
        holder.txtLuotNghe.setText(songList.get(position).getLuotNghe());
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtNameSinger, txtLuotNghe;
        private ImageView imgCover;
        public ViewHolder(View itemView) {
            super(itemView);

            imgCover = itemView.findViewById(R.id.imgSongCover);
            txtName = itemView.findViewById(R.id.txtSongName);
            txtLuotNghe = itemView.findViewById(R.id.txtSongLuotNghe);
            txtNameSinger = itemView.findViewById(R.id.txtSongSinger);

            handleOnLongClick(itemView);
            handleOnClick(itemView, null, 0);
        }

        private void handleOnClick(View itemView, @Nullable final AlertDialog dialog, @Nullable final int flag) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.txtNameCurrentSong.setText(songList.get(getAdapterPosition()).getNameSong());
                    MainActivity.imgPlayPause.setImageResource(R.drawable.ic_pause_black_24dp);
                    PlayerHelper.getInstance().play(songList.get(getAdapterPosition()));
                    if(dialog != null && flag == 1){
                        dialog.dismiss();
                    }
                    Toast.makeText(context, "Playing", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void handleOnLongClick(View itemView){
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Song song = songList.get(getAdapterPosition());
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myDialog));
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View dialogView = inflater.inflate(R.layout.dialog_show_more_player, null);
                    builder.setView(dialogView);

                    //khai bao

                    TextView titleSong, titleSinger;
                    ImageView cover;
                    LinearLayout layoutPlayNow;

                    //anh xa

                    titleSinger = dialogView.findViewById(R.id.dPlayerMoreSinger);
                    titleSong = dialogView.findViewById(R.id.dPlayerMoreTitle);
                    cover = dialogView.findViewById(R.id.dPlayerMoreCover);
                    layoutPlayNow = dialogView.findViewById(R.id.layoutPlayNow);

                    AlertDialog alertDialog = builder.create();

                    layoutPlayNow.setVisibility(View.VISIBLE);

                    //custom position
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Window dialogWindow = alertDialog.getWindow();
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    lp.alpha = 0.88f;
                    lp.x = 0; // The new position of the X coordinates
                    lp.y = 32; // The new position of the Y coordinates
                    dialogWindow.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                    dialogWindow.setAttributes(lp);

                    Picasso.with(context)
                            .load(Connect.URLASSET+song.getImageSong())
                            .resize(200, 200)
                            .centerCrop()
                            .into(cover);
                    titleSinger.setText(song.getNameSinger());
                    titleSong.setText(song.getNameSong());

                    handleOnClick(layoutPlayNow, alertDialog, 1);
                    //show
                    alertDialog.show();
                    return false;
                }
            });
        }
    }
}
