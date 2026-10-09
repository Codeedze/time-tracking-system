package dao;

import db.DBConnection;
import model.Admin;
import model.Employee;
import model.User;
import exception.DuplicateUserException;
import exception.UserNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public boolean addUser(User user) throws DuplicateUserException {
        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DuplicateUserException("User with email " + user.getEmail() + " already exists.");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUserById(int id) throws UserNotFoundException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRowToUser(rs);
            else throw new UserNotFoundException("User with ID " + id + " not found.");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserNotFoundException("DB error while fetching user.");
        }
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRowToUser(rs);
            else throw new UserNotFoundException("No user registered with email: " + email);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserNotFoundException("DB error while fetching user.");
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) users.add(mapRowToUser(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE users SET name = ?, email = ?, password = ?, role = ? WHERE id = ?";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());
            ps.setInt(5, user.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUserWithLogs(int userId) {
        String deleteLogsSql = "DELETE FROM time_logs WHERE user_id = ?";
        String deleteUserSql = "DELETE FROM users WHERE id = ?";

        Connection con = null;
        try {
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement ps1 = con.prepareStatement(deleteLogsSql)) {
                ps1.setInt(1, userId);
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = con.prepareStatement(deleteUserSql)) {
                ps2.setInt(1, userId);
                int rows = ps2.executeUpdate();

                if (rows == 0) {
                    con.rollback();
                    System.out.println("[Transaction] User not found. Rolled back.");
                    return false;
                }
            }

            con.commit();
            System.out.println("[Transaction] User + logs deleted. Committed.");
            return true;

        } catch (SQLException e) {
            System.out.println("[Transaction] Error. Rolling back.");
            e.printStackTrace();
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String password = rs.getString("password");
        String role = rs.getString("role");

        switch (role.toUpperCase()) {
            case "ADMIN": return new Admin(id, name, email, password);
            case "EMPLOYEE": return new Employee(id, name, email, password);
            default: return new User(id, name, email, password);
        }
    }
}