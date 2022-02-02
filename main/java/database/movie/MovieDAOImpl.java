package database.movie;

import model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class MovieDAOImpl implements MovieDAO {

    JdbcTemplate jdbcTemplate;

    private final String SQL_FIND_MOVIE = "select * from movie where idmovie = ?";
    private final String SQL_FIND_MOVIE_BY_TITLE = "select * from movie where title = ?";
    private final String SQL_FIND_MOVIE_ID_BY_TITLE = "select idmovie from movie where title = ?";
    private final String SQL_DELETE_MOVIE = "delete from movie where title = ?";
    private final String SQL_UPDATE_MOVIE = "update movie set title = ?, poster = ?, rating  = ?, descript = ?, cast = ?, director = ?, writer = ?, genre = ?, trailerurl = ?, duration = ?, classification = ? where idmovie = ?";
    private final String SQL_GET_ALL = "select * from movie";
    private final String SQL_INSERT_MOVIE = "insert into movie(title, poster, rating, descript, cast, director, writer, genre, trailerurl, duration, classification) values(?,?,?,?,?,?,?,?,?,?,?)";

    @Autowired
    public MovieDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Movie getMovieById(Integer id) {
        return jdbcTemplate.queryForObject(SQL_FIND_MOVIE, new Object[] { id }, new MovieMapper());
    }

    public Movie getMovieByTitle(String movieTitle) {
        return jdbcTemplate.queryForObject(SQL_FIND_MOVIE_BY_TITLE, new Object[] { movieTitle }, new MovieMapper());
    }

    public Integer getMovieIdByTitle(String movieTitle){
        return jdbcTemplate.queryForObject(SQL_FIND_MOVIE_ID_BY_TITLE, new Object[] { movieTitle }, Integer.class);
    }

    public List<Movie> getAllMovies() {
        return jdbcTemplate.query(SQL_GET_ALL, new MovieMapper());
    }

    public boolean deleteMovie(Movie movie) {
        jdbcTemplate.update("SET FOREIGN_KEY_CHECKS=0;");
        return jdbcTemplate.update(SQL_DELETE_MOVIE, movie.getTitle()) > 0;
    }

    public boolean updateMovie(Movie movie, int movieId) {
        return jdbcTemplate.update(SQL_UPDATE_MOVIE, movie.getTitle(), movie.getPoster(), movie.getRating(), movie.getDescription(), movie.getCast(), movie.getDirector(), movie.getWriter(), movie.getGenre(), movie.getTrailerURL(), movie.getDuration(), movie.getClassification(),
                movieId) > 0;
    }

    public boolean createMovie(Movie movie) {
        return jdbcTemplate.update(SQL_INSERT_MOVIE,movie.getTitle(), movie.getPoster(), movie.getRating(), movie.getDescription(),
                movie.getCast(), movie.getDirector(), movie.getWriter(), movie.getGenre(), movie.getTrailerURL(), movie.getDuration(), movie.getClassification()) > 0;
    }
}
