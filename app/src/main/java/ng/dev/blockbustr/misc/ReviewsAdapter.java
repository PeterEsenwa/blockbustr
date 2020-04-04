package ng.dev.blockbustr.misc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ng.dev.blockbustr.R;
import ng.dev.blockbustr.models.MovieReview;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {
    final private ArrayList<MovieReview> reviews;

    public ReviewsAdapter(ArrayList<MovieReview> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout view = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
        holder.setReview(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        return reviews == null ? 0 : reviews.size();
    }

    static class ReviewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView reviewerNameTV;
        final TextView reviewTextView;

        ReviewsViewHolder(@NonNull ConstraintLayout itemView) {
            super(itemView);
            reviewerNameTV = itemView.findViewById(R.id.reviewer_name);
            reviewTextView = itemView.findViewById(R.id.review_text);
        }

        @Override
        public void onClick(View v) {

        }

        void setReview(@NonNull MovieReview review) {
            reviewerNameTV.setText(review.author);
            reviewTextView.setText(review.content);
        }
    }
}
