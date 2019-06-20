package com.example.moviesstage_2.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface FavDao {

    @Insert(onConflict = REPLACE)
    void insertFav(FavouriteMovies favouriteMovies);

    @Delete
    void deleteData(FavouriteMovies favouriteMovies);

    @Query("Select * From favorite_movies")
    LiveData<List<FavouriteMovies>> getAllFavData();

@Query("SELECT *FROM favorite_movies WHERE id = :title LIMIT 1")
    FavouriteMovies isItInDatabase(String title);
}
