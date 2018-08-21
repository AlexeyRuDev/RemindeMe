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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CalendarItemsListAdapter extends RecyclerView.Adapter<CalendarItemsListAdapter.RemindViewHolder>  {
    private List<RemindDTO> data;
    private static RemindItemClickListener calItemClickListener;

    public CalendarItemsListAdapter(List<RemindDTO> data, RemindItemClickListener remindItemClickListener){
        this.data = data;
        calItemClickListener = remindItemClickListener;
    }

    public CalendarItemsListAdapter(RemindItemClickListener remindItemClickListener){
        calItemClickListener = remindItemClickListener;
    }

    @Override
    public CalendarItemsListAdapter.RemindViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.remind_item, parent, false);
        return new CalendarItemsListAdapter.RemindViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CalendarItemsListAdapter.RemindViewHolder holder, int position) {
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        holder.title.setText(data.get(position).getTitle());
        holder.date.setText(sdf.format(data.get(position).getDate()));
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
            ImageButton imageButton = itemView.findViewById(R.id.ib_popup_menu);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    calItemClickListener.popupMenuItemClicked(view, getLayoutPosition());
                }
            });

        }
        @Override
        public void onClick(View v)
        {
            calItemClickListener.remindListRemoveClicked(v, this.getLayoutPosition());

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
    public Date getDate(int position){

        return data.get(position).getDate();
    }
}
