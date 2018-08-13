package com.example.rudnev.remindme.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.rudnev.remindme.dao.NoteDAO;
import com.example.rudnev.remindme.dto.Notes;
import com.example.rudnev.remindme.roomdatabase.RemindRoomDataBase;

import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NoteRepository {
    private NoteDAO notesDAO;
    private LiveData<List<Notes>> mAllNotes;

    public NoteRepository(Application application) {
        RemindRoomDataBase db = RemindRoomDataBase.getDatabase(application);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        LocalDate localDate = LocalDate.now();
        String date = sdf.format(calendar.getTime());
        notesDAO = db.notesDAO();
        mAllNotes = notesDAO.getAllNotes();
    }

    public LiveData<List<Notes>> getAllNotes() {
        return mAllNotes;
    }

    public void insert (Notes note) {
        new NoteRepository.insertAsyncTask(notesDAO).execute(note);
    }

    public void update (Notes note) {
        new NoteRepository.updateAsyncTask(notesDAO).execute(note);
    }

    public void delete (Notes note) {
        new NoteRepository.deleteAsyncTask(notesDAO).execute(note);
    }

    private static class insertAsyncTask extends AsyncTask<Notes, Void, Void> {

        private NoteDAO mAsyncTaskDao;

        insertAsyncTask(NoteDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Notes... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Notes, Void, Void> {

        private NoteDAO mAsyncTaskDao;

        updateAsyncTask(NoteDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Notes... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Notes, Void, Void> {

        private NoteDAO mAsyncTaskDao;

        deleteAsyncTask(NoteDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Notes... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
