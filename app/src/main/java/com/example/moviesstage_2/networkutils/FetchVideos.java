package com.example.moviesstage_2.networkutils;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import com.example.moviesstage_2.VideoModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchVideos extends AsyncTaskLoader {

    private String moviesId;
    private static final String BASE_VIDEOS= "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY = "a2ae95b3c083c023197df998e1ef4475";
    public FetchVideos(@NonNull Context context, String movieId) {
        super(context);
        this.moviesId=movieId;
    }

    VideoModel[] v;
    @Override
    public void deliverResult(@Nullable Object data) {
        v = (VideoModel[])data;
        super.deliverResult(data);
    }

    @Nullable
    @Override
    public VideoModel[] loadInBackground() {
        Uri uri = Uri.parse(BASE_VIDEOS+moviesId+"/videos?api_key="+API_KEY);
        try {
            URL url = new URL(uri.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine())!=null)
            {
                stringBuffer.append(line);
            }
            return  NetworkUtils.parseVideoJson(stringBuffer.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (v!=null) {
            deliverResult(v);
        }else{
            forceLoad();
        }
    }
}
