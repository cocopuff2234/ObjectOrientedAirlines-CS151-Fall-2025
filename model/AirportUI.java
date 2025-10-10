package model;

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
        this.flights = flights;
    }

    public void start() {
        while (true) {
            System.out.println("Welcome. Please select what you would like to do:");
            System.out.println("1. Book Flight");
            System.out.println("2. Cancel Flight");
            System.out.println("3. Check Flight Status");
            System.out.println("4. See Available Flights");
            System.out.println("5. See Existing Reservations");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1": bookFlight(); break;
                case "2": cancelFlight(); break;
                case "3": checkFlightStatus(); break;
                case "4": seeAvailableFlights(); break;
                case "5": seeReservations(); break;
                case "6": System.out.println("Goodbye!"); return;
                default: System.out.println("Invalid choice");
            }
        }
    }

    private void bookFlight() {
        try {
            System.out.print("Enter departure date (YYYY-MM-DD): ");
            String dateStr = scanner.nextLine();
            LocalDate date = LocalDate.parse(dateStr);

            System.out.print("Enter departing airport: ");
            String origin = scanner.nextLine();

            System.out.print("Enter arrival airport: ");
            String destination = scanner.nextLine();

            List<Flight> matches = new ArrayList<>();
            for (Flight f : flights) {
                if (f.getOrigin().equalsIgnoreCase(origin) &&
                    f.getDestination().equalsIgnoreCase(destination) &&
                    f.getDepartureUTC().toLocalDate().equals(date)) {
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
                System.out.println((i + 1) + ". " + f.getFlightNumber() + " - " + f.getDepartureUTC() + " to " + f.getArrivalUTC());
            }

            System.out.print("Select flight number to book (or 0 to cancel): ");
            int sel = Integer.parseInt(scanner.nextLine());
            if (sel <= 0 || sel > matches.size()) return;

            Flight flight = matches.get(sel - 1);

            System.out.print("Enter your name: ");
            String name = scanner.nextLine();
            System.out.print("Enter your phone number: ");
            String phone = scanner.nextLine();
            System.out.print("Enter your email: ");
            String email = scanner.nextLine();

            Customer customer = null;
            for (Customer c : customers) {
                if (c.getName().equalsIgnoreCase(name) &&
                    c.getPhoneNumber().equals(phone) &&
                    c.getEmail().equalsIgnoreCase(email)) {
                    customer = c;
                    break;
                }
            }
            if (customer == null) {
                customer = new Customer(name, phone, email, new ArrayList<>(), 0.0);
                customers.add(customer);
            }

            System.out.print("Enter seat number (ex. 12A): ");
            String seatNum = scanner.nextLine();
            System.out.print("Enter seat class (Economy/Business/First): ");
            String seatClass = scanner.nextLine();
            Seat seat = new Seat(seatNum, seatClass);

            double price = 150.0;
            Ticket ticket = new Ticket(seat, flight, customer, price);

            customer.addTicket(ticket);

            System.out.println("Flight booked successfully for " + customer.getName() +
                    ". Ticket ID: " + ticket.getCustomer().getTicketList().size());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void cancelFlight() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your phone number: ");
        String phone = scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        Customer customer = null;
        for (Customer c : customers) {
            if (c.getName().equalsIgnoreCase(name) &&
                c.getPhoneNumber().equals(phone) &&
                c.getEmail().equalsIgnoreCase(email)) {
                customer = c;
                break;
            }
        }
        if (customer == null || customer.getTicketList().isEmpty()) {
            System.out.println("No tickets found for this customer.");
            return;
        }

        System.out.println("Your booked tickets:");
        List<Ticket> tickets = customer.getTicketList();
        for (int i = 0; i < tickets.size(); i++) {
            Ticket t = tickets.get(i);
            System.out.println((i + 1) + ". Flight " + t.getFlight().getFlightNumber() +
                    " from " + t.getFlight().getOrigin() + " to " + t.getFlight().getDestination() +
                    " | Seat: " + t.getSeat().getSeatNumber() +
                    " | Status: " + t.getStatus());
        }

        System.out.print("Select ticket to cancel (or 0 to exit): ");
        int sel = Integer.parseInt(scanner.nextLine());
        if (sel <= 0 || sel > tickets.size()) return;

        Ticket t = tickets.get(sel - 1);
        t.cancel();
        System.out.println("Ticket canceled successfully.");
    }

    private void checkFlightStatus() {
        System.out.print("Enter flight number: ");
        String number = scanner.nextLine();

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
        System.out.println("Flight " + flight.getFlightNumber() + " from " + flight.getOrigin() +
                " to " + flight.getDestination() +
                " departs at " + flight.getDepartureUTC() +
                " and arrives at " + flight.getArrivalUTC() +
                ". Status: " + status);
    }

    private void seeAvailableFlights() {
        System.out.println("All flights:");
        for (Flight f : flights) {
            System.out.println(f.getFlightNumber() + " | " + f.getAirline() + " | " +
                    f.getOrigin() + " -> " + f.getDestination() +
                    " | Departure: " + f.getDepartureUTC() +
                    " | Arrival: " + f.getArrivalUTC());
        }
    }

    private void seeReservations() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your phone number: ");
        String phone = scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        Customer customer = null;
        for (Customer c : customers) {
            if (c.getName().equalsIgnoreCase(name) &&
                c.getPhoneNumber().equals(phone) &&
                c.getEmail().equalsIgnoreCase(email)) {
                customer = c;
                break;
            }
        }

        if (customer == null || customer.getTicketList().isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }

        System.out.println("Your reservations:");
        for (Ticket t : customer.getTicketList()) {
            System.out.println("Flight " + t.getFlight().getFlightNumber() +
                    " from " + t.getFlight().getOrigin() + " to " + t.getFlight().getDestination() +
                    " | Seat: " + t.getSeat().getSeatNumber() +
                    " | Status: " + t.getStatus());
        }
    }
}
