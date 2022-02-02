package database.booking;

import model.Booking;
import model.Ticket;

import java.util.List;

public interface BookingDAO {
    List<Booking> getAllBookingsRepresentation(int representation);

    boolean createBooking(Booking booking);
}
