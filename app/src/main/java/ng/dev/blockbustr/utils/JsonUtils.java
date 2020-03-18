package ng.dev.blockbustr.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

import ng.dev.blockbustr.models.MovieDetails;

public class JsonUtils {

    public static MovieDetails parseMovieDetails(JSONObject movieObject) {

        MovieDetails movieDetails = new MovieDetails();

        try {

            movieDetails.setId(movieObject.getInt("id"));
            movieDetails.setVoteAverage(movieObject.getInt("vote_average"));
            movieDetails.setVoteCount(movieObject.getInt("vote_count"));
            movieDetails.setPopularity(movieObject.getDouble("popularity"));

            movieDetails.setTitle(movieObject.getString("title"));
            movieDetails.setOriginalTitle(movieObject.getString("original_language"));
            movieDetails.setOverview(movieObject.getString("overview"));
            movieDetails.setPosterPath("https://image.tmdb.org/t/p/w500"
                    + movieObject.getString("poster_path"));
            movieDetails.setBackdropPath("https://image.tmdb.org/t/p/original"
                    + movieObject.getString("backdrop_path"));

            movieDetails.setReleaseDate(movieObject.getString("release_date"));

            JSONArray genreIdsJSON = movieObject.getJSONArray("genre_ids");
            ArrayList<Integer> genreIds = new ArrayList<>();
            for (int i = 0; i < genreIdsJSON.length(); i++) {
                genreIds.add(genreIdsJSON.getInt(i));
            }
            movieDetails.setGenres(genreIds);

        } catch (JSONException | ParseException e) {
            Log.d(JsonUtils.class.getSimpleName(), Objects.requireNonNull(e.getMessage()));
        }

        return movieDetails;
    }

    public static JSONArray getMoviesArray(String json) throws JSONException {

        JSONObject responseObject = new JSONObject(json);
        return responseObject.getJSONArray("results");
    }
}
