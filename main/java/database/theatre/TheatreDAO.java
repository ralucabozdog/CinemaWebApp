package database.theatre;

import model.Movie;
import model.Theatre;

import java.util.List;

public interface TheatreDAO {
    Theatre getTheatreById(Integer id);

    Theatre getTheatreByLocation(String theatreLocation);

    List<Theatre> getAllTheatres();

    Integer getId(Theatre theatre);

    //boolean deleteMovie(Movie movie);

    //boolean updateMovie(Movie movie);

    //boolean createMovie(Movie movie);
}
