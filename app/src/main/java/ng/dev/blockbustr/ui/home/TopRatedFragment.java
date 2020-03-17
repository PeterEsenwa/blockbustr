package ng.dev.blockbustr.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ng.dev.blockbustr.R;
import ng.dev.blockbustr.misc.MoviesAdapter;

public class TopRatedFragment extends Fragment {

    private TopRatedViewModel topRatedViewModel;

    private RecyclerView moviesRecyclerView;
    private MoviesAdapter moviesAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        topRatedViewModel = ViewModelProviders.of(this).get(TopRatedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_top_rated, container, false);

        moviesRecyclerView = root.findViewById(R.id.movies_recycler_view);

        moviesAdapter = new MoviesAdapter(new ArrayList<>());
        layoutManager = new GridLayoutManager(getContext(), 2);

        moviesRecyclerView.setLayoutManager(layoutManager);
        moviesRecyclerView.setAdapter(moviesAdapter);

        topRatedViewModel.getMovies().observe(this, movies -> {
            // update UI
            moviesAdapter.updateMovies(movies);
            moviesAdapter.notifyDataSetChanged();
        });

//        final TextView textView = root.findViewById(R.id.text_home);
//        topRatedViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
}
