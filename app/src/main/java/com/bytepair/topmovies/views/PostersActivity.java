package com.bytepair.topmovies.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.bytepair.topmovies.R;
import com.bytepair.topmovies.views.adapters.MoviesAdapter;
import com.bytepair.topmovies.models.Movie;
import com.bytepair.topmovies.presenters.MoviesPresenter;
import com.bytepair.topmovies.views.interfaces.MoviesView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostersActivity extends AppCompatActivity implements MoviesView, MoviesAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = PostersActivity.class.getSimpleName();
    private static final String MOVIES_PREFERENCES = "movies_preferences";
    private static final String SORT_BY = "sort_by";
    private static final String MOST_POPULAR = "most_popular";
    private static final String HIGHEST_RATED = "highest_rated";
    public static final String MOVIE_ID = "movie_id";

    private MoviesPresenter moviesPresenter;
    private MoviesAdapter moviesAdapter;

    @BindView(R.id.movies_recycler_view)
    RecyclerView moviesRecyclerView;

    @BindView(R.id.movies_progress_bar)
    ProgressBar moviesProgressBar;

    @BindView(R.id.movies_load_fail_constraint_layout)
    ConstraintLayout moviesFailureConstraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posters);

        // set default sort order if none set already
        setDefaultSettings();

        // bind views using ButterKnife
        ButterKnife.bind(this);

        // obtain a reference to the recycler view and set hasFixedSize to improve performance
        moviesRecyclerView.setHasFixedSize(true);

        // connect to a layout manager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        moviesRecyclerView.setLayoutManager(gridLayoutManager);

        // define a presenter and fetch movies
        moviesPresenter = new MoviesPresenter(this);
        moviesPresenter.fetchMovies(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_poster, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_most_popular:
                saveSortingSetting(MOST_POPULAR);
                break;
            case R.id.menu_top_rated:
                saveSortingSetting(HIGHEST_RATED);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        Log.i(TAG, "Sorting preference changed to " + getSharedPreferences(MOVIES_PREFERENCES, MODE_PRIVATE).getString(SORT_BY, null));
        moviesPresenter.fetchMovies(this);
        return true;
    }

    private void setDefaultSettings() {
        if (getSharedPreferences(MOVIES_PREFERENCES, MODE_PRIVATE).getString(SORT_BY, null) == null) {
            saveSortingSetting(MOST_POPULAR);
        }
    }

    private void saveSortingSetting(String sortingSetting) {
        SharedPreferences.Editor spEditor = getSharedPreferences(MOVIES_PREFERENCES, MODE_PRIVATE).edit();
        spEditor.putString(SORT_BY, sortingSetting);
        spEditor.apply();
    }

    @Override
    public void loadMoviesInProgress() {
        moviesRecyclerView.setVisibility(View.INVISIBLE);
        moviesFailureConstraintLayout.setVisibility(View.INVISIBLE);
        moviesProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadMoviesSuccess() {
        moviesAdapter = new MoviesAdapter(this, moviesPresenter.getMovies());
        moviesRecyclerView.setAdapter(moviesAdapter);

        moviesFailureConstraintLayout.setVisibility(View.INVISIBLE);
        moviesProgressBar.setVisibility(View.INVISIBLE);
        moviesRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadMoviesFailure() {
        moviesRecyclerView.setVisibility(View.INVISIBLE);
        moviesProgressBar.setVisibility(View.INVISIBLE);
        moviesFailureConstraintLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie movie) {
        Log.i(TAG, movie.getTitle());
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra(MOVIE_ID, String.valueOf(movie.getId()));
        startActivity(intent);
    }
}
