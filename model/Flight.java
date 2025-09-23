package model;
import java.time.LocalDateTime;

public class Flight {
    private final String flightNumber;
    private final String airline;
    private final String origin;
    private final String destination;
    private final LocalDateTime departureUTC;
    private final LocalDateTime arrivalUTC;

    public Flight(String flightNumber, String airline, String origin, String destination, LocalDateTime departureUTC, LocalDateTime arrivalUTC) {
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.departureUTC = departureUTC;
        this.arrivalUTC = arrivalUTC;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getAirline() {
        return airline;
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

    public LocalDateTime getArrivalUTC() {
        return arrivalUTC;
    }

    


}
