package ng.dev.blockbustr.ui.favourites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ng.dev.blockbustr.R;
import ng.dev.blockbustr.database.FavouritesDB;
import ng.dev.blockbustr.misc.MoviesAdapter;
import ng.dev.blockbustr.models.MovieDetails;
import ng.dev.blockbustr.utils.MiscUtils;

public class FavouritesFragment extends Fragment {
    private MoviesAdapter moviesAdapter;
    private FavouritesDB favouritesDB;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        favouritesDB = FavouritesDB.getInstance(getContext());

        View root = inflater.inflate(R.layout.fragment_most_viewed, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.movies_rv);
        int spanCount = MiscUtils.calculateNoOfColumns(Objects.requireNonNull(getContext()));
        LayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        moviesAdapter = new MoviesAdapter(new ArrayList<>(), spanCount);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(moviesAdapter);

        FavouritesDB.dbReadIO.execute(() -> {
            LiveData<List<MovieDetails>> favourites = favouritesDB.dao().loadFavourites();
            favourites.observe(getViewLifecycleOwner(), (fMovies) -> moviesAdapter.setMovies(fMovies));
        });

        return root;
    }
}
