package model;

import java.time.LocalTime;

public class Movie {
    private String title;
    private String poster;
    private Float rating;
    private String descript;
    private String cast;
    private String director;
    private String writer;
    private String genre;
    private String trailerURL;
    private String duration;
    private String classification;

    public Movie(String title, String poster, Float rating, String description, String cast, String director, String writer, String genre, String trailerURL, String duration, String classification) {
        this.title = title;
        this.poster = poster;
        this.rating = rating;
        this.descript = description;
        this.cast = cast;
        this.director = director;
        this.writer = writer;
        this.genre = genre;
        this.trailerURL = trailerURL;
        this.duration = duration;
        this.classification = classification;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public Float getRating() {
        return rating;
    }

    public String getDescription() {
        return descript;
    }

    public String getCast() {
        return cast;
    }

    public String getDirector() {
        return director;
    }

    public String getWriter() {
        return writer;
    }

    public String getGenre() {
        return genre;
    }

    public String getTrailerURL() {
        return trailerURL;
    }

    public String getDuration() {
        return duration;
    }

    public String getClassification() {
        return classification;
    }
}

