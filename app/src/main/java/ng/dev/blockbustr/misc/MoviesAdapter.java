package ng.dev.blockbustr.misc;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ng.dev.blockbustr.R;
import ng.dev.blockbustr.models.MovieDetails;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    private ArrayList<MovieDetails> movies;

    public MoviesAdapter(ArrayList<MovieDetails> movies) {
        this.movies = movies;
    }

    @NonNull
    @Override
    public MoviesAdapter.MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConstraintLayout view = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new MoviesViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        int leftRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25 / 2, holder.constraintLayout.getResources().getDisplayMetrics());
        int topDownPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.5f, holder.constraintLayout.getResources().getDisplayMetrics());

        int column = position % 2;
        int right = 0;
        int left = 0;
        int top = 0;
        int bottom = 0;

        if (column == 0) {
            right = leftRightPadding;
        }

        if (column == 1) {
            left = leftRightPadding;
        }

        if (position > 1) {
            top = topDownPadding;
        }

        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) holder.constraintLayout.getLayoutParams();
        params.setMargins(left, top, right, bottom);
        holder.constraintLayout.setLayoutParams(params);

        Picasso.get().load(movies.get(position).getPosterPath())
                .placeholder(R.color.colorSecondary)
                .into(holder.posterImageView);


//        Picasso.get().load(movies.get(position).getPosterPath()).into(holder.imageView);
//        holder.textView.setText(movies.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }

    static class MoviesViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout constraintLayout;
        ImageView posterImageView;

        MoviesViewHolder(ConstraintLayout v) {
            super(v);
            constraintLayout = v;
            posterImageView = constraintLayout.findViewById(R.id.movie_poster);
        }
    }
}
