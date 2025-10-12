package model;

import model.enums.PlaneType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

public class PlaneTest {

    private Plane plane;

    @BeforeEach
    public void setUp() {
        // Create a plane with 150 capacity (30 first class, 120 economy)
        plane = new Plane("P001", PlaneType.A320, 150, 500.0, 200.0);
    }

    @Test
    public void testPlaneInitialization() {
        assertEquals("P001", plane.getPlaneId());
        assertEquals(PlaneType.A320, plane.getPlaneType());
        assertEquals(150, plane.getCapacity());
        assertEquals(150, plane.getAvailableSeats());
        assertEquals(500.0, plane.getFirstClassPrice());
        assertEquals(200.0, plane.getEconomyPrice());
    }

    @Test
    public void testSeatDistribution() {
        // 20% first class, 80% economy
        assertEquals(30, plane.getFirstClassSeats()); // 20% of 150
        assertEquals(120, plane.getEconomySeats());   // 80% of 150
        assertEquals(30, plane.getAvailableFirstClassSeats());
        assertEquals(120, plane.getAvailableEconomySeats());
    }

    @Test
    public void testReserveFirstClassSeat() {
        assertTrue(plane.reserveSeat("first"));
        assertEquals(149, plane.getAvailableSeats());
        assertEquals(29, plane.getAvailableFirstClassSeats());
        assertEquals(120, plane.getAvailableEconomySeats());
    }

    @Test
    public void testReserveEconomySeat() {
        assertTrue(plane.reserveSeat("economy"));
        assertEquals(149, plane.getAvailableSeats());
        assertEquals(30, plane.getAvailableFirstClassSeats());
        assertEquals(119, plane.getAvailableEconomySeats());
    }

    @Test
    public void testReserveSeatsUntilFirstClassFull() {
        // Reserve all first class seats
        for (int i = 0; i < 30; i++) {
            assertTrue(plane.reserveSeat("first"));
        }
        assertEquals(0, plane.getAvailableFirstClassSeats());
        assertEquals(120, plane.getAvailableSeats());

        // Try to reserve one more first class seat - should fail
        assertFalse(plane.reserveSeat("first"));
    }

    @Test
    public void testReserveSeatsUntilEconomyFull() {
        // Reserve all economy seats
        for (int i = 0; i < 120; i++) {
            assertTrue(plane.reserveSeat("economy"));
        }
        assertEquals(0, plane.getAvailableEconomySeats());
        assertEquals(30, plane.getAvailableSeats());

        // Try to reserve one more economy seat - should fail
        assertFalse(plane.reserveSeat("economy"));
    }

    @Test
    public void testReserveSeatsUntilPlaneFull() {
        // Reserve all seats
        for (int i = 0; i < 30; i++) {
            assertTrue(plane.reserveSeat("first"));
        }
        for (int i = 0; i < 120; i++) {
            assertTrue(plane.reserveSeat("economy"));
        }

        assertEquals(0, plane.getAvailableSeats());
        assertFalse(plane.reserveSeat("first"));
        assertFalse(plane.reserveSeat("economy"));
    }

    @Test
    public void testReleaseSeat() {
        // Reserve and release first class seat
        plane.reserveSeat("first");
        assertEquals(29, plane.getAvailableFirstClassSeats());

        plane.releaseSeat("first");
        assertEquals(30, plane.getAvailableFirstClassSeats());
        assertEquals(150, plane.getAvailableSeats());
    }

    @Test
    public void testReleaseMultipleSeats() {
        // Reserve 5 economy seats
        for (int i = 0; i < 5; i++) {
            plane.reserveSeat("economy");
        }
        assertEquals(115, plane.getAvailableEconomySeats());

        // Release 2 economy seats
        plane.releaseSeat("economy");
        plane.releaseSeat("economy");
        assertEquals(117, plane.getAvailableEconomySeats());
    }

    @Test
    public void testCannotReleaseMoreSeatsThanCapacity() {
        // Try to release more seats than exist
        for (int i = 0; i < 150; i++) {
            plane.releaseSeat("economy");
        }
        // Should not exceed original capacity
        assertEquals(120, plane.getAvailableEconomySeats());
        assertEquals(150, plane.getAvailableSeats());
    }

    @Test
    public void testDecrementCapacity() {
        assertTrue(plane.decrementCapacity());
        assertEquals(149, plane.getAvailableSeats());
    }

