package service;

      import model.User;
   import exception.DuplicateUserException;
        import exception.InvalidLoginException;
import exception.UserNotFoundException;

import java.util.List;

public interface UserService {
             boolean registerUser(User user) throws DuplicateUserException;
    User login(String email, String password) throws InvalidLoginException;
         User getUserById(int id) throws UserNotFoundException;
           List<User> getAllUsers();
       boolean updateUser(User user);
             boolean deleteUser(int id);
        boolean deleteUserWithLogs(int id);
}