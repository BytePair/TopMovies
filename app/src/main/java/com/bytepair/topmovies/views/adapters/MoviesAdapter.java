package com.bytepair.topmovies.views.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bytepair.topmovies.R;
import com.bytepair.topmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private static final String TAG = MoviesAdapter.class.getSimpleName();

    // Stores a list of movies from the MoviePresenter
    private List<Movie> mMovies;

    // Pass instance of a class that implements the click handler in the constructor
    // and store it for use when an item in the list is clicked
    private final MovieAdapterOnClickHandler mMovieAdapterOnClickHandler;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MoviesAdapter(MovieAdapterOnClickHandler clickHandler, List<Movie> movies) {
        mMovies = movies;
        mMovieAdapterOnClickHandler = clickHandler;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageView;

        MoviesViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            mImageView = view.findViewById(R.id.rv_movie_iv);
        }

        @Override
        public void onClick(View v) {
            mMovieAdapterOnClickHandler.onClick(mMovies.get(getAdapterPosition()));
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public MoviesAdapter.MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(viewType, parent, false);
        return new MoviesViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        // get element from movies at this position
        // replace the contents of the view with that element
        Movie movie = mMovies.get(position);
        Picasso.get()
                .load("http://image.tmdb.org/t/p/w342/" + movie.getPosterPath())
                .error(R.drawable.ic_not_found_placeholder)
                .into(holder.mImageView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    // Returns the view to be used in onCreateViewHolder
    @Override
    public int getItemViewType(int position) {
        return R.layout.recyclerview_movie;
    }

    // Updates the list of movies
    public void updateMovies(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    // Interface that receives the onClick
    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

}
