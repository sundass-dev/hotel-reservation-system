# hotel-reservation-system
A desktop hotel reservation system built in Java (OOP) with a Swing GUI and MySQL database integration. Supports user registration/login, room browsing, booking with add-on services, and billing.

## Features
- User registration & login
- View available rooms
- Book a room with check-in/check-out dates
- Add optional services (Breakfast, WiFi, Room Cleaning)
- MySQL database integration for persistent storage

## Tech Stack
- Java (OOP)
- Java Swing (GUI)
- MySQL (Database)
- JDBC (Database connectivity)

## Database Design
See `ERD.mwb` for the entity-relationship diagram (open with MySQL Workbench).

## How to Run
1. Set up a MySQL database using the schema shown in the ERD
2. Update database credentials in `DatabaseConnection.java` / `DBConnector.java`
3. Compile and run `Main.java`
