package Entities;

public class Customer extends User {
    public Customer(String id, String name, String password) {
        super(id, name, password, "Customer"); // Pass "Customer" as the type
    }
}
