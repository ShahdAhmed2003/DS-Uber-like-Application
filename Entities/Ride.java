package Entities;

public class Ride {
    public int rideId;
    public Customer customer;
    public Driver driver;
    public String pickupLocation;
    public String destination;
    public double price;
    public String status; // "Pending", "Accepted", "Ongoing", "Completed"
    public Customer RequestedCustomer;

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
}

