package com.bytepair.topmovies.views;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bytepair.topmovies.R;
import com.bytepair.topmovies.utilities.contentproviders.MovieContract;
import com.bytepair.topmovies.models.DetailedMovie;
import com.bytepair.topmovies.presenters.MoviePresenter;
import com.bytepair.topmovies.utilities.contentproviders.FavoriteMovieQueryHandler;
import com.bytepair.topmovies.views.interfaces.MovieView;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieActivity extends AppCompatActivity implements  MovieView, FavoriteMovieQueryHandler.FavoriteMovieQueryListener {

    private static final String TAG = MovieActivity.class.getSimpleName();
    private FavoriteMovieQueryHandler favoriteMovieQueryHandler;
    private MoviePresenter moviePresenter;
    private String movieID;
    private Boolean movieIsFavorite;

    @BindView(R.id.movie_activity_movie_pb)
    ProgressBar mProgressBar;

    @BindView(R.id.movie_activity_success_cl)
    ConstraintLayout mMovieDetailsLayout;

    @BindView(R.id.activity_movie_failure_cl)
    ConstraintLayout mMovieFailedLayout;

    @BindView(R.id.activity_movie_backgroup_iv)
    ImageView mBackdropImageView;

    @BindView(R.id.activity_movie_title_tv)
    TextView mMovieTitleTextView;

    @BindView(R.id.activity_movie_summary_tv)
    TextView mDescriptionTextView;

    @BindView(R.id.activity_movie_year_tv)
    TextView mMovieReleaseDate;

    @BindView(R.id.activity_movie_runtime_tv)
    TextView mMovieRuntime;

    @BindView(R.id.activity_movie_rating_rb)
    RatingBar mMovieRating;

    @BindView(R.id.full_screen_error_desc_tv)
    TextView mErrorTextView;

    @BindView(R.id.activity_movie_reviews)
    CardView mMovieReviews;

    @BindView(R.id.activity_movie_videos)
    CardView mMovieVideos;

    @BindView(R.id.favorite_fab)
    FloatingActionButton mFavoriteFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        // Use BufferKnife to bind views
        ButterKnife.bind(this);

        // Set up action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        // Get the Intent that started this activity and extract the movie id string
        Intent intent = getIntent();
        movieID = intent.getStringExtra(PostersActivity.MOVIE_ID);

        // Set on click listeners
        mMovieReviews.setOnClickListener(reviewsClickListener);
        mMovieVideos.setOnClickListener(videosClickListener);
        mFavoriteFAB.setOnClickListener(favoriteClickListener);

        // Then use that movie id to fetch more movie details from the movie presenter
        moviePresenter = new MoviePresenter(this);
        moviePresenter.fetchMovie(movieID);

        // Get the query handler that will be used to query/add/remove favorite status
        favoriteMovieQueryHandler = new FavoriteMovieQueryHandler(getContentResolver(), this);
        queryForFavorite();
    }

    @Override
    public void loadMovieInProgress() {
        mMovieDetailsLayout.setVisibility(View.INVISIBLE);
        mMovieFailedLayout.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadMovieSuccess() {
        mMovieFailedLayout.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mMovieDetailsLayout.setVisibility(View.VISIBLE);

        populateMovieDetails(moviePresenter.getDetailedMovie());
    }

    @Override
    public void loadMovieFailure() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mMovieDetailsLayout.setVisibility(View.INVISIBLE);
        mErrorTextView.setText(R.string.movie_failed_to_load);
        mMovieFailedLayout.setVisibility(View.VISIBLE);

        Toast.makeText(this, "Sorry, Could not load movie", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void populateMovieDetails(DetailedMovie detailedMovie) {

        // set backdrop
        Picasso.get()
                .load("http://image.tmdb.org/t/p/w342/" + detailedMovie.getBackdropPath())
                .error(R.drawable.movie_backdrop_default)
                .into(mBackdropImageView);

        // set title
        mMovieTitleTextView.setText(formatMovieTitle(detailedMovie.getTitle()));

        // set year
        mMovieReleaseDate.setText(formatMovieYear(detailedMovie.getReleaseDate()));

        // set description
        mDescriptionTextView.setText(formatMovieSummary(detailedMovie.getOverview()));

        // set runtime
        mMovieRuntime.setText(formatMovieRuntime(detailedMovie.getRuntime()));

        // set rating
        mMovieRating.setRating((float) detailedMovie.getVoteAverage() / 2);

    }

    private String formatMovieTitle(String title) {
        if (StringUtils.isBlank(title)) {
            return "No Title Found";
        }
        if (title.length() < 25) {
            return title;
        }
        return title.substring(0, 25) + "...";
    }

    private String formatMovieYear(String date) {
        if (StringUtils.isBlank(date)) {
            return "";
        }
        return date.substring(0, 4);
    }

    private String formatMovieSummary(String summary) {
        if (StringUtils.isBlank(summary)) {
            return getString(R.string.default_summary);
        }
        return "\t\t" + summary;
    }

    private String formatMovieRuntime(int runtime) {
        StringBuilder runtimeBuilder = new StringBuilder();
        int hours;
        int minutes;
        hours = runtime / 60;
        minutes = runtime % 60;
        if (hours > 0) runtimeBuilder.append(String.valueOf(hours)).append("h ");
        if (minutes != -1) runtimeBuilder.append(String.valueOf(minutes)).append("m");
        return runtimeBuilder.toString();
    }

    View.OnClickListener reviewsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), MovieReviewsActivity.class);
            intent.putExtra(PostersActivity.MOVIE_ID, movieID);
            startActivity(intent);
        }
    };

    View.OnClickListener videosClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), MovieVideosActivity.class);
            intent.putExtra(PostersActivity.MOVIE_ID, movieID);
            startActivity(intent);
        }
    };

    View.OnClickListener favoriteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (movieIsFavorite) {
                deleteFavorite();
            } else {
                addFavorite();
            }
            queryForFavorite();
        }
    };

    private void queryForFavorite() {
        favoriteMovieQueryHandler.startQuery(
                0,
                null,
                MovieContract.MovieEntry.buildMovieUri(Long.valueOf(movieID)),
                new String[]{MovieContract.MovieEntry._ID, MovieContract.MovieEntry.POSTER_PATH},
                null,
                null,
                null
        );
    }

    private void addFavorite() {
        if (moviePresenter.getDetailedMovie() != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieContract.MovieEntry._ID, movieID);
            contentValues.put(MovieContract.MovieEntry.POSTER_PATH, moviePresenter.getDetailedMovie().getPosterPath());
            favoriteMovieQueryHandler.startInsert(
                    0,
                    null,
                    MovieContract.MovieEntry.CONTENT_URI,
                    contentValues
            );
        }
    }

    private void deleteFavorite() {
        favoriteMovieQueryHandler.startDelete(
                0,
                null,
                MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry._ID + "=?",
                new String[]{movieID}
        );
    }

    @Override
    public void onQueryComplete(Cursor cursor) {
        if (cursor != null && cursor.moveToNext()) {
            mFavoriteFAB.setImageResource(R.drawable.ic_baseline_favorite_24px);
            movieIsFavorite = true;
        } else {
            mFavoriteFAB.setImageResource(R.drawable.ic_baseline_favorite_border_24px);
            movieIsFavorite = false;
        }
    }

    @Override
    public void onInsertComplete(Uri uri) {
        Log.i(TAG, "Added movie to favorites: " + ContentUris.parseId(uri));
    }

    @Override
    public void onDeleteComplete(int result) {
        Log.i(TAG, "Deleted " + result + " movie(s) from favorites");
    }
}
