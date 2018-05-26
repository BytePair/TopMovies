/**
 * Helpful guide for creating and using singleton for Retrofit
 * https://code.tutsplus.com/tutorials/getting-started-with-retrofit-2--cms-27792
 */

package com.bytepair.topmovies.utilities.services;

import com.bytepair.topmovies.utilities.RetrofitClient;

public class MovieService {

    private static final String BASE_URL = "http://api.themoviedb.org/";

    public static MovieAPI getAPI() {
        return RetrofitClient.getClient(BASE_URL).create(MovieAPI.class);
    }

}
