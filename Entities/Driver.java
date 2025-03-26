package Entities;

public class Driver extends User {
    public boolean isAvailable = true; // Default to available

    public double Rating=4.5;

    public Driver(String id, String name, String password) {
        super(id, name, password,"Driver"); // Pass "Driver" as the type
    }
}

