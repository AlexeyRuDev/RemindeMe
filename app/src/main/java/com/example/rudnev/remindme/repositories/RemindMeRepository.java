package com.example.rudnev.remindme.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.rudnev.remindme.dao.RemindMeDAO;
import com.example.rudnev.remindme.dto.RemindDTO;
import com.example.rudnev.remindme.roomdatabase.RemindRoomDataBase;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        LocalDateTime localDate = LocalDateTime.now();
        String date = sdf.format(calendar.getTime());
        remindMeDAO = db.remindMeDAO();
        mAllReminds = remindMeDAO.getAllReminds();
        mRemindsForToday = remindMeDAO.getRemindsForTodayFragment(localDate);
        mRemindsForArchive = remindMeDAO.getRemindsForArchiveFragment(localDate);
        mRemindsForCalendar = remindMeDAO.getRemindsForCalendarFragment(localDate);
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

    public LiveData<List<RemindDTO>> getRemindsForConcreteDate(LocalDateTime dateTime){
        return remindMeDAO.getRemindsForTodayFragment(dateTime);
    }

    public void insert(RemindDTO remind) {
        new insertAsyncTask(remindMeDAO).execute(remind);
    }

    public void update(RemindDTO remind) {
        new updateAsyncTask(remindMeDAO).execute(remind);
    }

    public void delete(RemindDTO remind) {
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
