package model;

public class Settings {
    private int id;
    private double workHours;
    private double breakTime;

    public Settings() {}

    public Settings(int id, double workHours, double breakTime) {
        this.id = id;
        this.workHours = workHours;
        this.breakTime = breakTime;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getWorkHours() { return workHours; }
    public void setWorkHours(double workHours) { this.workHours = workHours; }

    public double getBreakTime() { return breakTime; }
    public void setBreakTime(double breakTime) { this.breakTime = breakTime; }

    @Override
    public String toString() {
        return "Settings{workHours=" + workHours + ", breakTime=" + breakTime + "}";
    }
}