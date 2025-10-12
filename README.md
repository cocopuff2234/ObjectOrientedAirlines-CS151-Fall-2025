# Overview: Object-Oriented Airlines 

The Object-Oriented Airlines client allows for flyers to perform operations to interact with our airline. Flyers are able to see available flights, view flight details, book and cancel tickets, and view their own tickets. Within our client, resource provisioning is done to ensure that users fly with confidence. This means ensuring that flights are operated by a qualified crew, flown on state of the art planes, and transparent with flight states.

# Design: Our Classes

## Airport - Ryuto

Airports are essential infrastructure for managing flight operations. Each airport handles multiple flights, assigns gates, and coordinates departures to ensure smooth operations.

The Airport class is defined in our client by 5 different attributes and 5 different methods:

### Attributes
- airportId - unique identifier for the airport (e.g., "JFK", "SFO")
- airportName - full name of the airport (e.g., "New York JFK", "San Francisco International")
- terminals - list of terminal names available at the airport
- gates - set of gate identifiers available for flight assignments
- scheduledFlights - set of flights scheduled to depart from or arrive at this airport

### Methods
- scheduleFlight - registers a flight that involves this airport as origin or destination
- assignGate - assigns an available gate to a flight, ensuring no conflicts with other flights at the same time
- releaseGate - releases a gate after the flight departs, making it available for other flights
- flightsDepartingOn - returns a list of flights departing from this airport on a specific date, sorted by departure time
- findAvailableGate - finds the first available gate at a given time, checking for scheduling conflicts

## Plane - Ryuto

The aircraft are the backbone of our airline operations. Each plane has specific capacity, seat configurations, and pricing tiers to accommodate different customer needs.

The Plane class is defined in our client by 10 different attributes and 8 different methods:

### Attributes
- planeId - unique identifier for the aircraft
- planeType - aircraft type enum (e.g., A320, B737, B787) containing manufacturer details and specifications
- capacity - total number of seats available on the aircraft
- availableSeats - current number of unbooked seats
- firstClassSeats - total number of first class seats (20% of capacity)
- economySeats - total number of economy seats (80% of capacity)
- availableFirstClassSeats - current number of available first class seats
- availableEconomySeats - current number of available economy seats
- firstClassPrice - price per first class seat
- economyPrice - price per economy seat

### Methods
- reserveSeat - reserves a seat of specified type (first class or economy), returns true if successful
- releaseSeat - releases a reserved seat back into available inventory
- decrementCapacity - decrements the overall available seat count, returns false if plane is full
- incrementCapacity - increments available seat count when a booking is cancelled
- getPrice - returns the price for a given seat type
- seatCodes - generates deterministic seat codes (e.g., "A1", "B2") based on PlaneType layout
- isOperable - checks if the plane is operational and ready for flight
- defaultSeatLetters - returns the default seat letter configuration based on aircraft type

## Customer - Erik

The customers are the heart of Object-Oriented Airlines. Without them we could not do what we do. So naturally, we made sure to account for them well. 

The Customer is defined in our client by 6 different attributes and 3 different methods:

### Attributes
- customerID - unique identifier of the customer
- name - customer’s name
- phoneNumber - customer’s phone number
- Email - customer’s email address
- ticketList - list of tickets the customer has purchased
- Balance - balance the customer owes from the tickets purchased

### Methods
- bookTicket - given a flight and price, this method books a ticket for the customer
- cancelTicket - given a ticket object, this method cancels a customer’s ticket 
- seeTickets - returns the list of tickets a customer has purchased
- generateCustomerId - generates the next customer id

## Ticket - Erik 

Airlines revolve around tickets, without them we could not link our business with our customers.

The Ticket class is defined in our client by 6 different attributes and 3 different methods:

### Attributes:
- ticketId - Unique identifier of the ticket
- price - price of the ticket
- flight - flight the ticket is associated with 
- customer - customer the ticket is associated with 
- status - status of the ticket
- seatType - seat type the ticket reserves

### Methods
- generateTicketId - creates the ID of the ticket booked
- purchase - purchases a ticket, checking resources
- cancel - cancels the ticker
- upgradeTicket - upgrades booked ticket

## Crew - Sajid
We need a qualified crew assigned to our flights to ensure a smooth operation so our customer lands at their destination safely.
Crew is an abstract class that contains shared attributes between our pilots and flight attendants, like their employee IDs and airport base.

### Attributes:
- employeeId - the unique ID for crew
- fullName - the crew's full name
- hiredOn - the date the crew was hired
- baseAirport - the airport this crew is based at

### Methods
- canOperate: check whether crew has necessary qualifications to operate flight

