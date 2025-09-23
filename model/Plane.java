package model;

public class Plane {
    private final String planeId;
    // TODO: Decide if we need to implement plane type with enum
    private final PlaneType planeType;
    private final int capacity;

    public Plane(String planeId, PlaneType planeType, int capacity) {
        this.planeId = planeId;
        this.planeType = planeType;
        this.capacity = capacity;
    }

    public String getPlaneType() {
        return planeType;
    }

    public int getCapacity() {
        return capacity;
    }
}
