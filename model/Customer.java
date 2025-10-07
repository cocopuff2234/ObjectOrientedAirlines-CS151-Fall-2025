package model;
import java.util.List;

public class Customer{
    private int customerId;
    private String name;
    private String phoneNumber;
    private String email;
    // List of tickets a customer has
    private List<Ticket> ticketList;
    private double balance;

    // constructor
    public Customer{
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.ticketList = ticketList;
        this.balance = 0;
    }

    public void bookTicket(Flight flight, Seat seat, double price){
        // is flight operable
            // is seat available
                ticketList.add(ticket);
                balance += price;
                // decrement amount of seats on flight
        // else ->
        System.out.println("This seat or flight is not available");
    }
    public void cancelTicket(Ticket ticket){
        ticketList.remove(ticket);
    }

    public void seeTickets(){
        // add logic
    }


}

