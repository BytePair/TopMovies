package com.bytepair.topmovies.views.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bytepair.topmovies.R;
import com.bytepair.topmovies.models.pojos.Video;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieVideosAdapter extends RecyclerView.Adapter<MovieVideosAdapter.ViewHolder> {

    private MovieVideoClickHandler mMovieVideoClickHandler;
    private List<Video> mVideos;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MovieVideosAdapter(List<Video> videos, MovieVideoClickHandler movieVideoClickHandler) {
        mVideos = videos;
        mMovieVideoClickHandler = movieVideoClickHandler;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        TextView description;
        ImageView thumbnail;
        ViewHolder(CardView v) {
            super(v);
            v.setOnClickListener(this);

            description = v.findViewById(R.id.video_desc_tv);
            thumbnail = v.findViewById(R.id.video_thumbnail_iv);
        }

        @Override
        public void onClick(View v) {
            mMovieVideoClickHandler.onClick(mVideos.get(getAdapterPosition()));
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public MovieVideosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                             int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_movie_video, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Video video = mVideos.get(position);
        holder.description.setText(video.getName());
        Picasso.get()
                .load("http://img.youtube.com/vi/" + video.getKey() + "/hqdefault.jpg")
                .error(R.drawable.movie_backdrop_default)
                .into(holder.thumbnail);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    // Updates the list of videos
    public void updateVideos(List<Video> videos) {
        mVideos = videos;
        notifyDataSetChanged();
    }

    public interface MovieVideoClickHandler {
        void onClick(Video video);
    }
}
