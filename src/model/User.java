package model;

public class User extends Person {

    public User() {
        super();
    }

    public User(int id, String name, String email, String password) {
        super(id, name, email, password);
    }

    @Override
    public String getRole() {
        return "USER";
    }

    @Override
    public void showDashboard() {
        System.out.println("Showing User Dashboard for: " + getName());
    }
}