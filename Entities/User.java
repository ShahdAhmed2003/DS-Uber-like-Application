package Entities;

public class User {
    public String id;
    public String name;
    public String password;
    public String type; // "Customer", "Driver", or "Admin"

    public User(String id, String name, String password, String type) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.type = type;
    }

    @Override
    public String toString() {
        return "User{id='" + id + "', name='" + name + "', type='" + type + "'}";
    }
}
