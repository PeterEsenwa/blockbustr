package ng.dev.blockbustr.utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ng.dev.blockbustr.models.Genre;

public class GenreListConverter {
    @TypeConverter
    public static List<Genre> toGenreList(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) return new ArrayList<>();
        else {
            Type type = new TypeToken<List<Genre>>() {
            }.getType();
            return new Gson().fromJson(jsonString, type);
        }
    }

    @TypeConverter
    public static String toJSONString(List<Genre> genres) {
        if (genres == null) return null;

        Gson gson = new Gson();
        Type type = new TypeToken<List<Genre>>() {
        }.getType();
        return gson.toJson(genres, type);
    }

}
