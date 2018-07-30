package com.example.rudnev.remindme.roomdatabase;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.rudnev.remindme.dao.RemindMeDAO;
import com.example.rudnev.remindme.dto.RemindDTO;

@Database(entities = {RemindDTO.class}, version = 3)
@TypeConverters({DateTypeConverter.class})
public abstract class RemindRoomDataBase extends RoomDatabase {

    private static RemindRoomDataBase INSTANCE;

    public abstract RemindMeDAO remindMeDAO();

    public static RemindRoomDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RemindRoomDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RemindRoomDataBase.class, "myRemindDB").addCallback(sRoomDatabaseCallback).fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }



    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final RemindMeDAO mDao;

        PopulateDbAsync(RemindRoomDataBase db) {
            mDao = db.remindMeDAO();
        }

        @Override
        protected Void doInBackground(final Void... params) {
           /* mDao.deleteAll();
            RemindDTO remind = new RemindDTO("Hello");
            mDao.insert(remind);
            remind = new RemindDTO("World");
            mDao.insert(remind);*/
            return null;
        }
    }

}

