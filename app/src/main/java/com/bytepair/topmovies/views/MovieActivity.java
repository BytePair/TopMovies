package com.bytepair.topmovies.views;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bytepair.topmovies.R;
import com.bytepair.topmovies.models.DetailedMovie;
import com.bytepair.topmovies.presenters.MoviePresenter;
import com.bytepair.topmovies.views.interfaces.MovieView;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieActivity extends AppCompatActivity implements MovieView {

    private static final String TAG = MovieActivity.class.getSimpleName();

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

    private MoviePresenter moviePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        // Get the Intent that started this activity and extract the movie id string
        Intent intent = getIntent();
        String movieID = intent.getStringExtra(PostersActivity.MOVIE_ID);

        // Then use that movie id to fetch more movie details from the movie presenter
        moviePresenter = new MoviePresenter(this);
        moviePresenter.fetchMovie(movieID);
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

        Toast.makeText(this, "Sorry, could not load movie", Toast.LENGTH_SHORT).show();
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
        if (hours != -1) runtimeBuilder.append(String.valueOf(hours)).append("h ");
        if (minutes != -1) runtimeBuilder.append(String.valueOf(minutes)).append("m");
        return runtimeBuilder.toString();
    }

}
