package database.screen;

import model.Movie;
import model.Screen;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ScreenMapper implements RowMapper<Screen> {

    public Screen mapRow(ResultSet rs, int i) throws SQLException {

        Screen screen = new Screen(rs.getString("screenname"),
                rs.getInt("theatre"), rs.getInt("nbrows"),
                rs.getInt("nbchairsrow"));
        return screen;
    }
}
