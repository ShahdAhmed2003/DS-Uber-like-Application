package Entities;

import java.util.ArrayList;
import java.util.List;

public class Ride {
    public int rideId;
    public Customer customer;
    public Driver driver;
    public String pickupLocation;
    public String destination;
    public double price;
    public String status; // "Pending", "Accepted", "Ongoing", "Completed"
    public Customer RequestedCustomer;
    public static List<Driver> RequestedDrivers= new ArrayList<>();
    public Driver CurrentDriver;
    public Customer CurrentCustomer;

    public Ride(int  rideId, Customer customer, String pickupLocation, String destination) {
        this.rideId = rideId;
        this.customer = customer;
        this.pickupLocation = pickupLocation;
        this.destination = destination;
        this.status = "Pending";
    }

    public void assignDriver(Driver driver, double price) {
        this.driver = driver;
        this.price = price;
        this.status = "Pending";
        
    }
    public static synchronized List<User> getRequestedDrivers() {
        return new ArrayList<>(RequestedDrivers);
     }
}

