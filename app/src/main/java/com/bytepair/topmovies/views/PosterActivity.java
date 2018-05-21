package com.bytepair.topmovies.views;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bytepair.topmovies.R;
import com.bytepair.topmovies.models.Movie;
import com.bytepair.topmovies.presenters.MoviesPresenter;
import com.bytepair.topmovies.views.interfaces.MoviesView;

import java.util.List;

public class PosterActivity extends AppCompatActivity implements MoviesView {

    private static final String TAG = PosterActivity.class.getSimpleName();
    private static final String MOVIES_PREFERENCES = "movies_preferences";
    private static final String SORT_BY = "sort_by";
    private static final String MOST_POPULAR = "most_popular";
    private static final String HIGHEST_RATED = "highest_rated";

    private MoviesPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster);

        setDefaultSettings();

        presenter = new MoviesPresenter(this);
        presenter.getMovies(this);
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
        }

        Log.i(TAG, "Sorting preference changed to " + getSharedPreferences(MOVIES_PREFERENCES, MODE_PRIVATE).getString(SORT_BY, null));

        presenter.getMovies(this);
        return super.onOptionsItemSelected(item);
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
    public void displayMovies(List<Movie> movies) {
        // TODO: Show the returned movies
        if (movies == null) {
            return;
        }
        for (Movie movie : movies) {
            Log.i(TAG, movie.getTitle() + " " + movie.getPopularity());
        }
    }

}
