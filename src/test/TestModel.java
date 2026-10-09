package test;

import model.*;
import exception.*;

public class TestModel {
    public static void main(String[] args) {
        try {
            // Polymorphism demo
            Person p1 = new Admin(1, "Kshitiz", "admin@tts.com", "pass");
                      Person p2 = new Employee(2, "Rahul", "rahul@tts.com", "pass");

            System.out.println(p1);
                 p1.showDashboard();

            System.out.println(p2);
                 p2.showDashboard();

            // Exception demo
                   throw new InvalidLoginException("Wrong credentials for test");

        } catch (InvalidLoginException e) {
            System.out.println("Caught exception: " + e.getMessage());
        }
    }
}