package database.screen;

import database.movie.MovieDAO;
import database.movie.MovieMapper;
import model.Movie;
import model.Screen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class ScreenDAOImpl implements ScreenDAO {

    JdbcTemplate jdbcTemplate;

    private final String SQL_FIND_SCREEN = "select * from screen where idscreen = ?";
    private final String SQL_GET_SCREEN_ID = "select idscreen from screen where theatre = (select idtheatre from theatre where location = ?) and screenname = ?";
    private final String SQL_DELETE_MOVIE = "delete from movie where idmovie = ?";
    private final String SQL_UPDATE_MOVIE = "update movie set title = ?, poster = ?, rating  = ?, descript = ?, cast = ?, director = ?, writer = ?, genre = ?, trailerurl = ?, duration = ?, classification = ? where idmovie = ?";
    private final String SQL_GET_ALL = "select * from movie";
    private final String SQL_INSERT_MOVIE = "insert into movie(title, poster, rating, descript, cast, director, writer, genre, trailerurl, duration, classification) values(?,?,?,?,?,?,?,?,?,?,?)";

    @Autowired
    public ScreenDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Screen getScreenById(Integer id) {
        return jdbcTemplate.queryForObject(SQL_FIND_SCREEN, new Object[] { id }, new ScreenMapper());
    }

    public Integer getIdOfScreen(String cinema, String screenName){
        return jdbcTemplate.queryForObject(SQL_GET_SCREEN_ID, new Object[] { cinema, screenName }, Integer.class);
    }

    /*public Movie getMovieByTitle(String movieTitle) {
        return jdbcTemplate.queryForObject(SQL_FIND_MOVIE_BY_TITLE, new Object[] { movieTitle }, new MovieMapper());
    }

    public List<Movie> getAllMovies() {
        return jdbcTemplate.query(SQL_GET_ALL, new MovieMapper());
    }

    public boolean deleteMovie(Movie movie, int movieId) {
        return jdbcTemplate.update(SQL_DELETE_MOVIE, movieId) > 0;
    }

    public boolean updatePerson(Movie movie, int movieId) {
        return jdbcTemplate.update(SQL_UPDATE_MOVIE, movie.getTitle(), movie.getPoster(), movie.getRating(), movie.getDescription(), movie.getCast(), movie.getDirector(), movie.getWriter(), movie.getGenre(), movie.getTrailerURL(), movie.getDuration(), movie.getClassification(),
                movieId) > 0;
    }

    /*public boolean createPerson(Movie person) {
        return jdbcTemplate.update(SQL_INSERT_PERSON, person.getId(), person.getFirstName(), person.getLastName(),
                person.getAge()) > 0;
    }*/
}
