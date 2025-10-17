package model;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Customer {
    private int customerId;
    private static int nextCustomerId = 1;
    private String name;
    private String phoneNumber;
    private String email;
    private List<Ticket> ticketList;
    private double balance;

    private int generateCustomerId() {
        return nextCustomerId++;
    }

    public Customer(String name, String phoneNumber, String email) {
        setName(name);
        setPhoneNumber(phoneNumber);
        setEmail(email);
        this.ticketList = new ArrayList<>();
        this.balance = 0.0;
        this.customerId = generateCustomerId();
    }

    public int getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public List<Ticket> getTicketList() { return ticketList; }
    public double getBalance() { return balance; }

    public void setCustomerId(int customerId) {
        if (customerId <= 0) throw new IllegalArgumentException("Invalid customer ID.");
        this.customerId = customerId;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Invalid name.");
        this.name = name.trim();
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() < 7)
            throw new IllegalArgumentException("Invalid phone number.");
        this.phoneNumber = phoneNumber.trim();
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("Invalid email address.");
        this.email = email.trim();
    }

    public void setBalance(double balance) {
        if (balance < 0) throw new IllegalArgumentException("Balance cannot be negative.");
        this.balance = balance;
    }

    public void bookTicket(Flight flight, String seatType) {
        if (flight == null || seatType == null || seatType.isBlank()) {
            System.out.println("Invalid booking details.");
            return;
        }
        if (flight.getPlane().isOperable() && flight.getPlane().getCapacity() > 0) {
            double price = flight.getPlane().getPrice(seatType);
            Ticket ticket = new Ticket(seatType, flight, this);
            ticketList.add(ticket);
            balance += price;
            flight.getPlane().decrementCapacity();
            System.out.println("Ticket booked successfully for flight " + flight.getFlightNumber());
        } else {
            System.out.println("This flight is not available or has no seats left.");
        }
    }

    public void cancelTicket(Ticket ticket) {
        if (ticketList.contains(ticket)) {
            ticketList.remove(ticket);
            balance -= ticket.getPrice();
            System.out.println("Ticket cancelled for flight " + ticket.getFlight().getFlightNumber());
        } else {
            System.out.println("No such ticket found for " + name);
        }
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

    public List<Ticket> getTicketsByStatus(String status) {
        return ticketList.stream()
                .filter(t -> t.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    public double totalSpent() {
        return ticketList.stream().mapToDouble(Ticket::getPrice).sum();
    }

    public boolean hasFlight(String flightNumber) {
        return ticketList.stream()
                .anyMatch(t -> t.getFlight().getFlightNumber().equalsIgnoreCase(flightNumber));
    }

    @Override
    public String toString() {
        return "Customer[" + name + ", ID=" + customerId + ", Tickets=" + ticketList.size() + ", Balance=$" + balance + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Customer)) return false;
        Customer other = (Customer) obj;
        return email.equalsIgnoreCase(other.email);
    }

    @Override
    public int hashCode() {
        return email.toLowerCase().hashCode();
    }
}

