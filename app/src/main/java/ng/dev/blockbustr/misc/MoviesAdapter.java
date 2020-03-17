package ng.dev.blockbustr.misc;

import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ng.dev.blockbustr.R;
import ng.dev.blockbustr.models.MovieDetails;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    private ArrayList<MovieDetails> movies;

    public void updateMovies(ArrayList<MovieDetails> newMovies) {
        movies.clear();
        movies.addAll(newMovies);
        notifyDataSetChanged();
    }

    public MoviesAdapter(ArrayList<MovieDetails> movies) {
        this.movies = movies;
    }

    @NonNull
    @Override
    public MoviesAdapter.MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
//        TextView v = (TextView) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.my_text_view, parent, false);
        return new MoviesViewHolder(new ImageView(parent.getContext()));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
        holder.imageView.setImageBitmap(null);
        Picasso.get().load(movies.get(position).getPosterPath())
                .placeholder(R.color.colorSecondary)
                .into(holder.imageView);
//        Picasso.get().load(movies.get(position).getPosterPath()).into(holder.imageView);
//        holder.textView.setText(movies.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }

    static class MoviesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        MoviesViewHolder(ImageView v) {
            super(v);
            imageView = v;
        }
    }
}
