package com.bytepair.topmovies.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bytepair.topmovies.R;
import com.bytepair.topmovies.views.adapters.MoviesAdapter;
import com.bytepair.topmovies.models.Movie;
import com.bytepair.topmovies.presenters.MoviesPresenter;
import com.bytepair.topmovies.views.interfaces.MoviesView;
import com.bytepair.topmovies.views.listeners.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostersActivity extends AppCompatActivity implements MoviesView, MoviesAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = PostersActivity.class.getSimpleName();
    private static final String MOVIES_PREFERENCES = "movies_preferences";
    public static final String SORT_BY = "sort_by";
    public static final String MOST_POPULAR = "most_popular";
    public static final String HIGHEST_RATED = "highest_rated";
    public static final String FAVORITES = "favorites";
    public static final String MOVIE_ID = "movie_id";

    private MoviesPresenter moviesPresenter;
    private MoviesAdapter moviesAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;

    @BindView(R.id.activity_poster_rv)
    RecyclerView moviesRecyclerView;

    @BindView(R.id.activity_poster_pb)
    ProgressBar moviesProgressBar;

    @BindView(R.id.activity_movie_failure_cl)
    ConstraintLayout moviesFailureConstraintLayout;

    @BindView(R.id.full_screen_error_desc_tv)
    TextView mErrorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posters);

        // bind views using ButterKnife
        ButterKnife.bind(this);

        // initialize the movies presenter (will initially have empty list)
        moviesPresenter = new MoviesPresenter(this);

        // initialize the movies adapter and connect to recycler view
        moviesAdapter = new MoviesAdapter(this, moviesPresenter.getMovies());
        moviesRecyclerView.setAdapter(moviesAdapter);

        // initialize the layout manager and connect to the recycler view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        moviesRecyclerView.setLayoutManager(gridLayoutManager);
      
        // set hasFixedSize to improve performance
        moviesRecyclerView.setHasFixedSize(true);
      
        // add scroll listener to recycler view for endless scrolling
        setupScrollListener(gridLayoutManager);
        moviesRecyclerView.addOnScrollListener(scrollListener);

        // set default sort order if none set already
        setDefaultSettings();

        // fetch the first set of movies
        moviesPresenter.initializeMovies(this);
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
            case R.id.menu_favorites:
                saveSortingSetting(FAVORITES);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        moviesAdapter.updateMovies(new ArrayList<Movie>());
        moviesPresenter.initializeMovies(this);
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
        moviesAdapter.updateMovies(moviesPresenter.getMovies());
        setToolBarTitle();

        moviesFailureConstraintLayout.setVisibility(View.INVISIBLE);
        moviesProgressBar.setVisibility(View.INVISIBLE);
        moviesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void setToolBarTitle() {
        if (getSupportActionBar() != null) {
            String setting = getSharedPreferences(MOVIES_PREFERENCES, MODE_PRIVATE).getString(SORT_BY,  null);
            if (setting != null) {
                switch (setting) {
                    case MOST_POPULAR:
                        getSupportActionBar().setTitle(R.string.most_popular);
                        break;
                    case HIGHEST_RATED:
                        getSupportActionBar().setTitle(R.string.top_rated);
                        break;
                    case FAVORITES:
                        getSupportActionBar().setTitle(R.string.favorites);
                        break;
                    default:
                        getSupportActionBar().setTitle(R.string.app_name);
                }
            }
        }
    }

    @Override
    public void loadMoviesFailure() {
        setToolBarTitle();
        moviesRecyclerView.setVisibility(View.INVISIBLE);
        moviesProgressBar.setVisibility(View.INVISIBLE);
        mErrorTextView.setText(R.string.movies_failed_to_load);
        moviesFailureConstraintLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra(MOVIE_ID, String.valueOf(movie.getId()));
        startActivity(intent);
    }

    private void setupScrollListener(GridLayoutManager layoutManager) {
        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                moviesPresenter.loadMoreMovies(getApplicationContext(), getSharedPreferences(MOVIES_PREFERENCES, MODE_PRIVATE).getString(SORT_BY, null), page);
            }
        };
    }
}
