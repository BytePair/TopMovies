package com.bytepair.topmovies.presenters;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bytepair.topmovies.BuildConfig;
import com.bytepair.topmovies.models.Movie;
import com.bytepair.topmovies.models.MovieResults;
import com.bytepair.topmovies.utilities.services.MovieService;
import com.bytepair.topmovies.views.interfaces.MoviesView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesPresenter {

    private static final String MOVIES_PREFERENCES = "movies_preferences";
    private static final String SORT_BY = "sort_by";
    private static final String MOST_POPULAR = "most_popular";
    private static final String HIGHEST_RATED = "highest_rated";

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
        mMovies = new ArrayList<>();
        mMoviesView.loadMoviesInProgress();

        String sortSetting = context.getSharedPreferences(MOVIES_PREFERENCES, Context.MODE_PRIVATE).getString(SORT_BY, null);
        if (sortSetting == null) {
            return;
        }

        loadMoreMovies(sortSetting, 1);
    }

    // TODO: Request more movies on scroll
    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadMoreMovies(String sortSetting, int page) {
        // Send an API request to retrieve appropriate paginated data
        switch (sortSetting) {
            case MOST_POPULAR:
                enqueueCallback(MovieService.getAPI().getPopularMovies(BuildConfig.MOVIE_API_KEY, page));
                break;
            case HIGHEST_RATED:
                enqueueCallback(MovieService.getAPI().getTopRatedMovies(BuildConfig.MOVIE_API_KEY, page));
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

}
