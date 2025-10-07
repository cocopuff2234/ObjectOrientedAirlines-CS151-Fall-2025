package model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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


    public Flight(String flightNumber, String airline, String origin, String destination, LocalDateTime departureUTC,
            LocalDateTime arrivalUTC, PlaneType planeType, int minAttendants) {
        this.flightNumber = Objects.requireNonNull(flightNumber);
        this.airline = Objects.requireNonNull(airline);
        this.origin = Objects.requireNonNull(origin);
        this.destination = Objects.requireNonNull(destination);
        this.departureUTC = Objects.requireNonNull(departureUTC);
        this.arrivalUTC = Objects.requireNonNull(arrivalUTC);
        this.planeType = Objects.requireNonNull(planeType);
        this.minAttendants = Math.max(1, minAttendants);
        // require(!arrivalUTC.isBefore(departureUTC), "Arrival time must be after Departure time!") ;
    }
    public String getFlightNumber() { return flightNumber; }
    public String getAirline() { return airline; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public LocalDateTime getDepartureUTC() { return departureUTC; }
    public LocalDateTime getArrivalUTC() { return arrivalUTC; }
    public String getGate() { return gate; }
    public void setGate(String gate) { this.gate = gate; }
    public PlaneType getPlaneType() {return planeType;}
    public Pilot getCaptain() {return captain;}
    public void setCaptain(Pilot captain) {this.captain = captain;}
    public Pilot getFirstOfficer() {return firstOfficer;}
    public void setFirstOfficer(Pilot firstOfficer) {this.firstOfficer = firstOfficer;}
    public List<FlightAttendant> getAttendants() {return Collections.unmodifiableList(attendants);}
    public int getMinAttendants() {return minAttendants;}
    


}
