package model;

public class Employee extends User {

    public Employee() {
        super();
    }

    public Employee(int id, String name, String email, String password) {
        super(id, name, email, password);
    }

    @Override
    public String getRole() {
        return "EMPLOYEE";
    }

    @Override
    public void showDashboard() {
        System.out.println("Showing Employee Dashboard for: " + getName());
    }
}