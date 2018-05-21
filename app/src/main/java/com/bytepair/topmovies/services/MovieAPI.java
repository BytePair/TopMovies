package com.bytepair.topmovies.services;

import com.bytepair.topmovies.models.MovieResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieAPI {

    @GET("/3/movie/popular")
    Call<MovieResults> getPopularMovies(@Query("api_key") String apiKey);

    @GET("/3/movie/top_rated")
    Call<MovieResults> getTopRatedMovies(@Query("api_key") String apiKey);

}
