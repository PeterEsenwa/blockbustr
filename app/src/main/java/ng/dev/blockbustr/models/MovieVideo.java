package ng.dev.blockbustr.models;

import androidx.annotation.NonNull;

public class MovieVideo {
    public final String name;
    public final VideoType type;
    private String url;

    public MovieVideo(String key, String name, VideoType type) {
        this.setUrl(key);
        this.name = name;
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    private void setUrl(String key) {
        this.url = String.format("https://www.youtube.com/watch?v=%s", key);
    }

    public enum VideoType {
        TRAILER("Trailer"),
        BLOOPERS("Bloopers"),
        RECAP("Recap"),
        BEHIND_THE_SCENES("Behind the Scenes"),
        ;

        private final String type;

        VideoType(String type) {
            this.type = type;
        }

        public static VideoType fromString(String type) throws IllegalArgumentException {
            for (VideoType t : VideoType.values()) {
                if (t.type.equalsIgnoreCase(type)) {
                    return t;
                }
            }

            throw new IllegalArgumentException(String.format("No VideoType %s was found", type));
        }

        @NonNull
        @Override
        public String toString() {
            return type;
        }
    }

}
