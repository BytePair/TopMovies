/**
 * Helpful guide at https://github.com/codepath/android_guides/wiki/Creating-Content-Providers
 * for implementing content provider.
 */

package com.bytepair.topmovies.utilities.contentproviders;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "topMovies.db";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when first creating the database
     * @param db    {@link SQLiteDatabase} being created
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        addFavoritesTable(db);
    }

    /**
     * Called when version is incremented
     * @param db            The {@link SQLiteDatabase} being upgraded
     * @param oldVersion    The previous version of the database
     * @param newVersion    The new version of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // do nothing for now (only 1 version)
    }

    /**
     * Creates the table for favorite movies
     * @param db    {@link SQLiteDatabase} that will contain the favorite movies table
     */
    private void addFavoritesTable(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME  + " (" +
                        MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                        MovieContract.MovieEntry.POSTER_PATH + " TEXT NOT NULL " + " );"
        );
    }
}
