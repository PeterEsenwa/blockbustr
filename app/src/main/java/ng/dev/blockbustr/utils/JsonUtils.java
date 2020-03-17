package ng.dev.blockbustr.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
            movieDetails.setPosterPath(String.format("https://image.tmdb.org/t/p/w500%s", movieObject.getString("poster_path")));

            movieDetails.setReleaseDate(movieObject.getString("release_date"));

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
