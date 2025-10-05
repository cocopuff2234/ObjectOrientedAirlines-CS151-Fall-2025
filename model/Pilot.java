package model;

import model.enums.PlaneType;
import model.enums.Role;
import model.enums.PilotRank;
import model.enums.CrewStatus;

import java.time.LocalDate;
import java.util.*;

/**
 * Represents aPilot who is a type of crew member.
 * Each attendant has a position (Captain/First officer) and certifications
 * for which aircraft types they can fly.
 */
public class Pilot extends Crew {
    private PilotRank rank;
    private final Set<PlaneType> typeRatings = new HashSet<>(); 
    private int totalFlightHours;


    
    /**
     * Constructs a new Pilot object.
     *
     * @param employeeId       the unique ID for this pilot
     * @param fullName         the pilot's full name
     * @param hiredOn          the date the pilot was hired
     * @param baseAirport      the airport this pilot is based at
     * @param rank             the pilot's current rank (CAPTAIN or FIRST_OFFICER)
     * @param ratings          a collection of aircraft types the pilot is certified to operate
     * @param totalFlightHours the total flight hours the pilot has accumulated
     *
     * <p>
     * The constructor initializes all required crew and pilot-specific fields.
     * It ensures that rank and other non-nullable parameters are validated,
     * and uses defensive copying for collections to prevent external modification.
     * </p>
     */
    public Pilot(String employeeID, String fullName, LocalDate hiredOn, String baseAirport, PilotRank rank,
                Collection<PlaneType> ratings, int totalFlightHours) {
        super(employeeID, fullName, hiredOn, baseAirport);
        this.rank = rank;
        if (ratings != null) {
            typeRatings.addAll(ratings);
        }
        this.totalFlightHours = Math.max(0, totalFlightHours);
    }

    // ---------------- Getters and Setters ----------------

    public PilotRank getRank() {
        return rank;
    }


    public void setRank(PilotRank rank) {
        this.rank = Objects.requireNonNull(rank);
    }


    public Set<PlaneType> getTypeRatings() {
        return Collections.unmodifiableSet(typeRatings);
    }

    // If a pilot gains new certification, we need to add a new type rating
    public void addTypeRating(PlaneType type){
        typeRatings.add(type);
    }


    public int getTotalFlightHours() {
        return totalFlightHours;
    }

    // No need for flight hours setter, but we add flight hours after each flight
    public void addFlightHours(int hours){
        totalFlightHours += Math.min(0, hours);
    }

    // ---------------- Abstract Overrides from Crew ----------------

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


    @Override
    public String toString() {
        return "Pilot {" + 
                "name:' " + getFullName() + '\'' +
                ", id: '" + getEmployeeID() +  '\'' +
                ", rank: '" + getRank() + '\'' +
                ", ratings: '" + getTypeRatings() + '\'' +
                ", hours: '" + getTotalFlightHours() + '\''+
                "}";

    }


    

    

    

    
    
}
