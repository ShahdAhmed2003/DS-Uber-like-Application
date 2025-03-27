package Features;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import Entities.*;

public class UserService {
    private static List<User> users = new ArrayList<>();
    private static List<Ride> rides = new ArrayList<>();

    // Register a new user
    public static synchronized String registerUser(String username, String password, String type) {
        // Check if username already exists
        for (User user : users) {
            if (user.name.equalsIgnoreCase(username)) {
                return "Username already exists!";
            }
        }

        // Validate type
        if (!type.equalsIgnoreCase("Customer") && !type.equalsIgnoreCase("Driver")) {
            return "Invalid user type! Must be 'Customer' or 'Driver'.";
        }

        // Generate unique user ID
        String userId = UUID.randomUUID().toString();

        // Create user object
        User newUser;
        if (type.equalsIgnoreCase("Customer")) {
            newUser = new Customer(userId, username, password);
        } else {
            newUser = new Driver(userId, username, password);
        }

        // Add to user list
        users.add(newUser);
        return "Registration successful! Your ID: " + userId;
    }

    //  Login user
    public static synchronized User loginUser(String username, String password) {
        for (User user : users) {
            if (user.name.equalsIgnoreCase(username) && user.password.equals(password)) {
                return user; // Return user if credentials match
            }
        }
        return null; // Invalid credentials
    }
//customer request ride
     public static synchronized String requestRide(Customer customer, String pickup, String destination) {
        Ride newRide = new Ride(rides.size() + 1, customer, pickup, destination);
        rides.add(newRide);
        return "Ride request submitted successfully!";
    }
//driver get requested ride
    public static synchronized List<Ride> getRequestedRides() {
        List<Ride> availableRides = new ArrayList<>();
        for (Ride ride : rides) {
            if (ride.status.equals("Pending")) {
                availableRides.add(ride);
            }
        }
        return availableRides;
    }
    public static synchronized String offerFare(Driver driver, int rideId, double price) {
        System.out.println(rideId);
        for (Ride ride : rides) {
            ride.assignDriver(driver, price);
            System.out.println(ride.rideId);
            if (ride.rideId == rideId && ride.status.equals("Pending")&&ride.driver.isAvailable) {
                ride.RequestedDrivers.add(driver);
                ride.driver.isAvailable=false;
                return "Fare offer submitted for Ride ID: " + ride.rideId;
            }
        }
        return "Cannot offer this ride";
    }
    public static synchronized String AcceptRide(String d_name,Customer C)
    {
        for(Ride ride:rides)
        {
            if(ride.status.equals("Pending"))
            {
                for(Driver driver:ride.RequestedDrivers)
                {
                    if(driver.name.equals(d_name))
                    {
                       ride.CurrentDriver=driver;
                       ride.CurrentCustomer=C;
                       ride.status="Driver on the way";
                       driver.isAvailable=false;     
                       return "Ride accepted successfully! Driver: " + driver.name + " | Price: $" + ride.price;
                    }
                }
            }
        }
        return "Ride not found or no accepted offers yet.";

    }
   /* public static synchronized List<String> getAvailableRidesWithOffers(String customerId) {
        List<String> availableRides = new ArrayList<>();
        for (Ride ride : rides) {
            if (ride.status.equals("Accepted") && ride.customer.id.equals(customerId)) {
                availableRides.add("Ride ID: " + ride.rideId + " | Driver: " + ride.driver.name + 
                                   " | Rating: " + ride.driver.Rating + "⭐ | Fare: $" + ride.price);
            }
        }
        return availableRides;
    }*/
    

