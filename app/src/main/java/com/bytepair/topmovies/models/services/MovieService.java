package com.bytepair.topmovies.models.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieService {

    private static final String BASE_URL = "http://api.themoviedb.org/";

    private static MovieAPI movieAPI = null;

    public static MovieAPI getAPI() {
        if (movieAPI == null) {
            Retrofit retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            movieAPI = retrofit.create(MovieAPI.class);
        }
        return movieAPI;
    }

}
