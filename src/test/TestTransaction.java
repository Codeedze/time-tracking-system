package test;

import dao.UserDAO;

public class TestTransaction {
    public static void main(String[] args) throws Exception {
        UserDAO dao = new UserDAO();

        // Test 1: Delete an existing user with logs (successful case)
                      System.out.println("--- Test 1: Delete existing user ---");
        boolean r1 = dao.deleteUserWithLogs(3); // change to a real user id
                  System.out.println("Result: " + r1);

        // Test 2: Delete a NON-existing user (rollback case)
                   System.out.println("--- Test 2: Delete non-existing user ---");
         boolean r2 = dao.deleteUserWithLogs(99999);
        System.out.println("Result: " + r2);
    }
}