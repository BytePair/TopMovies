package com.bytepair.topmovies.views;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bytepair.topmovies.R;
import com.bytepair.topmovies.models.DetailedMovie;
import com.bytepair.topmovies.presenters.MoviePresenter;
import com.bytepair.topmovies.views.interfaces.MovieView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieActivity extends AppCompatActivity implements MovieView {

    private static final String TAG = MovieActivity.class.getSimpleName();

    @BindView(R.id.movie_progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.movie_constraint_layout)
    ConstraintLayout mMovieDetailsLayout;

    @BindView(R.id.movie_load_fail_constraint_layout)
    ConstraintLayout mMovieFailedLayout;

    @BindView(R.id.backdrop_image_view)
    ImageView mBackdropImageView;

    @BindView(R.id.movie_title_text_view)
    TextView mMovieTitleTextView;

    @BindView(R.id.year_text_view)
    TextView mYearTextView;

    @BindView(R.id.description_text_view)
    TextView mDescriptionTextView;

    @BindView(R.id.movie_db_text_view)
    TextView mMovieDbTextView;

    @BindView(R.id.imdb_rating_text_view)
    TextView mImdbTextView;

    @BindView(R.id.imdb_constraint_layout)
    ConstraintLayout mImdbConstraintLayout;

    private MoviePresenter moviePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        ButterKnife.bind(this);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String movieID = intent.getStringExtra(PostersActivity.MOVIE_ID);
        Toast.makeText(this, "Movie ID: " + movieID, Toast.LENGTH_SHORT).show();

        mImdbConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
        mMovieFailedLayout.setVisibility(View.VISIBLE);

        Toast.makeText(this, "Sorry, could not load movie", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void populateMovieDetails(DetailedMovie detailedMovie) {

        // get title
        mMovieTitleTextView.setText(detailedMovie.getTitle());

        // get release date
        mYearTextView.setText(detailedMovie.getReleaseDate().substring(0, 4));

        // get description
        mDescriptionTextView.setText("\t\t" + detailedMovie.getOverview());

        // set ratings
        mImdbTextView.setText(String.valueOf(detailedMovie.getVoteAverage()));
        mMovieDbTextView.setText(String.valueOf(detailedMovie.getVoteAverage()));

        Picasso.get()
                .load("http://image.tmdb.org/t/p/w342/" + detailedMovie.getBackdropPath())
                .error(R.drawable.movie_backdrop_default)
                .into(mBackdropImageView);
    }

}
