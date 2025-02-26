package dataaccess;

import model.AuthData;
import java.util.ArrayList;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private static final ArrayList<AuthData> authList = new ArrayList<>();

    public String createAuthToken(){
        return UUID.randomUUID().toString();
    }

    public void addAuthData(String authToken, String username){
        AuthData authData = new AuthData(authToken, username);
        authList.add(authData);
    }

    public void lookUpAuthToken() throws DataAccessException {
    }

    public void removeAuthToken() throws DataAccessException {

    }

    public void deleteAllAuthTokens() throws DataAccessException {

    }
}
