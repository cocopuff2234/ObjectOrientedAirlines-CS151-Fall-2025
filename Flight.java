import java.time.LocalDateTime;

public class Flight {
    private final String flightNumber;
    private final String origin;
    private final String destination;
    private final LocalDateTime departureUTC;

    public Flight(String flightNumber, String origin, String destination, LocalDateTime departureUTC) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureUTC = departureUTC;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDepartureUTC() {
        return departureUTC;
    }

    

    


}
