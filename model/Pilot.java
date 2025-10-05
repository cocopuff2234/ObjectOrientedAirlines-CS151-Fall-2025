import model.Crew;
import model.enums.PlaneType;
import model.enums.PilotRank;
import model.enums.CrewStatus;

import java.time.LocalDate;
import java.util.*;
public class Pilot extends Crew {
    private PilotRank rank;
    private final Set<PlaneType> typeRatings = new HashSet<>();
    private int totalFlightHours;


    public Pilot(String employeeID, String fullName, LocalDate hiredOn, String baseAirport, PilotRank rank,
                Collection<PlaneType> ratings, int totalFlightHours) {
        super(employeeID, fullName, hiredOn, baseAirport);
        this.rank = rank;
        if (ratings != null) {
            typeRatings.addAll(ratings);
        }
        this.totalFlightHours = Math.max(0, totalFlightHours);
    }

    
    
}
