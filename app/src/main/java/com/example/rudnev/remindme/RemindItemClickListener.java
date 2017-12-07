package com.example.rudnev.remindme;

import android.view.View;

/**
 * Created by rudnev on 10.10.2017.
 */

public interface RemindItemClickListener {
    public void remindListRemoveClicked(View v, int position);

    public void remindListUpdateClicked(View v, int position);

    public void popupMenuItemClicked(View v, int position);
}
