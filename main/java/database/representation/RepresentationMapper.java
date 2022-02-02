package database.representation;

import model.Representation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RepresentationMapper implements RowMapper<Representation> {

    public Representation mapRow(ResultSet rs, int i) throws SQLException {

        Representation representation = new Representation(rs.getInt("movie"),
                rs.getInt("dayofweek"), rs.getString("starttime"),
                rs.getInt("screen"), rs.getString("typeofplay"));
        return representation;
    }
}