package com.example.moviesstage_2.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class FavRepository {

    private FavDao mFavDao;
    private LiveData<List<FavouriteMovies>> mAllMovies;
    FavRepository(Application application) {
        FavRoomDatabase db = FavRoomDatabase.getDatabase(application);
        mFavDao = db.movDao();
       mAllMovies = mFavDao.getAllFavData();
    }

    public LiveData<List<FavouriteMovies>>getAllFavData() {
        return mAllMovies;
    }
    public void insert (FavouriteMovies favouriteMovies) {
        new insertAsyncTask(mFavDao).execute(favouriteMovies);
    }
    public void delete(FavouriteMovies favouriteMovies)
    {
        new deleteAsyncTask(mFavDao).execute(favouriteMovies);
    }
    FavouriteMovies isItInDatabase(String id)
    {
        FavouriteMovies favouriteMovies =  mFavDao.isItInDatabase(id);
        return favouriteMovies;
    }
    private static class deleteAsyncTask extends AsyncTask<FavouriteMovies, Void, Void> {

        private FavDao mAsyncTaskDao;

        deleteAsyncTask(FavDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final FavouriteMovies... params) {
            mAsyncTaskDao.deleteData(params[0]);
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<FavouriteMovies, Void, Void> {

        private FavDao mAsyncTaskDao;

        insertAsyncTask(FavDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final FavouriteMovies... params) {
            mAsyncTaskDao.insertFav(params[0]);
            return null;
        }
    }

}
