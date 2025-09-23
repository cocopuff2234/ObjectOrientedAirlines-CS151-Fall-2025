package model;
public class Ticket{
    private int ticketId;
    private double price;
    private Seat seat;
    private Flight flight;
    private Customer customer;
    // cancelled ticket, in-use, booked etc. enumerated variable fs
    private String status;

    public Ticket(Seat seat, Flight flight, Customer customer, double price){
        this.seat = seat;
        this.flight = flight;
        this.customer = customer;
        this.price = price;
        this.status = "Booked";
    }

    public Seat getSeat(){
        return seat;
    }

    public Flight getFlight(){
        return flight;
    }

    public Customer getCustomer(){
        return Customer;
    }

    public void purchase(){
        // seat available?
        // reservse seat
        // set seat to taken
    }

    public void cancel(){
        // make seat empty
        // change from booked to cancelled
    }

    public void findNewSeat(){
        // is seat available
        // new seat = seat

    }
}