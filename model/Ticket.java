package model;
public class Ticket{
    private int ticketId;
    private double price;
    private String seat;
    private Flight flight;
    private Customer customer;
    // cancelled ticket, in-use, booked etc. enumerated variable fs
    private String status;
    private String seatType;

    public Ticket(Seat seat, Flight flight, Customer customer, double price){
        this.seat = seat;
        this.flight = flight;
        this.customer = customer;
        this.price = price;
        this.status = "Booked";
        this.seatType = "Economy"
    }

    public String getSeat(){
        return seat;
    }

    public Flight getFlight(){
        return flight;
    }

    public Customer getCustomer(){
        return Customer;
    }

    public String getseatType() {return seatType; }

    public void purchase(){
        // is crew available?
        // add this method to crew
        if(!Flight.getCrew().isAvailable()){
            System.out.println("There is not a crew to operate this flight. Please exit and try again later.");
        }
        // is plane operable
        // add this method to PLANE
        if(!Flight.getPlane().isOperable()){
            System.out.println("Plane is not operable to fly. Please exit and try again later.");
        }
        // seat available?
        if(Flight.getCapacity() < 0){
            System.out.println("There are no seats left on the plane. Please exit and try again later.");
        }

        // decrement plane capacity
        // add this method to PLANE
        Flight.getPlane().decrementCapacity();

        // customer books the ticket.
        Customer.bookTicket(this);

        // update state
        status = "Booked";

        System.out.println("Ticket booked successfully!");
    }

    public void cancel(){
        // is the ticket already cancelled?
        if (status.equals("Cancelled")){
            System.out.println("This ticket is already cancelled");
            return;
        }
        // change from booked to cancelled
        status = "Cancelled";
        // remove the ticket from the customer's list of tickets
        Customer.cancelTicket(this);
        // increment number of seats on the plane
        Flight.getPlane().incrementCapacity();
    }

    public void upgradeTicket(){
        // more specific capacities is what we need
        if(Flight.getPlane.getCapacity > 0){
            // blah blah blah something with prices
            // maybe need to define old & new prices, and standard rates for certain flights
        }

    }
}