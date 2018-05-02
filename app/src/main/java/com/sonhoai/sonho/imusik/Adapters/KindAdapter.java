package com.sonhoai.sonho.imusik.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sonhoai.sonho.imusik.Models.Kind;
import com.sonhoai.sonho.imusik.R;

import java.util.List;

public class KindAdapter extends RecyclerView.Adapter<KindAdapter.ViewHolder> {
    private List<Kind> kinds;

    public KindAdapter(List<Kind> kinds) {
        this.kinds = kinds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_kind, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(kinds.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return kinds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView  txtName;
        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtNameKind);
        }
    }
}
