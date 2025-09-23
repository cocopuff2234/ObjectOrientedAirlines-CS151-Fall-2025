import java.util.List;

public class Airport {
    private final String airportName;
    private final List<String> terminals[];

    public Airport (String airportName, List<String> terminals[]){
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
