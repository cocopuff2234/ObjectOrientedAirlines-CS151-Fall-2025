import model.Crew;
import model.enums.PlaneType;
import model.enums.Role;
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


    public PilotRank getRank() {
        return rank;
    }


    public void setRank(PilotRank rank) {
        this.rank = rank;
    }


    public Set<PlaneType> getTypeRatings() {
        return Collections.unmodifiableSet(PlaneType);
    }


    public int getTotalFlightHours() {
        return totalFlightHours;
    }



    @Override
    public boolean canOperate(PlaneType type) {
        // From Crew, check if pilot is available and certified for flight
        return getStatus() == CrewStatus.AVAILABLE && typeRatings.contains(type);
    }


    @Override
    public Role getRole() {
        // Implements abstract from crew, either CAPTAIN or FIRST OFFICER
        return Role.PILOT;
    }

    

    
    
}
