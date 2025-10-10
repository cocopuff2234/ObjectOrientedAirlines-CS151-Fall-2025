import model.*;
import model.enums.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        List<Flight> flights = new ArrayList<>();

        // Planes
        Plane p1 = new Plane("PL001", PlaneType.A320, PlaneType.A320.getTypicalSeats(), 200.0, 100.0);
        Plane p2 = new Plane("PL002", PlaneType.B737, PlaneType.B737.getTypicalSeats(), 200.0, 100.0);
        Plane p3 = new Plane("PL003", PlaneType.A320, PlaneType.A320.getTypicalSeats(), 200.0, 100.0);
        Plane p4 = new Plane("PL004", PlaneType.B737, PlaneType.B737.getTypicalSeats(), 200.0, 100.0);
        Plane p5 = new Plane("PL005", PlaneType.A320, PlaneType.A320.getTypicalSeats(), 200.0, 100.0);

        // Flights
        flights.add(new Flight("AA001", "American Airlines", "New York", "Los Angeles",
                LocalDateTime.of(2025, 10, 1, 14, 30),
                LocalDateTime.of(2025, 10, 1, 17, 45),
                p1, 2));

        flights.add(new Flight("DL002", "Delta", "Chicago", "Miami",
                LocalDateTime.of(2025, 10, 2, 9, 15),
                LocalDateTime.of(2025, 10, 2, 13, 0),
                p2, 2));

        flights.add(new Flight("UA003", "United", "San Francisco", "Seattle",
                LocalDateTime.of(2025, 10, 3, 16, 0),
                LocalDateTime.of(2025, 10, 3, 18, 15),
                p3, 2));

        flights.add(new Flight("SW004", "Southwest", "Dallas", "Houston",
                LocalDateTime.of(2025, 10, 4, 12, 0),
                LocalDateTime.of(2025, 10, 4, 13, 15),
                p4, 2));

        flights.add(new Flight("BA005", "British Airways", "London", "New York",
                LocalDateTime.of(2025, 10, 5, 8, 0),
                LocalDateTime.of(2025, 10, 5, 11, 30),
                p5, 3));

        // --- Flight 1 Crew (A320) ---
        Pilot captain1 = new Pilot("P001", "John Pilot", LocalDate.of(2020,1,15), "JFK",
                PilotRank.CAPTAIN, Set.of(PlaneType.A320), 5000);
        Pilot firstOfficer1 = new Pilot("P006", "Jane Pilot", LocalDate.of(2021,5,10), "JFK",
                PilotRank.FIRST_OFFICER, Set.of(PlaneType.A320), 2000);
        FlightAttendant fa1 = new FlightAttendant("FA001", "Alice Liddel", LocalDate.of(2021,5,10), "JFK",
                FAPosition.LEAD, Set.of(PlaneType.A320));
        FlightAttendant fa2 = new FlightAttendant("FA002", "Bob Burger", LocalDate.of(2022,3,20), "JFK",
                FAPosition.JUNIOR, Set.of(PlaneType.A320));
        Flight flight1 = flights.get(0);
        flight1.assignCaptain(captain1);
        flight1.assignFirstOfficer(firstOfficer1);
        flight1.addAttendant(fa1);
        flight1.addAttendant(fa2);

        // --- Flight 2 Crew (B737) ---
        Pilot captain2 = new Pilot("P002", "Jane Doe", LocalDate.of(2019,7,10), "ORD",
                PilotRank.CAPTAIN, Set.of(PlaneType.B737), 6000);
        Pilot firstOfficer2 = new Pilot("P007", "Mike Tyson", LocalDate.of(2020,6,5), "ORD",
                PilotRank.FIRST_OFFICER, Set.of(PlaneType.B737), 2500);
        FlightAttendant fa3 = new FlightAttendant("FA003", "John Cena", LocalDate.of(2020,2,5), "ORD",
                FAPosition.LEAD, Set.of(PlaneType.B737));
        FlightAttendant fa4 = new FlightAttendant("FA004", "Dwayne Rock", LocalDate.of(2021,8,15), "ORD",
                FAPosition.JUNIOR, Set.of(PlaneType.B737));
        Flight flight2 = flights.get(1);
        flight2.assignCaptain(captain2);
        flight2.assignFirstOfficer(firstOfficer2);
        flight2.addAttendant(fa3);
        flight2.addAttendant(fa4);

        // --- Flight 3 Crew (A320) ---
        Pilot captain3 = new Pilot("P003", "John Adams", LocalDate.of(2021,3,1), "SFO",
                PilotRank.CAPTAIN, Set.of(PlaneType.A320), 4000);
        Pilot firstOfficer3 = new Pilot("P008", "Big Tony", LocalDate.of(2022,2,10), "SFO",
                PilotRank.FIRST_OFFICER, Set.of(PlaneType.A320), 1500);
        FlightAttendant fa5 = new FlightAttendant("FA005", "Emma Watson", LocalDate.of(2022,6,12), "SFO",
                FAPosition.LEAD, Set.of(PlaneType.A320));
        FlightAttendant fa6 = new FlightAttendant("FA006", "Frank Abagnale", LocalDate.of(2022,9,18), "SFO",
                FAPosition.JUNIOR, Set.of(PlaneType.A320));
        Flight flight3 = flights.get(2);
        flight3.assignCaptain(captain3);
        flight3.assignFirstOfficer(firstOfficer3);
        flight3.addAttendant(fa5);
        flight3.addAttendant(fa6);

        // --- Flight 4 Crew (B737) ---
        Pilot captain4 = new Pilot("P004", "Bill Nye", LocalDate.of(2020,11,5), "DAL",
                PilotRank.CAPTAIN, Set.of(PlaneType.B737), 5500);
        Pilot firstOfficer4 = new Pilot("P009", "Neil Degrasse Tyson", LocalDate.of(2021,12,1), "DAL",
                PilotRank.FIRST_OFFICER, Set.of(PlaneType.B737), 1800);
        FlightAttendant fa7 = new FlightAttendant("FA007", "Mr. Rogers", LocalDate.of(2021,1,20), "DAL",
                FAPosition.LEAD, Set.of(PlaneType.B737));
        FlightAttendant fa8 = new FlightAttendant("FA008", "Steve Irwin", LocalDate.of(2022,4,30), "DAL",
                FAPosition.JUNIOR, Set.of(PlaneType.B737));
        Flight flight4 = flights.get(3);
        flight4.assignCaptain(captain4);
        flight4.assignFirstOfficer(firstOfficer4);
        flight4.addAttendant(fa7);
        flight4.addAttendant(fa8);

        // --- Flight 5 Crew (A320) ---
        Pilot captain5 = new Pilot("P005", "Tom Brady", LocalDate.of(2018,12,1), "LHR",
                PilotRank.CAPTAIN, Set.of(PlaneType.A320), 7000);
        Pilot firstOfficer5 = new Pilot("P010", "Steve Harvey", LocalDate.of(2019,7,15), "LHR",
                PilotRank.FIRST_OFFICER, Set.of(PlaneType.A320), 3000);
        FlightAttendant fa9 = new FlightAttendant("FA009", "Gianmarco Scollessi", LocalDate.of(2019,3,25), "LHR",
                FAPosition.LEAD, Set.of(PlaneType.A320));
        FlightAttendant fa10 = new FlightAttendant("FA010", "Sam Reich", LocalDate.of(2021,7,10), "LHR",
                FAPosition.JUNIOR, Set.of(PlaneType.A320));
        FlightAttendant fa11 = new FlightAttendant("FA011", "Brennan Lee Mulligan", LocalDate.of(2022,2,18), "LHR",
                FAPosition.JUNIOR, Set.of(PlaneType.A320));
        Flight flight5 = flights.get(4);
        flight5.assignCaptain(captain5);
        flight5.assignFirstOfficer(firstOfficer5);
        flight5.addAttendant(fa9);
        flight5.addAttendant(fa10);
        flight5.addAttendant(fa11);

        // Start UI
        AirportUI ui = new AirportUI(flights);
        ui.start();
    }
}
