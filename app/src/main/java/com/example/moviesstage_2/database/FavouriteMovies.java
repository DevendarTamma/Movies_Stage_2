package com.example.moviesstage_2.database;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favorite_movies")
public class FavouriteMovies {


    @PrimaryKey
    int id;

    String title;
    String overview;
    String posterPath;
    String release_date;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterpath() {
        return posterPath;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getRating() {
        return rating;
    }

    String rating;

    public FavouriteMovies(int id, String title, String overview, String posterPath, String release_date, String rating) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.release_date = release_date;
        this.rating = rating;
    }
}
