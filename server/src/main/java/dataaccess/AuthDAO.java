package dataaccess;

public interface AuthDAO {

    String createAuthToken();

    void addAuthData(String username, String authToken);

    void lookUpAuthToken() throws DataAccessException;

    void removeAuthToken() throws DataAccessException;

    void deleteAllAuthTokens() throws DataAccessException;

}
