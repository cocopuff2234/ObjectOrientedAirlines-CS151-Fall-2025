package model;

import model.enums.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.EnumSet;
// import java.util.List;

public class FlightTest {

    @Test
    public void testAssignValidCaptainAndFO() {
        Plane plane = new Plane("P001", PlaneType.A320, 180, 300, 100);
        Flight flight = new Flight(
            "OA123", "OAir", "SFO", "SEA",
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(1).plusHours(2),
            plane, 2
        );

        Pilot captain = new Pilot(
            "P001", "John Captain", java.time.LocalDate.now().minusYears(5), "SFO",
            PilotRank.CAPTAIN, EnumSet.of(PlaneType.A320), 5000
        );

        Pilot fo = new Pilot(
            "P002", "John Officer", java.time.LocalDate.now().minusYears(2), "SFO",
            PilotRank.FIRST_OFFICER, EnumSet.of(PlaneType.A320), 2500
        );

        // Should not throw
        assertDoesNotThrow(() -> flight.assignCaptain(captain));
        assertDoesNotThrow(() -> flight.assignFirstOfficer(fo));

        assertTrue(flight.hasRequiredCrew() == false); // attendants missing
    }

    @Test
    public void testAddFlightAttendantsAndCheckCrewComplete() {
        Plane plane = new Plane("P002", PlaneType.A320, 180, 300, 100);
        Flight flight = new Flight(
            "OA456", "OAir", "LAX", "SEA",
            LocalDateTime.now().plusDays(2),
            LocalDateTime.now().plusDays(2).plusHours(2),
            plane, 2
        );

        FlightAttendant fa1 = new FlightAttendant(
            "FA1", "Evelyn Chevalier", java.time.LocalDate.now().minusYears(3), "SFO",
            FAPosition.LEAD, EnumSet.of(PlaneType.A320)
        );

        FlightAttendant fa2 = new FlightAttendant(
            "FA2", "Astra Yao", java.time.LocalDate.now().minusYears(1), "SFO",
            FAPosition.JUNIOR, EnumSet.of(PlaneType.A320)
        );

        assertDoesNotThrow(() -> flight.addAttendant(fa1));
        assertDoesNotThrow(() -> flight.addAttendant(fa2));

        assertEquals(2, flight.getAttendants().size());
    }

    @Test
    public void testDelayByMinutesUpdatesTimes() {
        Plane plane = new Plane("P003", PlaneType.B787, 250, 500, 250);
        Flight flight = new Flight(
            "OA789", "OAir", "DEN", "PHX",
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(1).plusHours(3),
            plane, 3
        );

        LocalDateTime originalDeparture = flight.getDepartureUTC();
        LocalDateTime originalArrival = flight.getArrivalUTC();

        flight.delayByMinutes(30);

        assertEquals(originalDeparture.plusMinutes(30), flight.getDepartureUTC());
        assertEquals(originalArrival.plusMinutes(30), flight.getArrivalUTC());
    }

    @Test
    public void testAssignCaptainWrongRankThrows() {
        Plane plane = new Plane("P004", PlaneType.A321, 190, 200, 100);
        Flight flight = new Flight(
            "OA321", "OAir", "SEA", "LAX",
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(1).plusHours(2),
            plane, 2
        );

        Pilot foAsCaptain = new Pilot(
            "P999", "Wrong Rank", java.time.LocalDate.now().minusYears(3), "SEA",
            PilotRank.FIRST_OFFICER, EnumSet.of(PlaneType.A321), 1500
        );

        Exception ex = assertThrows(IllegalArgumentException.class, () -> flight.assignCaptain(foAsCaptain));
        assertTrue(ex.getMessage().toLowerCase().contains("captain"));
    }
}
