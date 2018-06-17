package com.bytepair.topmovies.views;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bytepair.topmovies.R;
import com.bytepair.topmovies.models.Video;
import com.bytepair.topmovies.presenters.MovieVideosPresenter;
import com.bytepair.topmovies.views.adapters.MovieVideosAdapter;
import com.bytepair.topmovies.views.interfaces.MovieVideosView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieVideosActivity extends AppCompatActivity implements MovieVideosView, MovieVideosAdapter.MovieVideoClickHandler {

    private static final String TAG = MovieVideosActivity.class.getSimpleName();
    private MovieVideosPresenter mMovieVideosPresenter;
    private MovieVideosAdapter mMovieVideosAdapter;
    private String mMovieID;

    @BindView(R.id.movie_videos_error)
    ConstraintLayout mMovieVideosFail;

    @BindView(R.id.movie_videos_pb)
    ProgressBar mMovieVideosProgressBar;

    @BindView(R.id.movie_videos_rv)
    RecyclerView mMovieVideosRecyclerView;

    @BindView(R.id.full_screen_error_desc_tv)
    TextView mMovieVideoErrorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_videos);

        // Set up action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.videos);
        }

        // Bind views
        ButterKnife.bind(this);

        // Get the Intent that started this activity and extract the movie id string
        Intent intent = getIntent();
        mMovieID = intent.getStringExtra(PostersActivity.MOVIE_ID);

        // Create new reviews presenter
        mMovieVideosPresenter = new MovieVideosPresenter(this);

        // Create new reviews adapter
        mMovieVideosAdapter = new MovieVideosAdapter(mMovieVideosPresenter.getVideos(), this);

        // Connect to recycler view and set fixed size
        mMovieVideosRecyclerView.setAdapter(mMovieVideosAdapter);
        mMovieVideosRecyclerView.setHasFixedSize(true);

        // Create linear layout manager and connect to recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mMovieVideosRecyclerView.setLayoutManager(linearLayoutManager);

        // Use the movie id to fetch reviews
        mMovieVideosPresenter.initializeVideos(mMovieID);
    }

    @Override
    public void loadMovieVideosSuccess() {
        mMovieVideosFail.setVisibility(View.INVISIBLE);
        mMovieVideosProgressBar.setVisibility(View.INVISIBLE);
        mMovieVideosRecyclerView.setVisibility(View.VISIBLE);

        mMovieVideosAdapter.updateVideos(mMovieVideosPresenter.getVideos());
    }

    @Override
    public void loadMovieVideosFailure() {
        mMovieVideosProgressBar.setVisibility(View.INVISIBLE);
        mMovieVideosRecyclerView.setVisibility(View.INVISIBLE);
        mMovieVideoErrorTextView.setText(R.string.movie_videos_not_found);
        mMovieVideosFail.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadMovieVideosInProgress() {
        mMovieVideosFail.setVisibility(View.INVISIBLE);
        mMovieVideosRecyclerView.setVisibility(View.INVISIBLE);
        mMovieVideosProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Video video) {
        // https://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video.getKey()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + video.getKey()));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }
}
