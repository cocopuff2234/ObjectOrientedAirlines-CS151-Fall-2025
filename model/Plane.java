package model;

import model.enums.PlaneType;

public class Plane {
    private final String planeId;
    private final PlaneType planeType;
    private final int capacity;

    public Plane(String planeId, PlaneType planeType, int capacity) {
        this.planeId = planeId;
        this.planeType = planeType;
        this.capacity = capacity;
    }

    public String getPlaneId() { return planeId; }
    public PlaneType getPlaneType() { return planeType; }
    public int getCapacity() { return capacity; }
}
