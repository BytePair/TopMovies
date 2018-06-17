package com.bytepair.topmovies.presenters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.bytepair.topmovies.BuildConfig;
import com.bytepair.topmovies.utilities.contentproviders.MovieContract;
import com.bytepair.topmovies.models.Movie;
import com.bytepair.topmovies.models.MovieResults;
import com.bytepair.topmovies.utilities.contentproviders.FavoriteMovieQueryHandler;
import com.bytepair.topmovies.utilities.services.MovieService;
import com.bytepair.topmovies.views.interfaces.MoviesView;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bytepair.topmovies.views.PostersActivity.FAVORITES;
import static com.bytepair.topmovies.views.PostersActivity.HIGHEST_RATED;
import static com.bytepair.topmovies.views.PostersActivity.MOST_POPULAR;
import static com.bytepair.topmovies.views.PostersActivity.SORT_BY;

public class MoviesPresenter implements FavoriteMovieQueryHandler.FavoriteMovieQueryListener {

    private static final String MOVIES_PREFERENCES = "movies_preferences";

    private MoviesView mMoviesView;
    private List<Movie> mMovies;

    public MoviesPresenter(MoviesView moviesView) {
        mMoviesView = moviesView;
        mMovies = new ArrayList<>();
    }

    public List<Movie> getMovies() {
        return mMovies;
    }

    public void initializeMovies(Context context) {
        mMoviesView.loadMoviesInProgress();
        mMovies = new ArrayList<>();

        String sortSetting = context.getSharedPreferences(MOVIES_PREFERENCES, Context.MODE_PRIVATE).getString(SORT_BY, null);
        if (sortSetting == null) {
            return;
        }

        loadMoreMovies(context, sortSetting, 1);
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadMoreMovies(Context context, String sortSetting, int page) {
        // Send an API request to retrieve appropriate paginated data
        switch (sortSetting) {
            case MOST_POPULAR:
                enqueueCallback(MovieService.getAPI().getPopularMovies(BuildConfig.MOVIE_API_KEY, page));
                break;
            case HIGHEST_RATED:
                enqueueCallback(MovieService.getAPI().getTopRatedMovies(BuildConfig.MOVIE_API_KEY, page));
                break;
            case FAVORITES:
                fetchLocalFavorites(context);
                break;
            default:
                break;
        }
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
    }

    private void enqueueCallback(Call<MovieResults> movieResults) {
        movieResults.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(@NonNull Call<MovieResults> call, @NonNull Response<MovieResults> response) {
                if (response.isSuccessful()) {
                    mMovies.addAll(Objects.requireNonNull(response.body()).getResults());
                    mMoviesView.loadMoviesSuccess();
                } else {
                    onFailure(call, new Throwable("Fail response code: " +  response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResults> call, @NonNull Throwable t) {
                t.printStackTrace();
                mMoviesView.loadMoviesFailure();
            }
        });
    }

    private void fetchLocalFavorites(Context context) {

        FavoriteMovieQueryHandler favoriteMovieQueryHandler = new FavoriteMovieQueryHandler(context.getContentResolver(), this);

        String[] projection = new String[] {
                MovieContract.MovieEntry._ID, MovieContract.MovieEntry.POSTER_PATH
        };

        favoriteMovieQueryHandler.startQuery(
                0,
                null,
                MovieContract.MovieEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onQueryComplete(Cursor cursor) {
        mMovies = new ArrayList<>();

        while (cursor != null && cursor.moveToNext()) {
            Movie movie = new Movie();
            movie.setId(cursor.getInt(0));
            movie.setPosterPath(cursor.getString(1));
            mMovies.add(movie);
        }

        if (CollectionUtils.isNotEmpty(mMovies)) {
            mMoviesView.loadMoviesSuccess();
        } else {
            mMoviesView.loadMoviesFailure();
        }
    }

    @Override
    public void onInsertComplete(Uri uri) {
        // do nothing for now, no inserts
    }

    @Override
    public void onDeleteComplete(int result) {
        // no nothing for now, no deletes
    }
}
