package database.ticket;

import model.Movie;
import model.Ticket;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TicketMapper implements RowMapper<Ticket> {

    public Ticket mapRow(ResultSet rs, int i) throws SQLException {

        Ticket ticket = new Ticket(rs.getInt("idticket"), rs.getInt("customer"), rs.getInt("representation"),
                rs.getFloat("price"), rs.getInt("seatrow"), rs.getInt("seatcol"));
        return ticket;
    }
}
