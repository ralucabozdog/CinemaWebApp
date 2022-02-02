package database.booking;

import model.Booking;
import model.Movie;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingMapper implements RowMapper<Booking> {

    public Booking mapRow(ResultSet rs, int i) throws SQLException {

        Booking booking = new Booking(rs.getInt("idbooking"), rs.getInt("customer"), rs.getInt("representation"),
                rs.getInt("seatrow"), rs.getInt("seatcol"));
        return booking;
    }
}
