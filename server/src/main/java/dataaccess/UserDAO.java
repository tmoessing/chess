package dataaccess;

import model.*;

public interface UserDAO {

    boolean isUsernameTaken(String userName);

    void addUserData(String username, String password, String email);

    UserData pullUserData(String username);

    void clearAllUsers();

}
