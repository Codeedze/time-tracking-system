package db;

public class TestConnection {
    public static void main(String[] args) {
        try (var con = DBConnection.getInstance().getConnection()) {
            if (con != null && !con.isClosed()) {
                System.out.println("✅ Database connected successfully!");
            }
        } catch (Exception e) {
            System.out.println("❌ Connection failed: " + e.getMessage());
        }
    }
}