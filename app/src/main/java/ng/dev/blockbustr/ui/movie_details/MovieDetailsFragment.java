package ng.dev.blockbustr.ui.movie_details;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import ng.dev.blockbustr.R;
import ng.dev.blockbustr.models.Genre;

public class MovieDetailsFragment extends Fragment {
    private ImageView backdropImageView;
    private TextView overviewTextView;
    private TextView ratingsTextView;
    private TextView dateTextView;
    private ChipGroup genresChipGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup group, @Nullable Bundle bundle) {
        MovieDetailsViewModel movieDetailsVM = new ViewModelProvider(this).get(MovieDetailsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_movie_details, group, false);
        backdropImageView = root.findViewById(R.id.movie_backdrop);
        overviewTextView = root.findViewById(R.id.movie_overview);
        ratingsTextView = root.findViewById(R.id.ratings);
        dateTextView = root.findViewById(R.id.date_tv);
        genresChipGroup = root.findViewById(R.id.genres_holder);

        movieDetailsVM.getCurrentMovie().observe(getViewLifecycleOwner(), movieDetails -> {
            setTitle(movieDetails.getTitle());
            addGenreChips(movieDetails.getGenres());

            Picasso.get().load(movieDetails.getBackdropPath())
                    .placeholder(R.color.colorSecondary)
                    .into(backdropImageView);

            overviewTextView.setText(movieDetails.getOverview());
            ratingsTextView.setText(String.format("%s", movieDetails.getVoteAverage()));

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            dateTextView.setText(String.format(getString(R.string.release_date),
                    dateFormat.format(movieDetails.getReleaseDate())));
        });

        return root;
    }

    private void setTitle(String title) {
        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        assert activity != null;
        ActionBar actionBar = activity.getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(title);
    }

    private void addGenreChips(ArrayList<Genre> genres) {
        for (Genre genre : genres) {
            Context context = getContext();
            if (context != null) {
                Chip chip = new Chip(context);
                chip.setChipBackgroundColorResource(R.color.colorPrimaryLight);
                chip.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                chip.setText(genre.getName());
                chip.setCheckable(false);
                chip.setCheckable(false);
                genresChipGroup.addView(chip);
            }
        }
    }
}
