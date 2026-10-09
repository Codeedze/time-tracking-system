package model;

public abstract class Person {
    private int id;
    private String name;
    private String email;
    private String password;

    public Person() {}

    public Person(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // Abstract method — polymorphism
    public abstract String getRole();

    // Abstract method — polymorphism
    public abstract void showDashboard();

    @Override
    public String toString() {
        return "Person{id=" + id + ", name='" + name + "', email='" + email + "', role='" + getRole() + "'}";
    }
}