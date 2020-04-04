package ng.dev.blockbustr.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Entity(tableName = "favourites")
public class MovieDetails {
    private final List<Genre> genres = new ArrayList<>();
    @Ignore
    public ArrayList<MovieVideo> videos = new ArrayList<>();

    @Ignore
    public ArrayList<MovieReview> reviews = new ArrayList<>();
    @Ignore
    public boolean previouslyClicked;
    private String title;
    private String overview;
    private Double popularity;
    @PrimaryKey
    private Integer id;
    @ColumnInfo(name = "original_title")
    private String originalTitle;
    @ColumnInfo(name = "vote_count")
    private Integer voteCount;
    @ColumnInfo(name = "vote_average")
    private Integer voteAverage;
    @ColumnInfo(name = "poster_path")
    private String posterPath;
    @ColumnInfo(name = "backdrop_path")
    private String backdropPath;
    @ColumnInfo(name = "release_date")
    private Date releaseDate;

    public MovieDetails() {
    }

    public static Comparator<MovieDetails> releaseDateComparator() {
        return (movieOne, movieTwo) -> {
            if (movieOne == null || movieTwo == null || movieOne.getReleaseDate() == null || movieTwo.getReleaseDate() == null) {
                return 0;
            }
            return movieTwo.getReleaseDate().compareTo(movieOne.getReleaseDate());
        };
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Integer voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        this.releaseDate = dateFormat.parse(releaseDate);
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres.addAll(genres);
    }

    public void setGenresInt(List<Integer> genreIds) {
        for (Integer genreId : genreIds) {
            Genre g = Genre.allGenres.get(Genre.allGenres.indexOf(new Genre(genreId, "")));
            this.genres.add(g);
        }
    }
}
