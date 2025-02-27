package dataaccess;

public interface AuthDAO {

    String createAuthToken();

    void addAuthData(String username, String authToken);

    boolean isAuthTokenExistent(String authToken);

    String getUsernameViaAuthToken(String authToken);

    void removeAuthToken(String authToken);

    void clearAuthData();

}
