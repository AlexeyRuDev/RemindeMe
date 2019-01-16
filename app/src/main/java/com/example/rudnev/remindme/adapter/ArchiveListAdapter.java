package com.example.rudnev.remindme.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rudnev.remindme.R;
import com.example.rudnev.remindme.RemindItemClickListener;
import com.example.rudnev.remindme.dto.RemindDTO;

import org.joda.time.LocalDateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ArchiveListAdapter extends RecyclerView.Adapter<ArchiveListAdapter.RemindViewHolder> implements Filterable {
    private List<RemindDTO> data;
    private List<RemindDTO> filteredData;
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
        if(filteredData!=null)
            return filteredData.size();
        else
            return 0;
    }


    public static class RemindViewHolder extends CommonViewHolder implements View.OnClickListener{

        CardView cardView;
        TextView title;
        TextView date;
        public RelativeLayout viewBackground, viewForeground;

        public RemindViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.date);

            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
        }

        @Override
        public View getForegroundView() {
            return viewForeground;
        }

        @Override
        public View getBackgroundView() {
            return viewBackground;
        }

        @Override
        public void onClick(View v)
        {
            itemClickListener.remindListOpenClicked(v, this.getLayoutPosition());
        }

    }

    public void setData(List<RemindDTO> data) {
        this.data = data;
        this.filteredData = data;
        notifyDataSetChanged();
    }

    public RemindDTO getItemById(int id){
        return filteredData.get(id);
    }

    public String getTitle(int position){

        return filteredData.get(position).getTitle();
    }
    public String getNote(int position){

        return filteredData.get(position).getNote();
    }
    public LocalDateTime getDate(int position){

        return filteredData.get(position).getDate();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredData = data;
                } else {
                    List<RemindDTO> filteredList = new ArrayList<>();
                    for (RemindDTO row : data) {

                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    filteredData = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredData;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredData = (ArrayList<RemindDTO>) filterResults.values;
                setData(filteredData);
            }
        };
    }
}
