package com.bytepair.topmovies.presenters;

import android.support.annotation.NonNull;

import com.bytepair.topmovies.BuildConfig;
import com.bytepair.topmovies.models.Review;
import com.bytepair.topmovies.models.ReviewResults;
import com.bytepair.topmovies.utilities.services.MovieService;
import com.bytepair.topmovies.views.interfaces.MovieReviewsView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieReviewsPresenter {

    private MovieReviewsView movieReviewsView;
    private List<Review> reviews;

    public MovieReviewsPresenter(MovieReviewsView movieReviewsView) {
        this.movieReviewsView = movieReviewsView;
        this.reviews = new ArrayList<>();
    }

    public void initializeReviews(String movieID) {
        movieReviewsView.loadMovieReviewsInProgress();
        this.reviews = new ArrayList<>();
        loadMoreReviews(movieID, 1);
    }

    public void loadMoreReviews(String movieID, int page) {
        MovieService.getAPI().getMovieReviews(movieID, BuildConfig.MOVIE_API_KEY, page).enqueue(new Callback<ReviewResults>() {
            @Override
            public void onResponse(@NonNull Call<ReviewResults> call, @NonNull Response<ReviewResults> response) {
                if (response.isSuccessful()) {
                    reviews.addAll(Objects.requireNonNull(response.body()).getResults());
                    if (reviews.size() < 1) {
                        movieReviewsView.loadMovieReviewsFailure();
                    } else {
                        movieReviewsView.loadMovieReviewsSuccess();
                    }
                } else {
                    onFailure(call, new Throwable("Fail response code: " +  response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewResults> call, @NonNull Throwable t) {
                t.printStackTrace();
                movieReviewsView.loadMovieReviewsFailure();
            }
        });
    }

    public List<Review> getReviews() {
        return reviews;
    }

}
