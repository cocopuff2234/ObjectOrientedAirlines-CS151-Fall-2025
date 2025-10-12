package model;

import model.enums.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.Optional;

public class AirportTest {

    private Airport airport;
    private Flight flight1;
    private Flight flight2;

    @BeforeEach
    public void setUp() {
        airport = new Airport(
            "SFO",
            "San Francisco International",
            List.of("Terminal 1", "Terminal 2"),
            Set.of("A1", "A2", "A3", "B1", "B2")
        );

        Plane plane1 = new Plane("P001", PlaneType.A320, 180, 300, 100);
        Plane plane2 = new Plane("P002", PlaneType.B737, 160, 300, 100);

        flight1 = new Flight(
            "OA100", "OAir", "SFO", "LAX",
            LocalDateTime.of(2025, 11, 1, 10, 0),
            LocalDateTime.of(2025, 11, 1, 11, 30),
            plane1, 2
        );

        flight2 = new Flight(
            "OA200", "OAir", "SFO", "SEA",
            LocalDateTime.of(2025, 11, 1, 14, 0),
            LocalDateTime.of(2025, 11, 1, 16, 0),
            plane2, 2
        );
    }

    @Test
    public void testScheduleFlightSuccessfully() {
        assertDoesNotThrow(() -> airport.scheduleFlight(flight1));
        assertDoesNotThrow(() -> airport.scheduleFlight(flight2));
    }

    @Test
    public void testScheduleNullFlightThrows() {
        Exception ex = assertThrows(NullPointerException.class, () -> airport.scheduleFlight(null));
        assertTrue(ex.getMessage().contains("flight"));
    }

    @Test
    public void testAssignValidGate() {
        airport.scheduleFlight(flight1);
        assertDoesNotThrow(() -> airport.assignGate(flight1, "A1"));
        assertEquals("A1", flight1.getGate());
    }

    @Test
    public void testAssignInvalidGateThrows() {
        airport.scheduleFlight(flight1);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> airport.assignGate(flight1, "Z99"));
        assertTrue(ex.getMessage().contains("Unknown gate"));
    }

    @Test
    public void testAssignSameGateAtSameTimeThrows() {
        airport.scheduleFlight(flight1);
        airport.scheduleFlight(flight2);

        // Assign gate to first flight
        airport.assignGate(flight1, "A1");

        // Create a third flight at the exact same time
        Plane plane3 = new Plane("P003", PlaneType.A320, 180, 300, 100);
        Flight flight3 = new Flight(
            "OA300", "OAir", "SFO", "DEN",
            LocalDateTime.of(2025, 11, 1, 10, 0), // Same time as flight1
            LocalDateTime.of(2025, 11, 1, 12, 0),
            plane3, 2
        );
        airport.scheduleFlight(flight3);

        // Should throw because gate A1 is already used at 10:00
        Exception ex = assertThrows(IllegalStateException.class, () -> airport.assignGate(flight3, "A1"));
        assertTrue(ex.getMessage().contains("already used"));
    }

    @Test
    public void testFindAvailableGate() {
        airport.scheduleFlight(flight1);
        airport.assignGate(flight1, "A1");

        // At 10:00, A1 is busy but others should be available
        Optional<String> availableGate = airport.findAvailableGate(LocalDateTime.of(2025, 11, 1, 10, 0));
        assertTrue(availableGate.isPresent());
        assertNotEquals("A1", availableGate.get());
    }

    @Test
    public void testFindAvailableGateWhenAllBusy() {
        airport.scheduleFlight(flight1);
        
        // Assign all gates at the same time
        LocalDateTime busyTime = LocalDateTime.of(2025, 11, 1, 10, 0);
        for (String gate : Set.of("A1", "A2", "A3", "B1", "B2")) {
            Plane plane = new Plane("P" + gate, PlaneType.A320, 180, 300, 100);
            Flight flight = new Flight(
                "OA" + gate, "OAir", "SFO", "LAX",
                busyTime, busyTime.plusHours(2),
                plane, 2
            );
            airport.scheduleFlight(flight);
            airport.assignGate(flight, gate);
        }

        Optional<String> availableGate = airport.findAvailableGate(busyTime);
        assertFalse(availableGate.isPresent());
    }

    @Test
    public void testFlightsDepartingOnReturnsCorrectFlights() {
        airport.scheduleFlight(flight1);
        airport.scheduleFlight(flight2);

        LocalDate targetDate = LocalDate.of(2025, 11, 1);
        List<Flight> departingFlights = airport.flightsDepartingOn(targetDate);

        assertEquals(2, departingFlights.size());
        assertTrue(departingFlights.contains(flight1));
        assertTrue(departingFlights.contains(flight2));
    }

    @Test
    public void testFlightsDepartingOnSortedByTime() {
        Plane plane3 = new Plane("P003", PlaneType.A320, 180, 300, 100);
        Flight earlyFlight = new Flight(
            "OA300", "OAir", "SFO", "DEN",
            LocalDateTime.of(2025, 11, 1, 6, 0), // Earlier than flight1 and flight2
            LocalDateTime.of(2025, 11, 1, 8, 0),
            plane3, 2
        );

        airport.scheduleFlight(flight1); // 10:00
        airport.scheduleFlight(flight2); // 14:00
        airport.scheduleFlight(earlyFlight); // 6:00

        LocalDate targetDate = LocalDate.of(2025, 11, 1);
        List<Flight> departingFlights = airport.flightsDepartingOn(targetDate);

        assertEquals(3, departingFlights.size());
        assertEquals(earlyFlight, departingFlights.get(0)); // Should be first
        assertEquals(flight1, departingFlights.get(1));
        assertEquals(flight2, departingFlights.get(2));
    }

    @Test
    public void testFlightsDepartingOnDifferentDateReturnsEmpty() {
        airport.scheduleFlight(flight1);
        airport.scheduleFlight(flight2);

        LocalDate differentDate = LocalDate.of(2025, 12, 1);
        List<Flight> departingFlights = airport.flightsDepartingOn(differentDate);

        assertTrue(departingFlights.isEmpty());
    }

    @Test
    public void testReleaseGate() {
        airport.scheduleFlight(flight1);
        airport.assignGate(flight1, "A1");

        assertEquals("A1", flight1.getGate());

        // Release gate after departure time
        airport.releaseGate("A1", LocalDateTime.of(2025, 11, 1, 10, 1));

        assertNull(flight1.getGate());
    }

    @Test
    public void testGetAirportId() {
        assertEquals("SFO", airport.getAirportId());
    }

    @Test
    public void testGetAirportName() {
        assertEquals("San Francisco International", airport.getAirportName());
    }

    @Test
    public void testGetTerminals() {
        List<String> terminals = airport.getTerminals();
        assertEquals(2, terminals.size());
        assertTrue(terminals.contains("Terminal 1"));
        assertTrue(terminals.contains("Terminal 2"));
    }

    @Test
    public void testGetGates() {
        Set<String> gates = airport.getGates();
        assertEquals(5, gates.size());
        assertTrue(gates.contains("A1"));
        assertTrue(gates.contains("B2"));
    }

    @Test
    public void testAirportEquality() {
        Airport airport1 = new Airport("JFK", "JFK Airport", List.of("T1"), Set.of("G1"));
        Airport airport2 = new Airport("JFK", "JFK Airport", List.of("T1"), Set.of("G1"));
        Airport airport3 = new Airport("LAX", "LAX Airport", List.of("T1"), Set.of("G1"));

        assertEquals(airport1, airport2); // Same airport ID
        assertNotEquals(airport1, airport3); // Different airport ID
    }
}
