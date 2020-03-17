package ng.dev.blockbustr.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class MovieDetails {

    public MovieDetails() {
    }

    public MovieDetails(Integer id, String originalTitle, String title, String overview, Double popularity, Integer voteCount, Integer voteAverage, String posterPath, String backdropPath, String releaseDate, String originalLanguage) throws ParseException {
        this.id = id;
        this.originalTitle = originalTitle;
        this.title = title;
        this.overview = overview;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        this.releaseDate = dateFormat.parse(releaseDate);
        this.originalLanguage = originalLanguage;
    }

    private Integer id;
    private String originalTitle;
    private String title;
    private String overview;
    private Double popularity;
    private Integer voteCount;
    private Integer voteAverage;
    private String posterPath;
    private String backdropPath;
    private Date releaseDate;
    private String originalLanguage;
//    private List<Integer> genreIds = null;
//    private Boolean video;
//    private Boolean adult;
//    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
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

    public static Comparator<MovieDetails> releaseDateComparator() {
        return (movieOne, movieTwo) -> {
            if (movieOne == null || movieTwo == null || movieOne.getReleaseDate() == null || movieTwo.getReleaseDate() == null) {
                return 0;
            }
            return movieTwo.getReleaseDate().compareTo(movieOne.getReleaseDate());
        };
    }

    public static Comparator<MovieDetails> ratingsComparator() {
        return (movieOne, movieTwo) -> {
            if (movieOne == null || movieTwo == null || movieOne.getVoteAverage() == null || movieTwo.getVoteAverage() == null) {
                return 0;
            }
            return movieTwo.getVoteAverage().compareTo(movieOne.getVoteAverage());
        };
    }

//    static Comparator<MovieDetails> getAttribute2Comparator() {
//        return new Comparator<MovieDetails>() {
//            // compare using attribute 2
//        };
//    }
}
