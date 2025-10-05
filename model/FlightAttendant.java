package model;

import model.enums.PlaneType;
import model.enums.Role;
import model.enums.FAPosition;
import model.enums.CrewStatus;

import java.time.LocalDate;
import java.util.*;

/**
 * Represents a flight attendant who is a type of crew member.
 * Each attendant has a position (Lead or Junior) and certifications
 * for which aircraft types they can serve on.
 */
public class FlightAttendant extends Crew {

    private FAPosition position;
    private final Set<PlaneType> cabinQualifications = new HashSet<>();

        /**
     * Constructs a new FlightAttendant object.
     *
     * @param employeeId  the unique ID for this crew member
     * @param fullName    the full name of the flight attendant
     * @param hiredOn     the hire date
     * @param baseAirport the home base airport code
     * @param position    the flight attendant's position (Lead/Junior)
     * @param quals       the aircraft types this attendant is qualified for
     */
    public FlightAttendant(String employeeID, String fullName, LocalDate hiredOn, String baseAirport,
            FAPosition position, Collection<PlaneType> qualifcations) {
        super(employeeID, fullName, hiredOn, baseAirport);
        this.position = Objects.requireNonNull(position);
    }

    

}
