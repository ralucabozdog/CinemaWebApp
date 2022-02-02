package database.movie;

import model.Movie;

import java.util.List;


public interface MovieDAO {
    Movie getMovieById(Integer id);

    Movie getMovieByTitle(String movieTitle);

    Integer getMovieIdByTitle(String movieTitle);

    List<Movie> getAllMovies();

    boolean deleteMovie(Movie movie);

    boolean updateMovie(Movie movie, int movieId);

    boolean createMovie(Movie movie);
}
