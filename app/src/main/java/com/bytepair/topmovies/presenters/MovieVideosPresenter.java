package com.bytepair.topmovies.presenters;

import android.support.annotation.NonNull;

import com.bytepair.topmovies.BuildConfig;
import com.bytepair.topmovies.models.pojos.Video;
import com.bytepair.topmovies.models.pojos.VideoResults;
import com.bytepair.topmovies.utilities.services.MovieService;
import com.bytepair.topmovies.views.interfaces.MovieVideosView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieVideosPresenter {

    private MovieVideosView mMovieVideosView;
    private List<Video> mVideos;

    public MovieVideosPresenter(MovieVideosView mMovieVideosView) {
        this.mMovieVideosView = mMovieVideosView;
        mVideos = new ArrayList<>();
    }

    public void initializeVideos(String movieID) {
        mMovieVideosView.loadMovieVideosInProgress();
        mVideos = new ArrayList<>();
        loadMoreVideos(movieID);
    }

    private void loadMoreVideos(String movieID) {
        MovieService.getAPI().getMovieVideos(movieID, BuildConfig.MOVIE_API_KEY).enqueue(new Callback<VideoResults>() {
            @Override
            public void onResponse(@NonNull Call<VideoResults> call, @NonNull Response<VideoResults> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    mVideos = response.body().getResults();
                    if (mVideos.size() < 1) {
                        mMovieVideosView.loadMovieVideosFailure();
                    } else {
                        mMovieVideosView.loadMovieVideosSuccess();
                    }
                } else {
                    onFailure(call, new Throwable("Movie videos response not successful: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<VideoResults> call, @NonNull Throwable t) {
                t.printStackTrace();
                mMovieVideosView.loadMovieVideosSuccess();
            }
        });
    }

    public List<Video> getVideos() {
        return mVideos;
    }

}
