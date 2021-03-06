package ng.dev.blockbustr.ui.top_rated;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import ng.dev.blockbustr.R;
import ng.dev.blockbustr.misc.MoviesAdapter;
import ng.dev.blockbustr.models.MovieDetails;
import ng.dev.blockbustr.utils.MiscUtils;

public class TopRatedFragment extends Fragment {

    private MoviesAdapter moviesAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TopRatedViewModel topRatedViewModel = new ViewModelProvider(this).get(TopRatedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_top_rated, container, false);

        int spanCount = MiscUtils.calculateNoOfColumns(Objects.requireNonNull(getContext()));
        RecyclerView recyclerView = root.findViewById(R.id.top_rated_movies_rv);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);

        ArrayList<MovieDetails> movies = new ArrayList<>();
        moviesAdapter = new MoviesAdapter(movies, spanCount);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(moviesAdapter);

        topRatedViewModel.getMovies().observe(getViewLifecycleOwner(), newMovies -> {
            movies.clear();
            movies.addAll(newMovies);
            moviesAdapter.notifyDataSetChanged();
        });

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.movies_sorter_menu, menu);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sort_by_date) {
            moviesAdapter.sortByDate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
