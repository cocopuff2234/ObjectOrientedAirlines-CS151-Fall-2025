package model;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class AirportUI {
    private final List<Flight> flights;
    private final List<Airport> airports;
    private final Scanner scanner = new Scanner(System.in);
    private Customer currentCustomer;

    public AirportUI(List<Flight> flights, List<Airport> airports) {
        this.flights = flights;
        this.airports = airports;
    }

    public void start() {
        System.out.println("Welcome to OOP Airlines");
        System.out.println("-----------------------------------------------");
        createOrLoginCustomer();

        boolean exit = false;
        while (!exit) {
            System.out.println("\nPlease select an action:");
            System.out.println("1. Book a Flight");
            System.out.println("2. Cancel a Ticket");
            System.out.println("3. See My Tickets");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> bookFlight();
                case "2" -> cancelTicket();
                case "3" -> currentCustomer.seeTickets();
                case "4" -> exit = true;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
        System.out.println("Goodbye!");
    }

    private void createOrLoginCustomer() {
        System.out.println("Enter your name:");
        String name = scanner.nextLine().trim();
        System.out.println("Enter your phone number:");
        String phone = scanner.nextLine().trim();
        System.out.println("Enter your email:");
        String email = scanner.nextLine().trim();

        currentCustomer = new Customer(name, phone, email);
        System.out.println("Welcome, " + name + "!");
    }

    private void bookFlight() {
        System.out.println("\nAvailable flights:");
        for (int i = 0; i < flights.size(); i++) {
            Flight f = flights.get(i);
            System.out.printf("%d. %s: %s → %s at %s\n", i + 1, f.getFlightNumber(),
                    f.getOrigin(), f.getDestination(), f.getDepartureUTC());
        }

        System.out.print("Select a flight to book (number): ");
        int idx;
        try {
            idx = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (idx < 0 || idx >= flights.size()) {
            System.out.println("Invalid flight selection.");
            return;
        }

        Flight flight = flights.get(idx);

        System.out.print("Enter seat type (Economy/First): ");
        String seatType = scanner.nextLine().trim();

        // Assign gate if possible
        Optional<Airport> originAirportOpt = airports.stream()
                .filter(a -> a.getAirportName().equals(flight.getOrigin()))
                .findFirst();

        originAirportOpt.ifPresent(originAirport -> 
            originAirport.findAvailableGate(flight.getDepartureUTC())
                .ifPresentOrElse(
                    gate -> {
                        originAirport.assignGate(flight, gate);
                        System.out.println("Assigned gate " + gate + " for this flight.");
                    },
                    () -> System.out.println("No available gates for this flight. Proceeding without gate assignment.")
                )
        );

        Ticket ticket = new Ticket(seatType, flight, currentCustomer);
        ticket.purchase(); // Uses your Customer & Plane methods
    }

    private void cancelTicket() {
        List<Ticket> tickets = currentCustomer.getTicketList();
        if (tickets.isEmpty()) {
            System.out.println("You have no tickets to cancel.");
            return;
        }

        System.out.println("\nYour tickets:");
        for (int i = 0; i < tickets.size(); i++) {
            Ticket t = tickets.get(i);
            System.out.printf("%d. Flight: %s | Seat: %s | Status: %s\n",
                    i + 1, t.getFlight().getFlightNumber(), t.getSeatType(), t.getStatus());
        }

        System.out.print("Select ticket to cancel (number): ");
        int idx;
        try {
            idx = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (idx < 0 || idx >= tickets.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Ticket ticket = tickets.get(idx);
        ticket.cancel(); // Uses your Ticket and Customer cancel logic
    }
}
