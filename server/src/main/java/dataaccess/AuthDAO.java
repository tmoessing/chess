package dataaccess;

public interface AuthDAO {
    void createAuthToken() throws DataAccessException;

    void lookUpAuthToken() throws DataAccessException;

    void removeAuthToken() throws DataAccessException;

    void deleteAllAuthTokens() throws DataAccessException;

}
