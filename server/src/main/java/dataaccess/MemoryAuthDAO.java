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

    public boolean isAuthTokenExistent(String authToken) {
        for (AuthData authData : authList) {
            if (authData.authToken().equals(authToken)) {
                return true;
            }
        }
        return false;
    }

    public String getUsernameViaAuthToken(String authToken) {
        for (AuthData authData : authList) {
            if (authData.authToken().equals(authToken)) {
                return authData.username();
            }
        }
        return null;
    }

    public void removeAuthToken(String authToken) {
        for (AuthData authData : authList) {
            if (authData.authToken().equals(authToken)) {
                authList.remove(authData);
                return;
            }
        }
    }

    public void clearAuthData() {
        authList.clear();
    }
}
