package com.example.rudnev.remindme.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.rudnev.remindme.dao.RemindMeDAO;
import com.example.rudnev.remindme.dto.RemindDTO;
import com.example.rudnev.remindme.roomdatabase.RemindRoomDataBase;

import java.util.List;

public class RemindMeRepository {
    private RemindMeDAO remindMeDAO;
    private LiveData<List<RemindDTO>> mAllReminds;

    RemindMeRepository(Application application) {
        RemindRoomDataBase db = RemindRoomDataBase.getDatabase(application);
        remindMeDAO = db.remindMeDAO();
        mAllReminds = remindMeDAO.getAllReminds();
    }

    LiveData<List<RemindDTO>> getAllReminds() {
        return mAllReminds;
    }

    public void insert (RemindDTO remind) {
        new insertAsyncTask(remindMeDAO).execute(remind);
    }

    private static class insertAsyncTask extends AsyncTask<RemindDTO, Void, Void> {

        private RemindMeDAO mAsyncTaskDao;

        insertAsyncTask(RemindMeDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final RemindDTO... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
