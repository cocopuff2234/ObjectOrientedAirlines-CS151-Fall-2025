package model;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import model.core.Notifiable;
import model.enums.CrewStatus;
import model.enums.PilotRank;
import model.enums.PlaneType;

public class Flight implements Notifiable{
    private final String flightNumber;
    private final String airline;
    private final String origin;
    private final String destination;
    private LocalDateTime departureUTC;
    private LocalDateTime arrivalUTC;
    private String gate;
    private final PlaneType planeType;
    private Pilot captain;
    private Pilot firstOfficer;
    private final List<FlightAttendant> attendants = new ArrayList<>();
    private final int minAttendants;

    /**
     * Constructs a new Flight object that represents a scheduled airline flight.
     *
     * @param flightNumber   the unique flight identifier (e.g., "UA 1")
     * @param airline        the name of the operating airline
     * @param origin         the departure airport code (e.g., "SFO")
     * @param destination    the arrival airport code (e.g., "SIN")
     * @param departureUTC   the UTC date and time of departure
     * @param arrivalUTC     the UTC date and time of arrival
     * @param planeType      the aircraft type assigned to the flight
     * @param minAttendants  the minimum number of flight attendants required
     *
     * <p>
     * This constructor initializes the flight’s identifying information, schedule,
     * and assigned aircraft type. It also performs basic validation to ensure
     * that required parameters are not null and that the arrival time is not before
     * the departure time. The number of minimum attendants is normalized to at least 1.
     * </p>
     *
     * @throws NullPointerException     if any required argument is {@code null}
     * @throws IllegalArgumentException if {@code arrivalUTC} is before {@code departureUTC}
    */
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
        require(!arrivalUTC.isBefore(departureUTC), "Arrival time must be after Departure time!") ;
    }
    public String getFlightNumber() { return flightNumber; }
    public String getAirline() { return airline; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public LocalDateTime getDepartureUTC() { return departureUTC; }
    public LocalDateTime getArrivalUTC() { return arrivalUTC; }
    public String getGate() { return gate; }
    public void setGate(String gate) { this.gate = gate; }
    public PlaneType getPlaneType() { return planeType ;}
    public Pilot getCaptain() { return captain;}
    public void setCaptain(Pilot captain) { this.captain = captain; }
    public Pilot getFirstOfficer() { return firstOfficer; }
    public void setFirstOfficer(Pilot firstOfficer) { this.firstOfficer = firstOfficer;}
    public List<FlightAttendant> getAttendants() { return Collections.unmodifiableList(attendants); }
    public int getMinAttendants() { return minAttendants; }

    // -------- Crew assignment methods -------- 
    public void assignCaptain(Pilot p){
        require(p != null, "Captain assignment must not be NULL");
        require(p.getStatus() == CrewStatus.AVAILABLE, "Captain must not be ON LEAVE or SUSPENDED");
        require(p.getRank() == PilotRank.CAPTAIN, "Captain must have rank CAPTAIN");
        require(p.canOperate(planeType), "Captain lacks type rating for Aircraft: " + planeType);
        this.captain = p;
    }

    public void assignFirstOfficer(Pilot p){
        require(p != null, "First Officer assignment must not be NULL");
        require(p.getStatus() == CrewStatus.AVAILABLE, "First Officer must not be ON LEAVE or SUSPENDED");
        require(p.getRank() == PilotRank.FIRST_OFFICER, "First Officer must have rank FIRST OFFICER");
        require(p.canOperate(planeType), "First Officer lacks type rating for Aircraft: " + planeType);
        this.firstOfficer = p;
    }

    public void addAttendant(FlightAttendant fa){
        require(fa != null, "Flight attendant must not be NULL");
        require(fa.getStatus() == CrewStatus.AVAILABLE, "Flight Attendant must not be ON LEAVE or SUSPENDED");
        require(fa.canOperate(planeType), "Flight attendant not qualified for Aircraft: " + planeType);
        require(!attendants.contains(fa), "attendant already assigned");
        attendants.add(fa);
    }

    // boolean to make sure attendant is even assigned prior to removal
    public boolean removeAttendant(FlightAttendant fa){
        return attendants.remove(fa);
    }

    // Check if adequate crew is assigned to the flight
    public boolean hasRequiredCrew(){
        return captain != null && firstOfficer != null && attendants.size() >= minAttendants;
    }

    public Duration getDuration(){
        return Duration.between(departureUTC, arrivalUTC);
    }

    @Override
    public void notify(String message) {
        System.out.println("[Flight " + flightNumber + "] " + message);
    }

    // Delay a flight by n (> 0) minutes
    public void delayByMinutes(long minutes){
        require(minutes > 0, "Minutes must not be negative (< 0)");

        // Store old values for context
        LocalDateTime oldDeparture = departureUTC;
        LocalDateTime oldArrival = arrivalUTC;

        departureUTC = departureUTC.plusMinutes(minutes);
        arrivalUTC = arrivalUTC.plusMinutes(minutes);

        // Notify the change
        notifyWithPrefix("Delay", String.format(
            "Flight %s delayed by %d minute%s (Departure: %s → %s, Arrival: %s → %s)",
            flightNumber,
            minutes,
            (minutes == 1 ? "" : "s"),
            oldDeparture,
            departureUTC,
            oldArrival,
            arrivalUTC
        ));
    }

    // Reassign gate and notify 
    public void changeGate(String newGate){
        String oldGate = this.gate;
        this.gate = Objects.requireNonNull(newGate);

        // Trigger notification
        if (oldGate == null) {
            notify("Gate assigned to " + newGate);
        } else if (!oldGate.equals(newGate)) {
            notifyWithPrefix("Gate Change", "from " + oldGate + " to " + newGate);
        } else {
            notify("Gate remains the same (" + newGate + ")");
        }
    }

    // UTILITY: Require method for validation throughout this class
    private static void require(boolean condition, String message){
        if (!condition) { throw new IllegalArgumentException(message); }
    }

    @Override
    public String toString() {
        return """
            %s %s: %s → %s  (%s)
            Departs: %s Arrives: %s Gate: %s
            Captain: %s | First Officer: %s | Flight Attendants: %d
            Crew Complete: %s
            """.formatted(
                airline, flightNumber, origin, destination, planeType,
                departureUTC, arrivalUTC, gate,
                captain != null ? captain.getFullName() : "—",
                firstOfficer != null ? firstOfficer.getFullName() : "—",
                attendants.size(),  isCrewComplete()
            );
    }
    @Override
    public int hashCode() {
        return Objects.hash(flightNumber, departureUTC);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Flight other)) return false; 
        return Objects.equals(flightNumber, other.flightNumber) 
            && Objects.equals(departureUTC, other.departureUTC);
    }


    
    


}
