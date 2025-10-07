package model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.enums.PlaneType;

public class Flight {
    private final String flightNumber;
    private final String airline;
    private final String origin;
    private final String destination;
    private final LocalDateTime departureUTC;
    private final LocalDateTime arrivalUTC;
    private String gate;
    private final PlaneType planeType;
    private Pilot captain;
    private Pilot firstOfficer;
    private final List<FlightAttendant> attendants = new ArrayList<>();
    private final int minAttendants;

    

    /* 
    public Flight(String flightNumber, String airline, String origin, String destination, LocalDateTime departureUTC, LocalDateTime arrivalUTC) {
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.departureUTC = departureUTC;
        this.arrivalUTC = arrivalUTC;
        this.planeType = Objects.requireNonNull(planeType);
    }
    */

    public Flight(String flightNumber, String airline, String origin, String destination, LocalDateTime departureUTC,
            LocalDateTime arrivalUTC, PlaneType planeType, int minAttendants) {
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.departureUTC = departureUTC;
        this.arrivalUTC = arrivalUTC;
        this.planeType = planeType;
        this.minAttendants = minAttendants;
    }
    public String getFlightNumber() { return flightNumber; }
    public String getAirline() { return airline; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public LocalDateTime getDepartureUTC() { return departureUTC; }
    public LocalDateTime getArrivalUTC() { return arrivalUTC; }
    public String getGate() { return gate; }
    public void setGate(String gate) { this.gate = gate; }


}
