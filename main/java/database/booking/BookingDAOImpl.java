package database.booking;


import database.ticket.TicketDAO;
import database.ticket.TicketMapper;
import model.Booking;
import model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class BookingDAOImpl implements BookingDAO {

    JdbcTemplate jdbcTemplate;

    private final String SQL_GET_ALL_REPRESENTATION = "select * from booking where representation = ?";
    private final String SQL_INSERT_BOOKING = "insert into booking(customer, representation, seatrow, seatcol) values(?,?,?,?)";

    @Autowired
    public BookingDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Booking> getAllBookingsRepresentation(int representation) {
        return jdbcTemplate.query(SQL_GET_ALL_REPRESENTATION, new Object[]{representation} ,new BookingMapper());
    }

    public boolean createBooking(Booking booking){
        return jdbcTemplate.update(SQL_INSERT_BOOKING, booking.getCustomer(), booking.getRepresentation(), booking.getRow(), booking.getSeat()) > 0;
    }

}
