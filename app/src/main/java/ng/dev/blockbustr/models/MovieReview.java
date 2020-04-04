package ng.dev.blockbustr.models;

public class MovieReview {
    public final String author;
    public final String content;
    public final String id;
    public final String url;

    public MovieReview(String author, String content, String id, String url) {
        this.author = author;
        this.content = content;
        this.id = id;
        this.url = url;
    }
}
