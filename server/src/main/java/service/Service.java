package service;

import dataaccess.*;

public abstract class Service {
    protected UserDAO userDataAccess;
    protected GameDAO gameDataAccess;
    protected AuthDAO authDataAccess;

    public Service(){
        this.userDataAccess = new MemoryUserDAO();
        this.gameDataAccess = new MemoryGameDAO();
        this.authDataAccess = new MemoryAuthDAO();
    }
}


