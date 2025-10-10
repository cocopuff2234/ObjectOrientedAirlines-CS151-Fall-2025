package model;
import java.util.List;
import java.util.ArrayList;

public class Customer{
    private int customerId;
    private String name;
    private String phoneNumber;
    private String email;
    // List of tickets a customer has
    private List<Ticket> ticketList;
    private double balance;

    // constructors
    public Customer(String name, String phoneNumber, String email){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.ticketList = new ArrayList<>();
        this.balance = 0.0;
    }

    // getters
    public int getCustomerId(){ return customerId; }
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public List<Ticket> getTicketList() {return ticketList; }
    public double getBalance() { return balance; }

    // setters
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public void setName(String name) { this.name = name; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setEmail(String email) { this.email = email; }
    public void setBalance(double balance) { this.balance = balance; }

    public void bookTicket(Flight flight, double price){
        // first check if the flight's plane is operable and there are seats
        // on the plane
        if (flight.getPlane().isOperable() && flight.getPlane().getCapacity() > 0) {
            Ticket ticket = new Ticket("Economy", flight, this, price);
            ticketList.add(ticket);
            balance += price;
            flight.getPlane().decrementCapacity();
            System.out.println("Ticket booked successfully for flight " + flight.getFlightNumber());
        }
        // if the conditions are not satisfied alert the flyer
        else {
            System.out.println("This flight is not available or has no seats left.");
        }
    }

    public void cancelTicket(Ticket ticket){
        // remove the ticket from the list of tickets
        // remove the price of the ticket from the flyer's balance
        ticketList.remove(ticket);
        balance -= ticket.getPrice();
        System.out.println("Ticket cancelled.");
    }

    public void seeTickets() {
        // if the ticket list is empty, alert the flyer there are no
        // tickets in their name
        if (ticketList.isEmpty()) {
            System.out.println("No tickets found for " + name);
        }
        // print the tickets the flyer has under their name,
        // including flight number, seat type, price, and status
        else {
            System.out.println("Tickets for " + name + ":");
            for (Ticket ticket : ticketList) {
                System.out.println("- Flight: " + ticket.getFlight().getFlightNumber() +
                        ", Seat Type: " + ticket.getSeatType() +
                        ", Price: " + ticket.getPrice() +
                        ", Status: " + ticket.getStatus());
            }
        }
    }

}
