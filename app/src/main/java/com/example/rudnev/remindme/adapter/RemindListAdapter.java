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

import java.util.Date;
import java.util.List;

public class RemindListAdapter extends RecyclerView.Adapter<RemindListAdapter.RemindViewHolder> {

    private List<RemindDTO> data;
    //private Cursor cursor;
    //private DataSetObserver mDataSetObserver;
    private static RemindItemClickListener itemClickListener;

    public RemindListAdapter(List<RemindDTO> data, RemindItemClickListener remindItemClickListener/*,Cursor cursor*/){
        this.data = data;
        itemClickListener = remindItemClickListener;
        //this.cursor = cursor;
        //mDataSetObserver = new NotifyingDataSetObserver(this);
        /*if(cursor!=null){cursor.registerDataSetObserver(mDataSetObserver)}*/
    }

    public RemindListAdapter(RemindItemClickListener remindItemClickListener){
        itemClickListener = remindItemClickListener;

    }

    @Override
    public RemindViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.remind_item, parent, false);
        return new RemindViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RemindViewHolder holder, int position) {

        holder.title.setText(data.get(position).getTitle());
        holder.note.setText(data.get(position).getNote());
        holder.date.setText(data.get(position).getDate().toString());
    }

    //public Cursor getCursor(){return cursor;}
    /*@Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }*/

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
            note = (TextView) itemView.findViewById(R.id.note);
            date = (TextView) itemView.findViewById(R.id.date);
            ImageButton imageButton = itemView.findViewById(R.id.ib_popup_menu);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.popupMenuItemClicked(view, getLayoutPosition());
                }
            });

        }
        @Override
        public void onClick(View v)
        {
            itemClickListener.remindListRemoveClicked(v, this.getLayoutPosition());

        }
    }

    public void setData(List<RemindDTO> data) {
        this.data = data;
    }

    public String getTitle(int position){

        return data.get(position).getTitle();
    }
    public String getNote(int position){

        return data.get(position).getNote();
    }
    public String getDate(int position){

        return data.get(position).getDate();
    }

    /*public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }*/

    /**
     * Swap in a new Cursor, returning the old Cursor.  Unlike
     * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
     * closed.
     */
/*    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        final Cursor oldCursor = mCursor;
        if (oldCursor != null && mDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = newCursor;
        if (mCursor != null) {
            if (mDataSetObserver != null) {
                mCursor.registerDataSetObserver(mDataSetObserver);
            }
            mRowIdColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            notifyDataSetChanged();
        } else {
            mRowIdColumn = -1;
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
        return oldCursor;
    }

    public void setDataValid(boolean mDataValid) {
        this.mDataValid = mDataValid;
    }

    private class NotifyingDataSetObserver extends DataSetObserver {
        private RecyclerView.Adapter adapter;

        public NotifyingDataSetObserver(RecyclerView.Adapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void onChanged() {
            super.onChanged();
            ((CursorRecyclerViewAdapter) adapter).setDataValid(true);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            ((CursorRecyclerViewAdapter) adapter).setDataValid(false);
        }
    }*/
}
