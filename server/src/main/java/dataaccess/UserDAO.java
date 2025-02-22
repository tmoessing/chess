package dataaccess;

public interface UserDAO {

    void addUserData(String userData) throws DataAccessException;

    void findUserDataViaUsername(String userData) throws DataAccessException;

    void deleteAllUsers() throws DataAccessException;

}
