package model;

import model.enums.PlaneType;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Plane domain object:
 * - Generates deterministic seat codes based on PlaneType hints and capacity.
 * - Answers simple route feasibility questions (range).
 * - Availability ignores maintenance (always true) in this simplified version.
 */
public class Plane {
    private final String planeId;
    private final PlaneType planeType;
    private final int capacity;
    private int availableSeats;
    private int firstClassSeats;
    private int economySeats;
    private int availableFirstClassSeats;
    private int availableEconomySeats;
    private final double firstClassPrice;
    private final double economyPrice;

    /**
     * @param planeId    stable identifier (e.g., "P001")
     * @param planeType  aircraft type enum (provides seat layout hints/range)
     * @param capacity   total number of seats available for sale
     */
    public Plane(String planeId, PlaneType planeType, int capacity, double firstClassPrice, double economyPrice) {
        this.planeId = planeId;
        this.planeType = planeType;
        this.capacity = capacity;
        this.firstClassPrice = firstClassPrice;
        this.economyPrice = economyPrice;
        this.availableSeats = Math.max(0, capacity);
        // Split the seats: 20% first class, 80% economy
        this.firstClassSeats = (int)Math.round(capacity * 0.2);
        this.economySeats = capacity - this.firstClassSeats;
        this.availableFirstClassSeats = this.firstClassSeats;
        this.availableEconomySeats = this.economySeats;
    }

    public String getPlaneId() { return planeId; }
    public PlaneType getPlaneType() { return planeType; }
    public int getCapacity() { return capacity; }
    public int getAvailableSeats() { return availableSeats; }
    public int getAvailableFirstClassSeats() { return availableFirstClassSeats; }
    public int getAvailableEconomySeats() { return availableEconomySeats; }
    public int getFirstClassSeats() { return firstClassSeats; }
    public int getEconomySeats() { return economySeats; }
    public double getFirstClassPrice() { return firstClassPrice; }
    public double getEconomyPrice() { return economyPrice; }


    // Reserve one seat if available. Returns true if success, false if full.
    public boolean decrementCapacity() {
        if (availableSeats <= 0) return false;
        availableSeats--;
        return true;
    }

    public void incrementCapacity(){
        if (availableSeats < capacity) availableSeats++;
    }

    // for Customer class check
    public boolean isOperable() {
        // simplified: always true since maintenance model beyond scope
        return true;
    }

    /**
     * Generate seat codes like A1..F1, A2..F2, ... until capacity is reached.
     * Uses PlaneType's default seat letters (e.g., "ABCDEF" or "ABCDEFHJK") and seats-per-row hint.
     * If capacity is not a multiple of seats-per-row, the last row is partially filled from the start.
     *
     * Example: capacity=5, letters="ABCD" -> A1,B1,C1,D1,A2
     *
     * @return ordered, unique set of seat codes (LinkedHashSet preserves insertion order)
     */
    public Set<String> seatCodes() {
        String letters = defaultSeatLetters();
        int across = defaultSeatsPerRow(letters);

        LinkedHashSet<String> codes = new LinkedHashSet<>(capacity * 2);
        int fullRows = capacity / across;
        int remainder = capacity % across;

        // full rows
        for (int row = 1; row <= fullRows; row++) {
            for (int i = 0; i < across; i++) {
                codes.add(letters.charAt(i) + String.valueOf(row));
            }
        }
        // last partial row
        if (remainder > 0) {
            int row = fullRows + 1;
            for (int i = 0; i < remainder; i++) {
                codes.add(letters.charAt(i) + String.valueOf(row));
            }
        }
        return codes;
    }

     // ---------- Helpers ----------

    private String defaultSeatLetters() {
        String letters = planeType.getDefaultSeatLetters();
        if (letters != null && !letters.isBlank()) return letters;
        // Fallback: common 6-across narrow-body layout (skip 'I')
        return "ABCDEF";
    }

    private int defaultSeatsPerRow(String letters) {
        int across = planeType.getDefaultSeatsPerRow();
        if (across > 0) return across;
        return Math.max(1, letters.length());
    }

    // Equality by ID is practical for planes
    @Override public int hashCode() { return planeId.hashCode(); }
    @Override public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Plane other = (Plane) obj; // casting
        return Objects.equals(planeId, other.planeId);
    }

    // Reserve a seat of the given type ("first" or "economy").
    // Returns true if a seat was successfully reserved, false otherwise.
    public boolean reserveSeat(String seatType) {
        if ("first".equalsIgnoreCase(seatType)) {
            if (availableFirstClassSeats > 0 && availableSeats > 0) {
                availableFirstClassSeats--;
                availableSeats--;
                return true;
            }
            return false;
        } else if ("economy".equalsIgnoreCase(seatType)) {
            if (availableEconomySeats > 0 && availableSeats > 0) {
                availableEconomySeats--;
                availableSeats--;
                return true;
            }
            return false;
        }
        return false;
    }

    // Release a seat of the given type ("first" or "economy").
    public void releaseSeat(String seatType) {
        if ("first".equalsIgnoreCase(seatType)) {
            if (availableFirstClassSeats < firstClassSeats && availableSeats < capacity) {
                availableFirstClassSeats++;
                availableSeats++;
            }
        } else if ("economy".equalsIgnoreCase(seatType)) {
            if (availableEconomySeats < economySeats && availableSeats < capacity) {
                availableEconomySeats++;
                availableSeats++;
            }
        }
    }

    /**
     * Returns the price for the given seat type ("first" or "economy").
     * @param seatType "first" or "economy"
     * @return price for the seat type, or 0.0 if seatType is invalid
     */
    public double getPrice(String seatType) {
        if ("first".equalsIgnoreCase(seatType)) {
            return firstClassPrice;
        } else if ("economy".equalsIgnoreCase(seatType)) {
            return economyPrice;
        }
        return 0.0;
    }
}