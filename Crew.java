import java.time.LocalDate;

public abstract class Crew {
    private final String employeeID;
    private final String fullName;
    private final LocalDate hiredOn;
    private String baseAirport;

    protected Crew(String employeeID, String fullName, LocalDate hiredOn, String baseAirport) {
        this.employeeID = employeeID;
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

    public String getBaseAirport() {
        return baseAirport;
    }

    public void setBaseAirport(String baseAirport) {
        this.baseAirport = baseAirport;
    }

    
    
}
