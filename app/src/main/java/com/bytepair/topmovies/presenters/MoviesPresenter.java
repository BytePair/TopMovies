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
        switch (sortSetting) {
            case MOST_POPULAR:
                enqueueCallback(MovieService.getAPI().getPopularMovies(BuildConfig.MOVIE_API_KEY));
                break;
            case HIGHEST_RATED:
                enqueueCallback(MovieService.getAPI().getTopRatedMovies(BuildConfig.MOVIE_API_KEY));
                break;
            default:
                break;
        }
    }

    private void enqueueCallback(Call<MovieResults> movieResults) {
        movieResults.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(@NonNull Call<MovieResults> call, @NonNull Response<MovieResults> response) {
                if (response.isSuccessful()) {
                    mMovies = Objects.requireNonNull(response.body()).getResults();
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

    // TODO: Request more movies on scroll
}
