package com.sonhoai.sonho.imusik.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sonhoai.sonho.imusik.Models.ForYou;
import com.sonhoai.sonho.imusik.R;

import java.util.ArrayList;
import java.util.List;

public class ForYouAdapter extends RecyclerView.Adapter<ForYouAdapter.ViewHolder>{
    private List<ForYou> forYous;

    public ForYouAdapter() {
        forYous = new ArrayList<>();
        forYous.add(new ForYou(
                "FY2",
                "Song Added",
                R.drawable.ic_last_song_added,
                R.drawable.gradient_2
        ));
        forYous.add(new ForYou(
                "FY0",
                "Favorite",
                R.drawable.ic_favorite_white_24dp,
                R.drawable.gradient_5
        ));
        forYous.add(new ForYou(
                "FY1",
                "History",
                R.drawable.ic_history,
                R.drawable.gradient_1
        ));
        forYous.add(new ForYou(
                "FY3",
                "My Playlist",
                R.drawable.ic_album,
                R.drawable.gradient_4
        ));
        Log.i("MANG", String.valueOf(forYous));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_for_you, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bgForYou.setBackgroundResource(forYous.get(position).getBackground());
        holder.imgIcon.setImageResource(forYous.get(position).getImage());
        holder.txtName.setText(forYous.get(position).getName());
    }

    @Override
    public int getItemCount() {
        Log.i("SIZEEEE", forYous.size()+"");
        return forYous.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtName;
        private ImageView imgIcon;
        private LinearLayout bgForYou;
        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtNameForYou);
            imgIcon = itemView.findViewById(R.id.forYouImg);
            bgForYou = itemView.findViewById(R.id.bgForYou);
        }
    }
}
