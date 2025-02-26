package service;

import dataaccess.AuthDAO;

public class AuthService extends Service {

    public AuthService() {
        super();
    }

    public void clearAuthData(){
        authDataAccess.clearAuthData();
    }
}