    /*public static synchronized String acceptRideOffer(String D_name, String customerId) {
        for (Ride ride : rides) {
            if (ride. && ride.status.equals("Pending")) {
                if (ride.customer.id.equals(customerId)) {
                    ride.status = "Driver on The Way";
                    return "Ride accepted successfully! Driver: " + ride.driver.name + " | Price: $" + ride.price;
                } else {
                    return "Invalid ride ID or customer ID!";
                }
            }
        }
        return "Ride not found or no accepted offers yet.";
    }*/

    
public static synchronized String getCustomernameFromRideId(int rideId) {
    for (Ride ride : rides) {
        if (ride.rideId == rideId) {
            return ride.customer.name; // Return the customer's ID
        }
    }
    return "Ride not found"; // If rideId does not exist
}
public static synchronized List<User> getAllUsers() {
   return new ArrayList<>(users);
}
    // N6 Ride Status Updates
    public static synchronized String updateRideStatus(String rideId, String statusUpdate) {
        for (Ride ride : rides) {
            if (ride.rideId == Integer.parseInt(rideId)) {
                if (!ride.status.equals("Ongoing") && !ride.status.equals("Accepted")) {
                    return "Ride is not in progress, cannot update status.";
                }

                if (statusUpdate.equalsIgnoreCase("start")) {
                    ride.status = "Ongoing";
                    return "Ride status updated to Ongoing. Notified customer.";
                } else if (statusUpdate.equalsIgnoreCase("finish")) {
                    ride.status = "Completed";
                    ride.driver.isAvailable = true;
                    return "Ride marked as Completed. Notified customer.";
                } else {
                    return "Invalid status update.";
                }
            }
        }
        return "Ride not found.";
    }
    //N7 Disconnect
    public static synchronized boolean canDisconnect(String userId) {
        for (Ride ride : rides) {
            if (ride.status.equals("Ongoing") &&
                    (ride.customer.id.equals(userId) || (ride.driver != null && ride.driver.id.equals(userId)))) {
                return false; // In an ongoing ride
            }
        }
        return true;
    }
public static synchronized String rateDriver(String driverId, double cleanliness, double drivingSkills, double navigationSkills) {
    for (User user : users) {
        if (user instanceof Driver && user.id.equals(driverId)) {
            Driver driver = (Driver) user;

            // Validate ratings (1-5)
            if (cleanliness < 1 || cleanliness > 5 || drivingSkills < 1 || drivingSkills > 5 || navigationSkills < 1 || navigationSkills > 5) {
                return "Invalid rating! Each category must be between 1 and 5.";
            }

            // Calculate average rating
            double newrating = (cleanliness + drivingSkills + navigationSkills) / 3.0;
            driver.Rating=(driver.Rating*driver.ratingCount+newrating)/(driver.ratingCount+1);
            driver.ratingCount++;
            driver.isAvailable=true;
            return "Driver " + driver.name + " has been rated successfully!";

        }
    }
    return "Driver not found!";
    
}
    public static synchronized Ride getRideById(int rideId) {
    for (Ride ride : rides) {
        if (ride.rideId == rideId) {
            return ride; // Return the ride if found
        }
    }
    return null; // Return null if the ride is not found
}
    //N8 Statistics
    /* public static synchronized List<String> getAdminStatistics(User currentUser) {
        if (currentUser == null || !currentUser.type.equalsIgnoreCase("Admin")) {
            List<String> error = new ArrayList<>();
            error.add("Access Denied: Only Admin can view statistics.");
            return error;
        }

        int totalRides = rides.size();
        int completed = 0, ongoing = 0, pending = 0, accepted = 0;
        int totalDrivers = 0, totalCustomers = 0;

        for (Ride ride : rides) {
            switch (ride.status) {
                case "Completed": completed++; break;
                case "Ongoing": ongoing++; break;
                case "Pending": pending++; break;
                case "Accepted": accepted++; break;
            }
        }

        for (User user : users) {
            if (user instanceof Driver) totalDrivers++;
            else if (user instanceof Customer) totalCustomers++;
        }

        List<String> stats = new ArrayList<>();
        stats.add("Total Rides: " + totalRides);
        stats.add("Pending: " + pending);
        stats.add("Accepted: " + accepted);
        stats.add("Ongoing: " + ongoing);
        stats.add("Completed: " + completed);
        stats.add("Total Customers: " + totalCustomers);
        stats.add("Total Drivers: " + totalDrivers);

        return stats;
    }
*/

}
