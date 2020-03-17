package ng.dev.blockbustr.misc;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ng.dev.blockbustr.models.MovieDetails;

class MovieOnClickListener implements View.OnClickListener {
    private RecyclerView recyclerView;
    private ArrayList<MovieDetails> movieDetails;
    private Context ctx;

    public MovieOnClickListener(RecyclerView recyclerView, ArrayList<MovieDetails> movieDetails, Context ctx) {
        this.recyclerView = recyclerView;
        this.movieDetails = movieDetails;
        this.ctx = ctx;
    }

    @Override
    public void onClick(View view) {
        int itemPosition = recyclerView.getChildLayoutPosition(view);
        MovieDetails movie = movieDetails.get(itemPosition);
        Toast.makeText(ctx, movie.getTitle(), Toast.LENGTH_LONG).show();
    }
}
