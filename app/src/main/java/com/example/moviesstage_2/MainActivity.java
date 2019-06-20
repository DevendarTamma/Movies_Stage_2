package com.example.moviesstage_2;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.bumptech.glide.Glide;
import com.example.moviesstage_2.database.FavViewModel;
import com.example.moviesstage_2.database.FavouriteMovies;
import com.example.moviesstage_2.networkutils.NetworkUtils;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    /*private static final String TOP_RATED_TITTLE ="Top Rated Movies" ;
    private static final String TOP_RATED = "top_rated";
    private static final String POPULAR = "popular";
    private static final String ALERT_TITLE ="INTERNET IS NOT AVAILABLE" ;
    private static final String ALERT_MSG ="Network not available click Yes to Load favourite movies" ;
    private static final String MOVIES_OBJECT_KEY="movies_object";
    private static final String SAVED_INSTANCE_KEY="DATA";
    private static final String POPULAR_TITLE="Popular Movies";
    private static final String FAVOURITE_SORT="fav";
    private static final String FAV_TITLE ="Favourite Movies" ;*/
    private RecyclerView recyclerView;
    private ProgressBar PROGRESS;
    private MoviesAdapter moviesAdapter;
    private final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185";
    private static String sort_by = "popular";
    private MoviesModel[] mModel;
    FavViewModel favViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.rcv);
        PROGRESS=findViewById(R.id.main_progress);
        setTitle("Movies");
        favViewModel = ViewModelProviders.of(this).get(FavViewModel.class);
        if(savedInstanceState!=null)
        {
            PROGRESS.setVisibility(View.INVISIBLE);
            mModel=(MoviesModel[])savedInstanceState.getSerializable(getString(R.string.SAVED_INSTANCE_KEY));
            setRecycleAdapter(mModel);
        }
        else if(isNetworkAvailable())
        {
               new  FetchMovies().execute(sort_by);
        }
        else{
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.ALERT_TITLE))
                    .setMessage(getString(R.string.ALERT_MSG))
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                          loadFavourites();
                        }
                    })
                    .show();
        }
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,checkNoOfPosters(this)));

    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }
    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo info=connectivityManager.getActiveNetworkInfo();
        return info!=null&&info.isConnected();
    }
    public int checkNoOfPosters(Context context)
    {
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        float width=metrics.widthPixels/metrics.density;
        int scale=220;
        int p= (int) (width/scale);
        return (p>=2?p:2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.top_rated)
        {
            PROGRESS.setVisibility(View.VISIBLE);
            sort_by=getString(R.string.TOP_RATED);
            new  FetchMovies().execute(sort_by);
            setTitle(getString(R.string.TOP_RATED_TITTLE));
        }
        if(item.getItemId()==R.id.popular)
        {
            PROGRESS.setVisibility(View.VISIBLE);
            sort_by=getString(R.string.POPULAR);
            new  FetchMovies().execute(sort_by);
            setTitle(getString(R.string.POPULAR_TITLE));
        }
        if(item.getItemId()==R.id.favourites)
        {
            sort_by = getString(R.string.FAVOURITE_SORT);
            setTitle(getString(R.string.FAV_TITLE));
            loadFavourites();
        }
        return super.onOptionsItemSelected(item);
    }
    public void loadFavourites()
    {
        //Observer is used in main activity and when user clicks on
        //any movie we are passing Live data to MovieDetails activity
        favViewModel.getAllWords().observe(this, new Observer<List<FavouriteMovies>>() {
            @Override
            public void onChanged(@Nullable List<FavouriteMovies> fav)
            {

                if(fav!=null) {

                    convertListToModel(fav);
                }

            }
        });
    }
    public  void convertListToModel(List<FavouriteMovies> fav)
    {
        MoviesModel[] movies = new MoviesModel[fav.size()];
        for (int i = 0; i < fav.size(); i++) {
            movies[i] = new MoviesModel();
            movies[i].setOriginalTitle(fav.get(i).getTitle());
            movies[i].setMovieId(fav.get(i).getId());
            movies[i].setPosterPath(fav.get(i).getPosterpath());
            movies[i].setOverview(fav.get(i).getOverview());
            movies[i].setVoteAverage(fav.get(i).getRating());
            movies[i].setReleaseDate(fav.get(i).getRelease_date());
        }

        mModel = movies;
        PROGRESS.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        setRecycleAdapter(mModel);

    }
    private void setRecycleAdapter(MoviesModel[] movie) {
        moviesAdapter = new MoviesAdapter(this,movie);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,checkNoOfPosters(this)));
        recyclerView.setAdapter(moviesAdapter);
        moviesAdapter.notifyDataSetChanged();

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(getString(R.string.SAVED_INSTANCE_KEY),mModel);

    }
    public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesHolder>
    {
        MoviesModel[] movieModel;
        final Context context;
        public MoviesAdapter(Context context,MoviesModel[] movieModel) {
            this.movieModel = movieModel;
            this.context=context;
        }

        @NonNull
        @Override
        public MoviesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v= LayoutInflater.from(context).inflate(R.layout.movie_item,viewGroup,false);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,MovieDetails.class);
                    int position=(int)v.getTag();
                    intent.putExtra(getString(R.string.MOVIES_OBJECT_KEY),movieModel[position]);
                    startActivity(intent);

                }
            });
            return new MoviesHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MoviesHolder myViewHolder, int i) {
            Glide.with(context)
                    .load(POSTER_BASE_URL+movieModel[i].getPosterPath())
                    .into(myViewHolder.mView);
            myViewHolder.itemView.setTag(i);

        }
        @Override
        public int getItemCount() {
            if(movieModel!=null)

                return movieModel.length;
            return 0;
        }

        public class MoviesHolder extends  RecyclerView.ViewHolder{
            ImageView mView;
            public MoviesHolder(@NonNull View itemView) {
                super(itemView);
                mView=itemView.findViewById(R.id.imageView2);
            }
        }
    }
    public class FetchMovies extends AsyncTask<String,Void,MoviesModel[]>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(isNetworkAvailable())
            {}
            else{
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getString(R.string.ALERT_TITLE))
                        .setMessage(getString(R.string.ALERT_MSG))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                loadFavourites();
                            }
                        })
                        .show();
            }
        }

        @Override
        protected MoviesModel[] doInBackground(String... strings) {

            String moviesJsonString;
            try {
                URL url = NetworkUtils.buildUrl(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();
                if (inStream == null) {
                    return null;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }
                if (builder.length() == 0)
                    return null;
                moviesJsonString = builder.toString();
                return NetworkUtils.parseJson(moviesJsonString);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return  null;
        }
        @Override
        protected void onPostExecute(MoviesModel[] moviesModels) {
            super.onPostExecute(moviesModels);
            PROGRESS.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            mModel=moviesModels;
            setRecycleAdapter(moviesModels);
        }
    }


}
