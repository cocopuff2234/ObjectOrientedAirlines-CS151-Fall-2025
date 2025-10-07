package model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Airport {
    private final String airportId;
    private final String airportName;
    private final List<String> terminals;
    private final Set<String> gates;
    private final Set<Flight> scheduledFlights = new HashSet<>();

    public Airport (String airportId, String airportName, List<String> terminals, Set<String> gates){
        this.airportId = Objects.requireNonNull(airportId);
        this.airportName = Objects.requireNonNull(airportName);
        this.terminals = new ArrayList<>(Objects.requireNonNull(terminals));
        this.gates = new HashSet<>(Objects.requireNonNull(gates));
    }

    public String getAirportId() {
        return airportId;
    }

    public String getAirportName() {
        return airportName;
    }

    public List<String> getTerminals() {
        return Collections.unmodifiableList(terminals);
    }

    public Set<String> getGate() {
        return Collections.unmodifiableSet(gates);
    }

    // ====== Core operations ======

    /** Register a flight that involves this airport (origin or destination). */
    public void scheduleFlight(Flight flight) {
        Objects.requireNonNull(flight, "flight");
        scheduledFlights.add(flight); // Set naturally prevents duplicates
    }

    /**
     * Assign a gate to the given flight at this airport.
     * Throws if the gate is unknown or the time window overlaps with another flight using the same gate.
     */
    public void assignGate(Flight flight, String gate) {
        Objects.requireNonNull(flight, "flight");
        if (!gates.contains(gate)) {
            throw new IllegalArgumentException("Unknown gate: " + gate);
        }
        LocalDateTime dep = flight.getDepartureUTC();

        for (Flight f : scheduledFlights) {
            if (gate.equals(f.getGate()) && sameMinute(dep, f.getDepartureUTC())) {
                throw new IllegalStateException("Gate " + gate + " is already used at " + dep + " by flight " + f.getId());
            }
        }
        flight.setGate(gate);
    }

    public void releaseGate(String gate, LocalDateTime time) {
        
    }

    // ---- Helpers ----

    /** Two datetimes are considered the same "slot" if year, month, day, hour, and minute are all equal. */
    private static boolean sameMinute(LocalDateTime a, LocalDateTime b) {
        return a.getYear()  == b.getYear()  &&
               a.getMonth() == b.getMonth() &&
               a.getDayOfMonth() == b.getDayOfMonth() &&
               a.getHour() == b.getHour() &&
               a.getMinute() == b.getMinute();
    }
}