## Pilot - Sajid
Flights want a qualified pilot who is certified for certain aircraft.

### Attributes:
- employeeId - the unique ID for pilot
- fullName - the pilot's full name
- hiredOn - the date the pilot was hired
- baseAirport - the airport this pilot is based at
- rank - the pilot's current rank (CAPTAIN or FIRST_OFFICER)
- ratings - a collection of aircraft types the pilot is certified to operate
- totalFlightHours - the total flight hours the pilot has accumulated

### Methods
- canOperate - Check if pilot is qualified to fly plane
- addTypeRating - add aircraft rating if pilot receives training/certification
- addFlightHours - add flight hours after successful flight

## Flight Attendant - Sajid
Every flight needs their flight attendants to ensure customer has a comfortable experience.

### Attributes:
- employeeId — the unique ID for this crew member
- fullName — the full name of the flight attendant
- hiredOn — the hire date
- baseAirport — the home base airport code
- position — the flight attendant's position (Lead/Junior)
- quals — the aircraft types this attendant is qualified for

## Methods 
- canOperate - check if flight attendant is trained for aircraft
- addCabinQualification - if flight attendant has completed training for another plane type

## Flight - Sajid
Flights are scheduled with the plane and allocation of crew members so a customer can book them

### Attributes:
- flightNumber – the unique flight identifier
- airline – the airline operating the flight
- origin – the departure airport code
- destination – the arrival airport code
- departureUTC – the scheduled departure time (LocalDateTime)
- arrivalUTC – the scheduled arrival time (LocalDateTime)
- gate – the departure gate for the flight
- plane – the Plane assigned to this flight
- captain – the Pilot serving as captain
- firstOfficer – the Pilot serving as first officer
- attendants – list of FlightAttendant crew members
- tickets – list of Ticket objects for passengers
- minAttendants – the minimum number of flight attendants required
- MAXIMUM_INSTANCES – the maximum number of Flight instances allowed
- instanceCount – the current number of created Flight instances

## Methods
- assignCaptain(Pilot p) – assigns a pilot as captain
- assignFirstOfficer(Pilot p) – assigns a pilot as first officer
- addAttendant(FlightAttendant fa) – adds a flight attendant to the crew
- removeAttendant(FlightAttendant fa) – removes a flight attendant from the crew
- hasRequiredCrew() – checks if the flight has enough crew
- getDuration() – returns the flight duration as a Duration
- notify(String message) – sends a notification
- delayByMinutes(long minutes) – delays the flight by the specified minutes
- changeGate(String newGate) – changes the flight’s gate
- addTicket(Ticket ticket) – adds a ticket to the flight
- removeTicket(Ticket ticket) – removes a ticket and returns whether it was successful
- getAvailableSeats() – returns the number of remaining available seats
- hasSeats(String seatType) – checks if seats of a given type are available
- require(boolean condition, String message) – verifies a condition and throws an exception if false
  
## Installation
1. **Clone the repository**  
   - git clone https://github.com/cocopuff2234/ObjectOrientedAirlines-CS151-Fall-2025.git
   - cd ObjectOrientedAirlines-CS151-Fall-2025
   
2. **Compile, or at this step alternatively open with IDE of choice and run Main**
  - javac -d out src/Main.java src/model/*.java src/model/enums/*.java

3. **Run the program**
  - java -cp out Main
  
# Usage
The Object-Oriented Airlines Client has a text-based UI that flyers interact with.  
The client first prompts the user with what operation they would like to perform.  
The available operations include:

- Book a Flight  
- Cancel a Flight  
- Check Flight Status  
- See Available Flights  
- See Existing Reservations  
- Exit  

After selecting an option, the program follows these paths:

- **Book a Flight:**  
  Search for flights by date, origin, and destination.  
  If matches are found, select a flight, enter customer details, choose a seat class, and confirm the booking.

- **Cancel a Flight:**  
  Identify yourself with your name, phone, and email.  
  Choose a ticket from your reservations to cancel.

- **Check Flight Status:**  
  Enter a flight number to view its route, departure and arrival times, seat availability, and current status.

- **See Available Flights:**  
  Displays all available flights with route details, departure times, and seats remaining.

- **See Existing Reservations:**  
  Look up reservations by providing your name, phone, and email to view booked flights and ticket details.

- **Exit:**  
  Closes the application.

# Contributions
Erik - Customer class, Ticket class, seat type methods and attributes in Plane class, debugging in main, README Overview, Design (first two classes), Installation, Usage, Presentation Slides.
Ryuto - Airport class, Plane class, seat management system (first class/economy split), gate assignment and scheduling logic, price management, UML diagram, code review and refactoring, README Design (Airport and Plane sections).
