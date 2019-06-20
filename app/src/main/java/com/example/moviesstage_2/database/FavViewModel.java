package com.example.moviesstage_2.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class FavViewModel extends AndroidViewModel {
    private FavRepository mRepository;

    private LiveData<List<FavouriteMovies>> mAllWords;
    FavouriteMovies favouriteMovies;
    public FavViewModel (Application application) {
        super(application);
        mRepository = new FavRepository(application);
        mAllWords = mRepository.getAllFavData();
    }
    public LiveData<List<FavouriteMovies>> getAllWords()
    {
        return mAllWords;
    }
    public FavouriteMovies isItInDatabase(String id)
    {
        favouriteMovies = mRepository.isItInDatabase(id);
        return favouriteMovies;
    }
    public void insert(FavouriteMovies favouriteMovies) {
        mRepository.insert(favouriteMovies);
    }
    public  void delete(FavouriteMovies favouriteMovies)
    {
        mRepository.delete(favouriteMovies);
    }

}
