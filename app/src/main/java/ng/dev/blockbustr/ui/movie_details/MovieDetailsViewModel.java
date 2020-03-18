package ng.dev.blockbustr.ui.movie_details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ng.dev.blockbustr.models.MovieDetails;

public class MovieDetailsViewModel extends ViewModel {

    private static MutableLiveData<MovieDetails> movie = new MutableLiveData<>();

    public LiveData<MovieDetails> getCurrentMovie() {
        return movie;
    }

    public void setCurrentMovie(MovieDetails item) {
        movie.setValue(item);
    }

}
