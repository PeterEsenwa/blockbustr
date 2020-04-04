package ng.dev.blockbustr.ui.movie_details;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;

import ng.dev.blockbustr.R;
import ng.dev.blockbustr.models.MovieDetails;
import ng.dev.blockbustr.models.MovieReview;
import ng.dev.blockbustr.models.MovieVideo;
import ng.dev.blockbustr.utils.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static ng.dev.blockbustr.models.MovieVideo.VideoType.TRAILER;

public class MovieDetailsViewModel extends AndroidViewModel {

    private static final MutableLiveData<MovieDetails> movie = new MutableLiveData<>();
    private static String movieVideosApiURL;
    private static String apiKey;
    private static String movieReviewsApiURL;

    public MovieDetailsViewModel(@NonNull Application application) {
        super(application);
        movieVideosApiURL = application.getString(R.string.movie_videos_api_url);
        movieReviewsApiURL = application.getString(R.string.movie_reviews_api_url);
        apiKey = application.getString(R.string.api_key);
    }

    LiveData<MovieDetails> getCurrentMovie() {
        return movie;
    }

    public void setCurrentMovie(MovieDetails currentMovie) {
        movie.setValue(currentMovie);
        if (!currentMovie.previouslyClicked) {
            new RetrieveVideos().execute(currentMovie.getId().toString());
            new RetrieveReviews().execute(currentMovie.getId().toString());
        }
    }

    static class RetrieveVideos extends AsyncTask<String, Void, ArrayList<MovieVideo>> {

        @Override
        protected ArrayList<MovieVideo> doInBackground(String... strings) {
            if (strings.length == 0) return new ArrayList<>();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(String.format(movieVideosApiURL, strings[0], apiKey)).build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    return new ArrayList<>();
                }

                String responseBody = response.body() != null ? response.body().string() : null;
                if (responseBody == null) {
                    return new ArrayList<>();
                }

                JSONArray respVideos = JsonUtils.getTMDB_ResponseArray(responseBody);

                ArrayList<MovieVideo> tempVideos = new ArrayList<>();
                for (int i = 0; i < respVideos.length(); i++) {
                    MovieVideo video = JsonUtils.getVideo(respVideos.getJSONObject(i));
                    if (video != null) {
                        tempVideos.add(video);
                    }
                }

                Collections.sort(tempVideos, (movieVideo, movieVideo2) -> {
                    if (movieVideo.type == TRAILER && movieVideo2.type != TRAILER) {
                        return -1;
                    } else if (movieVideo.type != TRAILER && movieVideo2.type == TRAILER) {
                        return +1;
                    } else return 0;
                });

                return tempVideos;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(ArrayList<MovieVideo> videos) {
            super.onPostExecute(videos);
            MovieDetails tempMovieDetails = movie.getValue();
            if (tempMovieDetails != null) {
                tempMovieDetails.videos = videos;
            }
            movie.setValue(tempMovieDetails);
        }
    }

    static class RetrieveReviews extends AsyncTask<String, Void, ArrayList<MovieReview>> {

        @Override
        protected ArrayList<MovieReview> doInBackground(String... strings) {
            if (strings.length == 0) return new ArrayList<>();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(String.format(movieReviewsApiURL, strings[0], apiKey)).build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    return new ArrayList<>();
                }

                String responseBody = response.body() != null ? response.body().string() : null;
                if (responseBody == null) {
                    return new ArrayList<>();
                }

                JSONArray respReviews = JsonUtils.getTMDB_ResponseArray(responseBody);

                ArrayList<MovieReview> tempReviews = new ArrayList<>();
                for (int i = 0; i < respReviews.length(); i++) {
                    MovieReview review = JsonUtils.getReview(respReviews.getJSONObject(i));
                    if (review != null) {
                        tempReviews.add(review);
                    }
                }

                return tempReviews;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(ArrayList<MovieReview> reviews) {
            super.onPostExecute(reviews);
            MovieDetails tempMovieDetails = movie.getValue();
            if (tempMovieDetails != null) {
                tempMovieDetails.reviews = reviews;
            }
            movie.setValue(tempMovieDetails);
        }
    }

}
