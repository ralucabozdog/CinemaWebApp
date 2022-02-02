package database.theatre;

import database.movie.MovieDAO;
import database.movie.MovieMapper;
import model.Movie;
import model.Theatre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class TheatreDAOImpl implements TheatreDAO {

    JdbcTemplate jdbcTemplate;

    private final String SQL_FIND_THEATRE = "select * from theatre where idtheatre = ?";
    private final String SQL_FIND_THEATRE_BY_LOCATION = "select * from theatre where location = ?";
    private final String SQL_GET_ALL = "select * from theatre";
    private final String SQL_GET_ID = "select idtheatre from theatre where location = ?";

    @Autowired
    public TheatreDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Theatre getTheatreById(Integer id) {
        return jdbcTemplate.queryForObject(SQL_FIND_THEATRE, new Object[] { id }, new TheatreMapper());
    }

    public Theatre getTheatreByLocation(String theatreLocation) {
        return jdbcTemplate.queryForObject(SQL_FIND_THEATRE_BY_LOCATION, new Object[] { theatreLocation }, new TheatreMapper());
    }

    public List<Theatre> getAllTheatres() {
        return jdbcTemplate.query(SQL_GET_ALL, new TheatreMapper());
    }

    public Integer getId(Theatre theatre){
        return jdbcTemplate.queryForObject(SQL_GET_ID, new Object[] { theatre.getLocation() }, Integer.class);
    }
}
