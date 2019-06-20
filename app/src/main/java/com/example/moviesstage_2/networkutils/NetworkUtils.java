package com.example.moviesstage_2.networkutils;

import android.net.Uri;

import com.example.moviesstage_2.BuildConfig;
import com.example.moviesstage_2.MoviesModel;
import com.example.moviesstage_2.VideoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {
    public static URL buildUrl(String sort_order) throws MalformedURLException {
        final String BASE_URL = "https://api.themoviedb.org/3/movie/"+sort_order+"?";
        final String API_KEY_PARAM = "api_key";
        final String API_KEY = BuildConfig.THE_SECREAT_API_KEY;
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
        return new URL(builtUri.toString());
    }
    public static MoviesModel[] parseJson(String j)
    {
        final String RESULTS = "results";
        final String ORIGINAL_TITLE = "original_title";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String VOTE_AVERAGE = "vote_average";
        final String RELEASE_DATE = "release_date";
        final String MOVIE_ID="id";
        JSONObject moviesJson;
        try {
            moviesJson = new JSONObject(j);
            JSONArray resultsArray = moviesJson.getJSONArray(RESULTS);
            MoviesModel[] movies = new MoviesModel[resultsArray.length()];
            for (int i = 0; i < resultsArray.length(); i++) {
                movies[i] = new MoviesModel();
                JSONObject movieInfo = resultsArray.getJSONObject(i);
                movies[i].setOriginalTitle(movieInfo.getString(ORIGINAL_TITLE));
                movies[i].setPosterPath(movieInfo.getString(POSTER_PATH));
                movies[i].setOverview(movieInfo.getString(OVERVIEW));
                Double test=movieInfo.getDouble(VOTE_AVERAGE);
                int id=movieInfo.getInt(MOVIE_ID);
                movies[i].setMovieId(id);
                String vt=test.toString();
                movies[i].setVoteAverage(vt);
                movies[i].setReleaseDate(movieInfo.getString(RELEASE_DATE));
            }
            return movies;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }
    public static VideoModel[] parseVideoJson(String j)
    {
        final String RESULTS = "results";
        final String KEY="key";
        final String NAME="name";
        JSONObject root;
        try {
            root = new JSONObject(j);
            JSONArray results = root.getJSONArray(RESULTS);
            VideoModel[] videos = new VideoModel[results.length()];
            for(int i=0;i<results.length();i++)
            {
                videos[i]=new VideoModel();
                JSONObject result_item = results.getJSONObject(i);
                videos[i].setKey( result_item.getString(KEY));
                videos[i].setName(result_item.getString(NAME));
            }
            return  videos;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
