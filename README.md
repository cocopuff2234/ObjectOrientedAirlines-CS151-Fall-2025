# Overview: Object-Oriented Airlines 

The Object-Oriented Airlines client allows for flyers to perform operations to interact with our airline. Flyers are able to see available flights, view flight details, book and cancel tickets, and view their own tickets. Within our client, resource provisioning is done to ensure that users fly with confidence. This means ensuring that flights are operated by a qualified crew, flown on state of the art planes, and transparent with flight states.

# Design: Our Classes

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
- addTypeRating - add aircraft rating if pilot receives training/certification
- addFlightHours - add flight hours after successful flight
  
# Installation Instructions

# Usage

# Contributions

