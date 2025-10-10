package model;

import model.enums.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AirportUI {
    private List<Flight> flights;
    private List<Customer> customers = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public AirportUI(List<Flight> flights) {
        this.flights = flights != null ? flights : new ArrayList<>();
    }

    public void start() {
        while (true) {
            System.out.println();
            System.out.println("Welcome to OOP Airlines");
            System.out.println("-----------------------------------------------");
            System.out.println("Please select what you would like to do today:");
            System.out.println("1. Book a Flight");
            System.out.println("2. Cancel a Flight");
            System.out.println("3. Check Flight Status");
            System.out.println("4. See Available Flights");
            System.out.println("5. See Existing Reservations");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": bookFlight(); break;
                case "2": cancelFlight(); break;
                case "3": checkFlightStatus(); break;
                case "4": seeAvailableFlights(); break;
                case "5": seeReservations(); break;
                case "6": System.out.println("Goodbye!"); return;
                default: System.out.println("Invalid choice, try again."); break;
            }
        }
    }

    // Booking flight
    private void bookFlight() {
        try {
            System.out.print("Enter departure date (YYYY-MM-DD): ");
            String dateStr = scanner.nextLine().trim();
            LocalDate date = LocalDate.parse(dateStr);

            System.out.print("Enter departing airport (code/name): ");
            String origin = scanner.nextLine().trim();

            System.out.print("Enter arrival airport (code/name): ");
            String destination = scanner.nextLine().trim();

            List<Flight> matches = new ArrayList<>();
            for (Flight f : flights) {
                if (f.getOrigin().equalsIgnoreCase(origin)
                        && f.getDestination().equalsIgnoreCase(destination)
                        && f.getDepartureUTC().toLocalDate().equals(date)) {
                    matches.add(f);
                }
            }

            if (matches.isEmpty()) {
                System.out.println("No flights found for that date and route.");
                return;
            }

            System.out.println("Available flights:");
            for (int i = 0; i < matches.size(); i++) {
                Flight f = matches.get(i);
                System.out.printf("%d) %s | Departs: %s | Arrives: %s | Seats left: %d\n",
                        i + 1, f.getFlightNumber(), f.getDepartureUTC(), f.getArrivalUTC(), f.getAvailableSeats());
            }

            System.out.print("Select flight number to book (or 0 to cancel): ");
            int sel = Integer.parseInt(scanner.nextLine().trim());
            if (sel <= 0 || sel > matches.size()) return;

            Flight flight = matches.get(sel - 1);

            if (!flight.getPlane().isOperable()) {
                System.out.println("This flight's plane is not operable at the moment. Cannot book.");
                return;
            }
            if (!flight.hasRequiredCrew()) {
                System.out.println("This flight does not have the required crew assigned. Cannot book.");
                return;
            }
            if (!flight.hasSeats("any")) { // seatType isn't used by getAvailableSeats; just check >0
                System.out.println("No seats available on this flight.");
                return;
            }

            //Input Customer
            System.out.print("Enter your name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter your phone number: ");
            String phone = scanner.nextLine().trim();
            System.out.print("Enter your email: ");
            String email = scanner.nextLine().trim();

            Customer customer = findOrCreateCustomer(name, phone, email);

            // Seat Selection
            System.out.print("Enter seat class (Economy / Business / First): ");
            String seatClass = scanner.nextLine().trim();
            double price = priceForClass(seatClass);

            // Final availability check
            Ticket ticket = new Ticket(seatClass, flight, customer, price);
            try {
                // throw if full
                flight.addTicket(ticket);

                // add to customer and update balance
                customer.getTicketList().add(ticket);
                customer.setBalance(customer.getBalance() + price);
                ticket.setStatus(ReservationStatus.CONFIRMED);

                System.out.println("Booking confirmed! Ticket details:");
                System.out.println("- Flight: " + flight.getFlightNumber());
                System.out.println("- Seat class: " + ticket.getSeatType());
                System.out.printf("- Price charged: $%.2f\n", price);
            } catch (IllegalArgumentException e) {
                System.out.println("Failed to reserve seat: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Error during booking: " + e.getMessage());
        }
    }

    //Cancel
    private void cancelFlight() {
        Customer customer = findCustomerPrompt();
        if (customer == null) {
            System.out.println("No customer found by that info.");
            return;
        }
        List<Ticket> tickets = customer.getTicketList();
        if (tickets.isEmpty()) {
            System.out.println("No tickets found for " + customer.getName());
            return;
        }

        System.out.println("Your booked tickets:");
        for (int i = 0; i < tickets.size(); i++) {
            Ticket t = tickets.get(i);
            System.out.printf("%d) Flight %s | Seat: %s | Price: $%.2f | Status: %s\n",
                    i + 1, t.getFlight().getFlightNumber(), t.getSeatType(), t.getPrice(), t.getStatus());
        }

        System.out.print("Select ticket to cancel (or 0 to exit): ");
        int sel = Integer.parseInt(scanner.nextLine().trim());
        if (sel <= 0 || sel > tickets.size()) return;

        Ticket t = tickets.get(sel - 1);
        t.cancel();
        Flight flight = t.getFlight();
        flight.removeTicket(t);

        System.out.println("Ticket canceled successfully.");
    }

    //Status
    private void checkFlightStatus() {
        System.out.print("Enter flight number: ");
        String number = scanner.nextLine().trim();

        Flight flight = null;
        for (Flight f : flights) {
            if (f.getFlightNumber().equalsIgnoreCase(number)) {
                flight = f;
                break;
            }
        }

        if (flight == null) {
            System.out.println("Flight not found.");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        String status = now.isAfter(flight.getDepartureUTC()) ? "Departed" : "Scheduled";
        System.out.println("Flight " + flight.getFlightNumber() + " | " + flight.getOrigin() + " -> " + flight.getDestination());
        System.out.println("Departs: " + flight.getDepartureUTC() + " | Arrives: " + flight.getArrivalUTC() + " | Status: " + status);
        System.out.println("Available seats: " + flight.getAvailableSeats());
        System.out.println("Has required crew assigned: " + flight.hasRequiredCrew());
    }

    private void seeAvailableFlights() {
        System.out.println("All flights:");
        for (Flight f : flights) {
            System.out.printf("%s | %s | %s -> %s | Departs: %s | Arrives: %s | Seats left: %d\n",
                    f.getFlightNumber(), f.getAirline(), f.getOrigin(), f.getDestination(),
                    f.getDepartureUTC(), f.getArrivalUTC(), f.getAvailableSeats());
        }
    }

    //Reservations 
    private void seeReservations() {
        Customer customer = findCustomerPrompt();
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }
        List<Ticket> tickets = customer.getTicketList();
        if (tickets.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }
        System.out.println("Reservations for " + customer.getName() + ":");
        for (Ticket t : tickets) {
            System.out.printf("Flight %s | %s -> %s | Seat: %s | Price: $%.2f | Status: %s\n",
                    t.getFlight().getFlightNumber(), t.getFlight().getOrigin(), t.getFlight().getDestination(),
                    t.getSeatType(), t.getPrice(), t.getStatus());
        }
    }


    private Customer findOrCreateCustomer(String name, String phone, String email) {
        for (Customer c : customers) {
            if (c.getName().equalsIgnoreCase(name)
                    && c.getPhoneNumber().equals(phone)
                    && c.getEmail().equalsIgnoreCase(email)) {
                return c;
            }
        }
        Customer newCust = new Customer(name, phone, email);
        customers.add(newCust);
        return newCust;
    }

    private Customer findCustomerPrompt() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter your phone number: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine().trim();

        for (Customer c : customers) {
            if (c.getName().equalsIgnoreCase(name)
                    && c.getPhoneNumber().equals(phone)
                    && c.getEmail().equalsIgnoreCase(email)) {
                return c;
            }
        }
        return null;
    }

    private double priceForClass(String seatClass) {
        if (seatClass == null) return 150.0;
        String s = seatClass.trim().toLowerCase();
        if (s.startsWith("f")) return 700.0;       // First
        if (s.startsWith("b")) return 350.0;       // Business
        return 150.0;                              // Economy default
    }
}
