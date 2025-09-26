import model.Flight;
import java.time.LocalDateTime;

public class Main{
    public static void main(String[] args){
        Flight flight1 = new Flight("AA001", "American Airlines", "New York", "Los Angeles",
                LocalDateTime.of(2025, 10, 1, 14, 30),
                LocalDateTime.of(2025, 10, 1, 17, 45));
        Flight flight2 = new Flight("DL002", "Delta", "Chicago", "Miami",
                LocalDateTime.of(2025, 10, 2, 9, 15),
                LocalDateTime.of(2025, 10, 2, 13, 0));
        Flight flight3 = new Flight("UA003", "United", "San Francisco", "Seattle",
                LocalDateTime.of(2025, 10, 3, 16, 0),
                LocalDateTime.of(2025, 10, 3, 18, 15));
        Flight flight4 = new Flight("SW004", "Southwest", "Dallas", "Houston",
                LocalDateTime.of(2025, 10, 4, 12, 0),
                LocalDateTime.of(2025, 10, 4, 13, 15));
        Flight flight5 = new Flight("BA005", "British Airways", "London", "New York",
                LocalDateTime.of(2025, 10, 5, 8, 0),
                LocalDateTime.of(2025, 10, 5, 11, 30));

        System.out.println(flight1.getFlightNumber() + " | " + flight1.getAirline() + " | " + flight1.getOrigin() + " -> " + flight1.getDestination());
        System.out.println(flight2.getFlightNumber() + " | " + flight2.getAirline() + " | " + flight2.getOrigin() + " -> " + flight2.getDestination());
        System.out.println(flight3.getFlightNumber() + " | " + flight3.getAirline() + " | " + flight3.getOrigin() + " -> " + flight3.getDestination());
        System.out.println(flight4.getFlightNumber() + " | " + flight4.getAirline() + " | " + flight4.getOrigin() + " -> " + flight4.getDestination());
        System.out.println(flight5.getFlightNumber() + " | " + flight5.getAirline() + " | " + flight5.getOrigin() + " -> " + flight5.getDestination());


        String route1 = flight1.getOrigin() + " to " + flight1.getDestination();
        String route2 = flight2.getOrigin() + " to " + flight2.getDestination();
        String route3 = flight3.getOrigin() + " to " + flight3.getDestination();
        String route4 = flight4.getOrigin() + " to " + flight4.getDestination();
        String route5 = flight5.getOrigin() + " to " + flight5.getDestination();

        System.out.println("Route 1: " + route1);
        System.out.println("Route 2: " + route2);
        System.out.println("Route 3: " + route3);
        System.out.println("Route 4: " + route4);
        System.out.println("Route 5: " + route5);
    }
}