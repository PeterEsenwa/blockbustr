package ng.dev.blockbustr.ui.most_viewed;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;

import java.util.ArrayList;

import ng.dev.blockbustr.models.MovieDetails;
import ng.dev.blockbustr.utils.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MostViewedViewModel extends ViewModel {

    private static MutableLiveData<ArrayList<MovieDetails>> movies;

    LiveData<ArrayList<MovieDetails>> getMovies() {
        if (movies == null) {
            movies = new MutableLiveData<>();
            loadMovies();
        }
        return movies;
    }

    private void loadMovies() {
        new RetrieveMovies().execute();
    }

    static class RetrieveMovies extends AsyncTask<Void, Void, ArrayList<MovieDetails>> {

        final private ArrayList<MovieDetails> tempMovies = new ArrayList<>();

        @Override
        protected ArrayList<MovieDetails> doInBackground(Void... strings) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.themoviedb.org/3/movie/popular?api_key=12f065928e6fa02c8db32b3c15005d1c" +
                            "&language=en-US&page=1")
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    return tempMovies;
                }
                String responseBody = response.body() != null ? response.body().string() : null;
                if (responseBody == null) {
                    return tempMovies;
                }

                JSONArray respMovies = JsonUtils.getMoviesArray(responseBody);

                for (int i = 0; i < respMovies.length(); i++) {
                    tempMovies.add(JsonUtils.parseMovieDetails(respMovies.getJSONObject(i)));
                }

                return tempMovies;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return tempMovies;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieDetails> movieDetails) {
            super.onPostExecute(movieDetails);
            movies.setValue(movieDetails);
        }
    }
}
