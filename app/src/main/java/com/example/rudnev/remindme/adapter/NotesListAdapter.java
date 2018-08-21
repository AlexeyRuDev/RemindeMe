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
import com.example.rudnev.remindme.dto.Notes;

import java.util.List;


public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NoteViewHolder> {
    private List<Notes> data;
    private static RemindItemClickListener itemClickListener;

    public NotesListAdapter(RemindItemClickListener remindItemClickListener){
        itemClickListener = remindItemClickListener;
    }

    @Override
    public NotesListAdapter.NoteViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.remind_item, parent, false);
        return new NotesListAdapter.NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotesListAdapter.NoteViewHolder holder, int position) {

        holder.title.setText(data.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        if(data!=null)
            return data.size();
        else
            return 0;
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cardView;
        TextView title;
        TextView note;
        TextView date;

        public NoteViewHolder(View itemView) {
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
            //change to open item read only
            //itemClickListener.remindListRemoveClicked(v, this.getLayoutPosition());
        }

    }

    public void setData(List<Notes> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public Notes getItemById(int id){
        return data.get(id);
    }

    public String getTitle(int position){

        return data.get(position).getTitle();
    }
    public String getNote(int position){

        return data.get(position).getNote();
    }

}