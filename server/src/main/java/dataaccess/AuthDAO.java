package dataaccess;

public interface AuthDAO {

    String createAuthToken();

    void addAuthData(String username, String authToken);

    boolean isAuthTokenExistent(String authToken);

    void lookUpAuthToken() throws DataAccessException;

    void removeAuthToken(String authToken);

    void clearAuthData();

}
