package model;

public class Ticket {
    private int idTicket;
    private int customer;
    private int representation;
    private float price;
    private int row;
    private int seat;

    public Ticket(int customer, int representation, float price, int row, int seat) {
        this.customer = customer;
        this.representation = representation;
        this.price = price;
        this.row = row;
        this.seat = seat;
    }

    public Ticket(int idTicket, int customer, int representation, float price, int row, int seat) {
        this.idTicket = idTicket;
        this.customer = customer;
        this.representation = representation;
        this.price = price;
        this.row = row;
        this.seat = seat;
    }

    public int getCustomer() {
        return customer;
    }

    public int getRepresentation() {
        return representation;
    }

    public float getPrice() {
        return price;
    }

    public int getRow() {
        return row;
    }

    public int getSeat() {
        return seat;
    }
}
