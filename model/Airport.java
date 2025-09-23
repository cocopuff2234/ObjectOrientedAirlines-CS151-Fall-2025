package model;
import java.util.List;

public class Airport {
    private final String aiportId;
    private final String airportName;
    private final List<String>[] terminals;

    public Airport (String airportId, String airportName, List<String>[] terminals){
        this.aiportId = airportId;
        this.airportName = airportName;
        this.terminals = terminals;
    }

    public String getAirportName() {
        return airportName;
    }

    public List<String>[] getTerminals() {
        return this.terminals;
    }
}
