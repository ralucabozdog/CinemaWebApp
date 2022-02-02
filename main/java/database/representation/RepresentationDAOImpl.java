package database.representation;

import database.movie.MovieDAO;
import database.movie.MovieMapper;
import model.Movie;
import model.Representation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RepresentationDAOImpl implements RepresentationDAO {

    JdbcTemplate jdbcTemplate;

    private final String SQL_FIND_REPRESENTATION = "select * from representation where idrepresentation = ?";
    private final String SQL_DELETE_REPRESENTATION = "delete from representation where movie = ? and dayofweek = ? and starttime = ? and screen = ? and typeofplay = ?";
    private final String SQL_UPDATE_REPRESENTATION = "update representation set movie = ?, dayofweek = ?, starttime  = ?, screen = ?, typeofplay = ? where idrepresentation = ?";
    private final String SQL_GET_ALL_MOVIE_THEATRE = "select * from representation where movie = ? and (select theatre from screen where idscreen = representation.screen) = ?";
    private final String SQL_GET_ALL_THEATRE_DAY = "select * from representation where (select theatre from screen where idscreen = representation.screen) = ? and dayofweek = ?";
    private final String SQL_GET_ALL = "select * from representation";
    private final String SQL_INSERT_REPRESENTATION= "insert into representation(movie, dayofweek, starttime, screen, typeofplay) values(?,?,?,?,?)";
    private final String SQL_GET_ID = "select idrepresentation from representation where movie = ? and dayofweek = ? and starttime = ? and screen = ? and typeofplay = ?";

    @Autowired
    public RepresentationDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Representation getRepresentationById(Integer id) {
        return jdbcTemplate.queryForObject(SQL_FIND_REPRESENTATION, new Object[] { id }, new RepresentationMapper());
    }

    public List<Representation> getAllRepresentationsOfMovieInTheatre(int movie, int theatre) {

        return jdbcTemplate.query(SQL_GET_ALL_MOVIE_THEATRE, new Object[]{movie, theatre}, new RepresentationMapper());
    }

    public List<Representation> getAllRepresentationsInTheatreForDay(int theatre, int dayOfWeek){
        return jdbcTemplate.query(SQL_GET_ALL_THEATRE_DAY, new Object[]{theatre, dayOfWeek}, new RepresentationMapper());
    }

    public List<Representation> getAllRepresentations(){
        return jdbcTemplate.query(SQL_GET_ALL, new RepresentationMapper());
    }

    public boolean insertRepresentation(Representation representation){
        return jdbcTemplate.update(SQL_INSERT_REPRESENTATION, representation.getMovie(), representation.getDayOfWeek(), representation.getStartTime(),
                representation.getScreen(), representation.getTypeOfPlay()) > 0;
    }

    public boolean deleteRepresentation(Representation representation){
        return jdbcTemplate.update(SQL_DELETE_REPRESENTATION, representation.getMovie(), representation.getDayOfWeek(), representation.getStartTime(),
                representation.getScreen(), representation.getTypeOfPlay()) > 0;
    }

    public boolean updateRepresentation(Representation representation, int representationId){
        return jdbcTemplate.update(SQL_UPDATE_REPRESENTATION, representation.getMovie(), representation.getDayOfWeek(), representation.getStartTime(),
                representation.getScreen(), representation.getTypeOfPlay(), representationId) > 0;
    }

    public Integer getId(Representation representation){
        return jdbcTemplate.queryForObject(SQL_GET_ID, new Object[]{representation.getMovie(), representation.getDayOfWeek(), representation.getStartTime(),
        representation.getScreen(), representation.getTypeOfPlay()}, Integer.class);
    }
}
