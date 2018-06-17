package com.bytepair.topmovies.utilities.contentproviders;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class FavoriteMovieQueryHandler extends AsyncQueryHandler {

    private FavoriteMovieQueryListener favoriteMovieQueryListener;

    public FavoriteMovieQueryHandler(ContentResolver cr, FavoriteMovieQueryListener favoriteMovieQueryListener) {
        super(cr);
        this.favoriteMovieQueryListener = favoriteMovieQueryListener;
    }
    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        super.onQueryComplete(token, cookie, cursor);
        favoriteMovieQueryListener.onQueryComplete(cursor);
    }
    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        super.onInsertComplete(token, cookie, uri);
        favoriteMovieQueryListener.onInsertComplete(uri);
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int result) {
        super.onDeleteComplete(token, cookie, result);
        favoriteMovieQueryListener.onDeleteComplete(result);
    }

    public interface FavoriteMovieQueryListener {
        void onQueryComplete(Cursor cursor);
        void onInsertComplete(Uri uri);
        void onDeleteComplete(int result);
    }

}
