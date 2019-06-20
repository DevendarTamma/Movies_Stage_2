package com.example.moviesstage_2.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

@Database(entities ={FavouriteMovies.class},version = 2,exportSchema = false)
public abstract class FavRoomDatabase extends RoomDatabase {


    public abstract FavDao movDao();

    private static volatile FavRoomDatabase INSTANCE;

    static FavRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FavRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FavRoomDatabase.class, "word_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){

        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, LiveData<List<FavouriteMovies>>> {

        private final FavDao mDao;

        PopulateDbAsync(FavRoomDatabase db) {
            mDao = db.movDao();
        }


        @Override
        protected LiveData<List<FavouriteMovies>> doInBackground(Void... voids) {
            return mDao.getAllFavData();
        }
    }
}
