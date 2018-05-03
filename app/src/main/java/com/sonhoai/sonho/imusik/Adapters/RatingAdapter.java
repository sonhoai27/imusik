package com.sonhoai.sonho.imusik.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sonhoai.sonho.imusik.Models.Rate;
import com.sonhoai.sonho.imusik.R;

import org.w3c.dom.Text;

import java.util.List;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {
    private List<Rate> rates;
    private Context context;

    public RatingAdapter(List<Rate> rates, Context context) {
        this.rates = rates;
        this.context = context;
    }

    @NonNull
    @Override
    public RatingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_rate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingAdapter.ViewHolder holder, int position) {
        holder.name.setText(rates.get(position).getUser());
        if(rates.get(position).getNoLove() != 0){
            holder.ratingBar.setRating(1);
        }else if(rates.get(position).getLove() != 0){
            holder.ratingBar.setRating(3);
        }else if(rates.get(position).getLittleLove() != 0){
            holder.ratingBar.setRating(2);
        }else if(rates.get(position).getLotsofLove() != 0){
            holder.ratingBar.setRating(4);
        }else if(rates.get(position).getSuperLove() != 0){
            holder.ratingBar.setRating(5);
        }

    }

    @Override
    public int getItemCount() {
        return rates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private RatingBar ratingBar;
        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.emailUser);
            ratingBar = itemView.findViewById(R.id.itemRtBar);
        }
    }
}
