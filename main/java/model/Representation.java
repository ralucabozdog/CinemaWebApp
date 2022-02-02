package model;

import database.movie.MovieDAO;
import database.screen.ScreenDAO;
import database.theatre.TheatreDAO;

public class Representation {
    private Integer movie;
    private Integer dayOfWeek;
    private String startTime;
    private Integer screen;
    private String typeOfPlay;

    public Representation(Integer movie, Integer dayOfWeek, String startTime, Integer screen, String typeOfPlay) {
        this.movie = movie;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.screen = screen;
        this.typeOfPlay = typeOfPlay;
    }

    public Integer getMovie() {
        return movie;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public Integer getScreen() {
        return screen;
    }

    public String getTypeOfPlay() {
        return typeOfPlay;
    }

    public String getMovieTitle(MovieDAO movieDAO, int movieId){
        return movieDAO.getMovieById(movieId).getTitle();
    }

    public String getScreenName(ScreenDAO screenDAO, int screenId){
        return screenDAO.getScreenById(screenId).getScreenName();
    }

    public String getCinemaLocation(TheatreDAO theatreDAO, ScreenDAO screenDAO, int screenId){
        return theatreDAO.getTheatreById(screenDAO.getScreenById(screenId).getTheatre()).getLocation();
    }
}
