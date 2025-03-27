package ServerSide;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import Entities.*;
import Features.UserService;


public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> drivers = new ArrayList<ClientHandler>();
    public static ArrayList<ClientHandler> customers=new ArrayList<ClientHandler>();
    private Socket socket;
    private BufferedReader bufferedreader;
    private BufferedWriter bufferedwriter;
    private User user; 
    private List<User> userList;
    private PrintWriter output;
    private String c_name;
    private String d_name;

    public ClientHandler(Socket socket) throws IOException
    {
        this.socket = socket;
        this.bufferedwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.bufferedreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output= new PrintWriter(socket.getOutputStream(), true);
    }
    @Override
    public void run(){
        try {
            bufferedwriter.write("Do you want to Register or Login? (Type 'register' or 'login')");
            bufferedwriter.newLine(); // Ensures the message is properly received
            bufferedwriter.flush();   // Forces the message to be sent immediately
            String action = bufferedreader.readLine().trim().toLowerCase();
            if (!action.equals("register") && !action.equals("login")) {
                bufferedwriter.write("Invalid input! Connection closing...");
                bufferedwriter.newLine();
                bufferedwriter.flush();
                return;
            }
       
        bufferedwriter.write("Enter your username:");
        bufferedwriter.newLine();
        bufferedwriter.flush();
        String username = bufferedreader.readLine().trim();
        bufferedwriter.write("Enter your password:");
        bufferedwriter.newLine();
        bufferedwriter.flush();
        String password = bufferedreader.readLine().trim();
       // User user = null;
        if (action.equals("register")) {
            bufferedwriter.write("Enter type (Customer/Driver):");
            bufferedwriter.newLine();
            bufferedwriter.flush();
            String type = bufferedreader.readLine().trim();
            String response = UserService.registerUser(username, password, type);
            bufferedwriter.write(response);
            bufferedwriter.newLine();
            bufferedwriter.flush();
        } else {
            this.user = UserService.loginUser(username, password);
            if (user == null) {
                bufferedwriter.write("Invalid credentials! Connection closing...");
                bufferedwriter.newLine();
                bufferedwriter.flush();
                return;
            }
            if(user instanceof Customer){
                bufferedwriter.write("Customer " + user.name+ " logged in successfully!");
                bufferedwriter.newLine();
                bufferedwriter.flush();
                synchronized (customers) {
                customers.add(this);
                }
            }
            else if (user instanceof Driver)
            {
            bufferedwriter.write("Driver " + user.name+ " logged in successfully!");
            bufferedwriter.newLine();
            bufferedwriter.flush();
            synchronized (drivers) {
            drivers.add(this);
            broadcastRequestedRidestoDrivers();
            }
            }
        }
        this.userList = UserService.getAllUsers();
        
        if(user instanceof Customer)
        {
            handleCustomer();
        }
        else if(user instanceof Driver)
        {
            handleDriver();
        }
        
    }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void notifycustomerwithoffer(int rideId) throws IOException
    {
        List<Ride> availableRides = UserService.getRequestedRides();
        StringBuilder rideInfo = new StringBuilder("offered Rides:\n");
        for(Ride ride:availableRides)
        {
            if(ride.rideId==rideId)
            {
                c_name=ride.customer.name;
                d_name=ride.driver.name;
                rideInfo.append("Ride Price: ").append(ride.price)
                .append(" | Pickup: ").append(ride.pickupLocation)
                .append(" | Destination: ").append(ride.destination).append(" | DriverName: ").append(d_name)
                .append("\n");

                
            }
        }
        for(ClientHandler customer:customers)
        {
            if(customer.user.name.equals(c_name))
            {
                customer.sendMessage(rideInfo.toString());
            }
        }
    }
    public void notifydriverwithacceptedoffer(String dname,String cname) throws IOException
    {
        for(ClientHandler driver:drivers)
        {
            if(driver.user.name.equals(dname))
            {
                driver.sendMessage("Ride accepted successfully! Customer: " + cname);
            }
        }
    }
     
    
    public void broadcastRequestedRidestoDrivers() throws IOException
    {
    List<Ride> availableRides = UserService.getRequestedRides();
        
        if (availableRides.isEmpty()) {
            try {
                bufferedwriter.write("Waiting for ride request.");
                bufferedwriter.newLine();
                bufferedwriter.flush();
            } catch (IOException e) {
                e.printStackTrace(); // Log the error
            }
            return; // No rides to broadcast
        }

        StringBuilder rideInfo = new StringBuilder("Available Rides:\n");
        for (Ride ride : availableRides) {
            if(ride.status.equals("Pending"))
            {
            rideInfo.append("Ride ID: ").append(ride.rideId)
                    .append(" | Pickup: ").append(ride.pickupLocation)
                    .append(" | Destination: ").append(ride.destination)
                    .append("\n");
                    
        }
    }
        synchronized (drivers) {
            if(drivers.isEmpty())
            {
                bufferedwriter.write("Waiting for a Driver");
            }
            for (ClientHandler driver : drivers) {
                
                if (driver.user instanceof Driver && ((Driver) driver.user).isAvailable) {
                    System.out.println("Sending message to driver: " + rideInfo);
                    driver.sendMessage(rideInfo.toString());
                }
            }
        }
    

}
public void sendMessage(String message) {
    try {
        System.out.println("Sending message: to driver " + message);
        bufferedwriter.write(message);
        bufferedwriter.newLine();
        bufferedwriter.flush();
    } catch (IOException e) {
        e.printStackTrace(); // Log the error
    }
}
public void handleCustomer() throws IOException
{
        sendMessagee("1. Request a ride by entering a pickup location and destination.");
        sendMessagee("2. Accept Offer.");
        sendMessagee("3. View the current status of the requested ride.");
        sendMessagee("4. Disconnect from the server.");
        
        while(true)
        {
            String command=bufferedreader.readLine();
            if("1".equals(command))
            {
                sendMessagee("Enter pickup location:");
                String pickup = bufferedreader.readLine();
                sendMessagee("Enter destination:");
                String destination = bufferedreader.readLine();
                String rideResponse = UserService.requestRide((Customer) user, pickup, destination);
                sendMessagee(rideResponse);
                broadcastRequestedRidestoDrivers();


            }
            else if ("2".equals(command)) {
                sendMessage("Enter Driver Name to accept:");
                String D_name = bufferedreader.readLine();
                String acceptResponse = UserService.AcceptRide(D_name,(Customer) user);
                sendMessage(acceptResponse);
                notifydriverwithacceptedoffer(D_name,user.name);
                

            }
            else if ("3".equals(command)) {
            /*to be handled later */
           }
            else {
                sendMessage("Invalid choice. Try again.");
            }



        }
}
private void handleDriver() throws IOException {
    sendMessagee("1. Offer a fare for a ride request.");
    sendMessagee("2. Send status updates of the ride they have been assigned to (start/finish ride).");
    sendMessagee("3. Disconnect from the server.");

    while (true) {
        String command = bufferedreader.readLine();
        if ("1".equals(command)) {
        sendMessagee("Enter Ride ID to offer fare:");
        int rideId = Integer.parseInt(bufferedreader.readLine()); // Read ride ID
        sendMessagee("Enter fare amount:");
        double fare = Double.parseDouble(bufferedreader.readLine()); // Read fare amount
        String offerResponse = UserService.offerFare((Driver) user, rideId, fare);
        c_name=UserService.getCustomernameFromRideId(rideId);
        sendMessagee(offerResponse);
        if(offerResponse.contains("Fare offer submitted"))
        {
        notifycustomerwithoffer(rideId);
        }

    
        } else if ("2".equals(command)) {
            /*sendMessage("Enter status (start/finish):");
            String status = input.readLine();
            RideServer.updateRideStatus(this, status);
            sendMessage("✅ Ride status updated: " + status);*/
        } else if ("3".equals(command)) {
            sendMessage("Disconnected from server.");
            closeConnection();
            break;
        } else {
            sendMessage("Invalid choice. Try again.");
        }
    }
}

public void sendMessagee(String message) {
    try {
        bufferedwriter.write(message + "\n");
        bufferedwriter.flush();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
private void closeConnection() {
    try {
        bufferedreader.close();
        bufferedwriter.close();
        socket.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
