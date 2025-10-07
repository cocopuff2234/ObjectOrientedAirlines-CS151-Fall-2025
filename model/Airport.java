package model;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    public String getAirportId() { return airportId; }
    public String getAirportName() { return airportName; }
    public List<String> getTerminals() { return Collections.unmodifiableList(terminals); }
    public Set<String> getGates() { return Collections.unmodifiableSet(gates); }

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
                throw new IllegalStateException("Gate " + gate + " is already used at " + dep + " by flight " + f.getFlightNumber());
            }
        }
        flight.setGate(gate);
    }

    /**
     * Release a gate by clearing the gate of flights whose departure is strictly before 'time'.
     * (Very naive cleanup: once departure time has passed, free the gate.)
     */
    public void releaseGate(String gate, LocalDateTime time) {
        for (Flight f : scheduledFlights) {
            if (gate.equals(f.getGate()) && f.getDepartureUTC().isBefore(time)) {
                f.setGate(null);
            }
        }
    }

    /** Return flights that DEPART from this airport on the specified date, sorted by departure time. */
    public List<Flight> flightsDepartingOn(LocalDate date) {
        List<Flight> result = new ArrayList<>();
        for (Flight f : scheduledFlights) {
            if (airportId.equals(f.getOrigin()) && f.getDepartureUTC().toLocalDate().equals(date)) {
                result.add(f);
            }
        }
        result.sort(Comparator.comparing(Flight::getDepartureUTC));
        return result;
    }

    /**
     * Find the first available gate at the given minute.
     * A gate is available if no scheduled flight uses it at that exact minute.
     */
    public Optional<String> findAvailableGate(LocalDateTime minute) {
        for (String gate : gates) {
            boolean busy = false;
            for (Flight f : scheduledFlights) {
                if (gate.equals(f.getGate()) && sameMinute(minute, f.getDepartureUTC())) {
                    busy = true;
                    break;
                }
            }
            if (!busy) return Optional.of(gate);
        }
        return Optional.empty();
    }

    // ---- Helpers ----

    /** Two datetimes are considered the same "slot" if year, month, day, hour, and minute are all equal. */
    private static boolean sameMinute(LocalDateTime a, LocalDateTime b) {
        Objects.requireNonNull(a, "a");
        Objects.requireNonNull(b, "b");
        return a.truncatedTo(ChronoUnit.MINUTES)
            .equals(b.truncatedTo(ChronoUnit.MINUTES));
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Airport)) return false;
        Airport other = (Airport) o;
        return Objects.equals(airportId, other.airportId);
    }

    @Override public int hashCode() { return Objects.hash(airportId); }

    @Override public String toString() {
        return "Airport{" + airportId + ", " + airportName + "}";
    }
}
