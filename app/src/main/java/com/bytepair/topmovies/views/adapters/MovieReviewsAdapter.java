package com.bytepair.topmovies.views.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bytepair.topmovies.R;
import com.bytepair.topmovies.models.Review;

import java.util.List;

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.ViewHolder> {

    private List<Review> mReviews;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MovieReviewsAdapter(List<Review> reviews) {
        mReviews = reviews;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView contentTv;
        TextView authorTv;
        ViewHolder(CardView v) {
            super(v);
            contentTv = v.findViewById(R.id.review_content_tv);
            authorTv = v.findViewById(R.id.review_author_tv);
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public MovieReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                             int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_movie_review, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Review review = mReviews.get(position);
        holder.contentTv.setText(review.getContent());
        holder.authorTv.setText(review.getAuthor());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    // Updates the list of reviews
    public void updateReviews(List<Review> reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }
}