package dataaccess;

import model.RegisterRequest;
import model.RegisterResult;

public interface UserDAO {

    RegisterResult addUserData(RegisterRequest userData) throws DataAccessException;

    void findUserDataViaUsername(String userData) throws DataAccessException;

    void deleteAllUsers() throws DataAccessException;

}
