package com.sonhoai.sonho.imusik.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sonhoai.sonho.imusik.API.Delete;
import com.sonhoai.sonho.imusik.Constants.Connect;
import com.sonhoai.sonho.imusik.Fragments.DetailPlayListFragment;
import com.sonhoai.sonho.imusik.Interface.CallBack;
import com.sonhoai.sonho.imusik.MainActivity;
import com.sonhoai.sonho.imusik.Models.DetailPlayList;
import com.sonhoai.sonho.imusik.Models.Song;
import com.sonhoai.sonho.imusik.R;
import com.sonhoai.sonho.imusik.Util.CircleTransformHelper;
import com.sonhoai.sonho.imusik.Util.PlayerHelper;
import com.sonhoai.sonho.imusik.Util.SharedPreferencesHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DetailPlayListAdapter extends RecyclerView.Adapter<DetailPlayListAdapter.ViewHolder> {
    private Context context;
    private List<DetailPlayList> detailPlayLists;

    public DetailPlayListAdapter(Context context, List<DetailPlayList> detailPlayLists) {
        this.context = context;
        this.detailPlayLists = detailPlayLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_song_pl, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.with(context)
                .load(Connect.URLASSET+detailPlayLists.get(position).getImageSong())
                .resize(130, 130)
                .centerCrop()
                .transform(new CircleTransformHelper())
                .into(holder.imgCover);
        holder.txtNameSinger.setText(detailPlayLists.get(position).getNameSinger());
        holder.txtName.setText(detailPlayLists.get(position).getNameSong());
    }

    @Override
    public int getItemCount() {
        return detailPlayLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtNameSinger;
        private ImageButton btnMoreInfoSong;
        private ImageView imgCover;
        public ViewHolder(View itemView) {
            super(itemView);

            imgCover = itemView.findViewById(R.id.imgSongCover);
            txtName = itemView.findViewById(R.id.txtSongName);
            txtNameSinger = itemView.findViewById(R.id.txtSongSinger);

            btnMoreInfoSong = itemView.findViewById(R.id.btnMoreInfoSong);
            btnMoreInfoSong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(getAdapterPosition());
                }
            });

            handleOnLongClick(itemView);
            handleOnClick(itemView, null, 0);
        }

        private void handleOnClick(View itemView, @Nullable final AlertDialog dialog, @Nullable final int flag) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.txtNameCurrentSong.setText(detailPlayLists.get(getAdapterPosition()).getNameSong());
                    MainActivity.imgPlayPause.setImageResource(R.drawable.ic_pause_black_24dp);
                    Song song = new Song(
                            detailPlayLists.get(getAdapterPosition()).getIdSong(),
                            detailPlayLists.get(getAdapterPosition()).getIdKind(),
                            detailPlayLists.get(getAdapterPosition()).getIdSinger(),
                            detailPlayLists.get(getAdapterPosition()).getNameSong(),
                            detailPlayLists.get(getAdapterPosition()).getNameSinger(),
                            detailPlayLists.get(getAdapterPosition()).getImageSong(),
                            detailPlayLists.get(getAdapterPosition()).getUrlSong(),
                            detailPlayLists.get(getAdapterPosition()).getLuotNghe()
                    );
                    PlayerHelper.getInstance().play(song);
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
                    Song song = new Song(
                            detailPlayLists.get(getAdapterPosition()).getIdSong(),
                            detailPlayLists.get(getAdapterPosition()).getIdKind(),
                            detailPlayLists.get(getAdapterPosition()).getIdSinger(),
                            detailPlayLists.get(getAdapterPosition()).getNameSong(),
                            detailPlayLists.get(getAdapterPosition()).getNameSinger(),
                            detailPlayLists.get(getAdapterPosition()).getImageSong(),
                            detailPlayLists.get(getAdapterPosition()).getUrlSong(),
                            detailPlayLists.get(getAdapterPosition()).getLuotNghe()
                    );
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

    private void delete(final int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myDialog));
        builder.setTitle("Do you want to remove it?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doDelete(id);

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
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
                            detailPlayLists.remove(id);
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
        }).execute("/PlaylistsApi/deleteSong/?token="+ SharedPreferencesHelper.getInstance(context).getToken()
                +"&idUser="+ SharedPreferencesHelper.getInstance(context).getIdUser()+"&idList="+ DetailPlayListFragment.idPL+"&idDetail="+detailPlayLists.get(id).getId());
    }
}
