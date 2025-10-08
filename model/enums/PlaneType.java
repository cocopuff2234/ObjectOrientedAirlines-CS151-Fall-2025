package model.enums;

public enum PlaneType {

    // --- Narrow-body ---
    E175("Embraer", "E175", 2100, 76, false, 4, "ABCD"),
    A320("Airbus", "A320", 3300, 150, false, 6, "ABCDEF"),
    A321("Airbus", "A321", 3500, 185, false, 6, "ABCDEF"),
    B737("Boeing", "737-800", 2935, 160, false, 6, "ABCDEF"),

    // --- Wide-body ---
    B787("Boeing", "787-9", 7600, 296, true, 9, "ABCDEFHJK"), // 3-3-3 (skip I)
    A350("Airbus", "A350-900", 8100, 300, true, 9, "ABCDEFHJK"),
    B777("Boeing", "777-300ER", 7350, 350, true, 10, "ABCDEFGHJK"); // 3-4-3 (skip I)

    private final String manufacturer;
    private final String modelLabel;
    private final int rangeNm; // typical max range in nautical miles
    private final int typicalSeats; // rough two-class config
    private final boolean widebody;
    private final int defaultSeatsPerRow; // seat-count across (used for seat maps)
    private final String defaultSeatLetters;// e.g., "ABCDEF" or "ABCDEFHJK"

    PlaneType(String manufacturer,
            String modelLabel,
            int rangeNm,
            int typicalSeats,
            boolean widebody,
            int defaultSeatsPerRow,
            String defaultSeatLetters) {
        this.manufacturer = manufacturer;
        this.modelLabel = modelLabel;
        this.rangeNm = rangeNm;
        this.typicalSeats = typicalSeats;
        this.widebody = widebody;
        this.defaultSeatsPerRow = defaultSeatsPerRow;
        this.defaultSeatLetters = defaultSeatLetters;
    }

    /** @return true if the aircraft can fly a route of the given distance (nm). */
    public boolean supportsRouteNm(int distanceNm) {
        return distanceNm <= rangeNm;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModelLabel() {
        return modelLabel;
    }

    /** @return typical maximum range in nautical miles */
    public int getRangeNm() {
        return rangeNm;
    }

    /** @return typical seating count in a standard 2-class layout */
    public int getTypicalSeats() {
        return typicalSeats;
    }

    /** @return whether this is a wide-body aircraft */
    public boolean isWidebody() {
        return widebody;
    }

    /** @return default number of seats per row (useful for generating seat maps) */
    public int getDefaultSeatsPerRow() {
        return defaultSeatsPerRow;
    }

    /**
     * Letters typically used for seats across a row.
     * Example: "ABCDEF" (narrow-body), "ABCDEFHJK" (wide-body).
     */
    public String getDefaultSeatLetters() {
        return defaultSeatLetters;
    }

    /**
     * Parse a case-insensitive code like "a320" or "B777".
     * Falls back to valueOf semantics (throws IllegalArgumentException if unknown).
     */
    public static PlaneType fromCode(String code) {
        if (code == null)
            throw new IllegalArgumentException("PlaneType code cannot be null");
        return PlaneType.valueOf(code.trim().toUpperCase());
    }

    @Override
    public String toString() {
        return name() + " (" + manufacturer + " " + modelLabel + ")";
    }
}
