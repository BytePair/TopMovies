package com.bytepair.topmovies.views;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bytepair.topmovies.R;
import com.bytepair.topmovies.presenters.MovieReviewsPresenter;
import com.bytepair.topmovies.views.adapters.MovieReviewsAdapter;
import com.bytepair.topmovies.views.interfaces.MovieReviewsView;
import com.bytepair.topmovies.views.listeners.EndlessRecyclerViewScrollListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieReviewsActivity extends AppCompatActivity implements MovieReviewsView {

    private static final String TAG = MovieReviewsActivity.class.getSimpleName();
    private EndlessRecyclerViewScrollListener mScrollListener;
    private MovieReviewsPresenter mMovieReviewsPresenter;
    private MovieReviewsAdapter mMovieReviewsAdapter;
    private String mMovieID;

    @BindView(R.id.movie_reviews_error)
    ConstraintLayout mMovieReviewsFail;

    @BindView(R.id.movie_reviews_pb)
    ProgressBar mMovieReviewsProgressBar;

    @BindView(R.id.movie_reviews_rv)
    RecyclerView mMovieReviewsRecyclerView;

    @BindView(R.id.full_screen_error_desc_tv)
    TextView mMovieReviewErrorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_reviews);

        // Set up action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.reviews);
        }

        // Bind views
        ButterKnife.bind(this);

        // Get the Intent that started this activity and extract the movie id string
        Intent intent = getIntent();
        mMovieID = intent.getStringExtra(PostersActivity.MOVIE_ID);

        // Create new reviews presenter
        mMovieReviewsPresenter = new MovieReviewsPresenter(this);

        // Create new reviews adapter
        mMovieReviewsAdapter = new MovieReviewsAdapter(mMovieReviewsPresenter.getReviews());

        // Connect to recycler view and set fixed size
        mMovieReviewsRecyclerView.setAdapter(mMovieReviewsAdapter);
        mMovieReviewsRecyclerView.setHasFixedSize(true);

        // Create linear layout manager and connect to recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mMovieReviewsRecyclerView.setLayoutManager(linearLayoutManager);

        // add scroll listener to recycler view for endless scrolling
        setupScrollListener(linearLayoutManager);
        mMovieReviewsRecyclerView.addOnScrollListener(mScrollListener);

        // Use the movie id to fetch reviews
        mMovieReviewsPresenter.initializeReviews(mMovieID);
    }

    @Override
    public void loadMovieReviewsSuccess() {
        mMovieReviewsFail.setVisibility(View.INVISIBLE);
        mMovieReviewsProgressBar.setVisibility(View.INVISIBLE);
        mMovieReviewsRecyclerView.setVisibility(View.VISIBLE);

        mMovieReviewsAdapter.updateReviews(mMovieReviewsPresenter.getReviews());
    }

    @Override
    public void loadMovieReviewsFailure() {
        mMovieReviewsProgressBar.setVisibility(View.INVISIBLE);
        mMovieReviewsRecyclerView.setVisibility(View.INVISIBLE);
        mMovieReviewErrorTextView.setText(R.string.movie_reviews_not_found);
        mMovieReviewsFail.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadMovieReviewsInProgress() {
        mMovieReviewsFail.setVisibility(View.INVISIBLE);
        mMovieReviewsRecyclerView.setVisibility(View.INVISIBLE);
        mMovieReviewsProgressBar.setVisibility(View.VISIBLE);
    }

    private void setupScrollListener(LinearLayoutManager linearLayoutManager) {
        mScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mMovieReviewsPresenter.loadMoreReviews(mMovieID, page);
            }
        };
    }
}
