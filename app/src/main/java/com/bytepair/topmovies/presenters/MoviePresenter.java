package com.bytepair.topmovies.presenters;

import android.support.annotation.NonNull;

import com.bytepair.topmovies.BuildConfig;
import com.bytepair.topmovies.models.DetailedMovie;
import com.bytepair.topmovies.services.MovieService;
import com.bytepair.topmovies.views.interfaces.MovieView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviePresenter {

    private DetailedMovie mDetailedMovie;
    private MovieView mMovieView;

    public MoviePresenter(MovieView movieView) {
        mMovieView = movieView;
    }

    public void fetchMovie(String movieID) {
        mMovieView.loadMovieInProgress();

        MovieService.getAPI().getMovieDetails(movieID, BuildConfig.MOVIE_API_KEY).enqueue(new Callback<DetailedMovie>() {
            @Override
            public void onResponse(@NonNull Call<DetailedMovie> call, @NonNull Response<DetailedMovie> response) {
                if (response.isSuccessful()) {
                    mDetailedMovie = response.body();
                    mMovieView.loadMovieSuccess();
                } else {
                    onFailure(call, new Throwable("Response Error: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DetailedMovie> call, @NonNull Throwable t) {
                t.printStackTrace();
                mMovieView.loadMovieFailure();
            }
        });
    }

    public DetailedMovie getDetailedMovie() {
        return mDetailedMovie;
    }
}
