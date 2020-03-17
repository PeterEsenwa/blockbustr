package ng.dev.blockbustr.ui.home;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import ng.dev.blockbustr.models.MovieDetails;
import ng.dev.blockbustr.utils.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

@SuppressWarnings("WeakerAccess")
public class TopRatedViewModel extends ViewModel {
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

        private ArrayList<MovieDetails> tempMovies = new ArrayList<>();

        @Override
        protected ArrayList<MovieDetails> doInBackground(Void... strings) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Builder().url("https://api.themoviedb.org/3/movie/popular?api_key=12f065928e6fa02c8db32b3c15005d1c&language=en-US&page=1").build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    return null;
                }
                String responseBody = response.body() != null ? response.body().string() : null;
                if (responseBody == null) {
                    return null;
                }

                JSONArray respMovies = JsonUtils.getMoviesArray(responseBody);

                for (int i = 0; i < respMovies.length(); i++) {
                    tempMovies.add(JsonUtils.parseMovieDetails(respMovies.getJSONObject(i)));
                }

                return tempMovies;
            } catch (IOException e) {
                // ... handle IO exception
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieDetails> movieDetails) {
            super.onPostExecute(movieDetails);
            movies.setValue(movieDetails);
        }
    }
}
