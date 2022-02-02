package database.ticket;

import model.Ticket;

import java.util.List;

public interface TicketDAO {
    List<Ticket> getAllTicketsRepresentation(int representation);

    boolean createTicket(Ticket ticket);
}
