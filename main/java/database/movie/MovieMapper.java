package database.movie;

import model.Movie;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieMapper implements RowMapper<Movie> {

    public Movie mapRow(ResultSet rs, int i) throws SQLException {

        Movie movie = new Movie(rs.getString("title"),
                rs.getString("poster"), rs.getFloat("rating"),
                rs.getString("descript"), rs.getString("cast"),
                rs.getString("director"), rs.getString("writer"),
                rs.getString("genre"), rs.getString("trailerURL"),
                rs.getString("duration"), rs.getString("classification"));
        return movie;
    }
}
