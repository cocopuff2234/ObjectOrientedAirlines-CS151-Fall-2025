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

    // constructor
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
        if (flight.getPlane().isOperable() && flight.getPlane().getCapacity() > 0) {
            Ticket ticket = new Ticket("Economy", flight, this, price);
            ticketList.add(ticket);
            balance += price;
            flight.getPlane().decrementCapacity();
            System.out.println("Ticket booked successfully for flight " + flight.getFlightNumber());
        }
        else {
            System.out.println("This flight is not available or has no seats left.");
        }
    }

    public void cancelTicket(Ticket ticket){
        ticketList.remove(ticket);
        balance -= ticket.getPrice();
        System.out.println("Ticket cancelled.");
    }

    public void seeTickets() {
        if (ticketList.isEmpty()) {
            System.out.println("No tickets found for " + name);
        } else {
            System.out.println("Tickets for " + name + ":");
            for (Ticket ticket : ticketList) {
                System.out.println("- Flight: " + ticket.getFlight().getFlightNumber() +
                        ", Seat Type: " + ticket.getSeatType() +
                        ", Price: " + ticket.getPrice() +
                        ", Status: " + ticket.getStatus());
            }
        }

}
