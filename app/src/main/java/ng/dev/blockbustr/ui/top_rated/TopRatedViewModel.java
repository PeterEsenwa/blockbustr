package ng.dev.blockbustr.ui.top_rated;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONArray;

import java.util.ArrayList;

import ng.dev.blockbustr.R;
import ng.dev.blockbustr.models.MovieDetails;
import ng.dev.blockbustr.utils.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TopRatedViewModel extends AndroidViewModel {
    private static MutableLiveData<ArrayList<MovieDetails>> movies;
    private static String topRatedApiURL;
    private static String apiKey;

    public TopRatedViewModel(@NonNull Application application) {
        super(application);
        topRatedApiURL = application.getString(R.string.top_rated_api_url);
        apiKey = application.getString(R.string.api_key);
    }

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

        private final ArrayList<MovieDetails> tempMovies = new ArrayList<>();

        @Override
        protected ArrayList<MovieDetails> doInBackground(Void... strings) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url(String.format(topRatedApiURL, apiKey)).build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    return tempMovies;
                }
                String responseBody = response.body() != null ? response.body().string() : null;
                if (responseBody == null) {
                    return tempMovies;
                }

                JSONArray respMovies = JsonUtils.getTMDB_ResponseArray(responseBody);

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
