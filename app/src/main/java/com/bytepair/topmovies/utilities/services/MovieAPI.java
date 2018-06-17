/**
 * Helpful guide for creating and using singleton for Retrofit
 * https://code.tutsplus.com/tutorials/getting-started-with-retrofit-2--cms-27792
 */

package com.bytepair.topmovies.utilities.services;

import com.bytepair.topmovies.models.DetailedMovie;
import com.bytepair.topmovies.models.MovieResults;
import com.bytepair.topmovies.models.ReviewResults;
import com.bytepair.topmovies.models.VideoResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieAPI {

    @GET("/3/movie/popular")
    Call<MovieResults> getPopularMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("/3/movie/top_rated")
    Call<MovieResults> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("/3/movie/{movie_id}")
    Call<DetailedMovie> getMovieDetails(@Path("movie_id") String movieID, @Query("api_key") String apiKey);

    @GET("/3/movie/{movie_id}/reviews")
    Call<ReviewResults> getMovieReviews(@Path("movie_id") String movieID, @Query("api_key") String apiKey, @Query("page") int page);

    @GET("/3/movie/{movie_id}/videos")
    Call<VideoResults> getMovieVideos(@Path("movie_id") String movieID, @Query("api_key") String apiKey);

}
