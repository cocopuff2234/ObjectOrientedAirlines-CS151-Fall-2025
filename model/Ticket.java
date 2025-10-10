package model;

import model.enums.ReservationStatus;

public class Ticket{
    private int ticketId; 
    private double price;
    private Flight flight;
    private Customer customer;
    // cancelled ticket, in-use, booked etc.
    private ReservationStatus status;
    private String seatType;



    public Ticket(String seatType, Flight flight, Customer customer, double price){
        this.flight = flight;
        this.customer = customer;
        this.price = price;
        this.status = ReservationStatus.PENDING;
        this.seatType = seatType;
    }

    public Flight getFlight(){ return flight;}
    public Customer getCustomer(){ return customer;}
    public String getSeatType() { return seatType;}
    public double getPrice(){ return price; }

    public void purchase(){
        // is crew available?
        if(!flight.hasRequiredCrew()){
            System.out.println("There is not a crew to operate this flight. Please exit and try again later.");
            return;
        }
        // is plane operable
        if(!flight.getPlane().isOperable()){
            System.out.println("Plane is not operable to fly. Please exit and try again later.");
            return;
        }
        // seat available?
        if(flight.getAvailableSeats() <= 0){
            System.out.println("There are no seats left on the plane. Please exit and try again later.");
            return;
        }

        // decrement plane capacity
        // add this method to PLANE
        flight.getPlane().decrementCapacity();

        // customer books the ticket.
        customer.bookTicket(this);

        // update state
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
        flight.getPlane().incrementCapacity();
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