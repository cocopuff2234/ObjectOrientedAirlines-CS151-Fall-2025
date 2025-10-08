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

    /**
     * @param planeId    stable identifier (e.g., "P001")
     * @param planeType  aircraft type enum (provides seat layout hints/range)
     * @param capacity   total number of seats available for sale
     */
    public Plane(String planeId, PlaneType planeType, int capacity) {
        this.planeId = planeId;
        this.planeType = planeType;
        this.capacity = capacity;
    }

    public String getPlaneId() { return planeId; }
    public PlaneType getPlaneType() { return planeType; }
    public int getCapacity() { return capacity; }

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

    /**
     * Delegates to PlaneType range capability if available; otherwise falls back to a simple heuristic.
     * @param nmDistance route distance in nautical miles
     */
    public boolean supportsRoute(int nmDistance) {
        try {
            // Preferred: delegate to enum capability
            return planeType.supportsRouteNm(nmDistance);
        } catch (NoSuchMethodError | UnsupportedOperationException e) {
            // Fallback (very rough): narrow-body ~ 3500nm, wide-body ~ 7500nm
            boolean wide = planeType.isWidebody(); // if you don't have this, assume false
            int max = wide ? 7500 : 3500;
            return nmDistance <= max;
        }
    }

     // ---------- Helpers ----------

    private String defaultSeatLetters() {
        try {
            java.lang.reflect.Method method = planeType.getClass().getMethod("getDefaultSeatLetters");
            Object result = method.invoke(planeType);
            if (result instanceof String) {
                String letters = (String) result;
                if (letters != null && !letters.isBlank()) return letters;
            }
        } catch (NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException ignored) { }
        // Fallback: common 6-across narrow-body layout (skip 'I')
        return "ABCDEF";
    }

    private int defaultSeatsPerRow(String letters) {
        try {
            int across = planeType.getDefaultSeatsPerRow();
            if (across > 0) return across;
        } catch (NoSuchMethodError ignored) { }
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
}
