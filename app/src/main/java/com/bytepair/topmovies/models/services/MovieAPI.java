package com.bytepair.topmovies.models.services;

import com.bytepair.topmovies.models.DetailedMovie;
import com.bytepair.topmovies.models.MovieResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieAPI {

    @GET("/3/movie/popular")
    Call<MovieResults> getPopularMovies(@Query("api_key") String apiKey);

    @GET("/3/movie/top_rated")
    Call<MovieResults> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("/3/movie/{movie_id}")
    Call<DetailedMovie> getMovieDetails(@Path("movie_id") String movieID, @Query("api_key") String apiKey);

}
