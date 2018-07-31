package com.example.rudnev.remindme.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.example.rudnev.remindme.dao.RemindMeDAO;
import com.example.rudnev.remindme.dto.RemindDTO;
import com.example.rudnev.remindme.roomdatabase.RemindRoomDataBase;

import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RemindMeRepository {
    private RemindMeDAO remindMeDAO;
    private LiveData<List<RemindDTO>> mAllReminds;
    private LiveData<List<RemindDTO>> mRemindsForToday;
    private LiveData<List<RemindDTO>> mRemindsForArchive;
    private LiveData<List<RemindDTO>> mRemindsForCalendar;

    public RemindMeRepository(Application application) {
        RemindRoomDataBase db = RemindRoomDataBase.getDatabase(application);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        LocalDate localDate = LocalDate.now();
        String date = sdf.format(calendar.getTime());
        remindMeDAO = db.remindMeDAO();
        mAllReminds = remindMeDAO.getAllReminds();
        mRemindsForToday = mAllReminds;//remindMeDAO.getRemindsForTodayFragment(localDate.toDate());
        mRemindsForArchive = mAllReminds;//remindMeDAO.getRemindsForArchiveFragment(calendar.getTime());
        mRemindsForCalendar = mAllReminds;//remindMeDAO.getRemindsForCalendarFragment(calendar.getTime());
    }

    public LiveData<List<RemindDTO>> getAllReminds() {
        return mAllReminds;
    }
    public LiveData<List<RemindDTO>> getRemindsForToday() {
        return mRemindsForToday;
    }
    public LiveData<List<RemindDTO>> getRemindsForArchive() {
        return mRemindsForArchive;
    }
    public LiveData<List<RemindDTO>> getRemindsForCalendar() {
        return mRemindsForCalendar;
    }

    public void insert (RemindDTO remind) {
        new insertAsyncTask(remindMeDAO).execute(remind);
    }

    public void update (RemindDTO remind) {
        new updateAsyncTask(remindMeDAO).execute(remind);
    }

    public void delete (RemindDTO remind) {
        new deleteAsyncTask(remindMeDAO).execute(remind);
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

    private static class updateAsyncTask extends AsyncTask<RemindDTO, Void, Void> {

        private RemindMeDAO mAsyncTaskDao;

        updateAsyncTask(RemindMeDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final RemindDTO... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<RemindDTO, Void, Void> {

        private RemindMeDAO mAsyncTaskDao;

        deleteAsyncTask(RemindMeDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final RemindDTO... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

}
