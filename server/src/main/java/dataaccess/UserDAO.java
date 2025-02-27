package dataaccess;

import model.*;

public interface UserDAO {

    boolean isUsernameTaken(String userName);

    void addUserData(RegisterRequest userData);

    UserData pullUserData(String username);

    void clearAllUsers();

}
