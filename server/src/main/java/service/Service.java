package service;

import dataaccess.*;
import org.mindrot.jbcrypt.BCrypt;

public abstract class Service {
    protected UserDAO userDataAccess;
    protected GameDAO gameDataAccess;
    protected AuthDAO authDataAccess;

    public Service() {
        this.userDataAccess = new SQLUserDAO();
        this.gameDataAccess = new SQLGameDAO();
        this.authDataAccess = new SQLAuthDAO();
    }
}


