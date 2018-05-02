package com.sonhoai.sonho.imusik.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sonhoai.sonho.imusik.Activities.PlayerActivity;
import com.sonhoai.sonho.imusik.Fragments.DetailPlayListFragment;
import com.sonhoai.sonho.imusik.MainActivity;
import com.sonhoai.sonho.imusik.Models.Singer;
import com.sonhoai.sonho.imusik.R;
import com.squareup.picasso.Picasso;
import com.sonhoai.sonho.imusik.Util.*;

import java.util.List;

import static com.sonhoai.sonho.imusik.Constants.Connect.URLASSET;

public class SingerAdapter extends RecyclerView.Adapter<SingerAdapter.ViewHolder>{
    private List<Singer> singers;
    private Context context;

    public SingerAdapter(List<Singer> singers, Context context) {
        this.singers = singers;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_singer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(singers.get(position).getName());
        Picasso.with(context)
                .load(URLASSET+singers.get(position).getImage())
                .transform(new CircleTransformHelper())
                .into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return singers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView cover;
        private TextView txtName;
        public ViewHolder(View itemView) {
            super(itemView);

            cover = itemView.findViewById(R.id.imgSingerCover);
            txtName = itemView.findViewById(R.id.txtSingerName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
