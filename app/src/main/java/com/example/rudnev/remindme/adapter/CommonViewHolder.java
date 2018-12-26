package com.example.rudnev.remindme.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class CommonViewHolder extends RecyclerView.ViewHolder{
    public CommonViewHolder(View itemView) {
        super(itemView);
    }

    public abstract View getForegroundView();
}
