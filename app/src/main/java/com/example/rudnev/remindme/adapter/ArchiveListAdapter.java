package com.example.rudnev.remindme.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rudnev.remindme.R;
import com.example.rudnev.remindme.RemindItemClickListener;
import com.example.rudnev.remindme.dto.RemindDTO;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ArchiveListAdapter extends RecyclerView.Adapter<ArchiveListAdapter.RemindViewHolder> {
    private List<RemindDTO> data;
    private static RemindItemClickListener itemClickListener;

    public ArchiveListAdapter(List<RemindDTO> data, RemindItemClickListener remindItemClickListener){
        this.data = data;
        itemClickListener = remindItemClickListener;
    }

    public ArchiveListAdapter(RemindItemClickListener remindItemClickListener){
        itemClickListener = remindItemClickListener;
    }

    @Override
    public ArchiveListAdapter.RemindViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.remind_item, parent, false);
        return new ArchiveListAdapter.RemindViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArchiveListAdapter.RemindViewHolder holder, int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.title.setText(data.get(position).getTitle());
        holder.date.setText(sdf.format(data.get(position).getDate().toDate()));
    }

    @Override
    public int getItemCount() {
        if(data!=null)
            return data.size();
        else
            return 0;
    }

    public static class RemindViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cardView;
        TextView title;
        TextView note;
        TextView date;

        public RemindViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.date);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    itemClickListener.popupMenuItemClicked(view, getLayoutPosition());
                    return false;
                }
            });

        }
        @Override
        public void onClick(View v)
        {
            //itemClickListener.remindListRemoveClicked(v, this.getLayoutPosition());

        }
    }

    public void setData(List<RemindDTO> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public RemindDTO getItemById(int id){
        return data.get(id);
    }

    public String getTitle(int position){

        return data.get(position).getTitle();
    }
    public String getNote(int position){

        return data.get(position).getNote();
    }
    public LocalDateTime getDate(int position){

        return data.get(position).getDate();
    }
}
