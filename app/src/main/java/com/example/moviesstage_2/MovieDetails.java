package com.example.moviesstage_2;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviesstage_2.Adapters.ReviewAdapter;
import com.example.moviesstage_2.Adapters.VideoAdapter;
import com.example.moviesstage_2.database.FavViewModel;
import com.example.moviesstage_2.database.FavouriteMovies;
import com.example.moviesstage_2.networkutils.FetchVideos;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetails extends AppCompatActivity implements LoaderManager.LoaderCallbacks<VideoModel[]> {
    private static final String UNFAV_TEXT ="Unfavourite movies" ;
    private static final String ADD_TO_FAV ="Add to Favourites" ;
    private String mOriginal_title;
    private String mOverviews;
    private String mRelease;
    private String mVotes;
    private MoviesModel mModel;
    private TextView release_date,review_label;
    private TextView vote;
    private TextView overview,originalTitle;
    Button fav_button;
    int mid;
    boolean flag;
    private FavViewModel favViewModel;
    private final static int LOADER = 2;
    RecyclerView recycleTrailers, recycleReviews;
    VideoAdapter videoAdapter;
    ReviewAdapter reviewAdapter;
    VideoModel[] vm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_movie_details);

        favViewModel = ViewModelProviders.of(this).get(FavViewModel.class);

        mModel = (MoviesModel) intent.getSerializableExtra("movies_object");
        String path = "https://image.tmdb.org/t/p/w185";
        String mUrl = path + mModel.getPosterPath();
        mid = mModel.getMovieId();
        if (mModel.getVoteAverage() != null)
            mVotes = mModel.getVoteAverage();
        mOverviews = mModel.getOverview();
        mRelease = mModel.getReleaseDate();
        mOriginal_title = mModel.getOriginalTitle();
        if (getSupportLoaderManager().getLoader(LOADER) != null) {
            getSupportLoaderManager().initLoader(LOADER, null, this);
        } else {
            getSupportLoaderManager().restartLoader(LOADER, null, this);
        }
        recycleTrailers = findViewById(R.id.recycle_videos);
        videoAdapter = new VideoAdapter(this, vm);
        recycleTrailers.setLayoutManager(new LinearLayoutManager(this));
        recycleTrailers.setAdapter(videoAdapter);
        recycleReviews = findViewById(R.id.recycle_review);
        fav_button = findViewById(R.id.fav_button);
        loadReviews(String.valueOf(mid));
        updateFavButton();

        ImageView poster = findViewById(R.id.imageView);
        release_date = findViewById(R.id.release_date);
        vote = findViewById(R.id.vote);
        overview = findViewById(R.id.overview);
        originalTitle=findViewById(R.id.original_tittle);
        review_label=findViewById(R.id.review_label);


        Picasso
                .with(this)
                .load(mUrl)
                .placeholder(R.mipmap.image_placeholder)
                .into(poster);
        release_date.setText(mRelease);
        vote.setText(mVotes);
        overview.setText(mOverviews);
        originalTitle.setText(mOriginal_title);
    }
    public void updateFavButton() {
        FavouriteMovies r = favViewModel.isItInDatabase(String.valueOf(mid));
        if (r != null) {
            fav_button.setText(UNFAV_TEXT);
            flag = true;
        } else {
            fav_button.setText(ADD_TO_FAV);
            flag = false;
        }
    }
    public void addToFav(View view) {
        if (flag) {
            FavouriteMovies favouriteMovies = new FavouriteMovies(mid, mOriginal_title, mOverviews, mModel.getPosterPath(), mRelease, mVotes);
            favViewModel.delete(favouriteMovies);
            Toast.makeText(this, "Removed from favourites successfully", Toast.LENGTH_SHORT).show();
            fav_button.setText(ADD_TO_FAV);
            flag = !flag;
        } else {
            FavouriteMovies favouriteMovies = new FavouriteMovies(mid, mOriginal_title, mOverviews, mModel.getPosterPath(), mRelease, mVotes);
            Toast.makeText(this, "Added to Favourites successfully", Toast.LENGTH_SHORT).show();
            favViewModel.insert(favouriteMovies);
            fav_button.setText(UNFAV_TEXT);
            flag = !flag;
        }


    }
    @NonNull
    @Override
    public Loader<VideoModel[]> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new FetchVideos(this, String.valueOf(mid));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<VideoModel[]> loader, VideoModel[] s) {
        this.vm = s;
        VideoAdapter videoAdapter = new VideoAdapter(this, this.vm);
        recycleTrailers.setLayoutManager(new LinearLayoutManager(this));
        recycleTrailers.setAdapter(videoAdapter);
        videoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<VideoModel[]> loader) {

    }

    public void loadReviews(String id) {
        final String RESULTS="results";
        final String AUTHOR="author";
        final String CONTENT="content";
        final String BASE_URL="https://api.themoviedb.org/3/movie/";
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =  BASE_URL+ id + "/reviews?api_key=" + BuildConfig.THE_SECREAT_API_KEY;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject root;
                        ReviewModel[] reviews = null;
                        try {
                            root = new JSONObject(response);
                            JSONArray results = root.getJSONArray(RESULTS);
                            reviews = new ReviewModel[results.length()];
                            for (int i = 0; i < results.length(); i++) {
                                reviews[i] = new ReviewModel();
                                JSONObject result_item = results.getJSONObject(i);
                                reviews[i].setAuthor(result_item.getString(AUTHOR));
                                reviews[i].setComment(result_item.getString(CONTENT));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (reviews != null) {
                            reviewAdapter = new ReviewAdapter(reviews, MovieDetails.this);
                            recycleReviews.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recycleReviews.setAdapter(reviewAdapter);
                        }else{
                             review_label.setVisibility(View.GONE);
                            Toast.makeText(MovieDetails.this, "NO Reviews for this movie", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

}
