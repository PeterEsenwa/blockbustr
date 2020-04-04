package ng.dev.blockbustr.ui.movie_details;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import ng.dev.blockbustr.R;
import ng.dev.blockbustr.database.FavouritesDB;
import ng.dev.blockbustr.misc.ReviewsAdapter;
import ng.dev.blockbustr.models.Genre;
import ng.dev.blockbustr.models.MovieDetails;
import ng.dev.blockbustr.models.MovieReview;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_SETTLING;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.from;

public class MovieDetailsFragment extends Fragment {
    private ImageView backdropImageView;
    private TextView overviewTextView;
    private TextView ratingsTextView;
    private TextView dateTextView;
    private ChipGroup genresChipGroup;
    private ImageButton watchTrailerButton;
    private MovieDetails movieDetailsPersist = null;

    private FavouritesDB favouritesDB;
    private Menu menu;
    private BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior;
    private ConstraintLayout sheetLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup group, @Nullable Bundle bundle) {
        setHasOptionsMenu(true);
        favouritesDB = FavouritesDB.getInstance(getContext());

        MovieDetailsViewModel movieDetailsVM = new ViewModelProvider(this).get(MovieDetailsViewModel.class);
        CoordinatorLayout root = (CoordinatorLayout) inflater.inflate(R.layout.fragment_movie_details, group, false);
        ConstraintLayout mainLayout = root.findViewById(R.id.movie_details_layout);
        sheetLayout = root.findViewById(R.id.movie_reviews_bottomSheet);
        backdropImageView = mainLayout.findViewById(R.id.movie_backdrop);
        overviewTextView = mainLayout.findViewById(R.id.movie_overview);
        ratingsTextView = mainLayout.findViewById(R.id.ratings);
        dateTextView = mainLayout.findViewById(R.id.date_tv);
        genresChipGroup = mainLayout.findViewById(R.id.genres_holder);
        watchTrailerButton = mainLayout.findViewById(R.id.watch_main_trailer);

        bottomSheetBehavior = from(sheetLayout);
        bottomSheetBehavior.setSaveFlags(BottomSheetBehavior.SAVE_ALL);
        LayoutTransition transition = new LayoutTransition();
        transition.setAnimateParentHierarchy(false);
        sheetLayout.setLayoutTransition(transition);

        movieDetailsVM.getCurrentMovie().observe(getViewLifecycleOwner(), movieDetails -> {
            if (movieDetailsPersist == null || !movieDetails.getId().equals(movieDetailsPersist.getId())) {
                if (movieDetailsPersist == null) {
                    movieDetailsPersist = movieDetails;
                }

                FavouritesDB.dbReadIO.execute(() -> favouritesDB.dao().loadFavourites().observe(getViewLifecycleOwner(), (movies) -> {
                    for (MovieDetails movieDetail : movies) {
                        if (menu == null) return;
                        if (Objects.equals(movieDetail.getId(), movieDetailsPersist.getId())) {
                            menu.getItem(0).getIcon().setTint(Objects.requireNonNull(getContext()).getColor(R.color.colorFavouriteRed));
                            menu.getItem(0).setChecked(true);
                            break;
                        }
                    }
                }));

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

                if (!movieDetails.videos.isEmpty()) {
                    watchTrailerButton.setVisibility(View.VISIBLE);
                }
            }

            if (!movieDetails.videos.isEmpty() && movieDetails.getId().equals(movieDetailsPersist.getId())) {
                watchTrailerButton.setVisibility(View.VISIBLE);
            }

            if (watchTrailerButton.getVisibility() == View.VISIBLE) {
                watchTrailerButton.setOnClickListener(view -> {
                    Uri videoLink = Uri.parse(movieDetails.videos.get(0).getUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(videoLink);

                    PackageManager packageManager = Objects.requireNonNull(getActivity()).getPackageManager();
                    List<ResolveInfo> activities = packageManager.queryIntentActivities(intent,
                            PackageManager.MATCH_DEFAULT_ONLY);
                    boolean isIntentSafe = activities.size() > 0;
                    if (isIntentSafe) {
                        startActivity(intent);
                    }
                });

            }

            if (movieDetails.reviews.isEmpty() && movieDetails.getId().equals(movieDetailsPersist.getId())) {
                sheetLayout.setVisibility(View.GONE);
            } else if (!movieDetails.reviews.isEmpty()) {
                setupBottomSheet(sheetLayout, movieDetails.reviews);
            }

            bottomSheetBehavior.addBottomSheetCallback(new BottomSheetCallback() {
                int state;

                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    state = newState;
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    View reviewsTV = bottomSheet.findViewById(R.id.reviews_tv);
                    reviewsTV.setAlpha(1f - slideOffset);
                    if (slideOffset < 1) {
                        setActionBarElevation(0);
                        setTitle(movieDetails.getTitle());

                        if (reviewsTV.getVisibility() != View.VISIBLE && state != STATE_EXPANDED) {
                            final Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                LayoutTransition lt = ((ViewGroup) reviewsTV.getParent()).getLayoutTransition();
                                lt.disableTransitionType(LayoutTransition.APPEARING);
                                reviewsTV.setVisibility(View.VISIBLE);
                                lt.enableTransitionType(LayoutTransition.APPEARING);
                            }, 0);
                        }
                    }

                    if (slideOffset == 1 && state == STATE_SETTLING) {
                        bottomSheetBehavior.setState(STATE_EXPANDED);
                    }

                    if (slideOffset == 1 && state == STATE_EXPANDED) {
                        setActionBarElevation(12);
                        setTitle(String.format(Locale.getDefault(), "%s (%d)", getString(R.string.reviews_text),
                                movieDetails.reviews.size()));
                        final Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            LayoutTransition lt = ((ViewGroup) reviewsTV.getParent()).getLayoutTransition();
                            lt.disableTransitionType(LayoutTransition.DISAPPEARING);
                            reviewsTV.setVisibility(View.GONE);
                            lt.enableTransitionType(LayoutTransition.DISAPPEARING);
                        }, 0);
                    }
                }
            });

        });

        return root;
    }

    private void setupBottomSheet(ConstraintLayout sheetLayout, ArrayList<MovieReview> movieReviews) {
        sheetLayout.setVisibility(View.VISIBLE);
        String title = String.format(Locale.getDefault(), "%s (%d)", getString(R.string.reviews_text),
                movieReviews.size());
        ((TextView) sheetLayout.findViewById(R.id.reviews_tv)).setText(title);

        RecyclerView reviewsRecyclerView = sheetLayout.findViewById(R.id.reviews_holder);
        reviewsRecyclerView.setAdapter(new ReviewsAdapter(movieReviews));
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.movie_details_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.getItem(0).getIcon().setTint(Objects.requireNonNull(getContext()).getColor(R.color.colorFavouriteGray));
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_favorite) {
            item.setChecked(!item.isChecked());
            setFavourite(item.isChecked());
            return true;
        }

        if (id == android.R.id.home && bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            RecyclerView reviewsRecyclerView = sheetLayout.findViewById(R.id.reviews_holder);
            reviewsRecyclerView.smoothScrollToPosition(0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFavourite(boolean isFav) {
        if (isFav) {
            FavouritesDB.dbIO.execute(() -> favouritesDB.dao().addNewFavourite(movieDetailsPersist));
            menu.getItem(0).getIcon().setTint(Objects.requireNonNull(getContext()).getColor(R.color.colorFavouriteRed));
        } else {
            FavouritesDB.dbIO.execute(() -> favouritesDB.dao().deleteFavourite(movieDetailsPersist));
            menu.getItem(0).getIcon().setTint(Objects.requireNonNull(getContext()).getColor(R.color.colorFavouriteGray));
        }
    }

    private void setTitle(String title) {
        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        assert activity != null;
        ActionBar actionBar = activity.getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(title);
    }

    private void setActionBarElevation(int elevation) {
        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        assert activity != null;
        ActionBar actionBar = activity.getSupportActionBar();
        assert actionBar != null;
        actionBar.setElevation(elevation);
    }

    private void addGenreChips(List<Genre> genres) {
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
