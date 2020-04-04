package ng.dev.blockbustr.misc;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ng.dev.blockbustr.MainActivity;
import ng.dev.blockbustr.R;
import ng.dev.blockbustr.models.MovieDetails;
import ng.dev.blockbustr.ui.movie_details.MovieDetailsViewModel;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    final private ArrayList<MovieDetails> movies;
    private final int spanCount;

    public MoviesAdapter(ArrayList<MovieDetails> movies, int spanCount) {
        this.movies = movies;
        this.spanCount = spanCount;
    }

    public void setMovies(List<MovieDetails> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MoviesAdapter.MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConstraintLayout view = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        int leftRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12.5f,
                holder.constraintLayout.getResources().getDisplayMetrics());
        int topDownPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.5f,
                holder.constraintLayout.getResources().getDisplayMetrics());

        int right;
        int left;
        int top = 0;
        int bottom = 0;

        right = leftRightPadding / 2;
        left = leftRightPadding / 2;

        if (position > spanCount - 1) {
            top = topDownPadding;
        }

        GridLayoutManager.LayoutParams params =
                (GridLayoutManager.LayoutParams) holder.constraintLayout.getLayoutParams();
        params.setMargins(left, top, right, bottom);
        holder.constraintLayout.setLayoutParams(params);

        MovieDetails movie = movies.get(position);
        holder.movie = movie;
        Picasso.get().load(movie.getPosterPath())
                .placeholder(R.color.colorSecondary)
                .into(holder.posterImageView);
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }

    public void sortByDate() {
        ArrayList<MovieDetails> tempMovies = new ArrayList<>(movies);
        Collections.sort(tempMovies, MovieDetails.releaseDateComparator());

        movies.clear();
        movies.addAll(tempMovies);

        notifyDataSetChanged();
    }

    static class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ConstraintLayout constraintLayout;
        final ImageView posterImageView;
        MovieDetails movie;

        MoviesViewHolder(ConstraintLayout v) {
            super(v);
            itemView.setOnClickListener(this);
            constraintLayout = v;
            posterImageView = constraintLayout.findViewById(R.id.movie_poster);
        }

        @Override
        public void onClick(View view) {
            MainActivity mainActivity = (MainActivity) itemView.getContext();
            MovieDetailsViewModel movieDetailsVM = new ViewModelProvider(mainActivity).get(MovieDetailsViewModel.class);

            movieDetailsVM.setCurrentMovie(movie);

            NavController navController = Navigation.findNavController(itemView);
            NavDestination currentDestination = navController.getCurrentDestination();
            if (currentDestination != null) {
                int currentID = currentDestination.getId();

                switch (currentID) {
                    case (R.id.nav_most_viewed): {
                        navController.navigate(R.id.action_nav_most_viewed_to_movie_details);
                        break;
                    }
                    case (R.id.nav_now_showing): {
                        navController.navigate(R.id.action_nav_now_showing_to_movie_details);
                        break;
                    }
                    case (R.id.nav_top_rated): {
                        navController.navigate(R.id.action_nav_top_rated_to_movie_details);
                        break;
                    }
                    case (R.id.nav_favourites): {
                        navController.navigate(R.id.action_nav_favourites_to_nav_movie_details);
                        break;
                    }
                }
            }

            if (!movie.previouslyClicked) {
                movie.previouslyClicked = true;
            }
        }
    }
}
