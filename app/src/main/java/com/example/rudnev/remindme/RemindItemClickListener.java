package com.example.rudnev.remindme;

import android.view.View;


public interface RemindItemClickListener {
    public void remindListRemoveClicked(View v, int position);

    public void remindListUpdateClicked(View v, int position);

    public void popupMenuItemClicked(View v, int position);
}
