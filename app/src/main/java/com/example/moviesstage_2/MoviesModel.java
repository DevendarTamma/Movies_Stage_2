package com.example.moviesstage_2;

import java.io.Serializable;

public class MoviesModel implements Serializable {
    private String mOriginalTitle;
    private String mPosterPath;
    private String mOverview;
    private String mVoteAverage;
    private String mReleaseDate;
    private int movieId;
    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
    public MoviesModel() {
    }
    public void setOriginalTitle(String originalTitle) {
        mOriginalTitle = originalTitle;
    }
    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }
    public void setOverview(String overview) {
        if(!overview.equals("null")) {
            mOverview = overview;
        }
    }
    public void setVoteAverage(String voteAverage) {
        mVoteAverage = voteAverage;
    }
    public void setReleaseDate(String releaseDate) {
        if(!releaseDate.equals("null")) {
            mReleaseDate = releaseDate;
        }
    }
    public String getOriginalTitle() {
        return mOriginalTitle;
    }
    public String getPosterPath() {
        return  mPosterPath;
    }
    public String getOverview() {
        return mOverview;
    }
    public String getVoteAverage() {
        return mVoteAverage;
    }
    public String getReleaseDate() {
        return mReleaseDate;
    }
}

