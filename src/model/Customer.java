package model;
import java.util.List;
import java.util.ArrayList;

public class Customer{
    private int customerId;
    private static int nextCustomerId = 1;
    private String name;
    private String phoneNumber;
    private String email;
    // List of tickets a customer has
    private List<Ticket> ticketList;
    private double balance;

    private int generateCustomerId() {
        return nextCustomerId++;
    }

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


    public void bookTicket(Flight flight, String seatType){
        // first check if the flight's plane is operable and there are seats
        // on the plane
        if (flight.getPlane().isOperable() && flight.getPlane().getCapacity() > 0) {
            double price = flight.getPlane().getPrice(seatType);
            Ticket ticket = new Ticket(seatType, flight, this);
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

    
// Modify an existing ticket by changing its flight and/or seat type.

public boolean modifyTicket(Ticket oldTicket, Flight newFlight, String newSeatType) {
    if (oldTicket == null || newFlight == null || newSeatType == null || newSeatType.isBlank()) {
        System.out.println("Invalid request: ticket, flight, or seat type is missing.");
        return false;
    }
    if (!ticketList.contains(oldTicket)) {
        System.out.println("Ticket not found in your bookings.");
        return false;
    }
    Flight oldFlight = oldTicket.getFlight();
    String oldSeatType = oldTicket.getSeatType();
    if (oldFlight.equals(newFlight) && oldSeatType.equalsIgnoreCase(newSeatType)) {
        System.out.println("No changes detected: same flight and seat type.");
        System.out.println("If this is an error, please try again");
        return false;
    }
    if (!newFlight.getPlane().isOperable()) {
        System.out.println("Cannot modify: target flight's plane is not operable.");
        System.out.println("If this is an error, please try again");
        return false;
    }
    if (newFlight.getPlane().getCapacity() <= 0) {
        System.out.println("Cannot modify: target flight has no seats available.");
        System.out.println("If this is an error, please try again");
        return false;
    }
    double balanceBefore = balance;
    int ticketsBefore = ticketList.size();
    try {
        double newPrice = newFlight.getPlane().getPrice(newSeatType);
        double oldPrice = oldTicket.getPrice();
        System.out.println("Attempting to book replacement ticket...");
        bookTicket(newFlight, newSeatType);
        if (ticketList.size() != ticketsBefore + 1) {
            throw new IllegalStateException("Unexpected state: new ticket was not added.");
        }
        System.out.println("Cancelling old ticket...");
        cancelTicket(oldTicket);
        double delta = newPrice - oldPrice;
        if (delta == 0) {
            System.out.println("No price difference for this modification.");
        } else if (delta > 0) {
            System.out.println("Modification complete. Additional charge: " + delta);
        } else {
            System.out.println("Modification complete. Refund credited: " + delta);
        }
        System.out.println("Ticket successfully modified: " + "from flight " + oldFlight.getFlightNumber() + " (" + oldSeatType + ") to flight " + newFlight.getFlightNumber() + " (" + newSeatType + ").");
        System.out.println("Ticket removed details: " + oldTicket.toString());
        return true;
    } catch (Exception e) {
        System.out.println("Modification failed: " + e.getMessage());
        while (ticketList.size() > ticketsBefore) {
            Ticket last = ticketList.get(ticketList.size() - 1);
            try {
                cancelTicket(last);
            } catch (Exception ignored) {}
        }
        balance = balanceBefore;
        System.out.println("Your original ticket remains intact.");
        System.out.println("if this is a mistake, please try again.");
        return false;
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
