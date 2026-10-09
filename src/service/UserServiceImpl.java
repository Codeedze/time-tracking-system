package service;

import dao.UserDAO;
import model.User;
import exception.DuplicateUserException;
import exception.InvalidLoginException;
import exception.UserNotFoundException;

import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserDAO userDAO = new UserDAO();

    @Override
    public boolean registerUser(User user) throws DuplicateUserException {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        return userDAO.addUser(user);
    }

    @Override
    public User login(String email, String password) throws InvalidLoginException {
        try {
            User user = userDAO.getUserByEmail(email);
            if (!user.getPassword().equals(password)) {
                throw new InvalidLoginException("Incorrect password.");
            }
            return user;
        } catch (UserNotFoundException e) {
            throw new InvalidLoginException("No user found with email: " + email);
        }
    }

    @Override
    public User getUserById(int id) throws UserNotFoundException {
        return userDAO.getUserById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }

    @Override
    public boolean deleteUser(int id) {
        return userDAO.deleteUser(id);
    }

        @Override
    public boolean deleteUserWithLogs(int id) {
        return userDAO.deleteUserWithLogs(id);
    }
}