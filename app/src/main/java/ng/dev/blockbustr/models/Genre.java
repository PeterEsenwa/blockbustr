package ng.dev.blockbustr.models;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Genre {
    public static ArrayList<Genre> allGenres = new ArrayList<>(
            Arrays.asList(
                    new Genre(28, "Action"),
                    new Genre(12, "Adventure"),
                    new Genre(16, "Animation"),
                    new Genre(35, "Comedy"),
                    new Genre(80, "Crime"),
                    new Genre(99, "Documentary"),
                    new Genre(18, "Drama"),
                    new Genre(10751, "Family"),
                    new Genre(14, "Fantasy"),
                    new Genre(36, "History"),
                    new Genre(27, "Horror"),
                    new Genre(10402, "Music"),
                    new Genre(9648, "Mystery"),
                    new Genre(10749, "Romance"),
                    new Genre(878, "Science Fiction"),
                    new Genre(10770, "TV Movie"),
                    new Genre(53, "Thriller"),
                    new Genre(10752, "War"),
                    new Genre(37, "Western")
            )
    );
    private Integer id;
    private String name;

    public Genre(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Genre) && !(obj instanceof Integer)) {
            return false;
        }
        if (obj instanceof Genre) {
            return Objects.equals(((Genre) obj).id, this.id);
        }
        return this.id == obj;
    }
}
