package com.example.rudnev.remindme;

import android.view.View;

/**
 * Created by rudnev on 10.10.2017.
 */

public interface RemindItemClickListener {
    public void remindListClicked(View v, int position);

    public void popupMenuItemClicked(View v, int position);
}
