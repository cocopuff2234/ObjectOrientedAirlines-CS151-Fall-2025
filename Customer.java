import java.util.List;

public class Customer{
    private int customerId;
    private String name;
    private String phoneNumber;
    private String email;
    // List of tickets a customer has
    private List<Ticket> ticketList;
    private double balance;

    // constructor
    public Customer{
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.ticketList = ticketList;
        this.balance = 0;
    }

    // methods
    // (unsure of args atp)
    public void bookTicket(Flight flight, Seat seat, double price){
        // is flight operable
            // is seat available
                ticketList.add(ticket)
                balance += price;
        // else ->
        System.out.println("This seat or flight is not available")
    }
    public void cancelTicket(Ticket ticket){
        // add logic
    }

    public void seeTickets(){
        // add logic
    }


}

