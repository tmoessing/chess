package service;

import dataaccess.DataAccessException;

public class AuthService extends Service {

    public AuthService() {
        super();
    }

    public void logout(String authToken) throws DataAccessException {
        if (!authDataAccess.isAuthTokenExistent(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }

        authDataAccess.removeAuthToken(authToken);

    }


    public void clearAuthData(){
        authDataAccess.clearAuthData();
    }

}
