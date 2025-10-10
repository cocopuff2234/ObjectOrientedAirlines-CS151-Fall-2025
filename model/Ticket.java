package model;
import model.enums.ReservationStatus;

public class Ticket{
    private int ticketId; 
    private static int nextTicketId = 1;
    private double price;
    private Flight flight;
    private Customer customer;
    // cancelled ticket, in-use, booked etc.
    private ReservationStatus status;
    private String seatType;

    private int generateTicketId() {
        return nextTicketId++;
    }

    public Ticket(String seatType, Flight flight, Customer customer){
        this.ticketId = generateTicketId();
        this.flight = flight;
        this.customer = customer;
        this.price = flight.getPlane().getPrice(seatType);;
        this.status = ReservationStatus.PENDING;
        this.seatType = seatType;
    }

    public Flight getFlight(){ return flight;}
    public Customer getCustomer(){ return customer;}
    public String getSeatType() { return seatType;}
    public double getPrice(){ return price; }
    public ReservationStatus getStatus() { return status;}
    public void setStatus(ReservationStatus status){ this.status = status; }

    public void purchase() {
        // Check crew availability
        if(!flight.hasRequiredCrew()){
            System.out.println("There is not a crew to operate this flight. Please exit and try again later.");
            return;
        }

        // Check plane operability
        if(!flight.getPlane().isOperable()){
            System.out.println("Plane is not operable to fly. Please exit and try again later.");
            return;
        }

        // Check overall seats
        if(flight.getPlane().getAvailableSeats() <= 0){
            System.out.println("No seats left on the plane. Please exit and try again later.");
            return;
        }

        // Decrement overall capacity
        if (!flight.getPlane().decrementCapacity()) {
            System.out.println("Seat reservation failed (flight became full).");
            return;
        }

        // Reserve the specific seat type (First/Economy)
        if (!flight.getPlane().reserveSeat(seatType)) {
            // Release the overall seat if specific type not available
            flight.getPlane().incrementCapacity();
            System.out.println("No " + seatType + " seats available on this flight.");
            return;
        }

        // Customer books the ticket
        customer.bookTicket(this);

        // Update status
        status = ReservationStatus.CONFIRMED;

        System.out.println("Ticket booked successfully!");
    }

    public void cancel(){
        // is the ticket already cancelled?
        if (status.equals(ReservationStatus.CANCELED)){
            System.out.println("This ticket is already cancelled");
            return;
        }
        // change from booked to cancelled
        status = ReservationStatus.CANCELED;
        // remove the ticket from the customer's list of tickets
        customer.cancelTicket(this);
        // increment number of seats on the plane
        flight.getPlane().releaseSeat(seatType);
    }

    public void upgradeTicket(String newSeatType, double newPrice) {
        if (flight.getPlane().getCapacity() <= 0) {
            System.out.println("No seats available for upgrade.");
            return;
        }

        // adjust customer balance if upgrading costs more
        if (newPrice > price) {
            customer.setBalance(customer.getBalance() + (newPrice - price));
        }

        seatType = newSeatType;
        price = newPrice;

        System.out.println("Ticket upgraded to " + seatType + " for flight " + flight.getFlightNumber());
    }
}