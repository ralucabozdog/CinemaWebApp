package database.theatre;

import model.Theatre;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TheatreMapper implements RowMapper<Theatre> {

    public Theatre mapRow(ResultSet rs, int i) throws SQLException {

        Theatre theatre = new Theatre(rs.getString("location"));
        return theatre;
    }
}
