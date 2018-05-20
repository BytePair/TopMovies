package com.bytepair.topmovies;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class PosterActivity extends AppCompatActivity {

    private static final String TAG = PosterActivity.class.getSimpleName();
    private static final String MOVIES_PREFERENCES = "movies_preferences";
    private static final String SORT_BY = "sort_by";
    private static final String MOST_POPULAR = "most_popular";
    private static final String HIGHEST_RATED = "highest_rated";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster);
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
                saveSortingSetting(MOST_POPULAR);
        }

        Log.i(TAG, "Sorting preference changed to " + getSharedPreferences(MOVIES_PREFERENCES, MODE_PRIVATE).getString(SORT_BY, ""));

        return super.onOptionsItemSelected(item);
    }

    private void saveSortingSetting(String sortingSetting) {
        SharedPreferences.Editor spEditor = getSharedPreferences(MOVIES_PREFERENCES, MODE_PRIVATE).edit();
        spEditor.putString(SORT_BY, sortingSetting);
        spEditor.apply();
    }
}
