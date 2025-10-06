package model;

import java.time.LocalDate;

import model.enums.CrewStatus;
import model.enums.PlaneType;
import model.enums.Role;

public abstract class Crew {
    private final String employeeId;
    private final String fullName;
    private final LocalDate hiredOn;
    private CrewStatus status = CrewStatus.AVAILABLE;
    private String baseAirport;

    protected Crew(String employeeId, String fullName, LocalDate hiredOn, String baseAirport) {
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.hiredOn = hiredOn;
        this.baseAirport = baseAirport;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDate getHiredOn() {
        return hiredOn;
    }

    public CrewStatus getStatus() {
        return status;
    }

    public void setStatus(CrewStatus status) {
        this.status = status;
    }

    public String getBaseAirport() {
        return baseAirport;
    }

    public void setBaseAirport(String baseAirport) {
        this.baseAirport = baseAirport;
    }

    public abstract Role getRole();

    // Is crew member licensed to operate given plane type?
    public abstract boolean canOperate(PlaneType type);

    @Override
    public int hashCode() {
        return employeeID.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Crew crew) && crew.employeeID.equals(this.employeeID);
    }

    @Override
    public String toString() {
        return "%s(%s %s)".formatted(getRole(), fullName, employeeID);
    }

}
