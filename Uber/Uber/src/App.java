/*import java.util.*;
import java.io.*;
import Entities.Customer;
import Entities.Driver;
import Entities.User;

public class App {
    private static final String FILE_NAME = "C:\\Users\\DELL\\Downloads\\Uber\\Uber\\src\\users.txt";
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Customer> customers = new ArrayList<>();
    private static final List<Driver> drivers = new ArrayList<>();

    public static void main(String[] args) {
        loadUsers(); // Load users on startup

        while (true) {
            System.out.println("\n1. Register\n2. Login\n3. Save & Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                case 3:
                    saveUsers();
                    System.out.println("Users saved. Exiting...");
                    return;
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }

    private static void registerUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter type (customer/driver): ");
        String type = scanner.nextLine().toLowerCase();

        if (!type.equals("customer") && !type.equals("driver")) {
            System.out.println("Invalid type. Must be 'customer' or 'driver'.");
            return;
        }

        if (userExists(username)) {
            System.out.println("Username already exists! Try another.");
            return;
        }

        if (type.equals("customer")) {
            customers.add(new Customer(username, password));
        } else {
            drivers.add(new Driver(username, password));
        }

        System.out.println("Registration successful!");
    }

    private static void loginUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (validateUser(username, password)) {
            System.out.println("Login successful! Welcome " + username);
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private static boolean userExists(String username) {
        return customers.stream().anyMatch(u -> u.getUsername().equals(username)) ||
               drivers.stream().anyMatch(u -> u.getUsername().equals(username));
    }

    private static boolean validateUser(String username, String password) {
        return customers.stream().anyMatch(u -> u.getUsername().equals(username) && u.getPassword().equals(password)) ||
               drivers.stream().anyMatch(u -> u.getUsername().equals(username) && u.getPassword().equals(password));
    }

    private static void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write("{\n  \"customers\": [\n");
            for (int i = 0; i < customers.size(); i++) {
                writer.write("    " + customers.get(i).toJSON());
                if (i < customers.size() - 1) writer.write(",");
                writer.write("\n");
            }
            writer.write("  ],\n  \"drivers\": [\n");
            for (int i = 0; i < drivers.size(); i++) {
                writer.write("    " + drivers.get(i).toJSON());
                if (i < drivers.size() - 1) writer.write(",");
                writer.write("\n");
            }
            writer.write("  ]\n}");
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    private static void loadUsers() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isCustomerSection = false, isDriverSection = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("\"customers\"")) isCustomerSection = true;
                else if (line.startsWith("\"drivers\"")) {
                    isCustomerSection = false;
                    isDriverSection = true;
                } else if (line.startsWith("]")) {
                    isCustomerSection = false;
                    isDriverSection = false;
                } else if (line.startsWith("{")) {
                    if (isCustomerSection) customers.add(Customer.fromJSON(line));
                    if (isDriverSection) drivers.add(Driver.fromJSON(line));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }
}*/