    @Test
    public void testDecrementCapacityWhenFull() {
        // Fill the plane
        for (int i = 0; i < 150; i++) {
            plane.decrementCapacity();
        }

        assertEquals(0, plane.getAvailableSeats());
        assertFalse(plane.decrementCapacity());
    }

    @Test
    public void testIncrementCapacity() {
        plane.decrementCapacity();
        assertEquals(149, plane.getAvailableSeats());

        plane.incrementCapacity();
        assertEquals(150, plane.getAvailableSeats());
    }

    @Test
    public void testIncrementCapacityDoesNotExceedMaximum() {
        assertEquals(150, plane.getAvailableSeats());

        plane.incrementCapacity();
        // Should not exceed original capacity
        assertEquals(150, plane.getAvailableSeats());
    }

    @Test
    public void testGetPriceFirstClass() {
        assertEquals(500.0, plane.getPrice("first"));
        assertEquals(500.0, plane.getPrice("First"));
        assertEquals(500.0, plane.getPrice("FIRST"));
    }

    @Test
    public void testGetPriceEconomy() {
        assertEquals(200.0, plane.getPrice("economy"));
        assertEquals(200.0, plane.getPrice("Economy"));
        assertEquals(200.0, plane.getPrice("ECONOMY"));
    }

    @Test
    public void testGetPriceInvalidSeatType() {
        assertEquals(0.0, plane.getPrice("business"));
        assertEquals(0.0, plane.getPrice("invalid"));
        assertEquals(0.0, plane.getPrice(""));
    }

    @Test
    public void testIsOperable() {
        // In simplified implementation, always returns true
        assertTrue(plane.isOperable());
    }

    @Test
    public void testSeatCodesGeneration() {
        Set<String> seatCodes = plane.seatCodes();
        assertEquals(150, seatCodes.size());
        
        // Check some expected seat codes for A320 (6 seats per row, ABCDEF)
        assertTrue(seatCodes.contains("A1"));
        assertTrue(seatCodes.contains("F1"));
        assertTrue(seatCodes.contains("A25")); // Last row
    }

    @Test
    public void testSeatCodesAreUnique() {
        Set<String> seatCodes = plane.seatCodes();
        // Set automatically ensures uniqueness, but verify size matches capacity
        assertEquals(plane.getCapacity(), seatCodes.size());
    }

    @Test
    public void testPlaneEquality() {
        Plane plane1 = new Plane("P001", PlaneType.A320, 150, 500, 200);
        Plane plane2 = new Plane("P001", PlaneType.A320, 150, 500, 200);
        Plane plane3 = new Plane("P002", PlaneType.B737, 160, 500, 200);

        assertEquals(plane1, plane2); // Same plane ID
        assertNotEquals(plane1, plane3); // Different plane ID
    }

    @Test
    public void testPlaneWithDifferentTypes() {
        Plane boeing737 = new Plane("B001", PlaneType.B737, 160, 400, 150);
        Plane boeing787 = new Plane("B002", PlaneType.B787, 296, 800, 300);

        assertEquals(PlaneType.B737, boeing737.getPlaneType());
        assertEquals(PlaneType.B787, boeing787.getPlaneType());
        
        // Verify seat distribution
        assertEquals(32, boeing737.getFirstClassSeats()); // 20% of 160
        assertEquals(128, boeing737.getEconomySeats());

        assertEquals(59, boeing787.getFirstClassSeats()); // 20% of 296 (rounded)
        assertEquals(237, boeing787.getEconomySeats());
    }

    @Test
    public void testReserveSeatCaseInsensitive() {
        assertTrue(plane.reserveSeat("First"));
        assertTrue(plane.reserveSeat("ECONOMY"));
        assertTrue(plane.reserveSeat("first"));
        assertTrue(plane.reserveSeat("economy"));

        assertEquals(28, plane.getAvailableFirstClassSeats());
        assertEquals(118, plane.getAvailableEconomySeats());
    }

    @Test
    public void testReleaseSeatCaseInsensitive() {
        plane.reserveSeat("first");
        plane.reserveSeat("economy");

        plane.releaseSeat("First");
        plane.releaseSeat("ECONOMY");

        assertEquals(30, plane.getAvailableFirstClassSeats());
        assertEquals(120, plane.getAvailableEconomySeats());
    }
}
