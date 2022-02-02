package database.ticket;

import database.movie.MovieDAO;
import database.movie.MovieMapper;
import model.Movie;
import model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class TicketDAOImpl implements TicketDAO {

    JdbcTemplate jdbcTemplate;

    private final String SQL_GET_ALL_REPRESENTATION = "select * from ticket where representation = ?";
    private final String SQL_INSERT_TICKET = "insert into ticket(customer, representation, seatrow, seatcol, price) values(?,?,?,?,?)";

    @Autowired
    public TicketDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Ticket> getAllTicketsRepresentation(int representation) {
        return jdbcTemplate.query(SQL_GET_ALL_REPRESENTATION, new Object[]{representation} ,new TicketMapper());
    }

    public boolean createTicket(Ticket ticket){
        return jdbcTemplate.update(SQL_INSERT_TICKET, ticket.getCustomer(), ticket.getRepresentation(), ticket.getRow(), ticket.getSeat(), ticket.getPrice()) > 0;
    }

}

