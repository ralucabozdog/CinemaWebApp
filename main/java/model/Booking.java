package model;

public class Booking {
    private int idBooking;
    private int customer;
    private int representation;
    private int row;
    private int seat;

    public Booking(int customer, int representation, int row, int seat) {
        this.customer = customer;
        this.representation = representation;
        this.row = row;
        this.seat = seat;
    }

    public Booking(int idBooking, int customer, int representation, int row, int seat) {
        this.idBooking = idBooking;
        this.customer = customer;
        this.representation = representation;
        this.row = row;
        this.seat = seat;
    }

    public int getIdBooking() {
        return idBooking;
    }

    public int getCustomer() {
        return customer;
    }

    public int getRepresentation() {
        return representation;
    }

    public int getRow() {
        return row;
    }

    public int getSeat() {
        return seat;
    }
}
